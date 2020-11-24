package com.gildedgames.the_aether.world.biome.decoration;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.World;
import java.util.Random;

public class AetherDungeonOakTreeFeature extends WorldGenAbstractTree {

	public AetherDungeonOakTreeFeature() {
		super(true);
	}

	private static void branch(World world, Random random, int x, int y, int z, int slant) {
		int x_direction = random.nextInt(3) - 1;
		int y_direction = slant;
		int z_direction = random.nextInt(3) - 1;
		x += x_direction;
		y += y_direction;
		z += z_direction;
		int branch_x = x - x_direction;
		int branch_z = z - z_direction;

		if(world.getBlock(x, y, z) == AetherBlocks.golden_oak_leaves) {
			world.setBlock(x, y, z, AetherBlocks.golden_oak_log);
			world.setBlock(branch_x, y, branch_z, AetherBlocks.golden_oak_log);
		}
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		if(world.getBlock(x, y - 1, z) != AetherBlocks.aether_grass || world.getBlockMetadata(x, y - 1, z) != 3) {
			return false;
		}

		int height = 9;

		for (int x1 = x - 3; x1 < x + 4; x1++) {
			for (int y1 = y + 5; y1 < y + 12; y1++) {
				for (int z1 = z - 3; z1 < z + 4; z1++) {
					if ((x1 - x) * (x1 - x) + (y1 - y - 8) * (y1 - y - 8) + (z1 - z) * (z1 - z) < 12 + random.nextInt(5) && world.isAirBlock(x1, y1, z1)) {
						world.setBlock(x1, y1, z1, AetherBlocks.golden_oak_leaves);
					}
				}
			}
		}

		for (int n = 0; n < height; n++) {
			if(n > 4 && random.nextInt(3) > 1) {
				branch(world, random, x, y + n, z, n / 4 - 1);
			}
			world.setBlock(x, y + n, z, AetherBlocks.golden_oak_log);
		}

		return true;
	}

}
