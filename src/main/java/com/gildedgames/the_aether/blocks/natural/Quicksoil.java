package com.gildedgames.the_aether.blocks.natural;

import com.gildedgames.the_aether.items.util.DoubleDropHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class Quicksoil extends Block {

	public Quicksoil() {
		super(Material.sand);

		this.slipperiness = 1.1F;

		this.setHardness(0.5F);
		this.setStepSound(soundTypeSand);
		this.setHarvestLevel("shovel", 0);
		this.setBlockTextureName("aether_legacy:quicksoil");
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
