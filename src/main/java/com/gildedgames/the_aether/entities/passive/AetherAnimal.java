package com.gildedgames.the_aether.entities.passive;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.items.AetherItems;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import java.util.Random;

public abstract class AetherAnimal extends EntityAnimal {

	Random random = new Random();

	public AetherAnimal(World world) {
		super(world);
	}

	@Override
	public float getBlockPathWeight(int x, int y, int z) {
		return this.worldObj.getBlock(x, y - 1, z) == AetherBlocks.aether_grass ? 10.0F : this.worldObj.getLightBrightness(x, y, z) - 0.5F;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == AetherItems.blueberry;
	}

}
