package com.gildedgames.the_aether.entities.hostile;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.entity.EntityLiving;	// Bad MCP name: should be 'Mob'
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class Sentry extends EntityLiving implements IMob {

	private int jumpCount;

	public int searchTicks;

	public int lostTicks;

	public Sentry(World world) {
		super(world);

		this.yOffset = 0F;
		this.jumpCount = this.rand.nextInt(20) + 10;
	}

	public Sentry(World world, double x, double y, double z) {
		this(world);

		this.rotationYaw = (float) this.rand.nextInt(4) * 1.570796F;

		this.setPosition(x, y, z);
		this.setSize(0.85F, 0.85F);
	}

	@Override
	public void entityInit() {
		super.entityInit();

		this.dataWatcher.addObject(20, new Byte((byte) 0));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1D);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		compound.setBoolean("awake", isAwake());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		set_awake(compound.getBoolean("awake"));
	}

	@Override
	public void onUpdate() {
		boolean prev_on_ground = this.onGround;

		super.onUpdate();

		if(this.onGround && !prev_on_ground) {
			this.worldObj.playSoundAtEntity(this, "mob.slime", getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1F) / 0.8F);
		} else if(!this.onGround && prev_on_ground && getAttackTarget() != null) {
			this.motionX *= 3D;
			this.motionZ *= 3D;
		}

		Entity attack_target = getAttackTarget();
		if(attack_target != null && attack_target.isDead) {
			setAttackTarget(null);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		boolean attack_ok = super.attackEntityFrom(source, damage);
		if(attack_ok && source.getEntity() instanceof EntityLivingBase) {
			this.lostTicks = 0;
			set_awake(true);
			setAttackTarget((EntityLivingBase)source.getEntity());
		}
		return attack_ok;
	}

	@Override
	public void applyEntityCollision(Entity entity) {
		if (!this.isDead && this.getAttackTarget() != null && this.getAttackTarget() == entity) {
			this.worldObj.createExplosion(entity, this.posX, this.posY, this.posZ, 0.1F, false);

			entity.attackEntityFrom(DamageSource.causeMobDamage(this), 2F);

			if (entity instanceof EntityLivingBase) {
				EntityLivingBase living_entity = (EntityLivingBase)entity;
				double d = living_entity.posX - this.posX;
				double d2;
				for(d2 = living_entity.posZ - this.posZ; d * d + d2 * d2 < 0.0001D; d2 = (Math.random() - Math.random()) * 0.01D) {
					d = (Math.random() - Math.random()) * 0.01D;
				}

				living_entity.knockBack(this, 5, -d, -d2);
				living_entity.motionX *= 4D;
				living_entity.motionY *= 4D;
				living_entity.motionZ *= 4D;
			}

			float f = 0.01745329F;

			for (int i = 0; i < 40; i++) {
				double d1 = (float) this.posX + this.rand.nextFloat() * 0.25F;
				double d3 = (float) this.posY + 0.5F;
				double d4 = (float) this.posZ + this.rand.nextFloat() * 0.25F;
				float f1 = this.rand.nextFloat() * 360F;
				this.worldObj.spawnParticle("explode", d1, d3, d4, -Math.sin(f * f1) * 0.75D, 0.125D, Math.cos(f * f1) * 0.75D);
			}

			this.setDead();
		}
	}

	@Override
	protected void updateEntityActionState() {
		Entity attack_target = getAttackTarget();
		EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 8D);
		if(!isAwake() && this.searchTicks >= 8) {
			if (player != null && this.canEntityBeSeen(player)) {
				this.lostTicks = 0;
				set_awake(true);
				setAttackTarget(player);
				faceEntity(player, 10F, 10F);
			}
			this.searchTicks = 0;
		} else if(isAwake() && this.searchTicks >= 8) {
			if(attack_target == null) {
				if(player != null && canEntityBeSeen(player)) {
					this.lostTicks = 0;
					set_awake(true);
					setAttackTarget(player);
				} else if(++this.lostTicks >= 4) {
					setSentryLost();
				}
			} else if(attack_target.isDead || !canEntityBeSeen(attack_target) || getDistanceToEntity(attack_target) >= 16F) {
				if(++this.lostTicks >= 4) {
					setSentryLost();
				}
			} else {
				this.lostTicks = 0;
			}
			this.searchTicks = 0;
		} else {
			++this.searchTicks;
		}

		if(!isAwake()) {
			return;
		}

		attack_target = getAttackTarget();
		if(attack_target != null) {
			faceEntity(attack_target, 10F, 10F);
		}

		if (this.onGround && this.jumpCount-- <= 0) {
			this.isJumping = true;
			this.moveForward = 1F;
			this.jumpCount = this.rand.nextInt(20) + 10;
			this.moveStrafing = 0.5F - this.rand.nextFloat();
			if(attack_target != null) {
				this.jumpCount /= 2;
				this.moveForward = 1F;
			}
			playSound("mob.slime", getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1F) * 0.8F);
		} else {
			this.isJumping = false;
			if (this.onGround) {
				this.moveStrafing = this.moveForward = 0F;
			}
		}
	}

	private void setSentryLost() {
		this.lostTicks = 0;
		this.searchTicks = -64;
		this.setAttackTarget(null);
	}

	public void set_awake(boolean awake) {
		this.dataWatcher.updateObject(20, (byte) (awake ? 1 : 0));
	}

	public boolean isAwake() {
		return this.dataWatcher.getWatchableObjectByte(20) == (byte) 1;
	}

	@Override
	protected String getHurtSound() {
		return "mob.slime";
	}

	@Override
	protected String getDeathSound() {
		return "mob.slime";
	}

	@Override
	protected float getSoundVolume() {
		return 0.6F;
	}

	@Override
	protected Item getDropItem() {
		return this.rand.nextInt(5) == 0 ? Item.getItemFromBlock(AetherBlocks.sentry_stone) : Item.getItemFromBlock(AetherBlocks.carved_stone);
	}

}
