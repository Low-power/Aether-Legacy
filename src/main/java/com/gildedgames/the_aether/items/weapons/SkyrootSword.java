package com.gildedgames.the_aether.items.weapons;

import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class SkyrootSword extends ItemSword {

	public SkyrootSword() {
		super(ToolMaterial.WOOD);
		this.setCreativeTab(AetherCreativeTabs.weapons);
	}

	@Override
	public boolean getIsRepairable(ItemStack repairing_item, ItemStack material) {
		Item item = material.getItem();
		return item == Item.getItemFromBlock(AetherBlocks.golden_oak_log) || item == Item.getItemFromBlock(AetherBlocks.skyroot_log) || item == Item.getItemFromBlock(AetherBlocks.skyroot_planks);
	}

}
