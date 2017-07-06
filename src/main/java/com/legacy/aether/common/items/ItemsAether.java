package com.legacy.aether.common.items;

import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.legacy.aether.common.Aether;
import com.legacy.aether.common.containers.util.AccessoryType;
import com.legacy.aether.common.items.accessories.ItemAccessory;
import com.legacy.aether.common.items.armor.ItemAetherArmor;
import com.legacy.aether.common.items.armor.ItemZaniteArmor;
import com.legacy.aether.common.items.dungeon.ItemDungeonKey;
import com.legacy.aether.common.items.food.ItemAetherFood;
import com.legacy.aether.common.items.food.ItemAmbrosiumShard;
import com.legacy.aether.common.items.food.ItemGummySwet;
import com.legacy.aether.common.items.food.ItemHealingStone;
import com.legacy.aether.common.items.food.ItemLifeShard;
import com.legacy.aether.common.items.food.ItemWhiteApple;
import com.legacy.aether.common.items.staffs.ItemCloudStaff;
import com.legacy.aether.common.items.staffs.ItemNatureStaff;
import com.legacy.aether.common.items.tools.ItemAetherParachute;
import com.legacy.aether.common.items.tools.ItemGravititeTool;
import com.legacy.aether.common.items.tools.ItemHolystoneTool;
import com.legacy.aether.common.items.tools.ItemSkyrootBucket;
import com.legacy.aether.common.items.tools.ItemSkyrootTool;
import com.legacy.aether.common.items.tools.ItemValkyrieTool;
import com.legacy.aether.common.items.tools.ItemZaniteTool;
import com.legacy.aether.common.items.util.EnumAetherToolType;
import com.legacy.aether.common.items.util.ItemAether;
import com.legacy.aether.common.items.weapons.ItemCandyCaneSword;
import com.legacy.aether.common.items.weapons.ItemElementalSword;
import com.legacy.aether.common.items.weapons.ItemGravititeSword;
import com.legacy.aether.common.items.weapons.ItemHolystoneSword;
import com.legacy.aether.common.items.weapons.ItemLightningKnife;
import com.legacy.aether.common.items.weapons.ItemNotchHammer;
import com.legacy.aether.common.items.weapons.ItemPigSlayer;
import com.legacy.aether.common.items.weapons.ItemSkyrootSword;
import com.legacy.aether.common.items.weapons.ItemValkyrieLance;
import com.legacy.aether.common.items.weapons.ItemVampireBlade;
import com.legacy.aether.common.items.weapons.ItemZaniteSword;
import com.legacy.aether.common.items.weapons.projectile.ItemDart;
import com.legacy.aether.common.items.weapons.projectile.ItemDartShooter;
import com.legacy.aether.common.items.weapons.projectile.ItemPhoenixBow;
import com.legacy.aether.common.registry.creative_tabs.AetherCreativeTabs;
import com.legacy.aether.common.registry.sounds.SoundsAether;

public class ItemsAether 
{

	public static Item zanite_gemstone, ambrosium_shard, golden_amber, aechor_petal;

	public static Item skyroot_pickaxe, skyroot_axe, skyroot_shovel, skyroot_sword;

	public static Item holystone_pickaxe, holystone_axe, holystone_shovel, holystone_sword;

	public static Item zanite_pickaxe, zanite_axe, zanite_shovel, zanite_sword;

	public static Item gravitite_pickaxe, gravitite_axe, gravitite_shovel, gravitite_sword;

	public static Item valkyrie_pickaxe, valkyrie_axe, valkyrie_shovel, valkyrie_sword;

	public static Item zanite_helmet, zanite_chestplate, zanite_leggings, zanite_boots;

	public static Item gravitite_helmet, gravitite_chestplate, gravitite_leggings, gravitite_boots;

	public static Item neptune_helmet, neptune_chestplate, neptune_leggings, neptune_boots;

	public static Item phoenix_helmet, phoenix_chestplate, phoenix_leggings, phoenix_boots;

