package com.gildedgames.the_aether.items.food;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LifeShard extends Item {

	public LifeShard() {
		super();
		this.setMaxStackSize(1);
		this.setCreativeTab(AetherCreativeTabs.misc);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return AetherItems.aether_loot;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		PlayerAether playerAether = PlayerAether.get(player);
		ItemStack heldItem = player.getHeldItem();

		if (world.isRemote) {
			return heldItem;
		}

		if (playerAether.getShardsUsed() < playerAether.getMaxShardCount()) {
			playerAether.updateShardCount(playerAether.getShardsUsed() + 1);

			--heldItem.stackSize;

			return heldItem;
		}

		Aether.proxy.sendMessage(player, "You can only use a total of " + playerAether.getMaxShardCount() + " life shards.");

		return heldItem;
	}

}
