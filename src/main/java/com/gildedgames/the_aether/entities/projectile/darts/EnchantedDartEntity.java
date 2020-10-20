package com.gildedgames.the_aether.entities.projectile.darts;

import com.gildedgames.the_aether.items.AetherItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EnchantedDartEntity extends BaseDartEntity {

	public EnchantedDartEntity(World worldIn) {
		super(worldIn);
	}

	public EnchantedDartEntity(World world, EntityLivingBase entity, float velocity) {
		super(world, entity, velocity);
	}

	@Override
	public void entityInit() {
		super.entityInit();
		this.setDamage(6);
	}

	@Override
	protected ItemStack getStack() {
		return new ItemStack(AetherItems.dart, 1, 2);
	}

}
