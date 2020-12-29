package com.gildedgames.the_aether.entities.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LightningKnifeEntity extends BaseProjectileEntity {

	public LightningKnifeEntity(World world) {
		super(world);
	}

	public LightningKnifeEntity(World world, EntityLivingBase thrower) {
		super(world, thrower);
	}

	@Override
	protected void onImpact(MovingObjectPosition object) {
		if(this.worldObj.isRemote) return;

		if (object.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
			summonLightning();
		} else if (object.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			if (this.worldObj.getBlock(object.blockX, object.blockY, object.blockZ).getCollisionBoundingBoxFromPool(this.worldObj, object.blockX, object.blockY, object.blockZ) != null) {
				summonLightning();
			}
		}
	}

	private void summonLightning() {
		this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.posX, this.posY, this.posZ));
		setDead();
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player) {
	}

}
