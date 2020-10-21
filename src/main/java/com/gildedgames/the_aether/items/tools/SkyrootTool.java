package com.gildedgames.the_aether.items.tools;

import com.gildedgames.the_aether.items.util.EnumAetherToolType;
import com.gildedgames.the_aether.blocks.BlocksAether;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SkyrootTool extends ItemAetherTool {

	public SkyrootTool(float damage, EnumAetherToolType toolType) {
		super(damage, ToolMaterial.WOOD, toolType);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == Item.getItemFromBlock(BlocksAether.skyroot_planks);
	}
}
