package com.gildedgames.the_aether.blocks;

import com.gildedgames.the_aether.blocks.container.Enchanter;
import com.gildedgames.the_aether.blocks.container.Freezer;
import com.gildedgames.the_aether.blocks.container.Incubator;
import com.gildedgames.the_aether.blocks.container.SunAltar;
import com.gildedgames.the_aether.blocks.decorative.BlockAerogel;
import com.gildedgames.the_aether.blocks.decorative.AetherFence;
import com.gildedgames.the_aether.blocks.decorative.AetherFenceGate;
import com.gildedgames.the_aether.blocks.decorative.AetherSlab;
import com.gildedgames.the_aether.blocks.decorative.BlockAetherStairs;
import com.gildedgames.the_aether.blocks.decorative.AetherWall;
import com.gildedgames.the_aether.blocks.decorative.BlockAmbrosiumTorch;
import com.gildedgames.the_aether.blocks.decorative.Present;
import com.gildedgames.the_aether.blocks.decorative.BlockQuicksoilGlass;
import com.gildedgames.the_aether.blocks.decorative.SkyrootBookshelf;
import com.gildedgames.the_aether.blocks.decorative.BlockSkyrootPlanks;
import com.gildedgames.the_aether.blocks.decorative.BlockZanite;
import com.gildedgames.the_aether.blocks.dungeon.DungeonBaseBlock;
import com.gildedgames.the_aether.blocks.dungeon.DungeonTrap;
import com.gildedgames.the_aether.blocks.dungeon.MimicChest;
import com.gildedgames.the_aether.blocks.dungeon.BlockPillar;
import com.gildedgames.the_aether.blocks.dungeon.TreasureChest;
import com.gildedgames.the_aether.blocks.natural.Aercloud;
import com.gildedgames.the_aether.blocks.natural.AetherDirt;
import com.gildedgames.the_aether.blocks.natural.AetherFlower;
import com.gildedgames.the_aether.blocks.natural.AetherGrass;
import com.gildedgames.the_aether.blocks.natural.AetherLeaves;
import com.gildedgames.the_aether.blocks.natural.AetherLog;
import com.gildedgames.the_aether.blocks.natural.AetherOre;
import com.gildedgames.the_aether.blocks.natural.BerryBush;
import com.gildedgames.the_aether.blocks.natural.BerryBushStem;
import com.gildedgames.the_aether.blocks.natural.EnchantedAetherGrass;
import com.gildedgames.the_aether.blocks.natural.Holystone;
import com.gildedgames.the_aether.blocks.natural.Quicksoil;
import com.gildedgames.the_aether.blocks.portal.BlockAetherPortal;
import com.gildedgames.the_aether.blocks.util.FloatingBlock;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.items.block.AetherSlabItem;
import com.gildedgames.the_aether.items.block.EnchanterItem;
import com.gildedgames.the_aether.items.block.MetadataBlockItem;
import com.gildedgames.the_aether.items.block.RarityBlockItem;
import com.gildedgames.the_aether.world.biome.decoration.AetherOakTreeFeature;
import com.gildedgames.the_aether.world.biome.decoration.AetherSkyrootTreeFeature;
import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.Aether;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;	// BlockItem
import net.minecraft.item.Item;
import net.minecraft.item.EnumRarity;
import net.minecraft.init.Blocks;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class AetherBlocks {
	private static Method fml_gamedata_register_block_method;
	private static Method fml_gamedata_register_item_method;
	private static GameData game_data;

	public static Block aether_grass, enchanted_aether_grass, aether_dirt;
	public static Block holystone, mossy_holystone, holystone_brick;
	public static Block aercloud, quicksoil, icestone;
	public static Block ambrosium_ore, zanite_ore, gravitite_ore;
	public static Block skyroot_leaves, golden_oak_leaves, skyroot_log, golden_oak_log, skyroot_planks;
	public static Block quicksoil_glass, aerogel;
	public static Block enchanted_gravitite, zanite_block;
	public static Block berry_bush, berry_bush_stem;
	public static Block enchanter, freezer, incubator;
	public static Block ambrosium_torch;
	public static Block aether_portal;
	public static Block chest_mimic, treasure_chest;
	public static Block carved_stone, angelic_stone, hellfire_stone;
	public static Block sentry_stone, light_angelic_stone, light_hellfire_stone;
	public static Block locked_carved_stone, locked_angelic_stone, locked_hellfire_stone;
	public static Block locked_sentry_stone, locked_light_angelic_stone, locked_light_hellfire_stone;
	public static Block carved_trap, angelic_trap, hellfire_trap;
	public static Block purple_flower, white_flower;
	public static Block skyroot_sapling, golden_oak_sapling;
	public static Block crystal_leaves, crystal_fruit_leaves;
	public static Block pillar, pillar_top;
	public static Block skyroot_fence, skyroot_fence_gate;
	public static Block carved_stairs, angelic_stairs, hellfire_stairs, skyroot_stairs, mossy_holystone_stairs, holystone_stairs, holystone_brick_stairs, aerogel_stairs;
	public static Block carved_slab, angelic_slab, hellfire_slab, skyroot_slab, holystone_slab, holystone_brick_slab, mossy_holystone_slab, aerogel_slab;
	public static Block carved_double_slab, angelic_double_slab, hellfire_double_slab, skyroot_double_slab, holystone_double_slab, holystone_brick_double_slab, mossy_holystone_double_slab, aerogel_double_slab;
	public static Block holystone_wall, mossy_holystone_wall, holystone_brick_wall, carved_wall, angelic_wall, hellfire_wall, aerogel_wall;
	public static Block holiday_leaves, decorated_holiday_leaves, present;
	public static Block sun_altar;
	public static Block skyroot_bookshelf;
	public static Block skyroot_bed;

	private static Block register(int numeric_id, String name, Block block, boolean add_to_creative_tab) throws IllegalAccessException, InvocationTargetException {
		block.setBlockName(name);
		if(add_to_creative_tab) block.setCreativeTab(AetherCreativeTabs.blocks);
		if(numeric_id != -1 && AetherConfig.should_register_legacy_numeric_ids()) {
			Integer id = Integer.valueOf(numeric_id);
			name = Aether.MOD_ID + ":" + name;
			fml_gamedata_register_block_method.invoke(game_data, block, name, id);
			ItemBlock block_item = new ItemBlock(block);
			fml_gamedata_register_item_method.invoke(game_data, block_item, name, id);
		} else {
			GameRegistry.registerBlock(block, name);
		}
		return block;
	}

	private static Block register(int numeric_id, String name, Block block) throws IllegalAccessException, InvocationTargetException {
		return register(numeric_id, name, block, true);
	}

	private static Block register(String name, Block block) throws IllegalAccessException, InvocationTargetException {
		return register(-1, name, block, true);
	}

	private static Block register_without_creative_item(int numeric_id, String name, Block block) throws IllegalAccessException, InvocationTargetException {
		return register(numeric_id, name, block, false);
	}

	private static void register_with_block_item(int numeric_id, String name, Block block, ItemBlock block_item) throws IllegalAccessException, InvocationTargetException {
		Integer id = Integer.valueOf(numeric_id);
		name = Aether.MOD_ID + ":" + name;
		fml_gamedata_register_block_method.invoke(game_data, block, name, id);
		fml_gamedata_register_item_method.invoke(game_data, block_item, name, id);
	}

	private static Block register_slab(String name, Block slab, Block double_slab) {
		slab.setBlockName(name);
		slab.setCreativeTab(AetherCreativeTabs.blocks);

		GameRegistry.registerBlock(slab, AetherSlabItem.class, name, (AetherSlab)slab, (AetherSlab)double_slab, false);

		return slab;
	}

	private static Block register_with_rarity(int numeric_id, String name, Block block, EnumRarity rarity) throws IllegalAccessException, InvocationTargetException {
		block.setBlockName(name);
		block.setCreativeTab(AetherCreativeTabs.blocks);
		if(numeric_id != -1 && AetherConfig.should_register_legacy_numeric_ids()) {
			register_with_block_item(numeric_id, name, block, new RarityBlockItem(block, rarity));
		} else {
			GameRegistry.registerBlock(block, RarityBlockItem.class, name, rarity);
		}
		return block;
	}

	private static Block register_with_rarity(String name, Block block, EnumRarity rarity) throws IllegalAccessException, InvocationTargetException {
		return register_with_rarity(-1, name, block, rarity);
	}

	private static Block register_has_metadata(int numeric_id, String name, Block block) throws IllegalAccessException, InvocationTargetException {
		block.setBlockName(name);
		block.setCreativeTab(AetherCreativeTabs.blocks);
		if(numeric_id != -1 && AetherConfig.should_register_legacy_numeric_ids()) {
			register_with_block_item(numeric_id, name, block, new MetadataBlockItem(block));
		} else {
			GameRegistry.registerBlock(block, MetadataBlockItem.class, name);
		}
		return block;
	}

	private static Block register_has_metadata(String name, Block block) throws IllegalAccessException, InvocationTargetException {
		return register_has_metadata(-1, name, block);
	}

	private static Block register_enchanter(int numeric_id, String name, Enchanter block) throws IllegalAccessException, InvocationTargetException {
		block.setBlockName(name);
		block.setCreativeTab(AetherCreativeTabs.blocks);
		if(numeric_id != -1 && AetherConfig.should_register_legacy_numeric_ids()) {
			register_with_block_item(numeric_id, name, block, new EnchanterItem(block));
		} else {
			GameRegistry.registerBlock(block, EnchanterItem.class, name);
		}
		return block;
	}

	public static void initialization() throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if(AetherConfig.should_register_legacy_numeric_ids()) {
			fml_gamedata_register_block_method = GameData.class.getDeclaredMethod("registerBlock", Block.class, String.class, int.class);
			fml_gamedata_register_block_method.setAccessible(true);
			fml_gamedata_register_item_method = GameData.class.getDeclaredMethod("registerItem", Item.class, String.class, int.class);
			fml_gamedata_register_item_method.setAccessible(true);
			Field fml_gamedata_main_data_field = GameData.class.getDeclaredField("mainData");
			fml_gamedata_main_data_field.setAccessible(true);
			game_data = (GameData)fml_gamedata_main_data_field.get(null);
		}

		aether_portal = register_without_creative_item(165, "aether_portal", new BlockAetherPortal());
		aether_grass = register_has_metadata(167, "aether_grass", new AetherGrass());
		aether_dirt = register_has_metadata(166, "aether_dirt", new AetherDirt());
		quicksoil = register_has_metadata(168, "quicksoil", new Quicksoil());
		holystone = register_has_metadata(169, "holystone", new Holystone());
		icestone = register(170, "icestone", new BlockIcestone());
		aercloud = register_has_metadata(171, "aercloud", new Aercloud());
		aerogel = register_with_rarity(172, "aerogel", new BlockAerogel(), AetherItems.aether_loot);
		ambrosium_ore = register(181, "ambrosium_ore", new AetherOre(0).setBlockTextureName(Aether.find("ambrosium_ore")));
		zanite_ore = register(183, "zanite_ore", new AetherOre(1).setBlockTextureName(Aether.find("zanite_ore")));
		gravitite_ore = register(184, "gravitite_ore", new FloatingBlock(Material.rock, false).setHardness(5F).setBlockTextureName(Aether.find("gravitite_ore")));
		enchanted_gravitite = register_with_rarity(185, "enchanted_gravitite", new FloatingBlock(Material.rock, true).setHardness(5F).setBlockTextureName(Aether.find("enchanted_gravitite")), EnumRarity.rare);
		zanite_block = register(194, "zanite_block", new BlockZanite());
		skyroot_leaves = register(177, "skyroot_leaves", new AetherLeaves().setBlockTextureName(Aether.find("skyroot_leaves")));
		golden_oak_leaves = register(178, "golden_oak_leaves", new AetherLeaves().setBlockTextureName(Aether.find("golden_oak_leaves")));
		crystal_leaves = register(204, "crystal_leaves", new AetherLeaves().setBlockTextureName(Aether.find("crystal_leaves")));
		crystal_fruit_leaves = register("crystal_fruit_leaves", new AetherLeaves().setBlockTextureName(Aether.find("crystal_fruit_leaves")));
		enchanted_aether_grass = register_with_rarity("enchanted_aether_grass", new EnchantedAetherGrass(), EnumRarity.rare);
		mossy_holystone = register_has_metadata("mossy_holystone", new Holystone().setBlockTextureName(Aether.find("mossy_holystone")));
		holystone_brick = register("holystone_brick", new BlockAether(Material.rock, Aether.find("holystone_brick")).setHardness(0.5F).setResistance(10F));
		holiday_leaves = register(200, "holiday_leaves", new AetherLeaves().setBlockTextureName(Aether.find("holiday_leaves")));
		decorated_holiday_leaves = register("decorated_holiday_leaves", new AetherLeaves().setBlockTextureName(Aether.find("decorated_holiday_leaves")));
		present = register(201, "present", new Present());
		skyroot_log = register_has_metadata(175, "skyroot_log", new AetherLog().setBlockTextureName(Aether.find("skyroot_log")));
		golden_oak_log = register_has_metadata("golden_oak_log", new AetherLog().setBlockTextureName(Aether.find("golden_oak_log")));
		skyroot_planks = register(176, "skyroot_planks", new BlockSkyrootPlanks());
		quicksoil_glass = register_with_rarity(195, "quicksoil_glass", new BlockQuicksoilGlass(), EnumRarity.rare);
		ambrosium_torch = register(182, "ambrosium_torch", new BlockAmbrosiumTorch().setBlockTextureName(Aether.find("ambrosium_torch")));
		berry_bush_stem = register(203, "berry_bush_stem", new BerryBushStem());
		berry_bush = register(202, "berry_bush", new BerryBush());
		purple_flower = register(198, "purple_flower", new AetherFlower().setBlockTextureName(Aether.find("purple_flower")));
		white_flower = register(197, "white_flower", new AetherFlower().setBlockTextureName(Aether.find("white_flower")));
		skyroot_sapling = register(179, "skyroot_sapling", new AetherSapling(new AetherSkyrootTreeFeature(false)).setBlockTextureName(Aether.find("skyroot_sapling")));
		golden_oak_sapling = register(180, "golden_oak_sapling", new AetherSapling(new AetherOakTreeFeature()).setBlockTextureName(Aether.find("golden_oak_sapling")));
		enchanter = register_enchanter(173, "enchanter", new Enchanter());
		freezer = register_has_metadata(196, "freezer", new Freezer());
		incubator = register_has_metadata(174, "incubator", new Incubator());
		sun_altar = register(219, "sun_altar", new SunAltar());
		chest_mimic = register(187, "chest_mimic", new MimicChest());
		treasure_chest = register(188, "treasure_chest", new TreasureChest());
		pillar = register(193, "pillar", new BlockPillar("pillar_top", "pillar_side"));
		pillar_top = register("pillar_top", new BlockPillar("pillar_top", "pillar_carved"));
		carved_stone = register("carved_stone", new DungeonBaseBlock(false).setBlockTextureName(Aether.find("carved_stone")));
		sentry_stone = register("sentry_stone", new DungeonBaseBlock(true).setBlockTextureName(Aether.find("sentry_stone")));
		angelic_stone = register("angelic_stone", new DungeonBaseBlock(false).setBlockTextureName(Aether.find("angelic_stone")));
		light_angelic_stone = register("light_angelic_stone", new DungeonBaseBlock(true).setBlockTextureName(Aether.find("light_angelic_stone")));
		hellfire_stone = register("hellfire_stone", new DungeonBaseBlock(false).setBlockTextureName(Aether.find("hellfire_stone")));
		light_hellfire_stone = register("light_hellfire_stone", new DungeonBaseBlock(true).setBlockTextureName(Aether.find("light_hellfire_stone")));
		locked_carved_stone = register("locked_carved_stone", new DungeonBaseBlock(carved_stone, false).setBlockTextureName(Aether.find("carved_stone"))).setCreativeTab(null);
		locked_sentry_stone = register("locked_sentry_stone", new DungeonBaseBlock(sentry_stone, true).setBlockTextureName(Aether.find("sentry_stone"))).setCreativeTab(null);
		locked_angelic_stone = register("locked_angelic_stone", new DungeonBaseBlock(angelic_stone, false).setBlockTextureName(Aether.find("angelic_stone"))).setCreativeTab(null);
		locked_light_angelic_stone = register("locked_light_angelic_stone", new DungeonBaseBlock(light_angelic_stone, true).setBlockTextureName(Aether.find("light_angelic_stone"))).setCreativeTab(null);
		locked_hellfire_stone = register("locked_hellfire_stone", new DungeonBaseBlock(hellfire_stone, false).setBlockTextureName(Aether.find("hellfire_stone"))).setCreativeTab(null);
		locked_light_hellfire_stone = register("locked_light_hellfire_stone", new DungeonBaseBlock(light_hellfire_stone, true).setBlockTextureName(Aether.find("light_hellfire_stone"))).setCreativeTab(null);
		carved_trap = register("carved_trap", new DungeonTrap(carved_stone).setBlockTextureName(Aether.find("carved_stone"))).setCreativeTab(null);
		angelic_trap = register("angelic_trap", new DungeonTrap(angelic_stone).setBlockTextureName(Aether.find("angelic_stone"))).setCreativeTab(null);
		hellfire_trap = register("hellfire_trap", new DungeonTrap(hellfire_stone).setBlockTextureName(Aether.find("hellfire_stone"))).setCreativeTab(null);
		skyroot_fence = register(215, "skyroot_fence", new AetherFence());
		skyroot_fence_gate = register("skyroot_fence_gate", new AetherFenceGate());
		carved_wall = register("carved_wall", new AetherWall(carved_stone));
		angelic_wall = register("angelic_wall", new AetherWall(angelic_stone));
		hellfire_wall = register("hellfire_wall", new AetherWall(hellfire_stone));
		holystone_wall = register("holystone_wall", new AetherWall(holystone));
		holystone_brick_wall = register("holystone_brick_wall", new AetherWall(holystone_brick));
		mossy_holystone_wall = register("mossy_holystone_wall", new AetherWall(mossy_holystone));
		aerogel_wall = register_with_rarity("aerogel_wall", new AetherWall(aerogel), AetherItems.aether_loot);
		carved_stairs = register(209, "carved_stairs", new BlockAetherStairs(carved_stone));
		angelic_stairs = register(210, "angelic_stairs", new BlockAetherStairs(angelic_stone));
		hellfire_stairs = register(211, "hellfire_stairs", new BlockAetherStairs(hellfire_stone));
		skyroot_stairs = register(208, "skyroot_stairs", new BlockAetherStairs(skyroot_planks));
		holystone_stairs = register(205, "holystone_stairs", new BlockAetherStairs(holystone));
		holystone_brick_stairs = register("holystone_brick_stairs", new BlockAetherStairs(holystone_brick));
		mossy_holystone_stairs = register(206, "mossy_holystone_stairs", new BlockAetherStairs(mossy_holystone));
		aerogel_stairs = register_with_rarity("aerogel_stairs", new BlockAetherStairs(aerogel), AetherItems.aether_loot);
		skyroot_double_slab = register("skyroot_double_slab", new AetherSlab("skyroot_double_slab", true, Material.wood).setBlockTextureName(Aether.find("skyroot_planks")).setHardness(2F).setResistance(5F)).setCreativeTab(null);
		carved_double_slab = register("carved_double_slab", new AetherSlab("carved_double_slab", true, Material.rock).setBlockTextureName(Aether.find("carved_stone")).setHardness(2F).setResistance(10F)).setCreativeTab(null);
		angelic_double_slab = register("angelic_double_slab", new AetherSlab("angelic_double_slab", true, Material.rock).setBlockTextureName(Aether.find("angelic_stone")).setHardness(2F).setResistance(10F)).setCreativeTab(null);
		hellfire_double_slab = register("hellfire_double_slab", new AetherSlab("hellfire_double_slab", true, Material.rock).setBlockTextureName(Aether.find("hellfire_stone")).setHardness(2F).setResistance(10F)).setCreativeTab(null);
		holystone_double_slab = register("holystone_double_slab", new AetherSlab("holystone_double_slab", true, Material.rock).setBlockTextureName(Aether.find("holystone")).setHardness(2F).setResistance(10F)).setCreativeTab(null);
		mossy_holystone_double_slab = register("mossy_holystone_double_slab", new AetherSlab("mossy_holystone_double_slab", true, Material.rock).setBlockTextureName(Aether.find("mossy_holystone")).setHardness(2F).setResistance(10F)).setCreativeTab(null);
		holystone_brick_double_slab = register("holystone_brick_double_slab", new AetherSlab("holystone_brick_double_slab", true, Material.rock).setBlockTextureName(Aether.find("holystone_brick")).setHardness(2F).setResistance(10F)).setCreativeTab(null);
		aerogel_double_slab = register("aerogel_double_slab", new AetherSlab("aerogel_double_slab", true, Material.rock).setBlockTextureName(Aether.find("aerogel")).setHardness(2F).setResistance(2000F).setLightOpacity(3).setStepSound(Block.soundTypeMetal)).setCreativeTab(null);
		skyroot_slab = register_slab("skyroot_slab", new AetherSlab("skyroot_slab", false, Material.wood).setBlockTextureName(Aether.find("skyroot_planks")).setHardness(2F).setResistance(5F), skyroot_double_slab);
		carved_slab = register_slab("carved_slab", new AetherSlab("carved_slab", false, Material.rock).setBlockTextureName(Aether.find("carved_stone")).setHardness(0.5F).setResistance(10F), carved_double_slab);
		angelic_slab = register_slab("angelic_slab", new AetherSlab("angelic_slab", false, Material.rock).setBlockTextureName(Aether.find("angelic_stone")).setHardness(0.5F).setResistance(10F), angelic_double_slab);
		hellfire_slab = register_slab("hellfire_slab", new AetherSlab("hellfire_slab", false, Material.rock).setBlockTextureName(Aether.find("hellfire_stone")).setHardness(0.5F).setResistance(10F), hellfire_double_slab);
		holystone_slab = register_slab("holystone_slab", new AetherSlab("holystone_slab", false, Material.rock).setBlockTextureName(Aether.find("holystone")).setHardness(0.5F).setResistance(10F), holystone_double_slab);
		mossy_holystone_slab = register_slab("mossy_holystone_slab", new AetherSlab("mossy_holystone_slab", false, Material.rock).setBlockTextureName(Aether.find("mossy_holystone")).setHardness(0.5F).setResistance(10F), mossy_holystone_double_slab);
		holystone_brick_slab = register_slab("holystone_brick_slab", new AetherSlab("holystone_brick_slab", false, Material.rock).setBlockTextureName(Aether.find("holystone_brick")).setHardness(0.5F).setResistance(10F), holystone_brick_double_slab);
		aerogel_slab = register_slab("aerogel_slab", new AetherSlab("aerogel_slab", false, Material.rock).setBlockTextureName(Aether.find("aerogel")).setHardness(0.5F).setResistance(2000F).setLightOpacity(3).setStepSound(Block.soundTypeMetal), aerogel_double_slab);
		skyroot_bookshelf = register("skyroot_bookshelf", new SkyrootBookshelf());
		skyroot_bed = register_without_creative_item(199, "skyroot_bed", new SkyrootBedBlock().setBlockTextureName(Aether.find("skyroot_bed")));
	}

	public static void initializeHarvestLevels() {
		aether_grass.setHarvestLevel("shovel", 0);
		enchanted_aether_grass.setHarvestLevel("shovel", 0);
		aether_dirt.setHarvestLevel("shovel", 0);
		holystone.setHarvestLevel("pickaxe", 0);
		mossy_holystone.setHarvestLevel("pickaxe", 0);
		holystone_brick.setHarvestLevel("pickaxe", 0);
		aercloud.setHarvestLevel("shovel", 0);
		quicksoil.setHarvestLevel("shovel", 0);
		icestone.setHarvestLevel("pickaxe", 1);
		ambrosium_ore.setHarvestLevel("pickaxe", 0);
		zanite_ore.setHarvestLevel("pickaxe", 1);
		gravitite_ore.setHarvestLevel("pickaxe", 2);
		skyroot_log.setHarvestLevel("axe", 0);
		skyroot_planks.setHarvestLevel("axe", 0);
		aerogel.setHarvestLevel("pickaxe", 3);
		enchanted_gravitite.setHarvestLevel("pickaxe", 2);
		zanite_block.setHarvestLevel("pickaxe", 1);
		berry_bush_stem.setHarvestLevel("axe", 0);
		enchanter.setHarvestLevel("pickaxe", 0);
		freezer.setHarvestLevel("pickaxe", 0);
		incubator.setHarvestLevel("pickaxe", 0);
		carved_stone.setHarvestLevel("pickaxe", 0);
		angelic_stone.setHarvestLevel("pickaxe", 0);
		hellfire_stone.setHarvestLevel("pickaxe", 0);
		chest_mimic.setHarvestLevel("axe", 0);
		pillar.setHarvestLevel("pickaxe", 0);
		pillar_top.setHarvestLevel("pickaxe", 0);
		skyroot_fence.setHarvestLevel("axe", 0);
		skyroot_fence_gate.setHarvestLevel("axe", 0);
		carved_wall.setHarvestLevel("pickaxe", 0);
		angelic_wall.setHarvestLevel("pickaxe", 0);
		angelic_wall.setHarvestLevel("pickaxe", 0);
		hellfire_wall.setHarvestLevel("pickaxe", 0);
		holystone_wall.setHarvestLevel("pickaxe", 0);
		holystone_brick_wall.setHarvestLevel("pickaxe", 0);
		mossy_holystone_wall.setHarvestLevel("pickaxe", 0);
		aerogel_wall.setHarvestLevel("pickaxe", 0);
		carved_stairs.setHarvestLevel("pickaxe", 0);
		angelic_stairs.setHarvestLevel("pickaxe", 0);
		hellfire_stairs.setHarvestLevel("pickaxe", 0);
		skyroot_stairs.setHarvestLevel("axe", 0);
		mossy_holystone_stairs.setHarvestLevel("pickaxe", 0);
		holystone_stairs.setHarvestLevel("pickaxe", 0);
		holystone_brick_stairs.setHarvestLevel("pickaxe", 0);
		aerogel_stairs.setHarvestLevel("pickaxe", 0);
		skyroot_double_slab.setHarvestLevel("axe", 0);
		carved_double_slab.setHarvestLevel("pickaxe", 0);
		angelic_double_slab.setHarvestLevel("pickaxe", 0);
		hellfire_double_slab.setHarvestLevel("pickaxe", 0);
		holystone_double_slab.setHarvestLevel("pickaxe", 0);
		mossy_holystone_double_slab.setHarvestLevel("pickaxe", 0);
		holystone_brick_double_slab.setHarvestLevel("pickaxe", 0);
		aerogel_double_slab.setHarvestLevel("pickaxe", 0);
		skyroot_slab.setHarvestLevel("axe", 0);
		carved_slab.setHarvestLevel("pickaxe", 0);
		angelic_slab.setHarvestLevel("pickaxe", 0);
		hellfire_slab.setHarvestLevel("pickaxe", 0);
		holystone_slab.setHarvestLevel("pickaxe", 0);
		mossy_holystone_slab.setHarvestLevel("pickaxe", 0);
		holystone_brick_slab.setHarvestLevel("pickaxe", 0);
		aerogel_slab.setHarvestLevel("pickaxe", 0);
		sun_altar.setHarvestLevel("pickaxe", 0);
		skyroot_bookshelf.setHarvestLevel("axe", 0);
		skyroot_bed.setHarvestLevel("axe", 0);
	}

	public static boolean is_empty(Block block) {
		return block == Blocks.air || block == aercloud;
	}
}
