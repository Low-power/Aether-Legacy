package com.gildedgames.the_aether.items.weapons;

import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.registry.creative_tabs.AetherCreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import java.util.Random;

public class CandyCaneSword extends ItemSword {

	public CandyCaneSword() {
		super(ToolMaterial.GOLD);
		this.setCreativeTab(AetherCreativeTabs.weapons);
		random = new Random();
	}

	private Random random;

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack material) {
		return material.getItem() == AetherItems.candy_cane;
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase target_entity, EntityLivingBase entity) {
		if (target_entity.deathTime > 0) {
			return true;
		}
		if (random.nextBoolean() && entity != null && entity instanceof EntityPlayer && !entity.worldObj.isRemote && target_entity.hurtTime > 0) {
			target_entity.dropItem(AetherItems.candy_cane, 1);
		}
		itemstack.damageItem(1, entity);
		return true;
	}

}
