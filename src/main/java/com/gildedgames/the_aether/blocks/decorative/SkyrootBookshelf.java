package com.gildedgames.the_aether.blocks.decorative;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.Aether;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import java.util.Random;

public class SkyrootBookshelf extends Block {

	public SkyrootBookshelf() {
		super(Material.wood);

		this.setHardness(2F);
		this.setResistance(5F);
		this.setHarvestLevel("axe", 0);
		this.setStepSound(soundTypeWood);
		this.setBlockTextureName(Aether.find("skyroot_bookshelf"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		if(side != 1 && side != 0) return super.getIcon(side, metadata);
		return AetherBlocks.skyroot_planks.getBlockTextureFromSide(side);
	}

	@Override
	public int quantityDropped(Random random) {
		return 3;
	}

	@Override
	public float getEnchantPowerBonus(World world, int x, int y, int z) {
		return 1;
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		return Items.book;
	}
}
