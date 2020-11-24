package com.gildedgames.the_aether.world.biome.decoration;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import java.util.Random;

public class AetherMinableFeature extends WorldGenerator {

	private Block oreBlock;

	private int numberOfBlocks;

	public AetherMinableFeature() {
		super();
	}

	public void setSize(int size) {
		this.numberOfBlocks = size;
	}

	public void setBlock(Block state) {
		this.oreBlock = state;
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		float f = random.nextFloat() * (float) Math.PI;
		double d = (double) ((float) (x + 8) + MathHelper.sin(f) * (float) this.numberOfBlocks / 8F);
		double d1 = (double) ((float) (x + 8) - MathHelper.sin(f) * (float) this.numberOfBlocks / 8F);
		double d2 = (double) ((float) (z + 8) + MathHelper.cos(f) * (float) this.numberOfBlocks / 8F);
		double d3 = (double) ((float) (z + 8) - MathHelper.cos(f) * (float) this.numberOfBlocks / 8F);
		double d4 = (double) (y + random.nextInt(3) + 2);
		double d5 = (double) (y + random.nextInt(3) + 2);

		for (int l = 0; l <= this.numberOfBlocks; ++l) {
			double d6 = d + (d1 - d) * (double) l / (double) this.numberOfBlocks;
			double d7 = d4 + (d5 - d4) * (double) l / (double) this.numberOfBlocks;
			double d8 = d2 + (d3 - d2) * (double) l / (double) this.numberOfBlocks;
			double d9 = random.nextDouble() * (double) this.numberOfBlocks / 16D;
			double d10 = (double) (MathHelper.sin((float) l * (float) Math.PI / (float) this.numberOfBlocks) + 1F) * d9 + 1D;
			double d11 = (double) (MathHelper.sin((float) l * (float) Math.PI / (float) this.numberOfBlocks) + 1F) * d9 + 1D;
			int i1 = MathHelper.floor_double(d6 - d10 / 2D);
			int j1 = MathHelper.floor_double(d7 - d11 / 2D);
			int k1 = MathHelper.floor_double(d8 - d10 / 2D);
			int l1 = MathHelper.floor_double(d6 + d10 / 2D);
			int i2 = MathHelper.floor_double(d7 + d11 / 2D);
			int j2 = MathHelper.floor_double(d8 + d10 / 2D);

			for (int k2 = i1; k2 <= l1; ++k2) {
				double d12 = ((double) k2 + 0.5D - d6) / (d10 / 2D);
				if (d12 * d12 < 1D) {
					for (int l2 = j1; l2 <= i2; ++l2) {
						double d13 = ((double) l2 + 0.5D - d7) / (d11 / 2D);
						if (d12 * d12 + d13 * d13 < 1D) {
							for (int i3 = k1; i3 <= j2; ++i3) {
								double d14 = ((double) i3 + 0.5D - d8) / (d10 / 2D);
								if(d12 * d12 + d13 * d13 + d14 * d14 < 1D && world.getBlock(k2, l2, i3) == AetherBlocks.holystone) {
									setBlockAndNotifyAdequately(world, k2, l2, i3, this.oreBlock, 0);
								}
							}
						}
					}
				}
			}
		}

		return true;
	}

}
