package com.gildedgames.the_aether.items.util;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import com.google.common.collect.Sets;
import java.util.Set;

public enum AetherToolType {

	PICKAXE(Sets.newHashSet(new Block[] {
			Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab,
			Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone, Blocks.iron_ore,
			Blocks.iron_block, Blocks.coal_ore, Blocks.gold_ore, Blocks.gold_block,
			Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice, Blocks.netherrack,
			Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore, Blocks.lit_redstone_ore,
			Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.activator_rail, Blocks.mob_spawner,
			AetherBlocks.holystone, AetherBlocks.holystone_brick, AetherBlocks.mossy_holystone,
			AetherBlocks.enchanter, AetherBlocks.incubator, AetherBlocks.enchanter, AetherBlocks.ambrosium_ore,
			AetherBlocks.icestone, AetherBlocks.aerogel, AetherBlocks.carved_stone, AetherBlocks.angelic_stone,
			AetherBlocks.hellfire_stone, AetherBlocks.sentry_stone, AetherBlocks.light_angelic_stone,
			AetherBlocks.light_hellfire_stone
	})) {
		@Override
		public boolean canHarvestBlock(ToolMaterial toolMaterial, Block state) {
			Block block = state;

			if(block == AetherBlocks.zanite_ore || block == AetherBlocks.zanite_block || block == AetherBlocks.icestone) {
				return toolMaterial.getHarvestLevel() >= 1;
			} else if(block == AetherBlocks.gravitite_ore || block == AetherBlocks.enchanted_gravitite) {
				return toolMaterial.getHarvestLevel() >= 2;
			} else if(block == AetherBlocks.aerogel) {
				return toolMaterial.getHarvestLevel() == 3;
			}

			return block == Blocks.obsidian ? toolMaterial.getHarvestLevel() == 3 : (block != Blocks.diamond_block && block != Blocks.diamond_ore ? (block != Blocks.emerald_ore && block != Blocks.emerald_block ? (block != Blocks.gold_block && block != Blocks.gold_ore ? (block != Blocks.iron_block && block != Blocks.iron_ore ? (block != Blocks.lapis_block && block != Blocks.lapis_ore ? (block != Blocks.redstone_ore && block != Blocks.lit_redstone_ore ? (state.getMaterial() == Material.rock ? true : (state.getMaterial() == Material.iron ? true : state.getMaterial() == Material.anvil)) : toolMaterial.getHarvestLevel() >= 2) : toolMaterial.getHarvestLevel() >= 1) : toolMaterial.getHarvestLevel() >= 1) : toolMaterial.getHarvestLevel() >= 2) : toolMaterial.getHarvestLevel() >= 2) : toolMaterial.getHarvestLevel() >= 2);
		}

		@Override
		public float getStrVsBlock(ItemStack stack, Block block) {
			return block != null && (block.getMaterial() == Material.iron || block.getMaterial() == Material.anvil || block.getMaterial() == Material.rock) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(stack, block);
		}
	},
	SHOVEL(Sets.newHashSet(new Block[] {
			Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow,
			Blocks.snow_layer, Blocks.clay, Blocks.farmland, Blocks.soul_sand,
			Blocks.mycelium, AetherBlocks.aether_dirt, AetherBlocks.aether_grass,
			AetherBlocks.aercloud, AetherBlocks.enchanted_aether_grass, AetherBlocks.quicksoil
	})) {
		@Override
		public boolean canHarvestBlock(ToolMaterial toolMaterial, Block block) {
			return block == Blocks.snow ? true : block == Blocks.snow_layer;
		}
	},
	AXE(Sets.newHashSet(new Block[] {
			Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.chest,
			Blocks.double_stone_slab, Blocks.stone_slab, Blocks.pumpkin,
			Blocks.lit_pumpkin, AetherBlocks.skyroot_log, AetherBlocks.golden_oak_log, AetherBlocks.skyroot_planks,
			AetherBlocks.chest_mimic
	})) {
		@Override
		public float getStrVsBlock(ItemStack stack, Block block) {
			return block != null && (block.getMaterial() == Material.wood || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(stack, block);
		}
	};

	private Set<Block> toolBlockSet;

	public float efficiencyOnProperMaterial = 4F;

	AetherToolType(Set<Block> toolBlockSet) {
		this.toolBlockSet = toolBlockSet;
	}

	public Set<Block> getToolBlockSet() {
		return this.toolBlockSet;
	}

	public boolean canHarvestBlock(ToolMaterial toolMaterial, Block block) {
		return false;
	}

	public float getStrVsBlock(ItemStack stack, Block block) {
		return this.toolBlockSet.contains(block) ? this.efficiencyOnProperMaterial : 1F;
	}

}
