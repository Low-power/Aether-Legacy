package com.gildedgames.the_aether.blocks.natural;

import com.gildedgames.the_aether.items.util.DoubleDropHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class Holystone extends Block {

	public Holystone() {
		super(Material.rock);

		this.setHardness(0.5F);
		this.setStepSound(soundTypeStone);
		this.setHarvestLevel("pickaxe", 0);
		this.setBlockTextureName("aether_legacy:holystone");
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
		DoubleDropHelper.drop_block(player, x, y, z, this, meta, null);
	}

	@Override
	public int damageDropped(int meta) {
		return 1;
	}

}
