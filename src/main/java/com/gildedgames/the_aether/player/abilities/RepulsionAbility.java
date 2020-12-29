package com.gildedgames.the_aether.player.abilities;

import com.gildedgames.the_aether.api.player.IPlayerAether;
import com.gildedgames.the_aether.api.player.util.IAetherAbility;
import com.gildedgames.the_aether.entities.projectile.BaseProjectileEntity;
import com.gildedgames.the_aether.items.AetherItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.registry.IThrowableEntity;
import java.util.List;
import java.util.Random;

public class RepulsionAbility implements IAetherAbility {

	private Random rand = new Random();

	private final IPlayerAether player;

	public RepulsionAbility(IPlayerAether player) {
		this.player = player;
	}

	@Override
	public boolean shouldExecute() {
		return this.player.getAccessoryInventory().wearingAccessory(new ItemStack(AetherItems.repulsion_shield));
	}

	@Override
	public void onUpdate() {
		if (this.player.getEntity().worldObj.isRemote) {
			return;
		}

		List<Entity> entities = (List<Entity>)this.player.getEntity().worldObj.getEntitiesWithinAABBExcludingEntity(this.player.getEntity(), this.player.getEntity().boundingBox.expand(3D, 3D, 3D));
		for(Entity e : entities) {
			if(!isProjectile(e)) continue;
			Entity shooter = this.getShooter(e);
			if(shooter == null) return;
			if(shooter == this.player.getEntity()) continue;

			double x = this.player.getEntity().posX - shooter.posX;
			double y = this.player.getEntity().boundingBox.minY - shooter.boundingBox.minY;
			double z = this.player.getEntity().posZ - shooter.posZ;
			double difference = -Math.sqrt((x * x) + (y * y) + (z * z));
			x /= difference;
			y /= difference;
			z /= difference;

			e.setDead();

			double packX = (-e.motionX * 0.15F) + ((this.rand.nextFloat() - 0.5F) * 0.05F);
			double packY = (-e.motionY * 0.15F) + ((this.rand.nextFloat() - 0.5F) * 0.05F);
			double packZ = (-e.motionZ * 0.15F) + ((this.rand.nextFloat() - 0.5F) * 0.05F);

			((WorldServer)this.player.getEntity().worldObj).func_147487_a("flame", e.posX, e.posY, e.posZ, 12, packX, packY, packZ, 0.625F);

			this.player.getEntity().worldObj.playSoundAtEntity(this.player.getEntity(), "note.snare", 1F, 1F);
			this.player.getAccessoryInventory().damageWornStack(1, new ItemStack(AetherItems.repulsion_shield));
		}
	}

	public boolean onPlayerAttacked(DamageSource source) {
		return isProjectile(source.getEntity());
	}

	private Entity getShooter(Entity e) {
		if(e instanceof EntityArrow) return ((EntityArrow)e).shootingEntity;
		if(e instanceof EntityThrowable) return ((EntityThrowable)e).getThrower();
		if(e instanceof BaseProjectileEntity) return ((BaseProjectileEntity)e).getThrower();
		if(e instanceof EntityFireball) return ((EntityFireball)e).shootingEntity;
		return null;
	}

	public static boolean isProjectile(Entity entity) {
		return entity instanceof IProjectile || entity instanceof IThrowableEntity;
	}

}
