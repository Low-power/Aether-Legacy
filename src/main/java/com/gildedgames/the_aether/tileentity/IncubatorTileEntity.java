package com.gildedgames.the_aether.tileentity;

import com.gildedgames.the_aether.api.events.AetherHooks;
import com.gildedgames.the_aether.entities.passive.mountable.Moa;
import com.gildedgames.the_aether.registry.achievements.AetherAchievements;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.blocks.container.BlockAetherContainer;
import com.gildedgames.the_aether.items.MoaEgg;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.tileentity.util.AetherTileEntity;
import com.gildedgames.the_aether.util.FilledList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import java.util.List;

public class IncubatorTileEntity extends AetherTileEntity {

	public EntityPlayer owner;

	public int progress;

	public int powerRemaining;

	public int ticksRequired = 5700;

	private final FilledList<ItemStack> incubatorItemStacks = new FilledList<ItemStack>(3, null);

	public IncubatorTileEntity() {
		super("Incubator");
	}

	@Override
	public List<ItemStack> getTileInventory() {
		return this.incubatorItemStacks;
	}

	@Override
	public void onSlotChanged(int index) {
		if (index == 1) {
			this.progress = 0;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		this.progress = compound.getInteger("progress");
		this.powerRemaining = compound.getInteger("powerRemaining");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setInteger("progress", this.progress);
		compound.setInteger("powerRemaining", this.powerRemaining);
	}

	public int getProgressScaled(int i) {
		return (this.progress * i) / this.ticksRequired;
	}

	public int getPowerTimeRemainingScaled(int i) {
		return (this.powerRemaining * i) / 500;
	}

	public boolean isIncubating() {
		return this.powerRemaining > 0;
	}

	@Override
	public void updateEntity() {
		boolean incubating = this.isIncubating();

		if (this.powerRemaining > 0) {
			this.powerRemaining--;

			if (this.getStackInSlot(1) != null) {
				this.progress++;
			}
		}

		if (this.progress >= this.ticksRequired) {
			if (this.getStackInSlot(1).getItem() instanceof MoaEgg) {
				MoaEgg moaEgg = (MoaEgg)this.getStackInSlot(1).getItem();

				if (this.owner != null) {
					this.owner.triggerAchievement(AetherAchievements.incubator);
				}

				if (!this.worldObj.isRemote) {
					Moa moa = new Moa(this.worldObj);
					moa.setPlayerGrown(true);
					moa.setGrowingAge(-24000);
					moa.setMoaType(moaEgg.getMoaTypeFromItemStack(this.getStackInSlot(1)));
					for (int safeY = 0; !this.worldObj.isAirBlock(this.xCoord, this.yCoord + safeY, this.zCoord); safeY++) {
						moa.setPositionAndUpdate(this.xCoord + 0.5D, this.yCoord + safeY + 1.5D, this.zCoord + 0.5D);
					}
					this.worldObj.spawnEntityInWorld(moa);
				}

				AetherHooks.onMoaHatched(moaEgg.getMoaTypeFromItemStack(this.getStackInSlot(1)), this);
			}

			if (!this.worldObj.isRemote) {
				this.decrStackSize(1, 1);
			}

			this.progress = 0;
		}

		if (this.powerRemaining <= 0) {
			ItemStack item_stack_in_slot_0 = getStackInSlot(0);
			ItemStack item_stack_in_slot_1 = getStackInSlot(1);
			if(item_stack_in_slot_0 != null && item_stack_in_slot_1 != null && item_stack_in_slot_0.getItem() == Item.getItemFromBlock(AetherBlocks.ambrosium_torch) && item_stack_in_slot_1.getItem() == AetherItems.moa_egg) {
				this.powerRemaining += 1000;
				if (!this.worldObj.isRemote) {
					decrStackSize(0, 1);
				}
			} else {
				this.powerRemaining = 0;
				this.progress = 0;
			}
		}

		if(incubating != isIncubating()) {
			markDirty();
			BlockAetherContainer.setState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, isIncubating());
		}
	}

	@Override
	public boolean isValidSlotItem(int index, ItemStack itemstack) {
		if(index == 0 && itemstack.getItem() == Item.getItemFromBlock(AetherBlocks.ambrosium_torch)) return true;
		return index == 1 && itemstack.getItem() == AetherItems.moa_egg;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? new int[0] : new int[] { 0, 1 };
	}

}
