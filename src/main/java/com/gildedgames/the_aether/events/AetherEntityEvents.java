package com.gildedgames.the_aether.events;

import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.api.AetherAPI;
import com.gildedgames.the_aether.entities.util.EntityHook;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.world.AetherWorldProvider;
import com.gildedgames.the_aether.world.TeleporterAether;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldProviderSurface;	// OverworldWorldProvider
import net.minecraftforge.event.entity.living.LivingEvent;
//import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityEvent;
import java.util.Collection;

public class AetherEntityEvents {
	private static void teleport_entity(boolean shouldSpawnPortal, Entity entity, WorldServer new_world) {
		WorldServer previous_world = (WorldServer)entity.worldObj;
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

		entity.dimension = new_world.provider.dimensionId;
		previous_world.removePlayerEntityDangerously(entity);
		entity.isDead = false;

		ServerConfigurationManager server_config = server.getConfigurationManager();
		server_config.transferEntityToWorld(entity, previous_world.provider.dimensionId, previous_world, new_world, new TeleporterAether(shouldSpawnPortal, new_world));
		if(entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)entity;
			player.playerNetServerHandler.sendPacket(new S07PacketRespawn(new_world.provider.dimensionId, new_world.difficultySetting, new_world.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
			server_config.func_72375_a(player, previous_world);
			player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
			player.theItemInWorldManager.setWorld(new_world);
			server_config.updateTimeAndWeatherForPlayer(player, new_world);
			server_config.syncPlayerInventory(player);
			for(PotionEffect effect : (Collection<PotionEffect>)player.getActivePotionEffects()) {
				player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), effect));
			}
			FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, previous_world.provider.dimensionId, new_world.provider.dimensionId);
		}
	}

	@SubscribeEvent
	public void on_living_entity_update(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.entityLiving;

		if(entity instanceof EntityPlayer) {
			PlayerAether.get((EntityPlayer)entity).onUpdate();
		} else {
			((EntityHook)entity.getExtendedProperties("aether_legacy:entity_hook")).onUpdate();
		}

		if(entity.worldObj.isRemote) return;
		if(entity instanceof EntityPlayer && entity.worldObj.provider instanceof WorldProviderSurface && entity.posY < 256) {
			PlayerAether aplayer = (PlayerAether)AetherAPI.get((EntityPlayer)event.entity);
			if(aplayer.riddenEntity != null) {
				System.err.println(aplayer.riddenEntity);
				System.err.println(aplayer.riddenEntity.worldObj);
				System.err.println(aplayer.riddenEntity.dimension);
				System.err.println(aplayer.riddenEntity.isEntityAlive());
			}
			if(aplayer.riddenEntity != null && aplayer.riddenEntity.worldObj.provider instanceof WorldProviderSurface) {
				if(!entity.worldObj.loadedEntityList.contains(aplayer.riddenEntity)) {
					entity.worldObj.spawnEntityInWorld(aplayer.riddenEntity);
				}
				entity.mountEntity(aplayer.riddenEntity);
				aplayer.riddenEntity = null;
			}
			return;
		}
		if(!(entity.worldObj.provider instanceof AetherWorldProvider)) return;
		if(entity.posY < 0) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			int previous_world_id = entity.dimension;
			int transfer_world_id = previous_world_id == AetherConfig.getAetherDimensionID() ? 0 : AetherConfig.getAetherDimensionID();
			//System.err.printf("previous_world_id = %d, transfer_world_id = %d, entity = %s, entity.riddenByEntity = %s, entity.ridingEntity = %s\n", previous_world_id, transfer_world_id, entity, entity.riddenByEntity, entity.ridingEntity);
			if(entity.riddenByEntity != null && entity.riddenByEntity.isRiding()) {
				if (entity.riddenByEntity instanceof EntityPlayer) {
					((PlayerAether)AetherAPI.get((EntityPlayer)entity.riddenByEntity)).riddenEntity = entity;
					//entity.forceSpawn = true;
				}
				entity.riddenByEntity.ridingEntity = null;
				entity.riddenByEntity = null;
			}
			if(entity.ridingEntity != null && entity.ridingEntity.riddenByEntity != null) {
				entity.ridingEntity.riddenByEntity = null;
				entity.ridingEntity = null;
			}
			entity.timeUntilPortal = entity.getPortalCooldown();
			teleport_entity(false, entity, server.worldServerForDimension(transfer_world_id));
		}
	}

/*
	@SubscribeEvent
	public void on_entity_join_world(EntityJoinWorldEvent event) {
		if(!(event.entity instanceof EntityPlayerMP)) return;
		if(!(event.world.provider instanceof WorldProviderSurface)) return;
		PlayerAether aplayer = (PlayerAether)AetherAPI.get((EntityPlayer)event.entity);
		if(aplayer.riddenEntity == null) return;
		if(!event.world.loadedEntityList.contains(aplayer.riddenEntity)) {
			aplayer.riddenEntity.forceSpawn = true;
			boolean ok = event.world.spawnEntityInWorld(aplayer.riddenEntity);
			System.err.println(ok);
		}
		event.entity.mountEntity(aplayer.riddenEntity);
		aplayer.riddenEntity = null;
	}
*/

	@SubscribeEvent
	public void on_entity_constructing(EntityEvent.EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties("aether_legacy:player_aether", new PlayerAether());
		} else if (event.entity instanceof EntityLivingBase) {
			event.entity.registerExtendedProperties("aether_legacy:entity_hook", new EntityHook());
		}
	}
}
