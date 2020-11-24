package com.gildedgames.the_aether.items;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class AetherPortalFrame extends Item {
	public AetherPortalFrame() {
		setMaxStackSize(1);
	}

	private static boolean create_portal_frame(World world, int x, int y, int z, int facing) {
		final int posX = x;
		final int posY = world.getBlock(x, y, z).isReplaceable(world, x, y, z) ? y + 1 : y + 2;
		final int posZ = z;

		for (int zi = 0; zi < 4; ++zi) {
			for (int yi = -1; yi < 4; ++yi) {
				final int blockX = posX + (facing == 2 || facing == 0 ? zi - 1 : 0);
				final int blockY = posY + yi;
				final int blockZ = posZ + (facing == 2 || facing == 0 ? 0 : zi - 1);
				if(world.getBlock(blockX, blockY, blockZ) != Blocks.air && !world.getBlock(blockX, blockY, blockZ).isReplaceable(world, blockX, blockY, blockZ)) {
					return false;
				}
			}
		}

		final Block frame_block = Blocks.glowstone;
		final Block portal_block = AetherBlocks.aether_portal;

		for (int zi = 1; zi < 3; ++zi) {
			for (int yi = -1; yi < 3; ++yi) {
				final int blockX = posX + (facing == 2 || facing == 0 ? zi - 1 : 0);
				final int blockY = posY + yi;
				final int blockZ = posZ + (facing == 2 || facing == 0 ? 0 : zi - 1);
				world.setBlock(blockX, blockY, blockZ, Blocks.air);
			}
		}

		for (int zi = 0; zi < 4; ++zi) {
			for (int yi = -1; yi < 4; ++yi) {
				final int blockX = posX + (facing == 2 || facing == 0 ? zi - 1 : 0);
				final int blockY = posY + yi;
				final int blockZ = posZ + (facing == 2 || facing == 0 ? 0 : zi - 1);
				world.setBlock(blockX, blockY, blockZ, frame_block);
			}
		}

		for (int zi = 1; zi < 3; ++zi) {
			for (int yi = 0; yi < 3; ++yi) {
				final int blockX = posX + (facing == 2 || facing == 0 ? zi - 1 : 0);
				final int blockY = posY + yi;
				final int blockZ = posZ + (facing == 2 || facing == 0 ? 0 : zi - 1);
				world.setBlock(blockX, blockY, blockZ, portal_block);
			}
		}

		return true;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;
		ItemStack heldItem = player.getHeldItem();
		if (!player.canPlayerEdit(x, y, z, facing, heldItem)) return false;
		facing = MathHelper.floor_double((double)(player.rotationYaw * 4F / 360F) + 0.5D) & 3;
		if(create_portal_frame(world, x, y, z, facing)) {
			if (!player.capabilities.isCreativeMode) {
				heldItem.stackSize--;
			}
		}
		return true;
	}
}
