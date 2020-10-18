package com.gildedgames.the_aether.items.dungeon;

import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.registry.creative_tabs.AetherCreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class VictoryMedal extends Item {

	public VictoryMedal() {
		super();

		this.setMaxStackSize(10);
		this.setCreativeTab(AetherCreativeTabs.misc);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return AetherItems.aether_loot;
	}

}
