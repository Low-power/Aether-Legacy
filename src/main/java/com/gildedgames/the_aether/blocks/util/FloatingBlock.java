package com.gildedgames.the_aether.blocks.util;

import com.gildedgames.the_aether.entities.block.FloatingBlockEntity;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import java.util.Random;

public class FloatingBlock extends Block {

	private boolean leveled;

	public FloatingBlock(Material material, boolean leveled) {
		super(material);

		this.leveled = leveled;

		setTickRandomly(true);
		setHarvestLevel("pickaxe", 2);
	}

	@Override
	public boolean isBeaconBase(IBlockAccess world, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
		return this == AetherBlocks.enchanted_gravitite;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		world.scheduleBlockUpdate(x, y, z, this, 3);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor_block) {
		world.scheduleBlockUpdate(x, y, z, this, 3);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		if (!this.leveled || world.isBlockIndirectlyGettingPowered(x, y, z)) {
			floatBlock(world, x, y, z);
		}
	}

	private void floatBlock(World world, int x, int y, int z) {
		boolean float_instantly = BlockSand.fallInstantly;
		if (canContinue(world, x, y + 1, z) && y >= 0) {
			if(float_instantly) {
				world.setBlockToAir(x, y, z);
				int bottom_pos = y - 1;
				while(canContinue(world, x, bottom_pos, z) && bottom_pos > 0) {
					bottom_pos--;
				}
				if(bottom_pos > 0) {
					world.setBlock(x, bottom_pos + 1, z, this);
				}
			} else if(!world.isRemote) {
				FloatingBlockEntity entity = new FloatingBlockEntity(world, x, y, z, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
				world.setBlockToAir(x, y, z);
				world.spawnEntityInWorld(entity);
			}
		}
	}

	public static boolean canContinue(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		if (block == Blocks.air || block == Blocks.fire) {
			return true;
		}

		Material material = block.getMaterial();
		if (material == Material.water || material == Material.lava) {
			return true;
		}

		return false;
	}

}
