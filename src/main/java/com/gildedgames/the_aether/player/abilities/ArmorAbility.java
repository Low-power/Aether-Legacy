package com.gildedgames.the_aether.player.abilities;

import com.gildedgames.the_aether.api.player.IPlayerAether;
import com.gildedgames.the_aether.api.player.util.IAetherAbility;
import com.gildedgames.the_aether.api.player.util.IAccessoryInventory;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.player.movement.AetherLiquidMovement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.world.WorldServer;
import java.util.Random;

public class ArmorAbility implements IAetherAbility {

	private final AetherLiquidMovement player_movement;

	private final Random random = new Random();

	private final IPlayerAether player;

	private boolean jump_boosted;

	public ArmorAbility(IPlayerAether player) {
		this.player = player;
		this.player_movement = new AetherLiquidMovement(player);
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}

	private static void damage_phoenix_armor(EntityLivingBase entity, Item outcome, int slot) {
		if(entity.worldObj.getTotalWorldTime() % 5 != 0) return;
		ItemStack stack = entity.getEquipmentInSlot(slot + 1);
		stack.damageItem(1, entity);
		if (stack.stackSize <= 0) {
			ItemStack outcome_item_stack = new ItemStack(outcome);
			EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(stack), outcome_item_stack);
			entity.setCurrentItemOrArmor(slot + 1, outcome_item_stack);
		}
	}

	@Override
	public void onUpdate() {
		IAccessoryInventory inventory = this.player.getAccessoryInventory();
		EntityLivingBase entity = this.player.getEntity();

		if(inventory.isWearingNeptuneSet()) {
			this.player_movement.onUpdate();
		}

		if(inventory.isWearingGravititeSet() && this.player.isJumping() && !this.jump_boosted) {
			entity.motionY = 1D;
			this.jump_boosted = true;
		}

		if(entity.isWet()) {
			if(inventory.wearingArmor(new ItemStack(AetherItems.phoenix_boots))) {
				damage_phoenix_armor(entity, AetherItems.obsidian_boots, 0);
			}
			if(inventory.wearingArmor(new ItemStack(AetherItems.phoenix_leggings))) {
				damage_phoenix_armor(entity, AetherItems.obsidian_leggings, 1);
			}
			if(inventory.wearingArmor(new ItemStack(AetherItems.phoenix_chestplate))) {
				damage_phoenix_armor(entity, AetherItems.obsidian_chestplate, 2);
			}
			if(inventory.wearingArmor(new ItemStack(AetherItems.phoenix_helmet))) {
				damage_phoenix_armor(entity, AetherItems.obsidian_helmet, 3);
			}
		}

		if(inventory.isWearingPhoenixSet()) {
			entity.extinguish();
			this.player_movement.onUpdate();
			if(!entity.worldObj.isRemote) {
				((WorldServer)entity.worldObj).func_147487_a("flame", entity.posX + (this.random.nextGaussian() / 5D), entity.posY + (this.random.nextGaussian() / 5D), entity.posZ + (this.random.nextGaussian() / 3D), 0, 0D, 0D, 0D, 0D);
			}
		}

		if (!this.player.isJumping() && this.player.getEntity().onGround) {
			this.jump_boosted = false;
		}
	}
}
