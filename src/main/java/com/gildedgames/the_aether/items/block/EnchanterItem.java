package com.gildedgames.the_aether.items.block;

import com.gildedgames.the_aether.AetherConfig;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class EnchanterItem extends MetadataBlockItem {
	public EnchanterItem(Block block) {
		super(block);
	}

	public String getUnlocalizedName(ItemStack item_stack) {
		return AetherConfig.should_use_legacy_altar_name() ? "tile.enchanter" : "tile.altar";
	}

	public String getUnlocalizedName() {
		return AetherConfig.should_use_legacy_altar_name() ? "tile.enchanter" : "tile.altar";
	}
}
