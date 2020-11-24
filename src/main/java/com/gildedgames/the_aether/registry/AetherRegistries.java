package com.gildedgames.the_aether.registry;

import com.gildedgames.the_aether.api.AetherAPI;
import com.gildedgames.the_aether.registry.recipe.AccessoryDyesRecipe;
import com.gildedgames.the_aether.api.accessories.AccessoryType;
import com.gildedgames.the_aether.api.accessories.AetherAccessory;
import com.gildedgames.the_aether.api.enchantments.AetherEnchantment;
import com.gildedgames.the_aether.api.enchantments.AetherEnchantmentFuel;
import com.gildedgames.the_aether.api.freezables.AetherFreezable;
import com.gildedgames.the_aether.api.freezables.AetherFreezableFuel;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.items.AetherItems;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class AetherRegistries {

	public static void initializeAccessories() {
		AetherAPI api = AetherAPI.instance();

		api.register(new AetherAccessory(AetherItems.leather_gloves, AccessoryType.GLOVES));
		api.register(new AetherAccessory(AetherItems.iron_gloves, AccessoryType.GLOVES));
		api.register(new AetherAccessory(AetherItems.golden_gloves, AccessoryType.GLOVES));
		api.register(new AetherAccessory(AetherItems.chain_gloves, AccessoryType.GLOVES));
		api.register(new AetherAccessory(AetherItems.diamond_gloves, AccessoryType.GLOVES));

		api.register(new AetherAccessory(AetherItems.zanite_gloves, AccessoryType.GLOVES));
		api.register(new AetherAccessory(AetherItems.gravitite_gloves, AccessoryType.GLOVES));
		api.register(new AetherAccessory(AetherItems.neptune_gloves, AccessoryType.GLOVES));
		api.register(new AetherAccessory(AetherItems.phoenix_gloves, AccessoryType.GLOVES));
		api.register(new AetherAccessory(AetherItems.obsidian_gloves, AccessoryType.GLOVES));
		api.register(new AetherAccessory(AetherItems.valkyrie_gloves, AccessoryType.GLOVES));

		api.register(new AetherAccessory(AetherItems.iron_ring, AccessoryType.RING));
		api.register(new AetherAccessory(AetherItems.golden_ring, AccessoryType.RING));
		api.register(new AetherAccessory(AetherItems.zanite_ring, AccessoryType.RING));
		api.register(new AetherAccessory(AetherItems.ice_ring, AccessoryType.RING));

		api.register(new AetherAccessory(AetherItems.iron_pendant, AccessoryType.PENDANT));
		api.register(new AetherAccessory(AetherItems.golden_pendant, AccessoryType.PENDANT));
		api.register(new AetherAccessory(AetherItems.zanite_pendant, AccessoryType.PENDANT));
		api.register(new AetherAccessory(AetherItems.ice_pendant, AccessoryType.PENDANT));

		api.register(new AetherAccessory(AetherItems.red_cape, AccessoryType.CAPE));
		api.register(new AetherAccessory(AetherItems.blue_cape, AccessoryType.CAPE));
		api.register(new AetherAccessory(AetherItems.yellow_cape, AccessoryType.CAPE));
		api.register(new AetherAccessory(AetherItems.white_cape, AccessoryType.CAPE));
		api.register(new AetherAccessory(AetherItems.swet_cape, AccessoryType.CAPE));
		api.register(new AetherAccessory(AetherItems.invisibility_cape, AccessoryType.CAPE));
		api.register(new AetherAccessory(AetherItems.agility_cape, AccessoryType.CAPE));
		api.register(new AetherAccessory(AetherItems.valkyrie_cape, AccessoryType.CAPE));

		api.register(new AetherAccessory(AetherItems.golden_feather, AccessoryType.MISC));
		api.register(new AetherAccessory(AetherItems.regeneration_stone, AccessoryType.MISC));
		api.register(new AetherAccessory(AetherItems.iron_bubble, AccessoryType.MISC));

		api.register(new AetherAccessory(AetherItems.repulsion_shield, AccessoryType.SHIELD));
	}

	public static void initializeEnchantments() {
		AetherAPI api = AetherAPI.instance();

		api.register(new AetherEnchantment(AetherItems.skyroot_pickaxe, 225));
		api.register(new AetherEnchantment(AetherItems.skyroot_axe, 225));
		api.register(new AetherEnchantment(AetherItems.skyroot_shovel, 225));
		api.register(new AetherEnchantment(AetherItems.skyroot_sword, 225));

		api.register(new AetherEnchantment(AetherItems.holystone_pickaxe, 550));
		api.register(new AetherEnchantment(AetherItems.holystone_axe, 550));
		api.register(new AetherEnchantment(AetherItems.holystone_shovel, 550));
		api.register(new AetherEnchantment(AetherItems.holystone_sword, 550));

		api.register(new AetherEnchantment(AetherItems.zanite_pickaxe, 2250));
		api.register(new AetherEnchantment(AetherItems.zanite_axe, 2250));
		api.register(new AetherEnchantment(AetherItems.zanite_shovel, 2250));
		api.register(new AetherEnchantment(AetherItems.zanite_sword, 2250));

		api.register(new AetherEnchantment(AetherItems.gravitite_pickaxe, 5500));
		api.register(new AetherEnchantment(AetherItems.gravitite_axe, 5500));
		api.register(new AetherEnchantment(AetherItems.gravitite_shovel, 5500));
		api.register(new AetherEnchantment(AetherItems.gravitite_sword, 5500));

		api.register(new AetherEnchantment(AetherItems.zanite_helmet, 6000));
		api.register(new AetherEnchantment(AetherItems.zanite_chestplate, 6000));
		api.register(new AetherEnchantment(AetherItems.zanite_leggings, 6000));
		api.register(new AetherEnchantment(AetherItems.zanite_boots, 6000));

		api.register(new AetherEnchantment(AetherItems.gravitite_helmet, 13000));
		api.register(new AetherEnchantment(AetherItems.gravitite_chestplate, 13000));
		api.register(new AetherEnchantment(AetherItems.gravitite_leggings, 13000));
		api.register(new AetherEnchantment(AetherItems.gravitite_boots, 13000));

		api.register(new AetherEnchantment(AetherItems.zanite_ring, 2250));
		api.register(new AetherEnchantment(AetherItems.zanite_pendant, 2250));

		api.register(new AetherEnchantment(AetherItems.dart, new ItemStack(AetherItems.dart, 1, 2), 250));
		api.register(new AetherEnchantment(AetherItems.dart_shooter, new ItemStack(AetherItems.dart_shooter, 1, 2), 500));

		api.register(new AetherEnchantment(new ItemStack(AetherItems.skyroot_bucket, 1, 2), new ItemStack(AetherItems.skyroot_bucket.setContainerItem(null), 1, 3), 1000));

		api.register(new AetherEnchantment(new ItemStack(AetherBlocks.holystone, 1, 1), AetherItems.healing_stone, 750));
		api.register(new AetherEnchantment(AetherBlocks.gravitite_ore, AetherBlocks.enchanted_gravitite, 1000));
		api.register(new AetherEnchantment(new ItemStack(AetherBlocks.quicksoil, 1, 1), AetherBlocks.quicksoil_glass, 250));

		api.register(new AetherEnchantment(AetherItems.blueberry, AetherItems.enchanted_blueberry, 300));

		api.register(new AetherEnchantment(Items.bow, 4000));
		api.register(new AetherEnchantment(Items.fishing_rod, 600));

		api.register(new AetherEnchantment(Items.record_11, AetherItems.aether_tune, 2500));
		api.register(new AetherEnchantment(Items.record_13, AetherItems.aether_tune, 2500));
		api.register(new AetherEnchantment(Items.record_blocks, AetherItems.aether_tune, 2500));
		api.register(new AetherEnchantment(Items.record_cat, AetherItems.legacy, 2500));
		api.register(new AetherEnchantment(Items.record_far, AetherItems.aether_tune, 2500));
		api.register(new AetherEnchantment(Items.record_mall, AetherItems.aether_tune, 2500));
		api.register(new AetherEnchantment(Items.record_mellohi, AetherItems.aether_tune, 2500));
		api.register(new AetherEnchantment(Items.record_stal, AetherItems.aether_tune, 2500));
		api.register(new AetherEnchantment(Items.record_strad, AetherItems.aether_tune, 2500));
		api.register(new AetherEnchantment(Items.record_wait, AetherItems.aether_tune, 2500));
		api.register(new AetherEnchantment(Items.record_ward, AetherItems.aether_tune, 2500));

		api.register(new AetherEnchantment(Items.wooden_pickaxe, 225));
		api.register(new AetherEnchantment(Items.wooden_axe, 225));
		api.register(new AetherEnchantment(Items.wooden_shovel, 225));
		api.register(new AetherEnchantment(Items.wooden_hoe, 225));

		api.register(new AetherEnchantment(Items.stone_pickaxe, 550));
		api.register(new AetherEnchantment(Items.stone_axe, 550));
		api.register(new AetherEnchantment(Items.stone_shovel, 550));
		api.register(new AetherEnchantment(Items.stone_hoe, 550));

		api.register(new AetherEnchantment(Items.iron_pickaxe, 2250));
		api.register(new AetherEnchantment(Items.iron_axe, 2250));
		api.register(new AetherEnchantment(Items.iron_shovel, 2250));
		api.register(new AetherEnchantment(Items.iron_hoe, 2250));

		api.register(new AetherEnchantment(Items.diamond_pickaxe, 5500));
		api.register(new AetherEnchantment(Items.diamond_axe, 5500));
		api.register(new AetherEnchantment(Items.diamond_shovel, 5500));
		api.register(new AetherEnchantment(Items.diamond_hoe, 5500));

		api.register(new AetherEnchantment(Items.leather_helmet, 550));
		api.register(new AetherEnchantment(Items.leather_chestplate, 550));
		api.register(new AetherEnchantment(Items.leather_leggings, 550));
		api.register(new AetherEnchantment(Items.leather_boots, 550));

		api.register(new AetherEnchantment(Items.iron_helmet, 6000));
		api.register(new AetherEnchantment(Items.iron_chestplate, 6000));
		api.register(new AetherEnchantment(Items.iron_leggings, 6000));
		api.register(new AetherEnchantment(Items.iron_boots, 6000));

		api.register(new AetherEnchantment(Items.golden_helmet, 2250));
		api.register(new AetherEnchantment(Items.golden_chestplate, 2250));
		api.register(new AetherEnchantment(Items.golden_leggings, 2250));
		api.register(new AetherEnchantment(Items.golden_boots, 2250));

		api.register(new AetherEnchantment(Items.chainmail_helmet, 2250));
		api.register(new AetherEnchantment(Items.chainmail_chestplate, 2250));
		api.register(new AetherEnchantment(Items.chainmail_leggings, 2250));
		api.register(new AetherEnchantment(Items.chainmail_boots, 2250));

		api.register(new AetherEnchantment(Items.diamond_helmet, 10000));
		api.register(new AetherEnchantment(Items.diamond_chestplate, 10000));
		api.register(new AetherEnchantment(Items.diamond_leggings, 10000));
		api.register(new AetherEnchantment(Items.diamond_boots, 10000));
	}

	public static void initializeEnchantmentFuel() {
		AetherAPI api = AetherAPI.instance();
		api.register(new AetherEnchantmentFuel(AetherItems.ambrosium_shard, 500));
	}

	public static void initializeFreezables() {
		AetherAPI api = AetherAPI.instance();
		api.register(new AetherFreezable(AetherBlocks.aercloud, new ItemStack(AetherBlocks.aercloud, 1, 1), 100));
		api.register(new AetherFreezable(AetherBlocks.skyroot_leaves, AetherBlocks.crystal_leaves, 150));
		api.register(new AetherFreezable(AetherBlocks.golden_oak_leaves, AetherBlocks.crystal_leaves, 150));
		api.register(new AetherFreezable(new ItemStack(AetherItems.skyroot_bucket, 1, 1), Blocks.ice, 500));
		api.register(new AetherFreezable(AetherItems.ascending_dawn, AetherItems.welcoming_skies, 2500));
		api.register(new AetherFreezable(Blocks.ice, Blocks.packed_ice, 750));
		api.register(new AetherFreezable(Items.water_bucket, Blocks.ice, 500));
		api.register(new AetherFreezable(Items.lava_bucket, Blocks.obsidian, 500));
		api.register(new AetherFreezable(AetherItems.iron_ring, AetherItems.ice_ring, 2500));
		api.register(new AetherFreezable(AetherItems.golden_ring, AetherItems.ice_ring, 2500));
		api.register(new AetherFreezable(AetherItems.iron_pendant, AetherItems.ice_pendant, 2500));
		api.register(new AetherFreezable(AetherItems.golden_pendant, AetherItems.ice_pendant, 2500));
	}

	public static void initializeFreezableFuel() {
		AetherAPI api = AetherAPI.instance();
		api.register(new AetherFreezableFuel(AetherBlocks.icestone, 500));
	}

	public static void register() {
		initializeAccessories();
		initializeEnchantments();
		initializeEnchantmentFuel();
		initializeFreezables();
		initializeFreezableFuel();

		initializeRecipes();
		initializeShapelessRecipes();

		GameRegistry.addRecipe(new AccessoryDyesRecipe());
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(AetherBlocks.skyroot_log, 1, 1), new ItemStack(Items.coal, 1, 1), 0.15F);
		FurnaceRecipes.smelting().func_151394_a(new ItemStack(AetherBlocks.golden_oak_log, 1, 1), new ItemStack(Items.coal, 1, 1), 0.15F);

		OreDictionary.registerOre("stickWood", AetherItems.skyroot_stick);
	}

	private static void initializeShapelessRecipes() {
		registerShapeless("poison_dart_shooter", new ItemStack(AetherItems.dart_shooter, 1, 1), new ItemStack(AetherItems.dart_shooter, 1, 0), new ItemStack(AetherItems.skyroot_bucket, 1, 2));
		registerShapeless("purple_dye", new ItemStack(Items.dye, 2, 5), AetherBlocks.purple_flower);
		registerShapeless("skyroot_planks", new ItemStack(AetherBlocks.skyroot_planks, 4), new ItemStack(AetherBlocks.skyroot_log, 1, 1));
		registerShapeless("skyroot_planks", new ItemStack(AetherBlocks.skyroot_planks, 4), new ItemStack(AetherBlocks.golden_oak_log, 1, 1));
		registerShapeless("book_of_lore", new ItemStack(AetherItems.lore_book), new ItemStack(Items.book), new ItemStack(AetherItems.ambrosium_shard));
		registerShapeless("book_of_lore", new ItemStack(AetherItems.lore_book), new ItemStack(Items.book), new ItemStack(Items.flint));
		registerShapeless("book_of_lore", new ItemStack(AetherItems.lore_book), new ItemStack(Items.book), new ItemStack(Items.glowstone_dust));
	}

	private static void initializeRecipes() {
		register("nature_staf", new ItemStack(AetherItems.nature_staff), "Y", "X", 'Y', AetherItems.zanite_gemstone, 'X', AetherItems.skyroot_stick);
		register("skyroot_stick", new ItemStack(AetherItems.skyroot_stick, 4), "X", "X", 'X', AetherBlocks.skyroot_planks);
		register("trapdoor", new ItemStack(Blocks.trapdoor, 2), "XXX", "XXX", 'X', AetherBlocks.skyroot_planks);
		register("holystone_brick", new ItemStack(AetherBlocks.holystone_brick, 4), "XX", "XX", 'X', AetherBlocks.holystone);
		register("zanite_block", new ItemStack(AetherBlocks.zanite_block), "XXX", "XXX", "XXX", 'X', AetherItems.zanite_gemstone);
		register("zanite_gemstone", new ItemStack(AetherItems.zanite_gemstone, 9), "X", 'X', AetherBlocks.zanite_block);
		register("golden_dart_shooter", new ItemStack(AetherItems.dart_shooter, 1), "X  ", " Y ", "  Y", 'X', AetherItems.golden_amber, 'Y', AetherBlocks.skyroot_planks);
		register("golden_dart", new ItemStack(AetherItems.dart, 1), "X", "Y", "Z", 'X', Items.feather, 'Y', AetherItems.skyroot_stick, 'Z', AetherItems.golden_amber);
		register("poison_dart", new ItemStack(AetherItems.dart, 8, 1), "XXX", "XYX", "XXX", 'X', new ItemStack(AetherItems.dart, 1), 'Y', new ItemStack(AetherItems.skyroot_bucket, 1, 2));
		register("incubator", new ItemStack(AetherBlocks.incubator), "XXX", "XZX", "XXX", 'X', AetherBlocks.holystone, 'Z', AetherBlocks.ambrosium_torch);
		register("freezer", new ItemStack(AetherBlocks.freezer), "XXX", "XYX", "ZZZ", 'X', AetherBlocks.holystone, 'Y', AetherBlocks.icestone, 'Z', AetherBlocks.skyroot_planks);
		register("enchanter", new ItemStack(AetherBlocks.enchanter), "XXX", "XYX", "XXX", 'X', AetherBlocks.holystone, 'Y', AetherItems.zanite_gemstone);
		register("furnace", new ItemStack(Blocks.furnace), "XXX", "X X", "XXX", 'X', AetherBlocks.holystone);
		register("ladder", new ItemStack(Blocks.ladder, 4), "X X", "XXX", "X X", 'X', AetherItems.skyroot_stick);
		register("jukebox", new ItemStack(Blocks.jukebox), "XXX", "XYX", "XXX", 'X', AetherBlocks.skyroot_planks, 'Y', AetherBlocks.enchanted_gravitite);
		register("oak_door", new ItemStack(Items.wooden_door, 3), "XX", "XX", "XX", 'X', AetherBlocks.skyroot_planks);
		register("sign", new ItemStack(Items.sign, 3), "XXX", "XXX", " Y ", 'X', AetherBlocks.skyroot_planks, 'Y', AetherItems.skyroot_stick);
		register("ambrosium_torch", new ItemStack(AetherBlocks.ambrosium_torch, 2), "Z", "Y", 'Z', AetherItems.ambrosium_shard, 'Y', AetherItems.skyroot_stick);
		register("lead", new ItemStack(Items.lead, 2), "YY ", "YX ", "  Y", 'Y', Items.string, 'X', AetherItems.swet_ball);

		register("cloud_parachute", new ItemStack(AetherItems.cloud_parachute, 1), "XX", "XX", 'X', new ItemStack(AetherBlocks.aercloud, 1));
		register("golden_parachute", new ItemStack(AetherItems.golden_parachute, 1), "XX", "XX", 'X', new ItemStack(AetherBlocks.aercloud, 1, 2));
		register("saddle", new ItemStack(Items.saddle, 1), "XXX", "XZX", 'X', Items.leather, 'Z', Items.string);
		register("chest", new ItemStack(Blocks.chest, 1), "XXX", "X X", "XXX", 'X', AetherBlocks.skyroot_planks);
		register("skyroot_bucket", new ItemStack(AetherItems.skyroot_bucket, 1, 0), "Z Z", " Z ", 'Z', AetherBlocks.skyroot_planks);
		register("crafting_table", new ItemStack(Blocks.crafting_table, 1), "XX", "XX", 'X', AetherBlocks.skyroot_planks);

		register("gravitite_helmet", new ItemStack(AetherItems.gravitite_helmet, 1), "XXX", "X X", 'X', AetherBlocks.enchanted_gravitite);
		register("gravitite_chestplate", new ItemStack(AetherItems.gravitite_chestplate, 1), "X X", "XXX", "XXX", 'X', AetherBlocks.enchanted_gravitite);
		register("gravitite_leggings", new ItemStack(AetherItems.gravitite_leggings, 1), "XXX", "X X", "X X", 'X', AetherBlocks.enchanted_gravitite);
		register("gravitite_boots", new ItemStack(AetherItems.gravitite_boots, 1), "X X", "X X", 'X', AetherBlocks.enchanted_gravitite);
		register("zanite_helmet", new ItemStack(AetherItems.zanite_helmet, 1), "XXX", "X X", 'X', AetherItems.zanite_gemstone);
		register("zanite_chestplate", new ItemStack(AetherItems.zanite_chestplate, 1), "X X", "XXX", "XXX", 'X', AetherItems.zanite_gemstone);
		register("zanite_leggings", new ItemStack(AetherItems.zanite_leggings, 1), "XXX", "X X", "X X", 'X', AetherItems.zanite_gemstone);
		register("zanite_boots", new ItemStack(AetherItems.zanite_boots, 1), "X X", "X X", 'X', AetherItems.zanite_gemstone);

		register("skyroot_pickaxe", new ItemStack(AetherItems.skyroot_pickaxe, 1), "ZZZ", " Y ", " Y ", 'Z', AetherBlocks.skyroot_planks, 'Y', AetherItems.skyroot_stick);
		register("holystone_pickaxe", new ItemStack(AetherItems.holystone_pickaxe, 1), "ZZZ", " Y ", " Y ", 'Z', AetherBlocks.holystone, 'Y', AetherItems.skyroot_stick);
		register("zanite_pickaxe", new ItemStack(AetherItems.zanite_pickaxe, 1), "ZZZ", " Y ", " Y ", 'Z', AetherItems.zanite_gemstone, 'Y', AetherItems.skyroot_stick);
		register("gravitite_pickaxe", new ItemStack(AetherItems.gravitite_pickaxe, 1), "ZZZ", " Y ", " Y ", 'Z', AetherBlocks.enchanted_gravitite, 'Y', AetherItems.skyroot_stick);
		register("skyroot_axe", new ItemStack(AetherItems.skyroot_axe, 1), "ZZ", "ZY", " Y", 'Z', AetherBlocks.skyroot_planks, 'Y', AetherItems.skyroot_stick);
		register("holystone_axe", new ItemStack(AetherItems.holystone_axe, 1), "ZZ", "ZY", " Y", 'Z', AetherBlocks.holystone, 'Y', AetherItems.skyroot_stick);
		register("zanite_axe", new ItemStack(AetherItems.zanite_axe, 1), "ZZ", "ZY", " Y", 'Z', AetherItems.zanite_gemstone, 'Y', AetherItems.skyroot_stick);
		register("gravitite_axe", new ItemStack(AetherItems.gravitite_axe, 1), "ZZ", "ZY", " Y", 'Z', AetherBlocks.enchanted_gravitite, 'Y', AetherItems.skyroot_stick);
		register("skyroot_shovel", new ItemStack(AetherItems.skyroot_shovel, 1), "Z", "Y", "Y", 'Z', AetherBlocks.skyroot_planks, 'Y', AetherItems.skyroot_stick);
		register("holystone_shovel", new ItemStack(AetherItems.holystone_shovel, 1), "Z", "Y", "Y", 'Z', AetherBlocks.holystone, 'Y', AetherItems.skyroot_stick);
		register("zanite_shovel", new ItemStack(AetherItems.zanite_shovel, 1), "Z", "Y", "Y", 'Z', AetherItems.zanite_gemstone, 'Y', AetherItems.skyroot_stick);
		register("gravitite_shovel", new ItemStack(AetherItems.gravitite_shovel, 1), "Z", "Y", "Y", 'Z', AetherBlocks.enchanted_gravitite, 'Y', AetherItems.skyroot_stick);
		register("skyroot_sword", new ItemStack(AetherItems.skyroot_sword, 1), "Z", "Z", "Y", 'Z', AetherBlocks.skyroot_planks, 'Y', AetherItems.skyroot_stick);
		register("holystone_sword", new ItemStack(AetherItems.holystone_sword, 1), "Z", "Z", "Y", 'Z', AetherBlocks.holystone, 'Y', AetherItems.skyroot_stick);
		register("zanite_sword", new ItemStack(AetherItems.zanite_sword, 1), "Z", "Z", "Y", 'Z', AetherItems.zanite_gemstone, 'Y', AetherItems.skyroot_stick);
		register("gravitite_sword", new ItemStack(AetherItems.gravitite_sword, 1), "Z", "Z", "Y", 'Z', AetherBlocks.enchanted_gravitite, 'Y', AetherItems.skyroot_stick);

		register("white_cape", new ItemStack(AetherItems.white_cape), "ZZ", "ZZ", "ZZ", 'Z', new ItemStack(Blocks.wool, 1, 0));

		register("iron_pendant", new ItemStack(AetherItems.iron_pendant), " Z ", "Z Z", " ZS", 'Z', new ItemStack(Items.iron_ingot), 'S', new ItemStack(Items.string));
		register("golden_pendant", new ItemStack(AetherItems.golden_pendant), " Z ", "Z Z", " ZS", 'Z', new ItemStack(Items.gold_ingot), 'S', new ItemStack(Items.string));

		register("leather_gloves", new ItemStack(AetherItems.leather_gloves), "C C", 'C', Items.leather);
		register("iron_gloves", new ItemStack(AetherItems.iron_gloves), "C C", 'C', Items.iron_ingot);
		register("golden_gloves", new ItemStack(AetherItems.golden_gloves), "C C", 'C', Items.gold_ingot);
		register("diamond_gloves", new ItemStack(AetherItems.diamond_gloves), "C C", 'C', Items.diamond);
		register("zanite_gloves", new ItemStack(AetherItems.zanite_gloves), "C C", 'C', AetherItems.zanite_gemstone);
		register("gravitite_gloves", new ItemStack(AetherItems.gravitite_gloves), "C C", 'C', AetherBlocks.enchanted_gravitite);

		register("skyroot_fence", new ItemStack(AetherBlocks.skyroot_fence, 3), "ZXZ", "ZXZ", 'Z', new ItemStack(AetherBlocks.skyroot_planks), 'X', new ItemStack(AetherItems.skyroot_stick));
		register("skyroot_fence_gate", new ItemStack(AetherBlocks.skyroot_fence_gate), "ZXZ", "ZXZ", 'X', new ItemStack(AetherBlocks.skyroot_planks), 'Z', new ItemStack(AetherItems.skyroot_stick));

		register("carved_stone_slab", new ItemStack(AetherBlocks.carved_slab, 6), "ZZZ", 'Z', new ItemStack(AetherBlocks.carved_stone));
		register("angelic_stone_slab", new ItemStack(AetherBlocks.angelic_slab, 6), "ZZZ", 'Z', new ItemStack(AetherBlocks.angelic_stone));
		register("hellfire_stone_slab", new ItemStack(AetherBlocks.hellfire_slab, 6), "ZZZ", 'Z', new ItemStack(AetherBlocks.hellfire_stone));
		register("holystone_slab", new ItemStack(AetherBlocks.holystone_slab, 6), "ZZZ", 'Z', new ItemStack(AetherBlocks.holystone, 1, 1));
		register("mossy_holystone_slab", new ItemStack(AetherBlocks.mossy_holystone_slab, 6), "ZZZ", 'Z', new ItemStack(AetherBlocks.mossy_holystone, 1, 1));
		register("holystone_brick_slab", new ItemStack(AetherBlocks.holystone_brick_slab, 6), "ZZZ", 'Z', new ItemStack(AetherBlocks.holystone_brick, 1));
		register("skyroot_slab", new ItemStack(AetherBlocks.skyroot_slab, 6), "ZZZ", 'Z', new ItemStack(AetherBlocks.skyroot_planks));

		register("carved_stone_wall", new ItemStack(AetherBlocks.carved_wall, 6), "ZZZ", "ZZZ", 'Z', new ItemStack(AetherBlocks.carved_stone));
		register("angelic_stone_wall", new ItemStack(AetherBlocks.angelic_wall, 6), "ZZZ", "ZZZ", 'Z', new ItemStack(AetherBlocks.angelic_stone));
		register("hellfire_stone_wall", new ItemStack(AetherBlocks.hellfire_wall, 6), "ZZZ", "ZZZ", 'Z', new ItemStack(AetherBlocks.hellfire_stone));
		register("holystone_wall", new ItemStack(AetherBlocks.holystone_wall, 6), "ZZZ", "ZZZ", 'Z', new ItemStack(AetherBlocks.holystone, 1, 1));
		register("mossy_holystone_wall", new ItemStack(AetherBlocks.mossy_holystone_wall, 6), "ZZZ", "ZZZ", 'Z', new ItemStack(AetherBlocks.mossy_holystone, 1, 1));
		register("holystone_brick_wall", new ItemStack(AetherBlocks.holystone_brick_wall, 6), "ZZZ", "ZZZ", 'Z', new ItemStack(AetherBlocks.holystone_brick, 1));

		register("carved_stone_stairs", new ItemStack(AetherBlocks.carved_stairs, 4), "Z  ", "ZZ ", "ZZZ", 'Z', new ItemStack(AetherBlocks.carved_stone));
		register("angelic_stone_stairs", new ItemStack(AetherBlocks.angelic_stairs, 4), "Z  ", "ZZ ", "ZZZ", 'Z', new ItemStack(AetherBlocks.angelic_stone));
		register("hellfire_stone_stairs", new ItemStack(AetherBlocks.hellfire_stairs, 4), "Z  ", "ZZ ", "ZZZ", 'Z', new ItemStack(AetherBlocks.hellfire_stone));
		register("holystone_stairs", new ItemStack(AetherBlocks.holystone_stairs, 4), "Z  ", "ZZ ", "ZZZ", 'Z', new ItemStack(AetherBlocks.holystone, 1, 1));
		register("mossy_holystone_stairs", new ItemStack(AetherBlocks.mossy_holystone_stairs, 4), "Z  ", "ZZ ", "ZZZ", 'Z', new ItemStack(AetherBlocks.mossy_holystone, 1, 1));
		register("holystone_brick_stairs", new ItemStack(AetherBlocks.holystone_brick_stairs, 4), "Z  ", "ZZ ", "ZZZ", 'Z', new ItemStack(AetherBlocks.holystone_brick, 1));
		register("skyroot_stairs", new ItemStack(AetherBlocks.skyroot_stairs, 4), "Z  ", "ZZ ", "ZZZ", 'Z', new ItemStack(AetherBlocks.skyroot_planks));

		register("skyroot_bookshelf", new ItemStack(AetherBlocks.skyroot_bookshelf, 1),  "ZZZ", "XXX", "ZZZ", 'Z', new ItemStack(AetherBlocks.skyroot_planks), 'X', new ItemStack(Items.book));

		register("skyroot_bed_item", new ItemStack(AetherItems.skyroot_bed_item, 1),  "XXX", "ZZZ", 'Z', new ItemStack(AetherBlocks.skyroot_planks), 'X', Blocks.wool);
	}

	private static void register(String name, ItemStack stack, Object... recipe) {
		GameRegistry.addRecipe(stack, recipe);
	}

	private static void registerShapeless(String name, ItemStack stack, Object... recipe) {
		GameRegistry.addShapelessRecipe(stack, recipe);
	}

}