	public static Item obsidian_helmet, obsidian_chestplate, obsidian_leggings, obsidian_boots;

	public static Item valkyrie_helmet, valkyrie_chestplate, valkyrie_leggings, valkyrie_boots;

	public static Item blue_berry, gummy_swet, healing_stone, white_apple, ginger_bread_man, candy_cane, enchanted_blueberry;

	public static Item skyroot_stick, victory_medal;

	public static Item dungeon_key, skyroot_bucket, cloud_parachute, golden_parachute;

	public static Item nature_staff, cloud_staff, moa_egg;

	public static Item dart_shooter, phoenix_bow, dart;

	public static Item flaming_sword, lightning_sword, holy_sword;

	public static Item vampire_blade, pig_slayer, candy_cane_sword, notch_hammer, valkyrie_lance;

	public static Item leather_gloves, iron_gloves, golden_gloves, chain_gloves, diamond_gloves;

	public static Item zanite_gloves, gravitite_gloves, neptune_gloves, phoenix_gloves, obsidian_gloves, valkyrie_gloves;

	public static Item iron_ring, golden_ring, zanite_ring, ice_ring, iron_pendant, golden_pendant, zanite_pendant, ice_pendant;

	public static Item red_cape, blue_cape, yellow_cape, white_cape, swet_cape, invisibility_cape, agility_cape;

	public static Item golden_feather, regeneration_stone, iron_bubble, life_shard;

	public static Item sentry_boots, lightning_knife;

	public static Item aether_tune, ascending_dawn, welcoming_skies;

	public static Item repulsion_shield;

	public static Item lore_book;

