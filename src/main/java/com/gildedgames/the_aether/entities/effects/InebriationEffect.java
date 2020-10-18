package com.gildedgames.the_aether.entities.effects;

import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.items.util.SkyrootBucketType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import java.util.ArrayList;
import java.util.List;

public class InebriationEffect extends PotionEffect {
	private List<ItemStack> curativeItems;

	public InebriationEffect(int potionID, int duration, int amplifier) {
		this(potionID, duration, amplifier, false);
	}

	public InebriationEffect(int potionID, int duration, int amplifier, boolean isAmbient) {
		super(potionID, duration, amplifier, isAmbient);
		this.curativeItems = new ArrayList<>();
		this.curativeItems.add(new ItemStack(AetherItems.skyroot_bucket, SkyrootBucketType.Remedy.meta));
		this.curativeItems.add(new ItemStack(AetherItems.white_apple));
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return this.curativeItems;
	}

	@Override
	public boolean isCurativeItem(ItemStack stack) {
		for (ItemStack item : this.curativeItems) {
			if (item.isItemEqual(stack)) return true;
		}
		return false;
	}

	@Override
	public void setCurativeItems(List<ItemStack> curativeItems) {
		this.curativeItems = curativeItems;
	}
}
