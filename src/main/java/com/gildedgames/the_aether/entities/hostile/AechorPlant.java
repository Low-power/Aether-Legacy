package com.gildedgames.the_aether.entities.hostile;

import com.gildedgames.the_aether.entities.projectile.PoisonNeedleEntity;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.items.util.SkyrootBucketType;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

public class AechorPlant extends EntityCreature {

	public float sinage;

	public int poisonRemaining;

	private int reloadTime;

	public AechorPlant(World world) {
		super(world);

		this.sinage = this.rand.nextFloat() * 6F;
		this.poisonRemaining = this.rand.nextInt(4) + 2;

		setSize(this.rand.nextInt(4) + 1);
		setPosition(this.posX, this.posY, this.posZ);
		setSize(0.75F + ((float)getSize() * 0.125F), 0.5F + ((float)getSize() * 0.075F));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
	}

	@Override
	public void entityInit() {
		super.entityInit();

		dataWatcher.addObject(20, Byte.valueOf((byte)0));
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 3;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.hurtTime > 0) {
			this.sinage += 0.9F;
		} else {
			if (this.getEntityToAttack() != null) {
				this.sinage += 0.3F;
			} else {
				this.sinage += 0.1F;
			}
		}

		if (this.sinage > 3.141593F * 2F) {
			this.sinage -= (3.141593F * 2F);
		}

		if (this.getEntityToAttack() == null) {
			EntityPlayer player = this.worldObj.getClosestVulnerablePlayerToEntity(this, 10F);
			if(player != null) setTarget(player);
		}

		if (!this.isDead && getEntityToAttack() != null) {
			double distanceToPlayer = this.getEntityToAttack().getDistanceToEntity(this);
			double lookDistance = 5.5D + ((double) this.getSize() / 2D);

			if(getEntityToAttack().isDead || distanceToPlayer > lookDistance) {
				setTarget(null);
				this.reloadTime = 0;
			}

			if (this.reloadTime == 20 && canEntityBeSeen(getEntityToAttack())) {
				shootAtPlayer();
				this.reloadTime = -10;
			}

			if (this.reloadTime != 20) {
				++this.reloadTime;
			}
		}

		if(this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) - 1, MathHelper.floor_double(this.posZ)) != AetherBlocks.aether_grass) {
			setDead();
		}
	}

	public void shootAtPlayer() {
		if (this.worldObj.difficultySetting.equals(EnumDifficulty.PEACEFUL)) {
			return;
		}

		Entity attack_target = getEntityToAttack();
		double x = attack_target.posX - this.posX;
		double z = attack_target.posZ - this.posZ;
		double y = 0.1D + (Math.sqrt((x * x) + (z * z) + 0.1D) * 0.5D) + ((this.posY - this.getEntityToAttack().posY) * 0.25D);

		double distance = 1.5D / Math.sqrt((x * x) + (z * z) + 0.1D);

		x *= distance;
		z *= distance;

		PoisonNeedleEntity poison_needle = new PoisonNeedleEntity(this.worldObj, this, 0.5F);
		poison_needle.posY = this.posY + 1D;
		playSound("random.bow", 1F, 1.2F / (getRNG().nextFloat() * 0.2F + 0.9F));
		this.worldObj.spawnEntityInWorld(poison_needle);
		poison_needle.setThrowableHeading(x, y, z, 0.285F + ((float)y * 0.05F), 1F);
	}

	@Override
	public void knockBack(Entity entity, float strength, double xRatio, double zRatio) {
		if (this.getHealth() >= 0) {
			return;
		}

		super.knockBack(entity, strength, xRatio, zRatio);
	}

	@Override
	public boolean interact(EntityPlayer player) {
		ItemStack heldItem = player.getCurrentEquippedItem();
		if (heldItem != null && !this.worldObj.isRemote) {
			if (heldItem.getItem() == AetherItems.skyroot_bucket && SkyrootBucketType.getType(heldItem.getItemDamage()) == SkyrootBucketType.Empty && this.poisonRemaining > 0) {
				if (--heldItem.stackSize == 0) {
					player.setCurrentItemOrArmor(0, new ItemStack(AetherItems.skyroot_bucket, 1, SkyrootBucketType.Poison.meta));
				} else if (!player.inventory.addItemStackToInventory(new ItemStack(AetherItems.skyroot_bucket, 1, SkyrootBucketType.Poison.meta))) {
					player.entityDropItem(new ItemStack(AetherItems.skyroot_bucket, 1, SkyrootBucketType.Poison.meta), 1F);
				}
				--this.poisonRemaining;
			}
		}
		return false;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		compound.setByte("size", this.getSize());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		this.setSize(compound.getByte("size"));
	}

	public void setSize(int size) {
		this.dataWatcher.updateObject(20, (byte) size);
	}

	public byte getSize() {
		return this.dataWatcher.getWatchableObjectByte(20);
	}

	@Override
	protected void dropFewItems(boolean var1, int var2) {
		this.dropItem(AetherItems.aechor_petal, 2);
	}

	@Override
	public void applyEntityCollision(Entity entity) {

	}

	@Override
	public void addVelocity(double x, double y, double z) {

	}

	@Override
	protected boolean isMovementBlocked() {
		return true;
	}

	@Override
	protected String getHurtSound() {
		return "game.player.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "game.player.hurt.fall.big";
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected boolean canDespawn() {
		return true;
	}

}
