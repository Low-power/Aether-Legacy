package com.gildedgames.the_aether.entities.block;

import com.gildedgames.the_aether.blocks.util.FloatingBlock;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import io.netty.buffer.ByteBuf;
import java.util.List;

public class FloatingBlockEntity extends Entity implements IEntityAdditionalSpawnData {
	private Block block;
	private int metadata = 0;
	//private int floating_time = 0;

	public FloatingBlockEntity(World world) {
		super(world);

		this.block = Blocks.air;
		setSize(0.98F, 0.98F);
	}

	public FloatingBlockEntity(World world, int x, int y, int z, Block block, int metadata) {
		super(world);

		this.block = block;
		this.metadata = metadata;
		this.preventEntitySpawning = true;
		this.motionX = this.motionY = this.motionZ = 0;
		setSize(0.98F, 0.98F);
		setPosition(x + 0.5D, y, z + 0.5D);
	}

	@Override
	protected void entityInit() {
	}

	@Override
	public void onUpdate() {
		Block block = get_block();
		if(block == null || block == Blocks.air) {
			setDead();
			return;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		//this.floating_time++;
		this.motionY += 0.04D;
		moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		int x = MathHelper.floor_double(this.posX);
		int y = MathHelper.floor_double(this.posY);
		int z = MathHelper.floor_double(this.posZ);

		List<Entity> nearby_entities = (List<Entity>)this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0D, 1D, 0D));
		for(Entity entity : nearby_entities) {
			if(entity instanceof FloatingBlockEntity) continue;
			entity.setPosition(entity.posX, this.posY + 2.6D, entity.posZ);
			entity.motionY = 0D;
			entity.onGround = true;
			entity.fallDistance = 0F;
		}

		if (this.ticksExisted > 200) {
			if(!this.worldObj.isRemote) {
				entityDropItem(new ItemStack(Item.getItemFromBlock(get_block())), 0F);
			}
			setDead();
			return;
		}

		if (!FloatingBlock.canContinue(this.worldObj, x, y + 1, z)) {
			if (!this.worldObj.isRemote) {
				this.worldObj.setBlock(x, y, z, block);
				setDead();
			}
			this.posX = x + 0.5D;
			this.posY = y;
			this.posZ = z + 0.5D;
		}

		if(!this.worldObj.isRemote && this.posY > this.worldObj.getHeight()) {
			entityDropItem(new ItemStack(Item.getItemFromBlock(get_block())), 0F);
			setDead();
		}
	}

	public void set_block(Block block) {
		this.block = block;
	}

	public Block get_block() {
		return this.block;
	}

	public void set_metadata(int metadata) {
		this.metadata = metadata;
	}

	public int get_metadata() {
		return this.metadata;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public void setDead() {
		super.setDead();
	}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		set_block(Block.getBlockById(compound.getInteger("blockId")));
		set_metadata(compound.getInteger("metadata"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("blockId", Block.getIdFromBlock(get_block()));
		compound.setInteger("metadata", get_metadata());
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(Block.getIdFromBlock(get_block()));
		buffer.writeInt(get_metadata());
	}

	@Override
	public void readSpawnData(ByteBuf buffer) {
		set_block(Block.getBlockById(buffer.readInt()));
		set_metadata(buffer.readInt());
	}

}
