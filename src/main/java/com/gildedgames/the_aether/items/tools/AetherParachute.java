package com.gildedgames.the_aether.items.tools;

import com.gildedgames.the_aether.entities.passive.mountable.ParachuteEntity;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AetherParachute extends Item {

	public AetherParachute() {
		this.setMaxDamage(20);
		this.setMaxStackSize(1);
		this.setCreativeTab(AetherCreativeTabs.misc);
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		ItemStack heldItem = player.getHeldItem();

		if (ParachuteEntity.entityHasRoomForCloud(world, player)) {
			if (this == AetherItems.golden_parachute) {
				heldItem.damageItem(1, player);
			} else {
				--heldItem.stackSize;
			}

			world.spawnEntityInWorld(new ParachuteEntity(world, player, this == AetherItems.golden_parachute));

			return heldItem;
		}

		return super.onItemRightClick(stack, world, player);
	}

	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		if (this == AetherItems.golden_parachute) return 0xffff7f;

		return 0xffffff;
	}

}
