package com.gildedgames.the_aether.entities.projectile.darts;

import com.gildedgames.the_aether.items.AetherItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GoldenDartEntity extends BaseDartEntity {

	public GoldenDartEntity(World world) {
		super(world);
	}

	public GoldenDartEntity(World world, EntityLivingBase entity, float velocity) {
		super(world, entity, velocity);
	}

	@Override
	public void entityInit() {
		super.entityInit();
		this.setDamage(4);
	}

	@Override
	protected ItemStack getStack() {
		return new ItemStack(AetherItems.dart, 1, 0);
	}

}
