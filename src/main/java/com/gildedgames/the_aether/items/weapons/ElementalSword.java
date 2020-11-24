package com.gildedgames.the_aether.items.weapons;

import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;

public class ElementalSword extends ItemSword {

	public ElementalSword() {
		super(ToolMaterial.EMERALD);

		this.setMaxDamage(502);
		this.setCreativeTab(AetherCreativeTabs.weapons);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return AetherItems.aether_loot;
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase target_entity, EntityLivingBase entity) {
		if (this == AetherItems.flaming_sword) {
			int defaultTime = 30;
			int fireAspectModifier = EnchantmentHelper.getFireAspectModifier(entity);
			if (fireAspectModifier > 0)
			{
				defaultTime += (fireAspectModifier * 4);
			}
			target_entity.setFire(defaultTime);
		} else if (this == AetherItems.lightning_sword) {
			EntityLightningBolt lightning = new EntityLightningBolt(entity.worldObj, target_entity.posX, target_entity.posY, target_entity.posZ);

			entity.worldObj.spawnEntityInWorld(lightning);
		} else if (this == AetherItems.holy_sword && (target_entity.isEntityUndead() || target_entity.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)) {

			float damage = 15F;
			int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.smite.effectId, itemstack);
			if (level > 0) {
				damage += (level * 2.5);
			}
			target_entity.attackEntityFrom(DamageSource.drown, damage);
			itemstack.damageItem(10, entity);
		}

		return super.hitEntity(itemstack, target_entity, entity);
	}

	@Override
	public boolean getIsRepairable(ItemStack stack, ItemStack repairStack) {
		return false;
	}

}
