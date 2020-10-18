package com.gildedgames.the_aether.items.tools;

import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.items.util.EnumAetherToolType;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ValkyrieTool extends ItemAetherTool {

	public ValkyrieTool(float damage, EnumAetherToolType toolType) {
		super(damage, ToolMaterial.EMERALD, toolType);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return AetherItems.aether_loot;
	}

}
