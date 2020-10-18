package com.gildedgames.the_aether.entities.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntitySaddleMount extends MountableEntity {

	public EntitySaddleMount(World world) {
		super(world);
	}

	@Override
	public void entityInit() {
		super.entityInit();

		this.dataWatcher.addObject(19, new Byte((byte) 0));
	}

	@Override
	public boolean interact(EntityPlayer player) {
		if (!this.canSaddle()) {
			return super.interact(player);
		}

		if (!this.isSaddled()) {
			if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() == Items.saddle && !this.isChild()) {
				if (!player.capabilities.isCreativeMode) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}

				if (player.worldObj.isRemote) {
					player.worldObj.playSoundAtEntity(this, "mob.horse.leather", 0.5F, 1.0F);
				}

				this.setSaddled(true);

				return true;
			}
		} else if (this.riddenByEntity == null) {
			if (!player.worldObj.isRemote) {
				player.mountEntity(this);
				player.prevRotationYaw = player.rotationYaw = this.rotationYaw;
			}

			return true;
		}

		return super.interact(player);
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float i) {
		if ((damagesource.getEntity() instanceof EntityPlayer) && (this.riddenByEntity == damagesource.getEntity())) {
			return false;
		}

		return super.attackEntityFrom(damagesource, i);
	}

	@Override
	protected void dropFewItems(boolean recentlyHit, int lootLevel) {
		super.dropFewItems(recentlyHit, lootLevel);

		if (this.isSaddled()) {
			this.dropItem(Items.saddle, 1);
		}
	}

	@Override
	public boolean isEntityInsideOpaqueBlock() {
		return this.riddenByEntity != null ? false : super.isEntityInsideOpaqueBlock();
	}

	@Override
	public boolean shouldRiderFaceForward(EntityPlayer player) {
		return false;
	}

	@Override
	protected boolean canTriggerWalking() {
		return this.onGround;
	}

	@Override
	public boolean canBeSteered() {
		return true;
	}

	public void setSaddled(boolean saddled) {
		this.dataWatcher.updateObject(19, (byte) (saddled ? 1 : 0));
	}

	public boolean isSaddled() {
		return this.dataWatcher.getWatchableObjectByte(19) == (byte) 1;
	}

	public boolean canSaddle() {
		return true;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		compound.setBoolean("isSaddled", this.isSaddled());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		this.setSaddled(compound.getBoolean("isSaddled"));
	}

}
