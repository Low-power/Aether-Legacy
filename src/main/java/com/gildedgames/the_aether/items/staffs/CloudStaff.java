package com.gildedgames.the_aether.items.staffs;

import com.gildedgames.the_aether.entities.passive.EntityMiniCloud;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.registry.creative_tabs.AetherCreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CloudStaff extends Item {

	public CloudStaff() {
		this.setFull3D();
		this.setMaxDamage(60);
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

		if (world.isRemote) {
			return super.onItemRightClick(stack, world, player);
		}

		if (playerAether.clouds.isEmpty()) {
			EntityMiniCloud leftCloud = new EntityMiniCloud(world, player, 0);
			EntityMiniCloud rightCloud = new EntityMiniCloud(world, player, 1);

			playerAether.clouds.add(leftCloud);
			playerAether.clouds.add(rightCloud);

			world.spawnEntityInWorld(leftCloud);
			world.spawnEntityInWorld(rightCloud);

			stack.damageItem(1, player);
		}

		return stack;
	}

}
