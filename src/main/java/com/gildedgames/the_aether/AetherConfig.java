package com.gildedgames.the_aether;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import java.io.File;
import java.io.IOException;

public class AetherConfig {
	private static Configuration config;

	private static Property max_life_shards;
	private static Property christmas_content, tallgrass;
	private static Property aether_biome_id, aether_world_id;
	private static Property use_legacy_mobs_visuals;
	private static Property skyroot_bucket_only, valkyrie_cape, golden_feather;
	private static Property travel_world;
	private static Property show_trivia_message;
	private static Property menu_enabled, menu_button_enabled;
	private static Property legacy_altar_name;
	private static Property inebriation_id;
	private static Property repeat_sun_spirit_dialog;
	private static Property aether_start;
	private static Property disable_eternal_day;

	private static void migrate_old_configuration(File config_file) {
		Configuration old_config = new Configuration(config_file);
		Property prop;
		if(christmas_content.getString() == null) {
			prop = old_config.get("Aether World Generation", "Christmas Content", (String)null, null, Property.Type.BOOLEAN);
			if(prop != null) christmas_content.set(prop.getBoolean());
		}
		if(tallgrass.getString() == null) {
			prop = old_config.get("Aether World Generation", "Enable Tall Grass", (String)null, null, Property.Type.BOOLEAN);
			if(prop != null) tallgrass.set(prop.getBoolean());
		}
		if(aether_world_id.getString() == null) {
			prop = old_config.get("World Identification", "Aether Dimension ID", (String)null, null, Property.Type.INTEGER);
			if(prop != null) aether_world_id.set(prop.getInt());
		}
		if(aether_biome_id.getString() == null) {
			prop = old_config.get("World Identification", "Aether Biome ID", (String)null, null, Property.Type.INTEGER);
			if(prop != null) aether_biome_id.set(prop.getInt());
		}
		if(skyroot_bucket_only.getString() == null) {
			prop = old_config.get("Misc", "Activate portal with only Skyroot bucket", (String)null, null, Property.Type.BOOLEAN);
			if(prop != null) skyroot_bucket_only.set(prop.getBoolean());
		}
		if(travel_world.getString() == null) {
			prop = old_config.get("Misc", "Dimension below aether", (String)null, null, Property.Type.INTEGER);
			if(prop != null) {
				String value = prop.getString();
				try {
					int id = Integer.parseInt(value);
					travel_world.set(net.minecraftforge.common.DimensionManager.createProviderFor(id).getDimensionName());
				} catch(NumberFormatException e) {
					travel_world.set(value);
				}
			}
		}
		if(valkyrie_cape.getString() == null) {
			prop = old_config.get("Misc", "Enables the Valkyrie Cape in dungeon loot", (String)null, null, Property.Type.BOOLEAN);
			if(prop != null) valkyrie_cape.set(prop.getBoolean());
		}
		if(golden_feather.getString() == null) {
			prop = old_config.get("Misc", "Enables the Golden Feather in dungeon loot", (String)null, null, Property.Type.BOOLEAN);
			if(prop != null) golden_feather.set(prop.getBoolean());
		}
		if(show_trivia_message.getString() == null) {
			prop = old_config.get("Trivia", "Disable random trivia", (String)null, null, Property.Type.BOOLEAN);
			if(prop != null) show_trivia_message.set(!prop.getBoolean());
		}
		if(use_legacy_mobs_visuals.getString() == null) {
			prop = old_config.get("Misc", "Enable Legacy Visuals", (String)null, null, Property.Type.BOOLEAN);
			if(prop != null) use_legacy_mobs_visuals.set(prop.getBoolean());
		}
		if(aether_start.getString() == null) {
			prop = old_config.get("Gameplay", "Spawns Player with Aether Portal Frame", (String)null, null, Property.Type.BOOLEAN);
			if(prop != null) aether_start.set(prop.getBoolean());
		}
		if(max_life_shards.getString() == null) {
			prop = old_config.get("Gameplay", "Max Life Shards", (String)null, null, Property.Type.INTEGER);
			if(prop != null) max_life_shards.set(prop.getInt());
		}
		if(menu_enabled.getString() == null) {
			prop = old_config.get("Misc", "Enables the Aether Menu", (String)null, null, Property.Type.BOOLEAN);
			if(prop != null) menu_enabled.set(prop.getBoolean());
		}
		if(menu_button_enabled.getString() == null) {
			prop = old_config.get("Misc", "Enables the Aether Menu toggle button", (String)null, null, Property.Type.BOOLEAN);
			if(prop != null) menu_button_enabled.set(prop.getBoolean());
		}
		if(legacy_altar_name.getString() == null) {
			prop = old_config.get("Misc", "Changes whether the Altar should be named Enchanter or not.", (String)null, null, Property.Type.BOOLEAN);
			if(prop != null) legacy_altar_name.set(prop.getBoolean());
		}
		if(inebriation_id.getString() == null) {
			prop = old_config.get("Misc", "Sets the id for the Inebriation effect.", (String)null, null, Property.Type.INTEGER);
			if(prop != null) inebriation_id.set(prop.getInt());
		}
		if(repeat_sun_spirit_dialog.getString() == null) {
			prop = old_config.get("Misc", "If disabed, the Sun Spirit's dialog will only show once per world.", (String)null, null, Property.Type.BOOLEAN);
			if(prop != null) repeat_sun_spirit_dialog.set(prop.getBoolean());
		}
		if(disable_eternal_day.getString() == null) {
			prop = old_config.get("Misc", "Disables eternal day making time cycle in the Aether without having to kill the Sun Spirit. This is mainly intended for use in modpacks.", (String)null, null, Property.Type.BOOLEAN);
			if(prop != null) disable_eternal_day.set(prop.getBoolean());
		}

		config.removeCategory(new ConfigCategory("aether world generation"));
		config.removeCategory(new ConfigCategory("world identification"));
		config.removeCategory(new ConfigCategory("misc"));
		config.removeCategory(new ConfigCategory("trivia"));
	}

