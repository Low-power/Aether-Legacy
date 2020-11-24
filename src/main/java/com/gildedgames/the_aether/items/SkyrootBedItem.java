package com.gildedgames.the_aether.items;

import com.gildedgames.the_aether.blocks.SkyrootBedBlock;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class SkyrootBedItem extends Item {
	public SkyrootBedItem() {
		this.maxStackSize = 1;
		setCreativeTab(AetherCreativeTabs.blocks);
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack item_stack, EntityPlayer player, World world, int x, int y, int z, int side, float fx, float fy, float fz) {
		if (world.isRemote) return true;
		if (side != 1) return false;

		SkyrootBedBlock bed_block = (SkyrootBedBlock)AetherBlocks.skyroot_bed;
		int i1 = MathHelper.floor_double((double)(player.rotationYaw * 4F / 360F) + 0.5D) & 3;

		y++;

		byte x_offset = 0;
		byte z_offset = 0;

		switch(i1) {
			case 0:
				z_offset = 1;
				break;
			case 1:
				x_offset = -1;
				break;
			case 2:
				z_offset = -1;
				break;
			case 3:
				x_offset = 1;
				break;
		}

		if (player.canPlayerEdit(x, y, z, side, item_stack) && player.canPlayerEdit(x + x_offset, y, z + z_offset, side, item_stack)) {
			if (world.isAirBlock(x, y, z) && world.isAirBlock(x + x_offset, y, z + z_offset) && World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && World.doesBlockHaveSolidTopSurface(world, x + x_offset, y - 1, z + z_offset)) {
				world.setBlock(x, y, z, bed_block, i1, 3);

				if (world.getBlock(x, y, z) == bed_block) {
					world.setBlock(x + x_offset, y, z + z_offset, bed_block, i1 + 8, 3);
				}

				--item_stack.stackSize;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
