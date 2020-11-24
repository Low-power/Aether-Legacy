package com.gildedgames.the_aether.items.tools;

import com.gildedgames.the_aether.items.util.AetherToolType;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SkyrootTool extends AetherTool {

	public SkyrootTool(float damage, AetherToolType toolType) {
		super(damage, ToolMaterial.WOOD, toolType);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == Item.getItemFromBlock(AetherBlocks.skyroot_planks);
	}
}
