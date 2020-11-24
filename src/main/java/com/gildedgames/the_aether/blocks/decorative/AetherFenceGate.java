package com.gildedgames.the_aether.blocks.decorative;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.Aether;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.util.IIcon;

public class AetherFenceGate extends BlockFenceGate {

	public AetherFenceGate() {
		super();

		setHardness(2F);
		setResistance(5F);
		setStepSound(soundTypeWood);
		setBlockTextureName(Aether.find("skyroot_planks"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return AetherBlocks.skyroot_planks.getBlockTextureFromSide(side);
	}

}
