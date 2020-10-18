package com.gildedgames.the_aether.items.weapons;

import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.registry.creative_tabs.AetherCreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import java.util.Random;

public class HolystoneSword extends ItemSword {

	public HolystoneSword() {
		super(ToolMaterial.STONE);
		this.setCreativeTab(AetherCreativeTabs.weapons);
		random = new Random();
	}

	private Random random;

	@Override
	public boolean getIsRepairable(ItemStack repairingItem, ItemStack mateiral) {
		return mateiral.getItem() == Item.getItemFromBlock(BlocksAether.holystone);
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase target_entity, EntityLivingBase entity) {
		if (random.nextInt(20) == 0 && entity != null && entity instanceof EntityPlayer && !target_entity.worldObj.isRemote && target_entity.hurtTime > 0 && target_entity.deathTime <= 0) {
			target_entity.dropItem(AetherItems.ambrosium_shard, 1);
		}

		itemstack.damageItem(1, entity);
		return true;
	}

}
