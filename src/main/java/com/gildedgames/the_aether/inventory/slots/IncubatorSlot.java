package com.gildedgames.the_aether.inventory.slots;

import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.tileentity.IncubatorTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class IncubatorSlot extends Slot {

	private IncubatorTileEntity incubator;

	private EntityPlayer player;

	public IncubatorSlot(IncubatorTileEntity inv, int slot, int x, int y, EntityPlayer player) {
		super((IInventory) inv, slot, x, y);
		this.incubator = (IncubatorTileEntity) inv;
		this.player = player;
	}

	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() == AetherItems.moa_egg;
	}

	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	public void putStack(ItemStack stack) {
		super.putStack(stack);

		this.incubator.owner = player;
	}

}
