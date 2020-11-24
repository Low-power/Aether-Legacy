package com.gildedgames.the_aether.blocks.decorative;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.Aether;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class AetherFence extends BlockFence {

	public AetherFence() {
		super(Aether.find("skyroot_planks"), Material.wood);

		setHardness(2F);
		setResistance(5F);
		setStepSound(soundTypeWood);
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
		return true;
	}

	@Override
	public boolean canConnectFenceTo(IBlockAccess world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		if(block == this || block == Blocks.fence_gate || block == AetherBlocks.skyroot_fence_gate) return true;
		return block.getMaterial().isOpaque() && block.renderAsNormalBlock() && block.getMaterial() != Material.gourd;
	}

}
