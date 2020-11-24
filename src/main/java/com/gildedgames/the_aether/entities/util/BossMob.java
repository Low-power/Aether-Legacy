package com.gildedgames.the_aether.entities.util;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import java.util.List;

public abstract class BossMob extends EntityCreature implements IMob {
	public BossMob(World world) {
		super(world);

		this.experienceValue = 5;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
	}

	@Override
	public void onLivingUpdate() {
		updateArmSwingProgress();
		float f = this.getBrightness(1F);
		if (f > 0.5F) {
			this.entityAge += 2;
		}

		super.onLivingUpdate();
	}

	@Override
	protected Entity findPlayerToAttack() {
		EntityPlayer player = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16D);
		return player != null && this.canEntityBeSeen(player) ? player : null;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		if(isEntityInvulnerable()) return false;
		if(!super.attackEntityFrom(source, damage)) return false;

		Entity entity = source.getEntity();
		if(entity != this && entity != this.riddenByEntity && entity != this.ridingEntity) {
			this.entityToAttack = entity;
		}

		return true;
	}

	@Override
	public boolean attackEntityAsMob(Entity target) {
		float damage = (float)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
		int knockback = 0;
		if(target instanceof EntityLivingBase) {
			EntityLivingBase living_entity = (EntityLivingBase)target;
			damage += EnchantmentHelper.getEnchantmentModifierLiving(this, living_entity);
			knockback += EnchantmentHelper.getKnockbackModifier(this, living_entity);
		}

		if(target.attackEntityFrom(DamageSource.causeMobDamage(this), damage)) {
			if(knockback > 0) {
				target.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180F) * (float)knockback * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180F) * (float)knockback * 0.5F));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			int fire_aspect = EnchantmentHelper.getFireAspectModifier(this);
			if(fire_aspect > 0) {
				target.setFire(fire_aspect * 4);
			}

			if (target instanceof EntityLivingBase) {
				EnchantmentHelper.func_151384_a((EntityLivingBase)target, this);
			}

			EnchantmentHelper.func_151385_b(this, target);

			return true;
		}

		return false;
	}

	@Override
	protected void attackEntity(Entity target, float p_70785_2_) {
		if (this.attackTime <= 0 && p_70785_2_ < 2F && target.boundingBox.maxY > this.boundingBox.minY && target.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			attackEntityAsMob(target);
		}
	}

	@Override
	public float getBlockPathWeight(int x, int y, int z) {
		return 0.5F - this.worldObj.getLightBrightness(x, y, z);
	}

	protected boolean isValidLightLevel() {
		int x = MathHelper.floor_double(this.posX);
		int y = MathHelper.floor_double(this.boundingBox.minY);
		int z = MathHelper.floor_double(this.posZ);

		if(this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) > this.rand.nextInt(32)) {
			return false;
		}

		int l;
		if (this.worldObj.isThundering()) {
			int orig_value = this.worldObj.skylightSubtracted;
			this.worldObj.skylightSubtracted = 10;
			l = this.worldObj.getBlockLightValue(x, y, z);
			this.worldObj.skylightSubtracted = orig_value;
		} else {
			l = this.worldObj.getBlockLightValue(x, y, z);
		}

		return l <= this.rand.nextInt(8);
	}

	public boolean willDespawnPeacefully() {
		return false;
	}

	@Override
	public boolean getCanSpawnHere() {
		return isValidLightLevel() && super.getCanSpawnHere();
	}

	@Override
	protected boolean func_146066_aG() {
		return true;
	}

	@Override
	protected String func_146067_o(int p_146067_1_) {
		return p_146067_1_ > 4 ? "game.hostile.hurt.fall.big" : "game.hostile.hurt.fall.small";
	}

	@Override
	protected String getSwimSound() {
		return "game.hostile.swim";
	}

	@Override
	protected String getSplashSound() {
		return "game.hostile.swim.splash";
	}

	@Override
	protected String getHurtSound() {
		return "game.hostile.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "game.hostile.die";
	}
}
