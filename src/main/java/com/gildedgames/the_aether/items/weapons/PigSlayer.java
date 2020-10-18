package com.gildedgames.the_aether.items.weapons;

import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.registry.creative_tabs.AetherCreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;

public class PigSlayer extends ItemSword {

	public PigSlayer() {
		super(ToolMaterial.IRON);
		this.setMaxDamage(200);
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
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase target_entity, EntityLivingBase entity) {
		if (target_entity == null || entity == null) {
			return false;
		}

		String s = EntityList.getEntityString(target_entity);

		if (s != null && (s.toLowerCase().contains("pig") || s.toLowerCase().contains("phyg") || s.toLowerCase().contains("taegore") || target_entity.getUniqueID().toString().equals("1d680bb6-2a9a-4f25-bf2f-a1af74361d69"))) {
			if (target_entity.getHealth() > 0) {
				target_entity.attackEntityFrom(DamageSource.causeMobDamage(entity), 9999);
			}
		}

		return super.hitEntity(itemstack, target_entity, entity);
	}

}
