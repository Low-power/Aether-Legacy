package com.gildedgames.the_aether.world.biome.decoration;

import com.gildedgames.the_aether.blocks.natural.AetherFlower;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import java.util.Random;

public class AetherFoilageFeature extends WorldGenerator {

	private Block plantState;

	public AetherFoilageFeature() {
		super();
	}

	public void setPlantBlock(Block state) {
		this.plantState = state;
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		for (int i = 0; i < 64; i++) {
			int x1 = (x + random.nextInt(8)) - random.nextInt(8);
			int y1 = (y + random.nextInt(4)) - random.nextInt(4);
			int z1 = (z + random.nextInt(8)) - random.nextInt(8);
			if (world.isAirBlock(x1, y1, z1) && ((AetherFlower)this.plantState).canBlockStay(world, x1, y1, z1)) {
				this.setBlockAndNotifyAdequately(world, x1, y1, z1, this.plantState, 0);
			}
		}

		return true;
	}

}
