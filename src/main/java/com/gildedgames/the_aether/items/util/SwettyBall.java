package com.gildedgames.the_aether.items.util;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SwettyBall extends Item {

	public SwettyBall(CreativeTabs tab) {
		setCreativeTab(tab);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int facing, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem();
		Block block = world.getBlock(x, y, z);
		if(block == AetherBlocks.aether_dirt) {
			block = AetherBlocks.aether_grass;
		} else if(block == Blocks.dirt) {
			block = Blocks.grass;
		} else {
			return false;
		}
		world.setBlock(x, y, z, block);

		if (!player.capabilities.isCreativeMode) {
			--heldItem.stackSize;
		}

		return true;
	}

}
