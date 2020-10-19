package com.gildedgames.the_aether.player.abilities;

import com.gildedgames.the_aether.api.player.IPlayerAether;
import com.gildedgames.the_aether.api.player.util.IAetherAbility;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.player.movement.AetherLiquidMovement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import java.util.Random;

public class ArmorAbility implements IAetherAbility {

	private final AetherLiquidMovement player_movement;

	private final Random random = new Random();

	private final IPlayerAether player;

	private boolean jumpBoosted;

	public ArmorAbility(IPlayerAether player) {
		this.player = player;
		this.player_movement = new AetherLiquidMovement(player);
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}

	@Override
	public void onUpdate() {
		if (this.player.getAccessoryInventory().isWearingNeptuneSet()) {
			this.player_movement.onUpdate();
		}

		if (this.player.getAccessoryInventory().isWearingGravititeSet()) {
			if (this.player.isJumping() && !this.jumpBoosted) {
				this.player.getEntity().motionY = 1D;
				this.jumpBoosted = true;
			}
		}

		if (this.player.getEntity().isWet()) {
			if (this.player.getAccessoryInventory().wearingArmor(new ItemStack(AetherItems.phoenix_boots))) {
				this.damagePhoenixArmor(this.player.getEntity(), AetherItems.obsidian_boots, 0);
			}

			if (this.player.getAccessoryInventory().wearingArmor(new ItemStack(AetherItems.phoenix_leggings))) {
				this.damagePhoenixArmor(this.player.getEntity(), AetherItems.obsidian_leggings, 1);
			}

			if (this.player.getAccessoryInventory().wearingArmor(new ItemStack(AetherItems.phoenix_chestplate))) {
				this.damagePhoenixArmor(this.player.getEntity(), AetherItems.obsidian_chestplate, 2);
			}

			if (this.player.getAccessoryInventory().wearingArmor(new ItemStack(AetherItems.phoenix_helmet))) {
				this.damagePhoenixArmor(this.player.getEntity(), AetherItems.obsidian_helmet, 3);
			}
		}

		if (this.player.getAccessoryInventory().isWearingPhoenixSet()) {
			this.player.getEntity().extinguish();
			this.player_movement.onUpdate();

			if (!this.player.getEntity().worldObj.isRemote) {
				((WorldServer) this.player.getEntity().worldObj).func_147487_a("flame", this.player.getEntity().posX + (this.random.nextGaussian() / 5D), this.player.getEntity().posY + (this.random.nextGaussian() / 5D), this.player.getEntity().posZ + (this.random.nextGaussian() / 3D), 0, 0.0D, 0.0D, 0.0D, 0.0D);
			}
		}

		if (!this.player.isJumping() && this.player.getEntity().onGround) {
			this.jumpBoosted = false;
		}

	}

	public void damagePhoenixArmor(Entity entity, Item outcome, int slot) {
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase living_entity = (EntityLivingBase)entity;
			ItemStack stack = living_entity.getEquipmentInSlot(slot + 1);

			stack.damageItem(1, living_entity);

			if (stack.stackSize <= 0) {
				living_entity.setCurrentItemOrArmor(slot + 1, new ItemStack(outcome));
			}
		}
	}

}