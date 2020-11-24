package com.gildedgames.the_aether.world.biome.decoration;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.World;
import java.util.Random;

public class AetherCloudsFeature extends WorldGenerator {

	private int cloud_amount;
	private int cloud_metadata;

	public AetherCloudsFeature() {
	}

	public void setCloudAmount(int amount) {
		this.cloud_amount = amount;
	}

	public void setCloudMeta(int metadata) {
		this.cloud_metadata = metadata;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		y += rand.nextInt(64);
		for (int amount = 0; amount < this.cloud_amount; ++amount) {
			int x_length = rand.nextInt(2) + 3;
			int y_length = 2;
			int z_length = rand.nextInt(2) + 3;
			int x_offset = rand.nextInt(2);
			int y_offset = rand.nextBoolean() ? rand.nextInt(3) - 1 : 0;
			int z_offset = rand.nextInt(2);
			x += x_offset;
			y += y_offset;
			z += z_offset;
			for(int x1 = x; x1 < x + x_length; x1++) {
				for(int y1 = y; y1 < y + y_length; y1++) {
					for(int z1 = z; z1 < z + z_length; z1++) {
						if(!world.isAirBlock(x1, y1, z1)) continue;
						if(Math.abs(x1 - x) + Math.abs(y1 - y) + Math.abs(z1 - z) < 4 + rand.nextInt(2)) {
							setBlockAndNotifyAdequately(world, x1, y1, z1, AetherBlocks.aercloud, this.cloud_metadata);
						}
					}
				}
			}
		}

		return true;
	}

}