	public static void initialization()
	{
		zanite_gemstone = register("zanite_gemstone", new ItemAether(AetherCreativeTabs.material));
		ambrosium_shard = register("ambrosium_shard", new ItemAmbrosiumShard());
		golden_amber = register("golden_amber", new ItemAether(AetherCreativeTabs.material));
		aechor_petal = register("aechor_petal", new ItemAether(AetherCreativeTabs.material));

		skyroot_pickaxe = register("skyroot_pickaxe", new ItemSkyrootTool(EnumAetherToolType.PICKAXE));
		skyroot_axe = register("skyroot_axe", new ItemSkyrootTool(EnumAetherToolType.AXE));
		skyroot_shovel = register("skyroot_shovel", new ItemSkyrootTool(EnumAetherToolType.SHOVEL));

		holystone_pickaxe = register("holystone_pickaxe", new ItemHolystoneTool(EnumAetherToolType.PICKAXE));
		holystone_axe = register("holystone_axe", new ItemHolystoneTool(EnumAetherToolType.AXE));
		holystone_shovel = register("holystone_shovel", new ItemHolystoneTool(EnumAetherToolType.SHOVEL));

		zanite_pickaxe = register("zanite_pickaxe", new ItemZaniteTool(EnumAetherToolType.PICKAXE));
		zanite_axe = register("zanite_axe", new ItemZaniteTool(EnumAetherToolType.AXE));
		zanite_shovel = register("zanite_shovel", new ItemZaniteTool(EnumAetherToolType.SHOVEL));

		gravitite_pickaxe = register("gravitite_pickaxe", new ItemGravititeTool(EnumAetherToolType.PICKAXE));
		gravitite_axe = register("gravitite_axe", new ItemGravititeTool(EnumAetherToolType.AXE));
		gravitite_shovel = register("gravitite_shovel", new ItemGravititeTool(EnumAetherToolType.SHOVEL));

		valkyrie_pickaxe = register("valkyrie_pickaxe", new ItemValkyrieTool(EnumAetherToolType.PICKAXE));
		valkyrie_axe = register("valkyrie_axe", new ItemValkyrieTool(EnumAetherToolType.AXE));
		valkyrie_shovel = register("valkyrie_shovel", new ItemValkyrieTool(EnumAetherToolType.SHOVEL));

		zanite_helmet = register("zanite_helmet", new ItemZaniteArmor(EntityEquipmentSlot.HEAD, ArmorMaterial.IRON, "zanite", zanite_gemstone, 0x711ae8));
		zanite_chestplate = register("zanite_chestplate", new ItemZaniteArmor(EntityEquipmentSlot.CHEST, ArmorMaterial.IRON, "zanite", zanite_gemstone, 0x711ae8));
		zanite_leggings = register("zanite_leggings", new ItemZaniteArmor(EntityEquipmentSlot.LEGS, ArmorMaterial.IRON, "zanite", zanite_gemstone, 0x711ae8));
		zanite_boots = register("zanite_boots", new ItemZaniteArmor(EntityEquipmentSlot.FEET, ArmorMaterial.IRON, "zanite", zanite_gemstone, 0x711ae8));

		gravitite_helmet = register("gravitite_helmet", new ItemAetherArmor(EntityEquipmentSlot.HEAD, ArmorMaterial.DIAMOND, "gravitite", null, 0xe752db));
		gravitite_chestplate = register("gravitite_chestplate", new ItemAetherArmor(EntityEquipmentSlot.CHEST, ArmorMaterial.DIAMOND, "gravitite", null, 0xe752db));
		gravitite_leggings = register("gravitite_leggings", new ItemAetherArmor(EntityEquipmentSlot.LEGS, ArmorMaterial.DIAMOND, "gravitite", null, 0xe752db));
		gravitite_boots = register("gravitite_boots", new ItemAetherArmor(EntityEquipmentSlot.FEET, ArmorMaterial.DIAMOND, "gravitite", null, 0xe752db));

		neptune_helmet = register("neptune_helmet", new ItemAetherArmor(EntityEquipmentSlot.HEAD, ArmorMaterial.DIAMOND, "neptune", null, 0x2654FF));
		neptune_chestplate = register("neptune_chestplate", new ItemAetherArmor(EntityEquipmentSlot.CHEST, ArmorMaterial.DIAMOND, "neptune", null, 0x2654FF));
		neptune_leggings = register("neptune_leggings", new ItemAetherArmor(EntityEquipmentSlot.LEGS, ArmorMaterial.DIAMOND, "neptune", null, 0x2654FF));
		neptune_boots = register("neptune_boots", new ItemAetherArmor(EntityEquipmentSlot.FEET, ArmorMaterial.DIAMOND, "neptune", null, 0x2654FF));

		phoenix_helmet = register("phoenix_helmet", new ItemAetherArmor(EntityEquipmentSlot.HEAD, ArmorMaterial.DIAMOND, "phoenix", null));
		phoenix_chestplate = register("phoenix_chestplate", new ItemAetherArmor(EntityEquipmentSlot.CHEST, ArmorMaterial.DIAMOND, "phoenix", null));
		phoenix_leggings = register("phoenix_leggings", new ItemAetherArmor(EntityEquipmentSlot.LEGS, ArmorMaterial.DIAMOND, "phoenix", null));
		phoenix_boots = register("phoenix_boots", new ItemAetherArmor(EntityEquipmentSlot.FEET, ArmorMaterial.DIAMOND, "phoenix", null));

		obsidian_helmet = register("obsidian_helmet", new ItemAetherArmor(EntityEquipmentSlot.HEAD, ArmorMaterial.DIAMOND, "obsidian", null, 0x1b1447));
		obsidian_chestplate = register("obsidian_chestplate", new ItemAetherArmor(EntityEquipmentSlot.CHEST, ArmorMaterial.DIAMOND, "obsidian", null, 0x1b1447));
		obsidian_leggings = register("obsidian_leggings", new ItemAetherArmor(EntityEquipmentSlot.LEGS, ArmorMaterial.DIAMOND, "obsidian", null, 0x1b1447));
		obsidian_boots = register("obsidian_boots", new ItemAetherArmor(EntityEquipmentSlot.FEET, ArmorMaterial.DIAMOND, "obsidian", null, 0x1b1447));

		valkyrie_helmet = register("valkyrie_helmet", new ItemAetherArmor(EntityEquipmentSlot.HEAD, ArmorMaterial.DIAMOND, "valkyrie", null));
		valkyrie_chestplate = register("valkyrie_chestplate", new ItemAetherArmor(EntityEquipmentSlot.CHEST, ArmorMaterial.DIAMOND, "valkyrie", null));
		valkyrie_leggings = register("valkyrie_leggings", new ItemAetherArmor(EntityEquipmentSlot.LEGS, ArmorMaterial.DIAMOND, "valkyrie", null));
		valkyrie_boots = register("valkyrie_boots", new ItemAetherArmor(EntityEquipmentSlot.FEET, ArmorMaterial.DIAMOND, "valkyrie", null));

		blue_berry = register("blue_berry", new ItemAetherFood(2));
		enchanted_blueberry = register("enchanted_blueberry", new ItemAetherFood(8));
		white_apple = register("white_apple", new ItemWhiteApple());
		gummy_swet = register("gummy_swet", new ItemGummySwet());
		healing_stone = register("healing_stone", new ItemHealingStone());
		candy_cane = register("candy_cane", new ItemAetherFood(2));
		ginger_bread_man = register("ginger_bread_man", new ItemAetherFood(2));

		skyroot_stick = register("skyroot_stick", new Item().setCreativeTab(AetherCreativeTabs.material));
		victory_medal = register("victory_medal", new Item().setCreativeTab(AetherCreativeTabs.misc).setMaxStackSize(10));

		dungeon_key = register("dungeon_key", new ItemDungeonKey());
		skyroot_bucket = register("skyroot_bucket", new ItemSkyrootBucket());
		cloud_parachute = register("cold_parachute", new ItemAetherParachute());
		golden_parachute = register("golden_parachute", new ItemAetherParachute());

		moa_egg = register("moa_egg", new ItemMoaEgg());

		dart_shooter = register("dart_shooter", new ItemDartShooter());
		phoenix_bow = register("phoenix_bow", new ItemPhoenixBow());
		dart = register("dart", new ItemDart());

		skyroot_sword = register("skyroot_sword", new ItemSkyrootSword());
		holystone_sword = register("holystone_sword", new ItemHolystoneSword());
		zanite_sword = register("zanite_sword", new ItemZaniteSword());
		gravitite_sword = register("gravitite_sword", new ItemGravititeSword());

		flaming_sword = register("flaming_sword", new ItemElementalSword());
		lightning_sword = register("lightning_sword", new ItemElementalSword());
		holy_sword = register("holy_sword",new ItemElementalSword());

		vampire_blade = register("vampire_blade", new ItemVampireBlade());
		pig_slayer = register("pig_slayer", new ItemPigSlayer());
		candy_cane_sword = register("candy_cane_sword", new ItemCandyCaneSword());
		notch_hammer = register("notch_hammer", new ItemNotchHammer());

		leather_gloves = register("leather_gloves", new ItemAccessory(AccessoryType.GLOVE).setColor(0xc65c35));
		iron_gloves = register("iron_gloves", new ItemAccessory(AccessoryType.GLOVE));
		golden_gloves = register("golden_gloves", new ItemAccessory(AccessoryType.GLOVE).setColor(0xeaee57));
		chain_gloves = register("chain_gloves", new ItemAccessory(AccessoryType.GLOVE).setTexture("chain"));
		diamond_gloves = register("diamond_gloves", new ItemAccessory(AccessoryType.GLOVE).setColor(0x33ebcb));

		zanite_gloves = register("zanite_gloves", new ItemAccessory(AccessoryType.GLOVE).setColor(0x711ae8));
		gravitite_gloves = register("gravitite_gloves", new ItemAccessory(AccessoryType.GLOVE).setColor(0xe752db));
		neptune_gloves = register("neptune_gloves", new ItemAccessory(AccessoryType.GLOVE).setColor(0x2654FF));
		phoenix_gloves = register("phoenix_gloves", new ItemAccessory(AccessoryType.GLOVE).setTexture("phoenix").setColor(0xff7700).setMaxDamage(152));
		obsidian_gloves = register("obsidian_gloves", new ItemAccessory(AccessoryType.GLOVE).setColor(0x1b1447));
		valkyrie_gloves = register("valkyrie_gloves", new ItemAccessory(AccessoryType.GLOVE).setTexture("valkyrie"));

		iron_ring = register("iron_ring", new ItemAccessory(AccessoryType.RING));
		golden_ring = register("golden_ring", new ItemAccessory(AccessoryType.RING).setColor(0xeaee57));
		zanite_ring = register("zanite_ring", new ItemAccessory(AccessoryType.RING).setColor(0x711ae8).setMaxDamage(49));
		ice_ring = register("ice_ring", new ItemAccessory(AccessoryType.RING).setColor(0x95e6e7).setMaxDamage(125));

		iron_pendant = register("iron_pendant", new ItemAccessory(AccessoryType.PENDANT));
		golden_pendant = register("golden_pendant", new ItemAccessory(AccessoryType.PENDANT).setColor(0xeaee57));
		zanite_pendant = register("zanite_pendant", new ItemAccessory(AccessoryType.PENDANT).setColor(0x711ae8).setMaxDamage(98));
		ice_pendant = register("ice_pendant", new ItemAccessory(AccessoryType.PENDANT).setColor(0x95e6e7).setMaxDamage(250));

		red_cape = register("red_cape", new ItemAccessory(AccessoryType.CAPE).setColor(0xe81111));
		blue_cape = register("blue_cape", new ItemAccessory(AccessoryType.CAPE).setColor(0x137fb7));
		yellow_cape = register("yellow_cape", new ItemAccessory(AccessoryType.CAPE).setColor(0xcdcb0e));
		white_cape = register("white_cape", new ItemAccessory(AccessoryType.CAPE));
		swet_cape = register("swet_cape", new ItemAccessory(AccessoryType.CAPE).setTexture("swet_cape"));
		invisibility_cape = register("invisibility_cape", new ItemAccessory(AccessoryType.CAPE));
		agility_cape = register("agility_cape", new ItemAccessory(AccessoryType.CAPE).setTexture("agility_cape"));

		golden_feather = register("golden_feather", new ItemAccessory(AccessoryType.MISC));
		regeneration_stone = register("regeneration_stone", new ItemAccessory(AccessoryType.MISC));
		iron_bubble = register("iron_bubble", new ItemAccessory(AccessoryType.MISC));

		life_shard = register("life_shard", new ItemLifeShard());
		cloud_staff = register("cloud_staff", new ItemCloudStaff());
		nature_staff = register("nature_staff", new ItemNatureStaff());
		lightning_knife = register("lightning_knife", new ItemLightningKnife());

		valkyrie_lance = register("valkyrie_lance", new ItemValkyrieLance());
		sentry_boots = register("sentry_boots", new ItemAetherArmor(EntityEquipmentSlot.FEET, ArmorMaterial.DIAMOND, "sentry", null));

		aether_tune = register("aether_tune", new ItemAetherDisc("aether_tune", SoundsAether.aether_tune, "Noisestorm", "Aether Tune"));
		ascending_dawn = register("ascending_dawn", new ItemAetherDisc("ascending_dawn", SoundsAether.ascending_dawn, "HEmile", "Ascending Dawn"));
		welcoming_skies = register("welcoming_skies", new ItemAetherDisc("welcoming_skies", SoundsAether.welcoming_skies, "Void", "Welcoming Skies")).setCreativeTab(null);

		repulsion_shield = register("repulsion_shield", new ItemAccessory(AccessoryType.SHIELD).setTexture("repulsion").setMaxDamage(512));
		lore_book = register("lore_book", new ItemLoreBook());
	}

	public static Item register(String name, Item item)
	{
		item.setUnlocalizedName(name);
		GameRegistry.register(item.setRegistryName(Aether.locate(name)));

		return item;
	}

}