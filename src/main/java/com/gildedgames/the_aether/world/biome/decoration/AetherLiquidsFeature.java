package com.gildedgames.the_aether.world.biome.decoration;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import java.util.Random;

public class AetherLiquidsFeature extends WorldGenerator {

	public AetherLiquidsFeature() {
		super();
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		if(world.getBlock(x, y + 1, z) != AetherBlocks.holystone) {
			return false;
		} else if(world.getBlock(x, y - 1, z) != AetherBlocks.holystone) {
			return false;
		} else if(world.getBlock(x, y, z).getMaterial() != Material.air && world.getBlock(x, y, z) != AetherBlocks.holystone) {
			return false;
		}

		int stone_condition = 0;
		if(world.getBlock(x - 1, y, z) == AetherBlocks.holystone) {
			stone_condition++;
		}
		if(world.getBlock(x + 1, y, z) == AetherBlocks.holystone) {
			stone_condition++;
		}
		if(world.getBlock(x, y, z - 1) == AetherBlocks.holystone) {
			stone_condition++;
		}
		if(world.getBlock(x, y, z + 1) == AetherBlocks.holystone) {
			stone_condition++;
		}
		if(stone_condition != 3) return true;

		int air_condition = 0;
		if (world.isAirBlock(x - 1, y, z)) {
			air_condition++;
		}
		if (world.isAirBlock(x + 1, y, z)) {
			air_condition++;
		}
		if (world.isAirBlock(x, y, z - 1)) {
			air_condition++;
		}
		if (world.isAirBlock(x, y, z + 1)) {
			air_condition++;
		}
		if(air_condition != 1) return true;

		world.setBlock(x, y, z, Blocks.water, 0, 2);
		world.scheduledUpdatesAreImmediate = true;
		Blocks.water.updateTick(world, x, y, z, random);
		world.scheduledUpdatesAreImmediate = false;

		return true;
	}

}
