package com.gildedgames.the_aether.world.biome.decoration;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;

public class AetherSkyrootTreeFeature extends WorldGenAbstractTree {

	public AetherSkyrootTreeFeature(boolean notify) {
		super(notify);
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		Block block = world.getBlock(x, y - 1, z);
		if(block != AetherBlocks.aether_grass && block != AetherBlocks.aether_dirt) {
			return false;
		}

		int height = random.nextInt(3) + 4;

		setBlockAndNotifyAdequately(world, x, y - 1, z, AetherBlocks.aether_dirt, 0);

		for (int y1 = (y - 3) + height; y1 <= y + height; y1++) {
			int j2 = y1 - (y + height);
			int i3 = 1 - j2 / 2;
			for (int k3 = x - i3; k3 <= x + i3; k3++) {
				int l3 = k3 - x;
				for (int i4 = z - i3; i4 <= z + i3; i4++) {
					int j4 = i4 - z;
					if ((Math.abs(l3) != i3 || Math.abs(j4) != i3 || random.nextInt(2) != 0 && j2 != 0) && !world.getBlock(k3, y1, i4).isOpaqueCube()) {
						setBlockAndNotifyAdequately(world, k3, y1, i4, AetherBlocks.skyroot_leaves, 0);
					}
				}

			}
		}

		for (int y1 = y; y1 < y + height; y1++) {
			block = world.getBlock(x, y1, z);
			if(block == Blocks.air || block == AetherBlocks.skyroot_leaves) {
				setBlockAndNotifyAdequately(world, x, y1, z, AetherBlocks.skyroot_log, 0);
			}
		}

		return true;
	}

}
