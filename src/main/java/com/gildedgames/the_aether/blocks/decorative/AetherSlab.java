package com.gildedgames.the_aether.blocks.decorative;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import java.util.Random;

public class AetherSlab extends BlockSlab {

	private String name;

	public AetherSlab(String name, boolean double_slab, Material material) {
		super(double_slab, material);
		this.name = name;
		setLightOpacity(0);
		setStepSound(material == Material.wood ? soundTypeWood : soundTypeStone);
	}

	@Override
	public Item getItem(World world, int x, int y, int z) {
		return Item.getItemFromBlock(this.getDroppedSlab());
	}

	public Block getDroppedSlab() {
		if(this == AetherBlocks.skyroot_double_slab) {
			return AetherBlocks.skyroot_slab;
		} else if(this == AetherBlocks.carved_double_slab) {
			return AetherBlocks.carved_slab;
		} else if(this == AetherBlocks.angelic_double_slab) {
			return AetherBlocks.angelic_slab;
		} else if(this == AetherBlocks.hellfire_double_slab) {
			return AetherBlocks.hellfire_slab;
		} else if(this == AetherBlocks.holystone_brick_double_slab) {
			return AetherBlocks.holystone_brick_slab;
		} else if(this == AetherBlocks.holystone_double_slab) {
			return AetherBlocks.holystone_slab;
		} else if(this == AetherBlocks.mossy_holystone_double_slab) {
			return AetherBlocks.mossy_holystone_slab;
		} else {
			return this;
		}
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		return Item.getItemFromBlock(this.getDroppedSlab());
	}

	@Override
	public int damageDropped(int meta) {
		return 0;
	}

	@Override
	public String func_150002_b(int meta) {
		return this.name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return this == AetherBlocks.aerogel_slab || this == AetherBlocks.aerogel_double_slab ? 1 : 0;
	}
}
