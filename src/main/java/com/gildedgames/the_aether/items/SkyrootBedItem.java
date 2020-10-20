package com.gildedgames.the_aether.items;

import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.registry.creative_tabs.AetherCreativeTabs;
import com.gildedgames.the_aether.blocks.SkyrootBedBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class SkyrootBedItem extends Item {
	public SkyrootBedItem() {
		this.maxStackSize = 1;
		this.setCreativeTab(AetherCreativeTabs.blocks);
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack item_stack, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		if (world.isRemote) return true;
		if (p_77648_7_ != 1) return false;

		++y;
		SkyrootBedBlock blockbed = (SkyrootBedBlock)BlocksAether.skyroot_bed;
		int i1 = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
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

		if (player.canPlayerEdit(x, y, z, p_77648_7_, item_stack) && player.canPlayerEdit(x + x_offset, y, z + z_offset, p_77648_7_, item_stack)) {
			if (world.isAirBlock(x, y, z) && world.isAirBlock(x + x_offset, y, z + z_offset) && World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && World.doesBlockHaveSolidTopSurface(world, x + x_offset, y - 1, z + z_offset)) {
				world.setBlock(x, y, z, blockbed, i1, 3);

				if (world.getBlock(x, y, z) == blockbed) {
					world.setBlock(x + x_offset, y, z + z_offset, blockbed, i1 + 8, 3);
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
