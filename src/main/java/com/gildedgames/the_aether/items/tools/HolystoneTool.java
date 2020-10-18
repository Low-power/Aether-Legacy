package com.gildedgames.the_aether.items.tools;

import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.items.util.EnumAetherToolType;
import com.gildedgames.the_aether.blocks.BlocksAether;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class HolystoneTool extends ItemAetherTool {

	public HolystoneTool(float damage, EnumAetherToolType toolType) {
		super(damage, ToolMaterial.STONE, toolType);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == Item.getItemFromBlock(BlocksAether.holystone);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
		if (!world.isRemote && world.rand.nextInt(100) <= 5) {
			EntityItem item_entity = new EntityItem(world, x, y, z, new ItemStack(AetherItems.ambrosium_shard, 1));

			world.spawnEntityInWorld(item_entity);
		}

		return super.onBlockDestroyed(stack, world, block, x, y, z, entity);
	}

}
