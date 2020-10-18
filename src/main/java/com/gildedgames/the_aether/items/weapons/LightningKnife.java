package com.gildedgames.the_aether.items.weapons;

import com.gildedgames.the_aether.entities.projectile.EntityLightningKnife;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.registry.creative_tabs.AetherCreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LightningKnife extends Item {
	public LightningKnife() {
		this.setMaxStackSize(16);
		this.setCreativeTab(AetherCreativeTabs.weapons);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return AetherItems.aether_loot;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack heldItem, World world, EntityPlayer player) {
		if (!player.capabilities.isCreativeMode && EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, heldItem) == 0) {
			--heldItem.stackSize;
		}

		world.playSoundEffect(player.posX, player.posY, player.posZ, "aether_legacy:projectile.shoot", 1F, 1F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!world.isRemote) {
			EntityLightningKnife lightningKnife = new EntityLightningKnife(world, player);

			lightningKnife.shoot(player, player.rotationPitch, player.rotationYaw, 0F, 1.5F, 1F);

			world.spawnEntityInWorld(lightningKnife);
		}

		return heldItem;
	}

}
