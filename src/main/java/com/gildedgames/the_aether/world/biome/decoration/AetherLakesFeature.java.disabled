package com.gildedgames.the_aether.world.biome.decoration;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import java.util.Random;

public class AetherLakesFeature extends WorldGenerator {

	public AetherLakesFeature() {
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		if(block == AetherBlocks.carved_stone) return false;
		if(block == AetherBlocks.locked_carved_stone) return false;
		if(block == AetherBlocks.angelic_stone) return false;
		if(block == AetherBlocks.locked_angelic_stone) return false;
		if(block == AetherBlocks.hellfire_stone) return false;
		if(block == AetherBlocks.locked_hellfire_stone) return false;

		x -= 8;
		z -= 8;
		while(y > 5 && world.isAirBlock(x, y, z)) y--;
		if(y <= 4) return false;
		y -= 4;

		boolean[] aboolean = new boolean[2048];
		int l = random.nextInt(4) + 4;

		for(int i = 0; i < l; i++) {
			double d0 = random.nextDouble() * 6D + 3D;
			double d1 = random.nextDouble() * 4D + 2D;
			double d2 = random.nextDouble() * 6D + 3D;
			double d3 = random.nextDouble() * (16D - d0 - 2D) + 1D + d0 / 2D;
			double d4 = random.nextDouble() * (8D - d1 - 4D) + 2D + d1 / 2D;
			double d5 = random.nextDouble() * (16D - d2 - 2D) + 1D + d2 / 2D;

			for (int k1 = 1; k1 < 15; ++k1) {
				for (int l1 = 1; l1 < 15; ++l1) {
					for (int i2 = 1; i2 < 7; ++i2) {
						double d6 = ((double)k1 - d3) / (d0 / 2D);
						double d7 = ((double)i2 - d4) / (d1 / 2D);
						double d8 = ((double)l1 - d5) / (d2 / 2D);
						double d9 = d6 * d6 + d7 * d7 + d8 * d8;

						if (d9 < 1D) {
							aboolean[(k1 * 16 + l1) * 8 + i2] = true;
						}
					}
				}
			}
		}

		int j1;
		int j2;

		for (int i = 0; i < 16; i++) {
			for (j2 = 0; j2 < 16; ++j2) {
				for (j1 = 0; j1 < 8; ++j1) {
					boolean flag = !aboolean[(i * 16 + j2) * 8 + j1] && (i < 15 && aboolean[((i + 1) * 16 + j2) * 8 + j1] || i > 0 && aboolean[((i - 1) * 16 + j2) * 8 + j1] || j2 < 15 && aboolean[(i * 16 + j2 + 1) * 8 + j1] || j2 > 0 && aboolean[(i * 16 + (j2 - 1)) * 8 + j1] || j1 < 7 && aboolean[(i * 16 + j2) * 8 + j1 + 1] || j1 > 0 && aboolean[(i * 16 + j2) * 8 + (j1 - 1)]);
					if (flag) {
						Material material = world.getBlock(x + i, y + j1, z + j2).getMaterial();

						if (j1 >= 4 && material.isLiquid()) {
							return false;
						}

						if (j1 < 4 && !material.isSolid() && world.getBlock(x + i, y + j1, z + j2) != Blocks.water) {
							return false;
						}
					}
				}
			}
		}

		for (int i = 0; i < 16; i++) {
			for (j2 = 0; j2 < 16; ++j2) {
				for (j1 = 0; j1 < 8; ++j1) {
					if (aboolean[(i * 16 + j2) * 8 + j1]) {
						world.setBlock(x + i, y + j1, z + j2, j1 >= 4 ? Blocks.air : Blocks.water, 0, 2);
					}
				}
			}
		}

		for (int i = 0; i < 16; i++) {
			for (j2 = 0; j2 < 16; ++j2) {
				for (j1 = 4; j1 < 8; ++j1) {
					if(aboolean[(i * 16 + j2) * 8 + j1] && world.getBlock(x + i, y + j1 - 1, z + j2) == AetherBlocks.aether_dirt && world.getSavedLightValue(EnumSkyBlock.Sky, x + i, y + j1, z + j2) > 0) {
						world.setBlock(x + i, y + j1 - 1, z + j2, AetherBlocks.aether_grass, 0, 2);
					}
				}
			}
		}

		for (int i = 0; i < 16; i++) {
			for (j2 = 0; j2 < 16; ++j2) {
				byte b0 = 4;

				if (world.isBlockFreezable(x + i, y + b0, z + j2)) {
					world.setBlock(x + i, y + b0, z + j2, Blocks.ice, 0, 2);
				}
			}
		}

		return true;
	}
}
