package com.gildedgames.the_aether.entities.hostile.swet;

import com.gildedgames.the_aether.entities.util.MountableEntity;
import com.gildedgames.the_aether.entities.hostile.swet.SwetType;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.player.PlayerAether;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.command.IEntitySelector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import java.util.List;

public class Swet extends MountableEntity {

	private int slimeJumpDelay = 0;

	public int ticker;

	public int flutter;

	public int hops;

	public boolean kickoff;

	public Swet(World world) {
		super(world);

		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25);
		setHealth(25);

		set_swet_type(this.rand.nextInt(2));
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1.5F);
		setSize(0.8F, 0.8F);
		setPosition(this.posX, this.posY, this.posZ);
		this.hops = 0;
		this.flutter = 0;
		this.ticker = 0;
		this.slimeJumpDelay = this.rand.nextInt(20) + 10;
	}

	@Override
	public void entityInit() {
		super.entityInit();

		this.dataWatcher.addObject(20, new Byte((byte) 0));
		this.dataWatcher.addObject(21, new Byte((byte) this.rand.nextInt(SwetType.values().length)));
	}

	@Override
	public void updateRidden() {
		super.updateRidden();

		if (this.riddenByEntity != null && this.kickoff) {
			this.riddenByEntity.mountEntity(null);
			this.kickoff = false;
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(getAttackTarget() != null) {
			for (int i = 0; i < 3; i++) {
				double x = (float)this.posX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
				double y = (float)this.posY + this.height;
				double z = (float)this.posZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
				this.worldObj.spawnParticle("splash", x, y - 0.25D, z, 0D, 0D, 0D);
			}
		}

		if(this.riddenByEntity == null && !isFriendly()) {
			List<EntityLivingBase> entity_list = (List<EntityLivingBase>)this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.5D, 0.75D, 0.5D), new IEntitySelector() {
				@Override
				public boolean isEntityApplicable(Entity entity) {
					return !(entity instanceof Swet) && entity instanceof EntityLivingBase && entity.ridingEntity == null;
				}
			});
			for(EntityLivingBase entity : entity_list) {
				capturePrey(entity);
				if(this.riddenByEntity != null) break;
			}
		}

		if(handleWaterMovement()) {
			dissolve();
		}
	}

	@Override
	protected boolean canDespawn() {
		return !isFriendly();
	}

	@Override
	public void fall(float distance) {
		if(!isFriendly()) {
			super.fall(distance);
			if (this.hops >= 3 && this.getHealth() >= 0) {
				dissolve();
			}
		}
	}

	@Override
	public void knockBack(Entity entity, float damage, double distanceX, double distanceZ) {
		if (this.riddenByEntity != entity) {
			super.knockBack(entity, damage, distanceX, distanceZ);
		}
	}

	public void dissolve() {
		for (int i = 0; i < 50; i++) {
			float f = this.rand.nextFloat() * 3.141593F * 2F;
			float f1 = this.rand.nextFloat() * 0.5F + 0.25F;
			float f2 = MathHelper.sin(f) * f1;
			float f3 = MathHelper.cos(f) * f1;

			this.worldObj.spawnParticle("splash", this.posX + (double) f2, this.boundingBox.minY + 1.25D, this.posZ + (double) f3, (double) f2 * 1.5D + this.motionX, 4D, (double) f3 * 1.5D + this.motionZ);
		}

		this.setDead();
	}

	public void capturePrey(Entity entity) {
		splorch();

		this.prevPosX = this.posX = entity.posX;
		this.prevPosY = this.posY = entity.posY + 0.0099999997764825821D;
		this.prevPosZ = this.posZ = entity.posZ;
		this.prevRotationYaw = this.rotationYaw = entity.rotationYaw;
		this.prevRotationPitch = this.rotationPitch = entity.rotationPitch;
		this.motionX = entity.motionX;
		this.motionY = entity.motionY;
		this.motionZ = entity.motionZ;

		setSize(entity.width, entity.height);
		setPosition(this.posX, this.posY, this.posZ);

		entity.mountEntity(this);

		this.rotationYaw = this.rand.nextFloat() * 360F;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		Entity entity = damageSource.getEntity();

		if(this.hops == 3 && entity == null && getHealth() > 1) {
			setHealth(1);
		}

		boolean attack_ok = super.attackEntityFrom(damageSource, damage);
		if(attack_ok && this.riddenByEntity != null && (this.riddenByEntity instanceof EntityLivingBase)) {
			EntityLivingBase rider = (EntityLivingBase)this.riddenByEntity;
			if (entity != null && rider == entity) {
				if (this.rand.nextInt(3) == 0) {
					this.kickoff = true;
				}
			} else {
				rider.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
				if(getHealth() <= 0) {
					this.kickoff = true;
				}
			}
		}

		if(attack_ok && getHealth() <= 0) {
			dissolve();
		} else if(attack_ok && entity instanceof EntityLivingBase) {
			EntityLivingBase living_entity = (EntityLivingBase)entity;
			if(living_entity.getHealth() > 0 && (this.riddenByEntity == null || this.riddenByEntity != living_entity)) {
				setAttackTarget(living_entity);
				this.kickoff = true;
			}
		}

		if(isFriendly()) {
			setAttackTarget(null);
		}

		return attack_ok;
	}

	@Override
	public void updateEntityActionState() {
		super.updateEntityActionState();
		this.entityAge++;

		if(isFriendly() && this.riddenByEntity != null) {
			return;
		}

		if(getAttackTarget() == null && this.riddenByEntity == null && getHealth() > 0) {
			if (this.onGround && this.slimeJumpDelay-- <= 0) {
				this.slimeJumpDelay = this.getJumpDelay();

				this.isJumping = true;

				this.motionY = 0.34999999403953552D;

				this.playSound("mob.slime.small", 1F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1F) * 0.8F);

				this.moveStrafing = 1F - this.rand.nextFloat() * 2F;
				this.moveForward = 16F;
			} else {
				this.isJumping = false;

				if (this.onGround) {
					this.moveStrafing = this.moveForward = 0F;
				}
			}
		}
		if (!this.onGround && this.isJumping) {
			this.isJumping = false;
		}

		if (this.getAttackTarget() != null && this.riddenByEntity == null && this.getHealth() > 0) {
			float f = MathHelper.wrapAngleTo180_float(20F);

			if (f > 20F) f = 10F;
			else if (f < -20F) f = -210F;

			this.rotationYaw = f + this.getAttackTarget().rotationYaw + 214F;
			//this.faceEntity(this.getAttackTarget(), 10F, 20F);

			if (this.onGround && this.slimeJumpDelay-- <= 0) {
				this.slimeJumpDelay = this.getJumpDelay();
				this.isJumping = true;
				this.motionY = 0.34999999403953552D;
				this.playSound("mob.slime.small", 1F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1F) * 0.8F);
				this.moveStrafing = 1F - this.rand.nextFloat() * 2F;
				this.moveForward = 16F;
			} else {
				this.isJumping = false;
				if (this.onGround) {
					this.moveStrafing = this.moveForward = 0F;
				}
			}
		}

		if (this.getAttackTarget() != null && this.getAttackTarget().isDead) {
			this.setAttackTarget(null);
		}

		if (!this.onGround && this.motionY < 0.05000000074505806D && this.flutter > 0) {
			this.motionY += 0.070000000298023224D;
			this.flutter--;
		}
		if (this.ticker < 4) {
			this.ticker++;
		} else {
			if (this.onGround && this.riddenByEntity == null && this.hops != 0 && this.hops != 3) {
				this.hops = 0;
			}
			EntityLivingBase attack_target = getAttackTarget();
			if(attack_target == null && this.riddenByEntity == null) {
				EntityLivingBase entity = get_prey();
				if(entity != null) setAttackTarget(entity);
			} else if(attack_target != null && this.riddenByEntity == null) {
				if(getDistanceToEntity(attack_target) <= 9F) {
					if (this.onGround && canEntityBeSeen(attack_target)) {
						splotch();
						this.flutter = 10;
						this.isJumping = true;
						this.moveForward = 16F;
						this.moveStrafing = 1F - this.rand.nextFloat() * 2F;
						this.rotationYaw += 5F * (this.rand.nextFloat() - this.rand.nextFloat());
					}
				} else {
					setAttackTarget(null);
					this.isJumping = false;
					this.moveStrafing = this.moveForward = 0F;
				}
			} else if (this.riddenByEntity != null && this.riddenByEntity != null && this.onGround) {
				switch(this.hops) {
					case 0:
						splotch();
						this.onGround = false;
						this.motionY = 0.34999999403953552D;
						this.moveForward = 0.8F;
						this.hops = 1;
						this.flutter = 5;
						this.rotationYaw += 20F * (this.rand.nextFloat() - this.rand.nextFloat());
						break;
					case 1:
						splotch();
						this.onGround = false;
						this.motionY = 0.44999998807907104D;
						this.moveForward = 0.9F;
						this.hops = 2;
						this.flutter = 5;
						this.rotationYaw += 20F * (this.rand.nextFloat() - this.rand.nextFloat());
						break;
					case 2:
						splotch();
						this.onGround = false;
						this.motionY = 1.25D;
						this.moveForward = 1.25F;
						this.hops = 3;
						this.flutter = 5;
						this.rotationYaw += 20F * (this.rand.nextFloat() - this.rand.nextFloat());
						break;
				}
			}

			this.ticker = 0;
		}

		if (this.onGround && this.hops >= 3) {
			dissolve();
		}
	}

	protected int getJumpDelay() {
		return this.rand.nextInt(20) + 10;
	}

	public void moveEntityWithHeading(float par1, float par2) {
		EntityPlayer rider = this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer ? (EntityPlayer) this.riddenByEntity : null;

		if (rider != null) {
			PlayerAether aetherRider = PlayerAether.get(rider);

			if (aetherRider == null) {
				return;
			}

			setFriendly(aetherRider.getAccessoryInventory().wearingAccessory(new ItemStack(AetherItems.swet_cape)));
			if(isFriendly()) {
				this.prevRotationYaw = this.rotationYaw = rider.rotationYaw;
				this.rotationPitch = rider.rotationPitch * 0.5F;

				this.setRotation(this.rotationYaw, this.rotationPitch);

				this.rotationYawHead = this.renderYawOffset = this.rotationYaw;

				if (this.onGround && this.slimeJumpDelay-- <= 0) {
					par1 = rider.moveStrafing * 2F;
					par2 = rider.moveForward * 2;

					if (par1 != 0f || par2 != 0f) {
						this.jump();
						this.onGround = false;
						this.motionY = 0.7f;
						this.slimeJumpDelay = this.rand.nextInt(4);
					}

					if (aetherRider.isJumping()) {
						this.jump();
						this.onGround = false;
						this.motionY = 1f;
						this.slimeJumpDelay = this.rand.nextInt(15);
					}

					int rotate = MathHelper.floor_double(rider.rotationYaw * 4F / 360F + 0.5D) & 3;

					double x = Math.cos(this.getLookVec().xCoord);
					double z = Math.cos(this.getLookVec().zCoord);

					if (par2 > 0F) {
						this.motionX = this.getLookVec().xCoord / 2;
						this.motionZ = this.getLookVec().zCoord / 2;
					}

					if (par2 < 0F) {
						this.motionX = -this.getLookVec().xCoord / 2;
						this.motionZ = -this.getLookVec().zCoord / 2;
					}

					if (par1 > 0F) {
						if (rotate <= 1) {
							this.motionX = x / 2;
							this.motionZ = z / 2;
						} else {
							this.motionX = -x / 2;
							this.motionZ = -z / 2;
						}
					}

					if (par1 < 0F) {
						if (rotate <= 1) {
							this.motionX = +-x / 2;
							this.motionZ = +-z / 2;
						} else {
							this.motionX = +x / 2;
							this.motionZ = +z / 2;
						}
					}
				} else if (this.onGround) {
					return;
				}

				if (par2 <= 0F) {
					par2 *= 0.25F;
					this.field_110285_bP = 0;
				}

				this.stepHeight = 1F;
				this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

				if (!this.worldObj.isRemote) {
					setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
					super.moveEntityWithHeading(par1, par2);
				}
			} else {
				this.stepHeight = 0.5F;
				this.jumpMovementFactor = 0.02F;
				super.moveEntityWithHeading(this.moveForward, this.moveStrafing);
			}
		} else {
			this.stepHeight = 0.5F;
			this.jumpMovementFactor = 0.02F;
			super.moveEntityWithHeading(this.moveForward, this.moveStrafing);
		}
	}

	@Override
	protected void jump() {
		super.jump();

		playSound("mob.slime.small", 1F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1F) * 0.8F);
	}

	protected void set_swet_type(int id) {
		this.dataWatcher.updateObject(21, (byte)id);
	}

	public SwetType get_swet_type() {
		int id = this.dataWatcher.getWatchableObjectByte(21);
		return SwetType.get(id);
	}

	public void setFriendly(boolean friendly) {
		this.dataWatcher.updateObject(20, (byte) (friendly ? 1 : 0));
	}

	public boolean isFriendly() {
		return this.dataWatcher.getWatchableObjectByte(20) == (byte) 1;
	}

	public void splorch() {
		this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.attack", 0.5F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1F, false);
	}

	public void splotch() {
		this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.slime.small", 0.5F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1F, false);
	}

	@Override
	protected String getHurtSound() {
		return "mob.slime.small";
	}

	@Override
	protected String getDeathSound() {
		return "mob.slime.small";
	}

	@Override
	public void applyEntityCollision(Entity entity) {
		if(this.hops == 0 && !isFriendly() && this.riddenByEntity == null && getAttackTarget() != null && entity != null && entity == getAttackTarget() && (entity.ridingEntity == null || !(entity.ridingEntity instanceof Swet))) {
			capturePrey(entity);
		}

		super.applyEntityCollision(entity);
	}

	@Override
	public boolean interact(EntityPlayer player) {
		if (!this.worldObj.isRemote && this.isFriendly()) {
			if (this.riddenByEntity == null) {
				capturePrey(player);
			} else if (this.riddenByEntity == player) {
				player.mountEntity(null);
			}
		}

		return super.interact(player);
	}

	private EntityLivingBase get_prey() {
		List<Entity> list = (List<Entity>)this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(6D, 6D, 6D));
		for(Entity entity : list) {
			if(!(entity instanceof EntityLivingBase)) continue;
			if(entity instanceof Swet) continue;
			if(isFriendly()) {
				if(entity instanceof EntityPlayer) continue;
			} else {
				if(entity instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer)entity;
					if(player.capabilities.isCreativeMode) continue;
				}
				// Monster
				if(entity instanceof EntityMob) continue;
			}
			return (EntityLivingBase)entity;
		}
		return null;
	}

	@Override
	protected void dropFewItems(boolean recentlyHit, int lootLevel) {
		int count = this.rand.nextInt(3);
		if (lootLevel > 0) {
			count += this.rand.nextInt(lootLevel + 1);
		}
		int id = get_swet_type().get_id();
		entityDropItem(new ItemStack(id == 0 ? AetherBlocks.aercloud : Blocks.glowstone, count, id == 0 ? 1 : 0), 1F);
		entityDropItem(new ItemStack(AetherItems.swet_ball, count), 1F);
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entity) {
		return null;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		compound.setShort("Hops", (short)this.hops);
		compound.setShort("Flutter", (short)this.flutter);
		compound.setBoolean("isFriendly", isFriendly());
		compound.setInteger("swetType", get_swet_type().get_id());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		this.hops = compound.getShort("Hops");
		this.flutter = compound.getShort("Flutter");

		setFriendly(compound.getBoolean("isFriendly"));
		set_swet_type(compound.getInteger("swetType"));
	}

}
