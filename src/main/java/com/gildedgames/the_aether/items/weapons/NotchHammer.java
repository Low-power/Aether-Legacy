package com.gildedgames.the_aether.items.weapons;

import com.gildedgames.the_aether.entities.projectile.EntityHammerProjectile;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

public class NotchHammer extends ItemSword {

	public NotchHammer() {
		super(ToolMaterial.IRON);
		this.setCreativeTab(AetherCreativeTabs.weapons);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return AetherItems.aether_loot;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
		if (player.capabilities.isCreativeMode) {
			world.playSound(player.posX, player.posY, player.posZ, "mob.ghast.fireball", 1F, 1F / (itemRand.nextFloat() * 0.4F + 0.8F), false);

			if (!world.isRemote) {
				EntityHammerProjectile hammerProjectile = new EntityHammerProjectile(world, player);
				hammerProjectile.shoot(player, player.rotationPitch, player.rotationYaw, 0F, 1.5F, 1F);
				world.spawnEntityInWorld(hammerProjectile);
			}
		} else if (PlayerAether.get(player).setHammerCooldown(200, itemstack.getDisplayName())) {
			itemstack.damageItem(1, player);

			world.playSound(player.posX, player.posY, player.posZ, "mob.ghast.fireball", 1F, 1F / (itemRand.nextFloat() * 0.4F + 0.8F), false);

			if (!world.isRemote) {
				EntityHammerProjectile hammerProjectile = new EntityHammerProjectile(world, player);
				hammerProjectile.shoot(player, player.rotationPitch, player.rotationYaw, 0F, 1.5F, 1F);
				world.spawnEntityInWorld(hammerProjectile);
			}
		}

		return itemstack;
	}

}
