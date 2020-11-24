package com.gildedgames.the_aether.blocks.natural;

import com.gildedgames.the_aether.CommonProxy;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class AetherFlower extends BlockBush {

	public AxisAlignedBB FLOWER_AABB = AxisAlignedBB.getBoundingBox(0.30000001192092896D, 0D, 0.30000001192092896D, 0.699999988079071D, 0.6000000238418579D, 0.699999988079071D);

	public AetherFlower() {
		setHardness(0F);
		setTickRandomly(true);
		setStepSound(soundTypeGrass);
		setBlockBounds(0.5F - 0.2F, 0F, 0.5F - 0.2F, 0.5F + 0.2F, 0.2F * 3F, 0.5F + 0.2F);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		Block soil = world.getBlock(x, y - 1, z);
		return soil == AetherBlocks.aether_grass || soil == AetherBlocks.aether_dirt || soil == AetherBlocks.enchanted_aether_grass;
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		Block soil = world.getBlock(x, y - 1, z);
		return (soil != null && canPlaceBlockAt(world, x, y, z));
	}

	@Override
	public int getRenderType() {
		return CommonProxy.aetherFlowerRenderID;
	}

}
