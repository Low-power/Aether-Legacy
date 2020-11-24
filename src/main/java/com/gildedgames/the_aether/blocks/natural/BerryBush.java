package com.gildedgames.the_aether.blocks.natural;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.CommonProxy;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;

public class BerryBush extends AetherFlower {

	public BerryBush() {
		this.setHardness(0.2F);
		this.setHarvestLevel("axe", 0);
		this.setStepSound(soundTypeGrass);
		this.setBlockTextureName(Aether.find("berry_bush"));
		this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		return Item.getItemFromBlock(AetherBlocks.berry_bush_stem);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
		int min, max;
		if(world.getBlock(x, y, z) == AetherBlocks.enchanted_aether_grass) {
			min = 1;
			max = 4;
		} else {
			min = 1;
			max = 3;
		}
		int count = world.rand.nextInt(max - min + 1) + min;
		player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
		player.addExhaustion(0.025F);
		dropBlockAsItem(world, x, y, z, new ItemStack(AetherItems.blueberry, count, 0));
		world.setBlock(x, y, z, AetherBlocks.berry_bush_stem);
	}

	@Override
	protected void checkAndDropBlock(World world, int x, int y, int z) {
		if(!canBlockStay(world, x, y, z)) {
			int min, max;
			if(world.getBlock(x, y, z) == AetherBlocks.enchanted_aether_grass) {
				min = 1;
				max = 4;
			} else {
				min = 1;
				max = 3;
			}
			int count = world.rand.nextInt(max - min + 1) + min;
			dropBlockAsItem(world, x, y, z, new ItemStack(AetherItems.blueberry, count, 0));
			world.setBlock(x, y, z, AetherBlocks.berry_bush_stem);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		return true;
	}

	@Override
	public int getRenderType() {
		return CommonProxy.berryBushRenderID;
	}

}
