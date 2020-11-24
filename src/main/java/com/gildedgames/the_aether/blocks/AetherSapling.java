package com.gildedgames.the_aether.blocks;

import com.gildedgames.the_aether.blocks.natural.AetherFlower;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.World;
import java.util.Random;

public class AetherSapling extends AetherFlower implements IGrowable {

	public WorldGenerator generator;

	public AetherSapling(WorldGenerator generator) {
		super();

		float f = 0.4F;
		this.generator = generator;
		setBlockBounds(0.5F - f, 0F, 0.5F - f, 0.5F + f, f * 2F, 0.5F + f);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote) {
			super.updateTick(world, x, y, z, random);

			if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(30) == 0) {
				this.growTree(world, x, y, z, random);
			}
		}
	}

	public void growTree(World world, int x, int y, int z, Random rand) {
		world.setBlock(x, y, z, Blocks.air);

		if(!this.generator.generate(world, world.rand, x, y, z)) {
			world.setBlock(x, y, z, this);
		}
	}

	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean isClient) {
		return true;
	}

	@Override
	public boolean func_149852_a(World world, Random rand, int x, int y, int z) {
		return true;
	}

	@Override
	public void func_149853_b(World world, Random rand, int x, int y, int z) {
		if(world.rand.nextFloat() < 0.45D) {
			growTree(world, x, y, z, rand);
		}
	}

}
