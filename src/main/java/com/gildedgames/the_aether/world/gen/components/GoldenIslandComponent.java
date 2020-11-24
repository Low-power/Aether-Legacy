package com.gildedgames.the_aether.world.gen.components;

import com.gildedgames.the_aether.world.gen.AetherGenUtils;
import com.gildedgames.the_aether.world.gen.AetherStructure;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public class GoldenIslandComponent extends AetherStructure {

	public GoldenIslandComponent() {
	}

	public GoldenIslandComponent(int chunkX, int chunkZ) {
		this.coordBaseMode = 0;
		this.boundingBox = new StructureBoundingBox(chunkX, 80, chunkZ, chunkX + 100, 220, chunkZ + 100);
	}

	@Override
	public boolean generate() {
		this.replaceAir = true;
		this.replaceSolid = true;
		this.setStructureOffset(60, 0, 60);
		this.generateBaseIsland();
		return true;
	}

	public void generateBaseIsland() {
		int i1 = 0;
		int size = 21;
		for (int x = -size; x <= size; x++) {
			for (int y = 24; y >= -size; y--) {
				for (int z = -size; z <= size; z++) {
					Block block = getBlockStateWithOffset(x, y, z);
					if(block == AetherBlocks.locked_angelic_stone && ++i1 > 24 / 2) {
						return;
					}
				}

			}
		}

		for (int i2 = -24; i2 <= 24; i2++) {
			for (int l2 = 24; l2 >= -24; l2--) {
				for (int i3 = -24; i3 <= 24; i3++) {
					int k3 = MathHelper.floor_double(i2 * (1 + l2 / 240) / 0.8D);
					int i4 = l2;

					if (l2 > 15) {
						i4 = MathHelper.floor_double((double) i4 * 1.375D);
						i4 -= 6;
					} else if (l2 < -15) {
						i4 = MathHelper.floor_double((double) i4 * 1.3500000238418579D);
						i4 += 6;
					}

					int k4 = MathHelper.floor_double(i3 * (1 + l2 / 240) / 0.8D);

					if (Math.sqrt(k3 * k3 + i4 * i4 + k4 * k4) <= 24D) {
						if(AetherBlocks.is_empty(getBlockStateWithOffset(i2, l2 + 1, i3)) && l2 > 4) {
							setBlockWithOffset(i2, l2, i3, AetherBlocks.aether_grass, 3);
							setBlockWithOffset(i2, l2 - 1, i3, AetherBlocks.aether_dirt, 0);
							setBlockWithOffset(i2, l2 - (1 + this.random.nextInt(2)), i3, AetherBlocks.aether_dirt, 0);

							if (l2 >= 24 / 2) {
								int j5 = this.random.nextInt(48);

								if (j5 < 2) {
									//AetherGenUtils.generateGoldenOakTree(this, i2, l2 + 1, i3);
								} else if (j5 == 3) {
									if (this.random.nextInt(2) == 0) {
										//new WorldGenLakes(Blocks.FLOWING_WATER).generate(world, random, new BlockPos.MutableBlockPos((i2 + i + random.nextInt(3)) - random.nextInt(3), l2 + j, (i3 + k + random.nextInt(3)) - random.nextInt(3)));
									}
								} else if (j5 == 4) {
									if (this.random.nextInt(2) == 0) {
										AetherGenUtils.generateFlower(this, Blocks.red_flower, 0, (i2 + this.random.nextInt(3)) - this.random.nextInt(3), l2 + 1, (i3 + this.random.nextInt(3)) - this.random.nextInt(3));
									} else {
										AetherGenUtils.generateFlower(this, Blocks.yellow_flower, 0, (i2 + this.random.nextInt(3)) - this.random.nextInt(3), l2 + 1, (i3 + this.random.nextInt(3)) - this.random.nextInt(3));
									}
								}
							}
						} else if(AetherBlocks.is_empty(getBlockStateWithOffset(i2, l2, i3))) {
							setBlockWithOffset(i2, l2, i3, AetherBlocks.holystone, 0);
						}
					}
				}

			}

		}

		int l3 = MathHelper.floor_double(24D * 0.75D);

		for (int j4 = 0; j4 < l3; j4++) {
			int i5 = this.random.nextInt(24) - this.random.nextInt(24);
			int l5 = this.random.nextInt(24) - this.random.nextInt(24);
			int j6 = this.random.nextInt(24) - this.random.nextInt(24);

			this.generateCaves(i5, l5, j6, 24 + l3 / 3);
		}
	}

	public void generateCaves(int x, int y, int z, int size) {
		float f = this.random.nextFloat() * 3.141593F;

		double d = (float) (x + 8) + (MathHelper.sin(f) * (float) size) / 8F;
		double d1 = (float) (x + 8) - (MathHelper.sin(f) * (float) size) / 8F;
		double d2 = (float) (z + 8) + (MathHelper.cos(f) * (float) size) / 8F;
		double d3 = (float) (z + 8) - (MathHelper.cos(f) * (float) size) / 8F;
		double d4 = y + this.random.nextInt(3) + 2;
		double d5 = y + this.random.nextInt(3) + 2;

		for (int l = 0; l <= size; l++) {
			double d6 = d + ((d1 - d) * (double) l) / (double) size;
			double d7 = d4 + ((d5 - d4) * (double) l) / (double) size;
			double d8 = d2 + ((d3 - d2) * (double) l) / (double) size;
			double d9 = (this.random.nextDouble() * (double) size) / 16D;
			double d10 = (double)(MathHelper.sin(((float)l * 3.141593F) / (float)size) + 1F) * d9 + 1D;
			double d11 = (double)(MathHelper.sin(((float)l * 3.141593F) / (float)size) + 1F) * d9 + 1D;
			int i1 = (int) (d6 - d10 / 2D);
			int j1 = (int) (d7 - d11 / 2D);
			int k1 = (int) (d8 - d10 / 2D);
			int l1 = (int) (d6 + d10 / 2D);
			int i2 = (int) (d7 + d11 / 2D);
			int j2 = (int) (d8 + d10 / 2D);

			for (int k2 = i1; k2 <= l1; k2++) {
				double d12 = (((double) k2 + 0.5D) - d6) / (d10 / 2D);
				if (d12 * d12 < 1D) {
					for (int l2 = j1; l2 <= i2; l2++) {
						double d13 = (((double) l2 + 0.5D) - d7) / (d11 / 2D);
						if (d12 * d12 + d13 * d13 < 1D) {
							for (int i3 = k1; i3 <= j2; i3++) {
								double d14 = (((double) i3 + 0.5D) - d8) / (d10 / 2D);
								Block block = getBlockStateWithOffset(k2, l2, i3);
								if(d12 * d12 + d13 * d13 + d14 * d14 < 1D && (block == AetherBlocks.mossy_holystone || block == AetherBlocks.holystone || block == AetherBlocks.aether_grass || block == AetherBlocks.aether_dirt)) {
									setBlockWithOffset(k2, l2, i3, Blocks.air, 0);
								}
							}
						}
					}
				}
			}
		}
	}

}
