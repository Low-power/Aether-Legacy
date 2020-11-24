package com.gildedgames.the_aether.blocks.natural;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.block.IGrowable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;

public class BerryBushStem extends AetherFlower implements IGrowable {

	public BerryBushStem() {
		this.setHardness(0.2F);
		this.setStepSound(soundTypeGrass);
		this.setBlockTextureName(Aether.find("berry_bush_stem"));
		this.setBlockBounds(0.5F - 0.4F, 0F, 0.5F - 0.4F, 0.5F + 0.4F, 0.4F * 2F, 0.5F + 0.4F);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (world.isRemote) {
			return;
		}

		super.updateTick(world, x, y, z, random);

		if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(60) == 0) {
			world.setBlock(x, y, z, AetherBlocks.berry_bush);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		return true;
	}

	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean isClient) {
		return true;
	}

	@Override
	public boolean func_149852_a(World world, Random random, int x, int y, int z) {
		return (double) random.nextFloat() < 0.45D;
	}

	@Override
	public void func_149853_b(World world, Random random, int x, int y, int z) {
		world.setBlock(x, y, z, AetherBlocks.berry_bush);
	}

}
