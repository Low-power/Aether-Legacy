package com.gildedgames.the_aether.items.block;

import com.gildedgames.the_aether.blocks.decorative.AetherSlab;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;

public class AetherSlabItem extends ItemSlab {
	public AetherSlabItem(Block block, AetherSlab slab_block, AetherSlab double_slab_block, Boolean is_double_slab) {
		super(block, slab_block, double_slab_block, is_double_slab);
	}

	@Override
	public EnumRarity getRarity(ItemStack item_stack) {
		return this.field_150939_a == AetherBlocks.aerogel_slab ? AetherItems.aether_loot : EnumRarity.common;
	}
}
