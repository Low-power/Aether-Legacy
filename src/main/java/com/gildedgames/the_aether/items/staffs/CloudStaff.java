package com.gildedgames.the_aether.items.staffs;

import com.gildedgames.the_aether.entities.passive.EntityMiniCloud;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CloudStaff extends Item {

	public CloudStaff() {
		setFull3D();
		setMaxDamage(60);
		setMaxStackSize(1);
		setCreativeTab(AetherCreativeTabs.misc);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return AetherItems.aether_loot;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote) {
			return super.onItemRightClick(stack, world, player);
		}

		PlayerAether player_info = PlayerAether.get(player);
		if (player_info.clouds.isEmpty()) {
			EntityMiniCloud leftCloud = new EntityMiniCloud(world, player, 0);
			EntityMiniCloud rightCloud = new EntityMiniCloud(world, player, 1);
			player_info.clouds.add(leftCloud);
			player_info.clouds.add(rightCloud);
			world.spawnEntityInWorld(leftCloud);
			world.spawnEntityInWorld(rightCloud);
			stack.damageItem(1, player);
		} else if(player.isSneaking()) {
			for(Entity cloud : player_info.clouds) {
				if(cloud instanceof EntityMiniCloud) {
					((EntityMiniCloud)cloud).lifeSpan = 0;
				}
			}
		}

		return stack;
	}

}