	private static Property get_property(ConfigCategory cat, String key, String default_value, String comment, Property.Type type) {
		if(key == null) throw new IllegalArgumentException("key is null");
		if(default_value == null) throw new IllegalArgumentException("default_value is null");
		if(type == null) throw new IllegalArgumentException("type is null");

		Property prop = cat.get(key);
		if(prop == null || prop.getType() == null) {
			prop = new Property(key, (String)null, type);
			cat.put(key, prop);
		}
		prop.setDefaultValue(default_value);
		prop.comment = comment;
		return prop;
	}

	private static Property get_property(ConfigCategory cat, String key, String default_value, String comment) {
		return get_property(cat, key, default_value, comment, Property.Type.STRING);
	}

	private static Property get_property(ConfigCategory cat, String key, int default_value, String comment) {
		return get_property(cat, key, String.valueOf(default_value), comment, Property.Type.INTEGER);
	}

	private static Property get_property(ConfigCategory cat, String key, boolean default_value, String comment) {
		return get_property(cat, key, String.valueOf(default_value), comment, Property.Type.BOOLEAN);
	}

	private static void set_to_default_if_null(Property prop) {
		if(prop.getString() == null) prop.set(prop.getDefault());
	}

	public static void init(File game_config_dir) {
		File config_dir = new File(game_config_dir, "aether");
		config_dir.mkdir();
		File config_file = new File(config_dir, "AetherI.cfg");
		boolean should_try_migrate = config_file.exists();
		config = new Configuration(config_file);

		ConfigCategory cat = config.getCategory("generation");
		christmas_content = get_property(cat, "ChristmasContent", false, null);
		tallgrass = get_property(cat, "TallGrass", false, null);
		valkyrie_cape = get_property(cat, "ValkyrieCapeDungeonLoot", true, null);
		golden_feather = get_property(cat, "GoldenFeatherDungeonLoot", false, null);

		cat = config.getCategory("identification");
		aether_world_id = get_property(cat, "AetherNumericWorldId", 4, null);
		aether_biome_id = get_property(cat, "AetherNumericBiomeId", 127, null);
		inebriation_id = get_property(cat, "InebriationEffectNumericId", 31, null);

		cat = config.getCategory("miscellaneous");
		skyroot_bucket_only = get_property(cat, "SkyrootBucketOnly", false, "Only Skyroot buckets can be used to active Aether portal");
		travel_world = get_property(cat, "WorldBelowAether", "Overworld", "The world name that entities will be teleported from the Aether to, when using an Aether portal or falling out of the Aether");
		use_legacy_mobs_visuals = get_property(cat, "UseLegacyMobsVisuals", false, null);
		legacy_altar_name = get_property(cat, "UseLegacyAlterName", true, "The block will be called 'Enchanter' instead if disabled");

		cat = config.getCategory("gui");
		menu_enabled = get_property(cat, "UseAetherMainMenu", false, null);
		menu_button_enabled = get_property(cat, "ShowMainMenuToggleButton", true, "Enable a small button on the main menu screen to toggle menu theme without restarting game");
		show_trivia_message = get_property(cat, "ShowTriviaMessage", true, "Whether to show a trivia message on world loading screen");

		cat = config.getCategory("gameplay");
		aether_start = get_property(cat, "GivePlayerAetherPortalFrameOnFirstSpawn", false, null);
		max_life_shards = get_property(cat, "MaxLifeShards", 10, null);
		repeat_sun_spirit_dialog = get_property(cat, "RepeatSunSpiritDialog", true, "The Sun Spirit's dialogue will show only once per player if disabled");
		disable_eternal_day = get_property(cat, "DisableEternalDay", false, "Disables eternal day making time cycle in the Aether without having to kill the Sun Spirit. This is mainly intended for use in modpacks.");

		if(should_try_migrate) migrate_old_configuration(config_file);

		set_to_default_if_null(christmas_content);
		set_to_default_if_null(tallgrass);
		set_to_default_if_null(valkyrie_cape);
		set_to_default_if_null(golden_feather);
		set_to_default_if_null(aether_world_id);
		set_to_default_if_null(aether_biome_id);
		set_to_default_if_null(inebriation_id);
		set_to_default_if_null(skyroot_bucket_only);
		set_to_default_if_null(travel_world);
		set_to_default_if_null(use_legacy_mobs_visuals);
		set_to_default_if_null(legacy_altar_name);
		set_to_default_if_null(menu_enabled);
		set_to_default_if_null(menu_button_enabled);
		set_to_default_if_null(show_trivia_message);
		set_to_default_if_null(aether_start);
		set_to_default_if_null(max_life_shards);
		set_to_default_if_null(repeat_sun_spirit_dialog);
		set_to_default_if_null(disable_eternal_day);
/*
		for(String cat_name : config.getCategoryNames()) {
			cat = config.getCategory(cat_name);
			for(Property prop : cat.getValues().values()) {
				set_to_default_if_null(prop);
			}
		}
*/

		config.save();
	}

