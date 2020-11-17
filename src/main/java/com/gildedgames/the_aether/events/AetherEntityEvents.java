package com.gildedgames.the_aether.events;

import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.api.AetherAPI;
import com.gildedgames.the_aether.entities.util.EntityHook;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.world.AetherWorldProvider;
import com.gildedgames.the_aether.world.AetherTeleporter;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.common.DimensionManager;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

public class AetherEntityEvents {
	private static Hashtable<Integer, WorldServer> worlds;
	private static Hashtable<Integer, Class<? extends WorldProvider>> world_providers;

	private static WorldServer get_world_by_name(String name) {
		if(worlds == null) try {
			Field worlds_field = DimensionManager.class.getDeclaredField("worlds");
			if(worlds_field.getType() == Hashtable.class) {
				worlds_field.setAccessible(true);
				worlds = (Hashtable<Integer, WorldServer>)worlds_field.get(null);
			}
		} catch(ReflectiveOperationException e) {
			e.printStackTrace();
		}
		if(worlds != null) for(WorldServer world : worlds.values()) {
			if(world.provider.getDimensionName().equalsIgnoreCase(name)) return world;
		}

		if(world_providers == null) try {
			Field providers_field = DimensionManager.class.getDeclaredField("providers");
			if(providers_field.getType() == Hashtable.class) {
				providers_field.setAccessible(true);
				world_providers = (Hashtable<Integer, Class<? extends WorldProvider>>)providers_field.get(null);
			}
		} catch(ReflectiveOperationException e) {
			e.printStackTrace();
		}
		if(world_providers != null) try {
			for(Map.Entry<Integer, Class<? extends WorldProvider>> entry : world_providers.entrySet()) try {
				WorldProvider provider = entry.getValue().newInstance();
				if(provider.getDimensionName().equalsIgnoreCase(name)) {
					int provider_id = entry.getKey().intValue();
					int world_id = provider_id;	// XXX: Assuming they are same
					if(worlds == null) {
						WorldServer world = DimensionManager.getWorld(world_id);
						if(world != null) return world;
					}
					DimensionManager.initDimension(world_id);
					return DimensionManager.getWorld(world_id);
				}
			} catch(IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch(InstantiationException e) {
			//e.printStackTrace();
			throw new RuntimeException("Failed to create instance of world provider", e);
		}

		System.err.println("Failed to get world by name " + name);
		return null;
	}

	private static void teleport_entity(boolean should_spawn_portal, Entity entity, WorldServer new_world) {
		WorldServer previous_world = (WorldServer)entity.worldObj;
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

		entity.dimension = new_world.provider.dimensionId;
		previous_world.removePlayerEntityDangerously(entity);
		entity.isDead = false;

		ServerConfigurationManager server_config = server.getConfigurationManager();
		AetherTeleporter teleporter = new AetherTeleporter(should_spawn_portal, new_world);
		server_config.transferEntityToWorld(entity, previous_world.provider.dimensionId, previous_world, new_world, teleporter);
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
			player.addExperienceLevel(0);
			player.setPlayerHealthUpdated();
		}
	}

	public static void teleport_entity(boolean should_spawn_portal, Entity entity) {
		WorldServer transfer_world;
		if(entity.worldObj.provider instanceof AetherWorldProvider) {
			transfer_world = get_world_by_name(AetherConfig.get_travel_world_name());
			if(transfer_world == null) transfer_world = DimensionManager.getWorld(0);
		} else {
			int aether_world_id = AetherConfig.get_aether_world_id();
			transfer_world = DimensionManager.getWorld(aether_world_id);
			if(transfer_world == null) {
				DimensionManager.initDimension(aether_world_id);
				transfer_world = DimensionManager.getWorld(aether_world_id);
			}
		}
		Entity teleport_rider = null;
		if(entity.riddenByEntity != null && entity.riddenByEntity.isRiding()) {
			if (entity.riddenByEntity instanceof EntityPlayer) {
				((PlayerAether)AetherAPI.get((EntityPlayer)entity.riddenByEntity)).ridden_entity = entity;
				//entity.forceSpawn = true;
			} else if(entity instanceof EntityPlayer) {
				((PlayerAether)AetherAPI.get((EntityPlayer)entity)).ridden_by_entity = entity.riddenByEntity;
				teleport_rider = entity.riddenByEntity;
			}
			entity.riddenByEntity.ridingEntity = null;
			entity.riddenByEntity = null;
		}
		if(entity.ridingEntity != null && entity.ridingEntity.riddenByEntity != null) {
			entity.ridingEntity.riddenByEntity = null;
			entity.ridingEntity = null;
		}
		if(teleport_rider != null) {
			teleport_rider.timeUntilPortal = teleport_rider.getPortalCooldown();
			teleport_entity(should_spawn_portal, teleport_rider, transfer_world);
		}
		entity.timeUntilPortal = entity.getPortalCooldown();
		teleport_entity(should_spawn_portal, entity, transfer_world);
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
		if(entity instanceof EntityPlayer && entity.posY < 256) {
			String travel_world_name = AetherConfig.get_travel_world_name();
			if(entity.worldObj.provider.getDimensionName().equals(travel_world_name)) {
				PlayerAether aplayer = (PlayerAether)AetherAPI.get((EntityPlayer)event.entity);
				if(aplayer.ridden_entity != null && aplayer.ridden_entity.worldObj.provider.getDimensionName().equals(travel_world_name)) {
					if(!entity.worldObj.loadedEntityList.contains(aplayer.ridden_entity)) {
						entity.worldObj.spawnEntityInWorld(aplayer.ridden_entity);
					}
					entity.mountEntity(aplayer.ridden_entity);
					aplayer.ridden_entity = null;
				}
				if(aplayer.ridden_by_entity != null && aplayer.ridden_by_entity.worldObj.provider.getDimensionName().equals(travel_world_name)) {
					if(!entity.worldObj.loadedEntityList.contains(aplayer.ridden_by_entity)) {
						entity.worldObj.spawnEntityInWorld(aplayer.ridden_by_entity);
					}
					aplayer.ridden_by_entity.mountEntity(entity);
					aplayer.ridden_by_entity = null;
				}
				return;
			}
		}
		if(!(entity.worldObj.provider instanceof AetherWorldProvider)) return;
		if(entity.posY < 0) {
			teleport_entity(false, entity);
		}
	}

	@SubscribeEvent
	public void on_entity_constructing(EntityEvent.EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties("aether_legacy:player_aether", new PlayerAether());
		} else if (event.entity instanceof EntityLivingBase) {
			event.entity.registerExtendedProperties("aether_legacy:entity_hook", new EntityHook());
		}
	}
}
