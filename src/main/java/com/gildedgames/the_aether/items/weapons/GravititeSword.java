package com.gildedgames.the_aether.items.weapons;

import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class GravititeSword extends ItemSword {

	public GravititeSword() {
		super(ToolMaterial.EMERALD);
		setCreativeTab(AetherCreativeTabs.weapons);
	}

	@Override
	public boolean getIsRepairable(ItemStack repairing_item, ItemStack material) {
		return material.getItem() == Item.getItemFromBlock(AetherBlocks.enchanted_gravitite);
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase hitentity, EntityLivingBase entity) {
		if ((hitentity.hurtTime > 0 || hitentity.deathTime > 0)) {
			hitentity.addVelocity(0D, 1D, 0D);
		}

		if (hitentity instanceof EntityPlayerMP) {
			((EntityPlayerMP)hitentity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(hitentity));
		}

		return super.hitEntity(itemstack, hitentity, entity);
	}

}
