package com.gildedgames.the_aether.entities.projectile;

import cpw.mods.fml.common.registry.IThrowableEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import java.util.List;

public abstract class BaseProjectileEntity extends Entity implements IProjectile, IThrowableEntity {

	public int xTile = -1;
	public int yTile = -1;
	public int zTile = -1;
	public Block inTile;
	public boolean inGround;
	public int throwableShake;
	public int canBePickedUp;
	private EntityLivingBase thrower;
	private String throwerName;
	public int ticksInGround;
	public int ticksInAir;
	private int stay_ticks;

	public BaseProjectileEntity(World world) {
		super(world);

		this.yOffset = 0F;
		this.setSize(0.25F, 0.25F);
	}

	public BaseProjectileEntity(World world, double x, double y, double z) {
		this(world);

		this.setPosition(x, y, z);
	}

	public BaseProjectileEntity(World world, EntityLivingBase thrower) {
		this(world, thrower.posX, thrower.posY + (double) thrower.getEyeHeight() - 0.10000000149011612D, thrower.posZ);

		this.thrower = thrower;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		double d1 = this.boundingBox.getAverageEdgeLength() * 4D;
		d1 *= 64D;
		return distance < d1 * d1;
	}

	public void shoot(Entity thrower, float rotation_pitch, float rotation_yaw, float pitch_offset, float velocity, float inaccuracy) {
		float x = -MathHelper.sin(rotation_yaw * 0.017453292F) * MathHelper.cos(rotation_pitch * 0.017453292F);
		float y = -MathHelper.sin((rotation_pitch + pitch_offset) * 0.017453292F);
		float z = MathHelper.cos(rotation_yaw * 0.017453292F) * MathHelper.cos(rotation_pitch * 0.017453292F);
		setThrowableHeading(x, y, z, velocity, inaccuracy);
		this.motionX += thrower.motionX;
		this.motionZ += thrower.motionZ;
		if (!thrower.onGround) {
			this.motionY += thrower.motionY;
		}
	}

