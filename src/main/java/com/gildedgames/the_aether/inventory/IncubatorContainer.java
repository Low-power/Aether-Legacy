package com.gildedgames.the_aether.inventory;

import com.gildedgames.the_aether.tileentity.IncubatorTileEntity;
import com.gildedgames.the_aether.inventory.slots.IncubatorSlot;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.List;

public class IncubatorContainer extends Container {

	private int progress, powerRemaining;

	private IncubatorTileEntity incubator;

	public IncubatorContainer(EntityPlayer player, InventoryPlayer inventory, IncubatorTileEntity incubator) {
		this.incubator = incubator;

		this.addSlotToContainer(new IncubatorSlot(incubator, 1, 73, 17, player));
		this.addSlotToContainer(new Slot(incubator, 0, 73, 53));

		for (int j = 0; j < 3; ++j) {
			for (int k = 0; k < 9; ++k) {
				this.addSlotToContainer(new Slot(inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
			}
		}

		for (int j = 0; j < 9; ++j) {
			this.addSlotToContainer(new Slot(inventory, j, 8 + j * 18, 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting listener) {
		super.addCraftingToCrafters(listener);

		listener.sendProgressBarUpdate(this, 0, this.incubator.progress);
		listener.sendProgressBarUpdate(this, 1, this.incubator.powerRemaining);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for(ICrafting crafting : (List<ICrafting>)this.crafters) {
			if (this.progress != this.incubator.progress) {
				crafting.sendProgressBarUpdate(this, 0, this.incubator.progress);
			}
			if (this.powerRemaining != this.incubator.powerRemaining) {
				crafting.sendProgressBarUpdate(this, 1, this.incubator.powerRemaining);
			}
		}

		this.progress = this.incubator.progress;
		this.powerRemaining = this.incubator.powerRemaining;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value) {
		if (id == 0) {
			this.incubator.progress = value;
		}

		if (id == 1) {
			this.incubator.powerRemaining = value;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.incubator.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack item_stack_copy = null;
		Slot slot = (Slot)this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack item_stack = slot.getStack();
			item_stack_copy = item_stack.copy();

			if (index != 1 && index != 0) {
				Item item = item_stack_copy.getItem();
				if(item == Item.getItemFromBlock(AetherBlocks.ambrosium_torch) && mergeItemStack(item_stack, 1, 2, false)) {
					return item_stack_copy;
				} else if(item == AetherItems.moa_egg && mergeItemStack(item_stack, 0, 1, false)) {
					return item_stack_copy;
				} else if (index >= 2 && index < 29) {
					if(!mergeItemStack(item_stack, 29, 38, false)) {
						return null;
					}
				} else if(index >= 29 && index < 37 && !mergeItemStack(item_stack, 2, 29, false)) {
					return null;
				}
			} else if(!mergeItemStack(item_stack, 2, 38, false)) {
				return null;
			}

			if(item_stack.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if(item_stack.stackSize == item_stack_copy.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, item_stack);
		}

		return item_stack_copy;
	}

}