	public static int get_aether_world_id() {
		return AetherConfig.aether_world_id.getInt();
	}

	public static int getAetherBiomeID() {
		return AetherConfig.aether_biome_id.getInt();
	}

	public static int getMaxLifeShards() {
		return AetherConfig.max_life_shards.getInt();
	}

	public static String get_travel_world_name() {
		return travel_world.getString();
	}

	public static boolean triviaDisabled() {
		return !AetherConfig.show_trivia_message.getBoolean();
	}

	public static boolean should_use_legacy_mobs_visuals() {
		return AetherConfig.use_legacy_mobs_visuals.getBoolean();
	}

	public static boolean shouldLoadHolidayContent() {
		return AetherConfig.christmas_content.getBoolean();
	}

	public static boolean tallgrassEnabled() {
		return AetherConfig.tallgrass.getBoolean();
	}

	public static boolean activateOnlyWithSkyroot() {
		return AetherConfig.skyroot_bucket_only.getBoolean();
	}

	public static boolean valkyrieCapeEnabled() {
		return AetherConfig.valkyrie_cape.getBoolean();
	}

	public static boolean goldenFeatherEnabled() {
		return AetherConfig.golden_feather.getBoolean();
	}

	public static boolean is_aether_menu_enabled() {
		return menu_enabled.getBoolean();
	}

	public static void set_aether_menu_enabled(boolean value) {
		menu_enabled.set(value);
		config.save();
	}

	public static boolean is_menu_toggle_button_enabled() {
		return menu_button_enabled.getBoolean();
	}

/*
	public static void set_menu_toggle_button_enabled(boolean value) {
		menu_button_enabled.set(value);
		config.save();
	}
*/

	public static boolean legacyAltarName() {
		return AetherConfig.legacy_altar_name.getBoolean();
	}

	public static int getInebriationId() {
		return AetherConfig.inebriation_id.getInt();
	}

	public static boolean repeatSunSpiritDialogue() {
		return repeat_sun_spirit_dialog.getBoolean();
	}

	public static boolean shouldAetherStart() {
		return aether_start.getBoolean();
	}

	public static boolean eternalDayDisabled() {
		return disable_eternal_day.getBoolean();
	}
}
