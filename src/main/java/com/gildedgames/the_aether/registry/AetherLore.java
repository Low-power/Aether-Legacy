package com.gildedgames.the_aether.registry;

import com.gildedgames.the_aether.items.block.EnchanterItem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;

public class AetherLore {

	public static boolean hasKey;

	public static String getLoreEntryKey(ItemStack stack) {
		Item item = stack.getItem();
		String modid = GameRegistry.findUniqueIdentifierFor(item).modId;
		if(item instanceof EnchanterItem) {
			return "lore." + modid + ".enchanter";
		}
		return "lore." + modid + "." + stack.getUnlocalizedName().replace("item.", "").replace("tile.", "").replace(".name", "").replace(".", "_");
	}
}
