package com.gildedgames.the_aether.inventory;

import com.gildedgames.the_aether.tileentity.FreezerTileEntity;
import com.gildedgames.the_aether.inventory.slots.SlotFreezer;
import com.gildedgames.the_aether.api.AetherAPI;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import java.util.List;

public class FreezerContainer extends Container {

	private FreezerTileEntity freezer;

	public int progress, ticksRequired, powerRemaining;

	public FreezerContainer(InventoryPlayer inventory, FreezerTileEntity freezer) {
		this.freezer = freezer;

		this.addSlotToContainer(new Slot(freezer, 0, 56, 17));
		this.addSlotToContainer(new Slot(freezer, 1, 56, 53));
		this.addSlotToContainer(new SlotFreezer(freezer, 2, 116, 35));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for(int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting listener) {
		super.addCraftingToCrafters(listener);

		listener.sendProgressBarUpdate(this, 0, this.freezer.progress);
		listener.sendProgressBarUpdate(this, 1, this.freezer.powerRemaining);
		listener.sendProgressBarUpdate(this, 2, this.freezer.ticksRequired);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for(ICrafting crafting : (List<ICrafting>)this.crafters) {
			if (this.progress != this.freezer.progress) {
				crafting.sendProgressBarUpdate(this, 0, this.freezer.progress);
			}
			if (this.powerRemaining != this.freezer.powerRemaining) {
				crafting.sendProgressBarUpdate(this, 1, this.freezer.powerRemaining);
			}
			if (this.ticksRequired != this.freezer.ticksRequired) {
				crafting.sendProgressBarUpdate(this, 2, this.freezer.ticksRequired);
			}
		}

		this.progress = this.freezer.progress;
		this.powerRemaining = this.freezer.powerRemaining;
		this.ticksRequired = this.freezer.ticksRequired;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value) {
		switch(id) {
			case 0:
				this.freezer.progress = value;
				break;
			case 1:
				this.freezer.powerRemaining = value;
				break;
			case 2:
				this.freezer.ticksRequired = value;
				break;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.freezer.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		ItemStack item_stack_copy = null;
		Slot slot = (Slot)this.inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack item_stack = slot.getStack();
			item_stack_copy = item_stack.copy();

			if (i == 2) {
				if(!mergeItemStack(item_stack, 3, 39, true)) {
					return null;
				}

				slot.onSlotChange(item_stack, item_stack_copy);
			} else if (i != 1 && i != 0) {
				if(AetherAPI.instance().hasFreezable(item_stack)) {
					if(!mergeItemStack(item_stack, 0, 1, false)) {
						return null;
					}
				} else if(AetherAPI.instance().isFreezableFuel(item_stack)) {
					if(!mergeItemStack(item_stack, 1, 2, false)) {
						return null;
					}
				} else if (i >= 3 && i < 30) {
					if(!mergeItemStack(item_stack, 30, 39, false)) {
						return null;
					}
				} else if(i >= 30 && i < 39 && !mergeItemStack(item_stack, 3, 30, false)) {
					return null;
				}
			} else if(!mergeItemStack(item_stack, 3, 39, false)) {
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
