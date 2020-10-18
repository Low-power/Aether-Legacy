package com.gildedgames.the_aether.items.food;

import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.registry.creative_tabs.AetherCreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class AetherFoodItem extends ItemFood {

	public AetherFoodItem(int healAmmount) {
		super(healAmmount, false);
		this.setCreativeTab(AetherCreativeTabs.food);
	}

	public AetherFoodItem(int healAmmount, float saturationAmmount) {
		super(healAmmount, saturationAmmount, false);
		this.setCreativeTab(AetherCreativeTabs.food);
	}

	@Override
	public EnumRarity getRarity(ItemStack p_77613_1_) {
		return p_77613_1_.getItem() == AetherItems.enchanted_blueberry ? EnumRarity.rare : super.getRarity(p_77613_1_);
	}
}
