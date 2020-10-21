package com.gildedgames.the_aether.items.util;

import com.gildedgames.the_aether.items.tools.SkyrootTool;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import java.util.ArrayList;

public class DoubleDropHelper {
	private static void drop_item(World world, int x, int y, int z, ItemStack item_stack) {
		if(world.isRemote || !world.getGameRules().getGameRuleBooleanValue("doTileDrops")) return;
		// do not drop items while restoring blockstates, prevents item dupe
		if(world.restoringBlockSnapshots) return;

		float f = 0.7F;
		double x_offset = (double)(world.rand.nextFloat() * f) + (double)(1F - f) * 0.5D;
		double y_offset = (double)(world.rand.nextFloat() * f) + (double)(1F - f) * 0.5D;
		double z_offset = (double)(world.rand.nextFloat() * f) + (double)(1F - f) * 0.5D;
		EntityItem item_entity = new EntityItem(world, (double)x + x_offset, (double)y + y_offset, (double)z + z_offset, item_stack);
		item_entity.delayBeforeCanPickup = 10;
		world.spawnEntityInWorld(item_entity);
	}

	public static void drop_block(EntityPlayer player, int x, int y, int z, Block block, int meta, AetherToolType applicable_tool_type) {
		player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
		player.addExhaustion(0.025F);

		ItemStack stack = player.inventory.getCurrentItem();
		//boolean should_double_drop = stack != null && stack.getItem() instanceof SkyrootTool;
		boolean should_double_drop = false;
		if(meta == 0 && stack != null && stack.getItem() instanceof SkyrootTool) {
			SkyrootTool skyroot_tool = (SkyrootTool)stack.getItem();
			should_double_drop = applicable_tool_type == null ?
				(skyroot_tool.getDigSpeed(stack, block, 0) == skyroot_tool.getEffectiveSpeed()) :
				(skyroot_tool.tool_type == applicable_tool_type);
		}

		int fortune_level = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
		if (block.canSilkHarvest(player.worldObj, player, x, y, z, meta)) {
			int silk_touch_level = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack);
			if(silk_touch_level > 0) {
				Item item = Item.getItemFromBlock(block);
				ItemStack silk_touched_item_stack = new ItemStack(item, 1, item != null && item.getHasSubtypes() ? meta : 0);
				ArrayList<ItemStack> items = new ArrayList<ItemStack>(1);
				items.add(silk_touched_item_stack);
				ForgeEventFactory.fireBlockHarvesting(items, player.worldObj, block, x, y, z, meta, 0, 1f, true, player);
				for (ItemStack is : items) {
					drop_item(player.worldObj, x, y, z, is);
				}

				if(silk_touch_level < 2) return;
				if(!should_double_drop) return;
				should_double_drop = false;
				fortune_level = 0;
			}
		}

		if(should_double_drop) {
			block.dropBlockAsItem(player.worldObj, x, y, z, meta, fortune_level);

		}
		block.dropBlockAsItem(player.worldObj, x, y, z, meta, fortune_level);
	}
}
