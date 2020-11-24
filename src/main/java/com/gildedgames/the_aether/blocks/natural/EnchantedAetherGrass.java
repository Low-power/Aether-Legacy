package com.gildedgames.the_aether.blocks.natural;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class EnchantedAetherGrass extends Block {
	@SideOnly(Side.CLIENT)
	private IIcon block_top_icon;

	public EnchantedAetherGrass() {
		super(Material.grass);

		this.setHardness(0.2F);
		this.setStepSound(soundTypeGrass);
		this.setHarvestLevel("shovel", 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister registry) {
		this.blockIcon = registry.registerIcon("aether_legacy:enchanted_aether_grass_side");
		this.block_top_icon = registry.registerIcon("aether_legacy:enchanted_aether_grass_top");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		switch(side) {
			case 1:
				return this.block_top_icon;
			case 0:
				return AetherBlocks.aether_dirt.getBlockTextureFromSide(side);
			default:
				return this.blockIcon;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return getIcon(side, 0);
	}
}
