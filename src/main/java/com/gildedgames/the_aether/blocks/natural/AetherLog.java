package com.gildedgames.the_aether.blocks.natural;

import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.items.tools.*;
import com.gildedgames.the_aether.items.util.AetherToolType;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;

public class AetherLog extends BlockLog {

	public AetherLog() {
		super();
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
		player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
		player.addExhaustion(0.025F);
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null) {
			int silk_touch_level = 0;
			ArrayList<ItemStack> silk_touched_items = null;
			if(canSilkHarvest(world, player, x, y, z, meta)) {
				silk_touch_level = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack);
				if(silk_touch_level > 0) {
					ItemStack silk_touched_item_stack = this.createStackedBlock(meta);
					silk_touched_items = new ArrayList<ItemStack>();
					if (silk_touched_item_stack != null) {
						silk_touched_items.add(silk_touched_item_stack);
					}
					ForgeEventFactory.fireBlockHarvesting(silk_touched_items, world, this, x, y, z, meta, 0, 1.0f, true, player);
				}
			}
			Item item = stack.getItem();
			if((item instanceof AetherTool && ((AetherTool)item).tool_type == AetherToolType.AXE) || item == Items.diamond_axe) {
				boolean should_double_drop = meta == 0 && item instanceof SkyrootTool;
				int fortune_level = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
				if(silk_touched_items != null) {
					for(ItemStack is : silk_touched_items) {
						if(should_double_drop && silk_touch_level > 1) {
							// Not possible without additional mod
							is.setItemDamage(1);
						}
						dropBlockAsItem(world, x, y, z, is);
					}
					if(silk_touch_level < 2) return;
					if(!should_double_drop) return;
					should_double_drop = false;
					fortune_level = 0;
				} else if (item instanceof ZaniteTool || item instanceof GravititeTool || item instanceof ValkyrieTool || item == Items.diamond_axe) {
					if (this == BlocksAether.golden_oak_log) {
						dropBlockAsItem(world, x, y, z, new ItemStack(AetherItems.golden_amber, 1 + world.rand.nextInt(2)));
					}
					//should_double_drop = false;	// Not needed
				}
				dropBlockAsItem(player.worldObj, x, y, z, meta, fortune_level);
				if(should_double_drop) {
					dropBlockAsItem(player.worldObj, x, y, z, meta, fortune_level);
				}
				return;
			}
			if(silk_touched_items != null) {
				for(ItemStack is : silk_touched_items) dropBlockAsItem(world, x, y, z, is);
				return;
			}
		}
		super.harvestBlock(world, player, x, y, z, meta);
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return Item.getItemFromBlock(BlocksAether.skyroot_log);
	}

	@Override
	public int damageDropped(int meta) {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister registry) {
		this.field_150167_a = new IIcon[1];
		this.field_150166_b = new IIcon[1];

		for (int i = 0; i < this.field_150167_a.length; ++i) {
			this.field_150167_a[i] = registry.registerIcon(this.getTextureName() + "_side");
			this.field_150166_b[i] = registry.registerIcon(this.getTextureName() + "_top");
		}
	}

}
