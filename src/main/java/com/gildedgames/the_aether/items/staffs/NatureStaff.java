package com.gildedgames.the_aether.items.staffs;

import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import net.minecraft.item.Item;

public class NatureStaff extends Item {

	public NatureStaff() {
		this.setFull3D();
		this.setMaxDamage(100);
		this.setMaxStackSize(1);
		this.setCreativeTab(AetherCreativeTabs.misc);
	}

}
