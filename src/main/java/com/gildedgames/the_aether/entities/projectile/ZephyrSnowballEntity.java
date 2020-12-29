package com.gildedgames.the_aether.entities.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.player.PlayerAether;

public class ZephyrSnowballEntity extends BaseProjectileEntity {

	public ZephyrSnowballEntity(World world) {
		super(world);
	}

	public ZephyrSnowballEntity(World world, EntityLivingBase thrower, double x, double y, double z) {
		super(world, thrower);

		this.setPosition(x, y, z);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0D, 0D, 0D);
	}

	@Override
	protected void onImpact(MovingObjectPosition object) {
		if (object.entityHit instanceof EntityLivingBase) {
			if (object.entityHit instanceof EntityPlayer && PlayerAether.get((EntityPlayer) object.entityHit).getAccessoryInventory().wearingArmor(new ItemStack(AetherItems.sentry_boots))) {
				setDead();
				return;
			}

			object.entityHit.motionX += this.motionX * 1.5F;
			object.entityHit.motionY += 0.5D;
			object.entityHit.motionZ += this.motionZ * 1.5F;

			if (object.entityHit instanceof EntityPlayerMP) {
				((EntityPlayerMP) object.entityHit).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(object.entityHit));
			}

			setDead();
		}
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

}
