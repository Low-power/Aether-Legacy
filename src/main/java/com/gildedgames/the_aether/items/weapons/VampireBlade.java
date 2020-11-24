package com.gildedgames.the_aether.items.weapons;

import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class VampireBlade extends ItemSword {

	public VampireBlade() {
		super(ToolMaterial.EMERALD);

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
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if(attacker instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)attacker;
			if (player.getHealth() < player.getMaxHealth()) {
				player.heal(1F);
			}
		}

		return super.hitEntity(stack, target, attacker);
	}

}
