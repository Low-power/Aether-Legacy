package com.gildedgames.the_aether.items.tools;

import com.gildedgames.the_aether.entities.block.FloatingBlockEntity;
import com.gildedgames.the_aether.items.util.AetherToolType;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class GravititeTool extends AetherTool {

	public GravititeTool(float damage, AetherToolType toolType) {
		super(damage, ToolMaterial.EMERALD, toolType);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == Item.getItemFromBlock(AetherBlocks.enchanted_gravitite);
	}

	@Override
	public boolean onItemUse(ItemStack heldItem, EntityPlayer player, World world, int x, int y, int z, int facing, float hitX, float hitY, float hitZ) {
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);

		if ((this.getDigSpeed(heldItem, block, meta) == this.efficiencyOnProperMaterial || ForgeHooks.isToolEffective(heldItem, block, meta)) && world.isAirBlock(x, y + 1, z)) {
			if (world.getTileEntity(x, y, z) != null || world.getBlock(x, y, z).getBlockHardness(world, x, y, z) == -1F) {
				return false;
			}

			if (!world.isRemote) {
				FloatingBlockEntity entity = new FloatingBlockEntity(world, x, y, z, block, meta);
				world.spawnEntityInWorld(entity);
				world.setBlockToAir(x, y, z);
			}

			heldItem.damageItem(4, player);
		}

		return true;
	}

}
