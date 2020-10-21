package com.gildedgames.the_aether.blocks.natural;

import com.gildedgames.the_aether.items.util.DoubleDropHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class AetherDirt extends Block {

	public AetherDirt() {
		super(Material.ground);

		this.setHardness(0.2F);
		this.setHarvestLevel("shovel", 0);
		this.setStepSound(soundTypeGravel);
		this.setBlockTextureName("aether_legacy:aether_dirt");
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
