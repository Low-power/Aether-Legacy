package com.gildedgames.the_aether.entities.util;

import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.api.player.util.IAetherBoss;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.events.AetherEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class EntityHook implements IExtendedEntityProperties {

	private Entity entity;

	private boolean inPortal;

	//public int teleportDirection;

	@Override
	public void init(Entity entity, World world) {
		this.entity = entity;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
	}

	public void onUpdate() {
		this.entity.worldObj.theProfiler.startSection("portal");

		if (this.entity.dimension == AetherConfig.get_aether_world_id()) {
			if (this.entity.posY < -2 && this.entity.riddenByEntity == null && this.entity.ridingEntity == null) {
				if (!this.entity.worldObj.isRemote) {
					AetherEntityEvents.teleport_entity(false, entity);
				}
			}
		}

		if (this.inPortal) {
			if (this.entity.ridingEntity == null) {
				this.entity.timeUntilPortal = this.entity.getPortalCooldown();

				if (!this.entity.worldObj.isRemote) {
					AetherEntityEvents.teleport_entity(true, entity);
				}
			}

			this.inPortal = false;
		}

		if (this.entity.timeUntilPortal > 0) {
			--this.entity.timeUntilPortal;
		}

		this.entity.worldObj.theProfiler.endSection();

		if (this.entity instanceof EntityLiving) {
			EntityLiving livingEntity = (EntityLiving) this.entity;

			if (livingEntity.getAttackTarget() instanceof EntityPlayer) {
				PlayerAether playerAether = PlayerAether.get((EntityPlayer) livingEntity.getAttackTarget());

				if (playerAether.getAccessoryInventory().wearingAccessory(new ItemStack(AetherItems.invisibility_cape))) {
					livingEntity.setAttackTarget(null);
				}
			}
		}

		if (this.entity instanceof EntityCreature) {
			EntityCreature creature = (EntityCreature) this.entity;

			if (creature.getEntityToAttack() instanceof EntityPlayer) {
				PlayerAether aplayer = PlayerAether.get((EntityPlayer)creature.getEntityToAttack());
				if(aplayer.getAccessoryInventory().wearingAccessory(new ItemStack(AetherItems.invisibility_cape))) {
					creature.setTarget(null);
				}
			}
		}

		if (this.entity instanceof EntityLivingBase) {
			EntityLivingBase living = (EntityLivingBase) this.entity;

			if (living.getAITarget() instanceof EntityPlayer) {
				PlayerAether aplayer = PlayerAether.get((EntityPlayer)living.getAITarget());
				if(aplayer.getAccessoryInventory().wearingAccessory(new ItemStack(AetherItems.invisibility_cape))) {
					living.setRevengeTarget(null);
				}
			}
		}
	}

	public void setInPortal() {
		if (this.entity instanceof IAetherBoss || this.entity instanceof IBossDisplayData) {
			return;
		}

		if (this.entity.timeUntilPortal > 0) {
			this.entity.timeUntilPortal = this.entity.getPortalCooldown();
		} else {
/*
			double x_diff = this.entity.prevPosX - this.entity.posX;
			double z_diff = this.entity.prevPosZ - this.entity.posZ;
			if (!this.entity.worldObj.isRemote && !this.inPortal) {
				this.teleportDirection = Direction.getMovementDirection(x_diff, z_diff);
			}
*/
			this.inPortal = true;
		}
	}
}
