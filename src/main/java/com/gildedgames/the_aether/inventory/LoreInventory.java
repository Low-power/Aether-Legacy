package com.gildedgames.the_aether.inventory;

import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.registry.achievements.AetherAchievements;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;

public class LoreInventory extends InventoryBasic {

	private EntityPlayer player;

	public LoreInventory(EntityPlayer player) {
		super("Lore Item", false, 1);

		this.player = player;
	}

	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		if (stack != null && stack.getItem() == AetherItems.lore_book) {
			this.player.triggerAchievement(AetherAchievements.loreception);
		}

		super.setInventorySlotContents(index, stack);
	}

}