	@Override
	public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
		float distance = MathHelper.sqrt_double(x * x + y * y + z * z);
		x /= (double)distance;
		y /= (double)distance;
		z /= (double)distance;
		x += this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
		y += this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
		z += this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
		x *= (double)velocity;
		y *= (double)velocity;
		z *= (double)velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		distance = MathHelper.sqrt_double(x * x + z * z);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, distance) * 180D / Math.PI);
		this.ticksInGround = 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		if (this.prevRotationPitch == 0F && this.prevRotationYaw == 0F) {
			float distance = MathHelper.sqrt_double(x * x + z * z);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, distance) * 180D / Math.PI);
		}
	}

	@Override
	public void onUpdate() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		super.onUpdate();

		if (this.throwableShake > 0) {
			--this.throwableShake;
		}

		if (this.inGround) {
			if (this.worldObj.getBlock(this.xTile, this.yTile, this.zTile) == this.inTile) {
				if(++this.ticksInGround > 1200) {
					setDead();
				}
				return;
			}

			this.inGround = false;
			this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
			this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
			this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
			this.ticksInGround = 0;
			this.ticksInAir = 0;
		} else {
			++this.ticksInAir;
			if(Math.abs(this.motionX) < 1e-100 && Math.abs(this.motionY) < 1e-100 && Math.abs(this.motionZ) < 1e-100) {
				if(++this.stay_ticks > 400) {
					setDead();
					return;
				}
			} else {
				this.stay_ticks = 0;
			}
		}

		float bounding_box_expansion = getBoundingBoxExpansion();
		Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
		Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX + bounding_box_expansion, this.posY + this.motionY + bounding_box_expansion, this.posZ + this.motionZ + bounding_box_expansion);
		MovingObjectPosition position = this.worldObj.rayTraceBlocks(vec3, vec31);
		vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
		vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (position != null) {
			vec31 = Vec3.createVectorHelper(position.hitVec.xCoord, position.hitVec.yCoord, position.hitVec.zCoord);
		}

		if (!this.worldObj.isRemote) {
			Entity entity = null;
			List<Entity> entity_list = (List<Entity>)this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(bounding_box_expansion, bounding_box_expansion, bounding_box_expansion));
			double distance = 0D;
			EntityLivingBase thrower = getThrower();
			for(Entity e : entity_list) {
				if(!e.canBeCollidedWith()) continue;
				if(e == thrower && this.ticksInAir < 5) continue;

				double expansion = bounding_box_expansion + 0.3D;
				AxisAlignedBB aabb = e.boundingBox.expand(expansion, expansion, expansion);
				MovingObjectPosition position1 = aabb.calculateIntercept(vec3, vec31);
				if(position1 != null) {
					double d1 = vec3.distanceTo(position1.hitVec);
					if (d1 < distance || distance <= 0D) {
						entity = e;
						distance = d1;
					}
				}
			}
			if (entity != null) {
				position = new MovingObjectPosition(entity);
			}
		}

		if (position != null) {
			if (position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlock(position.blockX, position.blockY, position.blockZ) == Blocks.portal) {
				setInPortal();
			} else {
				onImpact(position);
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		double motion_distance = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180D / Math.PI);
		this.rotationPitch = (float)(Math.atan2(this.motionY, motion_distance) * 180.0D / Math.PI);
		while(this.rotationPitch - this.prevRotationPitch < -180F) {
			this.prevRotationPitch -= 360F;
		}
		while(this.rotationPitch - this.prevRotationPitch >= 180F) {
			this.prevRotationPitch += 360F;
		}
		while(this.rotationYaw - this.prevRotationYaw < -180F) {
			this.prevRotationYaw -= 360F;
		}
		while(this.rotationYaw - this.prevRotationYaw >= 180F) {
			this.prevRotationYaw += 360F;
		}
		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		double rate = 0.99D;
		if(isInWater()) {
			double size = 0.25D;
			for (int i = 0; i < 4; ++i) {
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * size, this.posY - this.motionY * size, this.posZ - this.motionZ * size, this.motionX, this.motionY, this.motionZ);
			}
			rate = 0.8D;
		}
		this.motionX *= rate;
		this.motionY *= rate;
		this.motionZ *= rate;
		this.motionY -= getGravityVelocity();
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	protected float getBoundingBoxExpansion() {
		return 1F;
	}

	protected float getGravityVelocity() {
		return 0.03F;
	}

	protected abstract void onImpact(MovingObjectPosition position);

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setShort("xTile", (short) this.xTile);
		compound.setShort("yTile", (short) this.yTile);
		compound.setShort("zTile", (short) this.zTile);
		compound.setByte("inTile", (byte) Block.getIdFromBlock(this.inTile));
		compound.setByte("shake", (byte) this.throwableShake);
		compound.setByte("inGround", (byte) (this.inGround ? 1 : 0));

		if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower != null && this.thrower instanceof EntityPlayer) {
			this.throwerName = this.thrower.getCommandSenderName();
		}

		compound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		this.xTile = compound.getShort("xTile");
		this.yTile = compound.getShort("yTile");
		this.zTile = compound.getShort("zTile");
		this.inTile = Block.getBlockById(compound.getByte("inTile") & 255);
		this.throwableShake = compound.getByte("shake") & 255;
		this.inGround = compound.getByte("inGround") == 1;
		this.throwerName = compound.getString("ownerName");

		if (this.throwerName != null && this.throwerName.length() == 0) {
			this.throwerName = null;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0F;
	}

	@Override
	public void setThrower(Entity entity) {
		if (entity instanceof EntityLivingBase) {
			this.thrower = (EntityLivingBase) entity;
		}
	}

	@Override
	public EntityLivingBase getThrower() {
		if (this.thrower == null && this.throwerName != null && this.throwerName.length() > 0) {
			this.thrower = this.worldObj.getPlayerEntityByName(this.throwerName);
		}

		return this.thrower;
	}
}
