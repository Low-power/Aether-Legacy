package com.gildedgames.the_aether.entities.hostile;

import com.gildedgames.the_aether.entities.projectile.ZephyrSnowballEntity;
import com.gildedgames.the_aether.blocks.BlocksAether;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class Zephyr extends EntityFlying implements IMob {

	public int courseChangeCooldown;

	public double waypoint_x, waypoint_y, waypoint_z;

	public int prevAttackCounter;

	public int attackCounter;

	private final float sound_pitch;

	public Zephyr(World world) {
		super(world);

		this.setSize(4F, 4F);
		this.attackCounter = 0;
		this.sound_pitch = (this.getRNG().nextFloat() - this.getRNG().nextFloat()) * 0.2F + 1F;
	}

	@Override
	public boolean getCanSpawnHere() {
		int x = MathHelper.floor_double(this.posX);
		int y = MathHelper.floor_double(this.boundingBox.minY);
		int z = MathHelper.floor_double(this.posZ);
		if(this.worldObj.getBlock(x, y - 1, z) != BlocksAether.aether_grass) return false;
		return this.rand.nextInt(85) == 0 && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox) && this.worldObj.getBlockLightValue(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY), MathHelper.floor_double(this.posZ)) > 8 && super.getCanSpawnHere();
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 1;
	}

	@Override
	protected void updateEntityActionState() {
		if (!this.worldObj.isRemote && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
			this.setDead();
		}

		this.despawnEntity();
		this.prevAttackCounter = this.attackCounter;
		double x_distance = this.waypoint_x - this.posX;
		double y_distance = this.waypoint_y - this.posY;
		double z_distance = this.waypoint_z - this.posZ;
		double distance = x_distance * x_distance + y_distance * y_distance + z_distance * z_distance;
		if(distance < 1D || distance > 3600D) {
			this.waypoint_x = this.posX + (double)((this.rand.nextFloat() * 2F - 1F) * 16F);
			this.waypoint_y = this.posY + (double)((this.rand.nextFloat() * 2F - 1F) * 16F);
			this.waypoint_z = this.posZ + (double)((this.rand.nextFloat() * 2F - 1F) * 16F);
			x_distance = this.waypoint_x - this.posX;
			y_distance = this.waypoint_y - this.posY;
			z_distance = this.waypoint_z - this.posZ;
			distance = x_distance * x_distance + y_distance * y_distance + z_distance * z_distance;
		}

		if (this.courseChangeCooldown-- <= 0) {
			this.courseChangeCooldown += this.rand.nextInt(5) + 2;
			distance = Math.sqrt(distance);
			if(is_course_traversable(x_distance, y_distance, z_distance, distance)) {
				this.motionX += x_distance / distance * 0.1D;
				this.motionY += y_distance / distance * 0.1D;
				this.motionZ += z_distance / distance * 0.1D;
			} else {
				this.waypoint_x = this.posX;
				this.waypoint_y = this.posY;
				this.waypoint_z = this.posZ;
			}
		}

		this.prevAttackCounter = this.attackCounter;

		if (this.getAttackTarget() == null) {
			if (this.attackCounter > 0) {
				this.attackCounter--;
			}

			this.setAttackTarget(this.worldObj.getClosestVulnerablePlayerToEntity(this, 100D));
		} else {
			if (this.getAttackTarget() instanceof EntityPlayer && (((EntityPlayer) this.getAttackTarget()).capabilities.isCreativeMode)) {
				this.setAttackTarget(null);
				return;
			}

			if (this.getAttackTarget().getDistanceSqToEntity(this) < 4096D && this.canEntityBeSeen(this.getAttackTarget())) {
				double x = this.getAttackTarget().posX - this.posX;
				double y = (this.getAttackTarget().boundingBox.minY + (this.getAttackTarget().height / 2F)) - (this.posY + (this.height / 2F));
				double z = this.getAttackTarget().posZ - this.posZ;

				this.rotationYaw = (-(float) Math.atan2(x, z) * 180F) / 3.141593F;

				++this.attackCounter;

				if (this.attackCounter == 10) {
					this.playSound("aether_legacy:aemob.zephyr.call", 3F, this.sound_pitch);
				} else if (this.attackCounter == 20) {
					this.playSound("aether_legacy:aemob.zephyr.call", 3F, this.sound_pitch);

					ZephyrSnowballEntity projectile = new ZephyrSnowballEntity(this.worldObj, this, x, y, z);
					Vec3 lookVector = this.getLook(1F);

					projectile.posX = this.posX + lookVector.xCoord * 4D;
					projectile.posY = this.posY + (double) (this.height / 2F) + 0.5D;
					projectile.posZ = this.posZ + lookVector.zCoord * 4D;

					if (!this.worldObj.isRemote) {
						projectile.setThrowableHeading(x, y, z, 1.2F, 1F);
						this.worldObj.spawnEntityInWorld(projectile);
					}

					this.attackCounter = -40;
				}
			} else if (this.attackCounter > 0) {
				this.attackCounter--;
			}
		}
	}

	private boolean is_course_traversable(double x_distance, double y_distance, double z_distance, double distance) {
		double x_offset = x_distance / distance;
		double y_offset = y_distance / distance;
		double z_offset = z_distance / distance;
		AxisAlignedBB aabb = this.boundingBox.copy();
		for (int i = 1; (double)i < distance; ++i) {
			aabb.offset(x_offset, y_offset, z_offset);
			if(!this.worldObj.getCollidingBoundingBoxes(this, aabb).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected String getLivingSound() {
		return "aether_legacy:aemob.zephyr.call";
	}

	@Override
	protected String getHurtSound() {
		return "aether_legacy:aemob.zephyr.call";
	}

	@Override
	protected String getDeathSound() {
		return null;
	}

	@Override
	protected void dropFewItems(boolean is_kill_by_player, int loot_modifier) {
		this.dropItem(Item.getItemFromBlock(BlocksAether.aercloud), 1);
	}

	@Override
	public boolean canDespawn() {
		return true;
	}

	@Override
	protected float getSoundVolume() {
		return 1F;
	}

}
