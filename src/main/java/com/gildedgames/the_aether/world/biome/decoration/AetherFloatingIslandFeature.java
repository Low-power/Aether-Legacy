package com.gildedgames.the_aether.world.biome.decoration;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;

public class AetherFloatingIslandFeature extends WorldGenerator {

	public AetherFloatingIslandFeature() {
	}

	private static Block set_crystal_leaves(World world, Random random) {
		if(random.nextInt(3) == 0) {
			return AetherBlocks.crystal_fruit_leaves;
		}
		return AetherBlocks.crystal_leaves;
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		if(y + 11 > world.getHeight()) return false;

		for (int x1 = x - 6; x1 < x + 12; x1++) {
			for (int y1 = y - 6; y1 < y + 17; y1++) {
				for (int z1 = z - 6; z1 < z + 12; z1++) {
					if (world.getBlock(x1, y1, z1) != Blocks.air) {
						return false;
					}
				}
			}
		}

		for (int z1 = -1; z1 < 2; z1++) {
			world.setBlock(x, y, z + z1, AetherBlocks.holystone);
		}
		for (int x1 = -1; x1 < 2; x1++) {
			world.setBlock(x + x1, y, z, AetherBlocks.holystone);
		}

		for (int z1 = -2; z1 < 3; z1++) {
			world.setBlock(x, y + 1, z + z1, AetherBlocks.holystone);
		}
		for (int x1 = -2; x1 < 3; x1++) {
			world.setBlock(x + x1, y + 1, z, AetherBlocks.holystone);
		}
		for (int x1 = -1; x1 < 2; x1++) {
			for (int z1 = 1; z1 > -2; z1--) {
				if (x1 != 0 || z1 != 0) {
					world.setBlock(x + x1, y + 1, z + z1, AetherBlocks.holystone);
				}
			}
		}
		world.setBlock(x + 1, y + 1, z + 1, AetherBlocks.holystone);
		world.setBlock(x - 1, y + 1, z - 1, AetherBlocks.holystone);

		for (int z1 = -2; z1 < 3; z1++) {
			world.setBlock(x, y + 2, z + z1, AetherBlocks.aether_grass);
		}
		for (int x1 = -2; x1 < 3; x1++) {
			world.setBlock(x + x1, y + 2, z, AetherBlocks.aether_grass);
		}
		for (int x1 = -1; x1 < 2; x1++) {
			for (int z1 = 1; z1 > -2; z1--) {
				if (x1 != 0 || z1 != 0) {
					world.setBlock(x + x1, y + 2, z + z1, AetherBlocks.aether_grass);
				}
			}
		}
		world.setBlock(x + 1, y + 2, z + 1, AetherBlocks.aether_grass);
		world.setBlock(x - 1, y + 2, z - 1, AetherBlocks.aether_grass);

		for (int z1 = -2; z1 < 3; z1++) {
			if(z1 == 0) continue;
			world.setBlock(x, y + 5, z + z1, set_crystal_leaves(world, random));
		}
		for (int x1 = -2; x1 < 3; x1++) {
			if(x1 == 0) continue;
			world.setBlock(x + x1, y + 5, z, set_crystal_leaves(world, random));
		}
		for (int x1 = -1; x1 < 2; x1++) {
			if(x1 == 0) continue;
			world.setBlock(x + x1, y + 5, z - 2, set_crystal_leaves(world, random));
			world.setBlock(x + x1, y + 5, z + 2, set_crystal_leaves(world, random));
		}
		for (int z1 = -1; z1 < 2; z1++) {
			if(z1 == 0) continue;
			world.setBlock(x - 2, y + 5, z + z1, set_crystal_leaves(world, random));
			world.setBlock(x + 2, y + 5, z + z1, set_crystal_leaves(world, random));
		}
		for (int x1 = -1; x1 < 2; x1++) {
			for (int z1 = 1; z1 > -2; z1--) {
				if (x1 != 0 || z1 != 0) {
					world.setBlock(x + x1, y + 5, z + z1, set_crystal_leaves(world, random));
				}
			}
		}
		world.setBlock(x + 1, y + 5, z + 1, set_crystal_leaves(world, random));
		world.setBlock(x - 1, y + 5, z - 1, set_crystal_leaves(world, random));

		for (int z1 = -2; z1 < 3; z1++) {
			if(z1 == 0) continue;
			world.setBlock(x, y + 6, z + z1, set_crystal_leaves(world, random));
		}
		for (int x1 = -2; x1 < 3; x1++) {
			if(x1 == 0) continue;
			world.setBlock(x + x1, y + 6, z, set_crystal_leaves(world, random));
		}
		for (int x1 = -1; x1 < 2; x1++) {
			for (int z1 = 1; z1 > -2; z1--) {
				if (x1 != 0 || z1 != 0) {
					world.setBlock(x + x1, y + 6, z + z1, set_crystal_leaves(world, random));
				}
			}
		}
		world.setBlock(x + 1, y + 6, z + 1, set_crystal_leaves(world, random));
		world.setBlock(x - 1, y + 6, z - 1, set_crystal_leaves(world, random));
		for (int z1 = -1; z1 < 2; z1++) {
			if(z1 == 0) continue;
			world.setBlock(x, y + 6, z + z1, set_crystal_leaves(world, random));
		}
		for (int x1 = -1; x1 < 2; x1++) {
			if(x1 == 0) continue;
			world.setBlock(x + x1, y + 6, z, set_crystal_leaves(world, random));
		}

		for (int z1 = -1; z1 < 2; z1++) {
			if(z1 == 0) continue;
			world.setBlock(x, y + 7, z + z1, set_crystal_leaves(world, random));
		}
		for (int x1 = -1; x1 < 2; x1++) {
			if(x1 == 0) continue;
			world.setBlock(x + x1, y + 7, z, set_crystal_leaves(world, random));
		}

		for (int z1 = -2; z1 < 3; z1++) {
			if(z1 == 0) continue;
			world.setBlock(x, y + 8, z + z1, set_crystal_leaves(world, random));
		}
		for (int x1 = -2; x1 < 3; x1++) {
			if(x1 == 0) continue;
			world.setBlock(x + x1, y + 8, z, set_crystal_leaves(world, random));
		}
		for (int x1 = -1; x1 < 2; x1++) {
			for (int z1 = 1; z1 > -2; z1--) {
				if (x1 != 0 || z1 != 0) {
					world.setBlock(x + x1, y + 8, z + z1, set_crystal_leaves(world, random));
				}
			}
		}
		world.setBlock(x + 1, y + 8, z + 1, set_crystal_leaves(world, random));
		world.setBlock(x - 1, y + 8, z - 1, set_crystal_leaves(world, random));

		for (int z1 = -1; z1 < 2; z1++) {
			if(z1 == 0) continue;
			world.setBlock(x, y + 9, z + z1, set_crystal_leaves(world, random));
		}
		for (int x1 = -1; x1 < 2; x1++) {
			if(x1 == 0) continue;
			world.setBlock(x + x1, y + 9, z, set_crystal_leaves(world, random));
		}

		world.setBlock(x, y + 10, z, set_crystal_leaves(world, random));

		for (int y1 = y + 3; y1 <= y + 9; y1++) {
			world.setBlock(x, y1, z, AetherBlocks.skyroot_log);
		}

		return true;
	}
}
