package com.gildedgames.the_aether.items.food;

import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AmbrosiumShard extends Item {

	public AmbrosiumShard() {
		setCreativeTab(AetherCreativeTabs.material);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int facing, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem();

		if(world.getBlock(x, y, z) == AetherBlocks.aether_grass) {
			if (!player.capabilities.isCreativeMode) {
				--heldItem.stackSize;
			}
			world.setBlock(x, y, z, AetherBlocks.enchanted_aether_grass);
			return true;
		}

		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		ItemStack heldItem = player.getHeldItem();

		if (player.shouldHeal()) {
			if (!player.capabilities.isCreativeMode) {
				--heldItem.stackSize;
			}
			player.heal(1F);
			return heldItem;
		}

		return super.onItemRightClick(stack, world, player);
	}

}
