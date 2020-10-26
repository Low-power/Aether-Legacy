package com.gildedgames.the_aether;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import java.io.File;
import java.io.IOException;

public class AetherConfig {
	private static Configuration config;

	private static int max_life_shards;
	private static boolean christmas_content, tallgrass;
	private static int aether_biome_id, aether_dimension_id;
	private static boolean disable_trivia, old_mobs;
	private static boolean skyrootBucketOnly, valkyrie_cape, golden_feather;
	private static int travel_dimension;
	private static Property menu_enabled, menu_button_enabled;
	private static boolean legacy_altar_name;
	private static int inebriation_id;
	private static boolean sun_altar_multiplayer, repeat_sun_spirit_dialog;
	private static boolean aether_start;
	private static boolean disable_eternal_day;

	public static void init(File location) {
		File config_file = new File(location + "/aether" + "/AetherI.cfg");
		try {
			config_file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		config = new Configuration(config_file);
		config.load();

		christmas_content = config.get("Aether World Generation", "Christmas Content", false).getBoolean(false);
		tallgrass = config.get("Aether World Generation", "Enable Tall Grass", false).getBoolean(false);

		aether_dimension_id = config.get("World Identification", "Aether Dimension ID", 4).getInt(4);
		aether_biome_id = config.get("World Identification", "Aether Biome ID", 127).getInt(127);

		skyrootBucketOnly = config.get("Misc", "Activate portal with only Skyroot bucket", false).getBoolean(false);
		valkyrie_cape = config.get("Misc", "Enables the Valkyrie Cape in dungeon loot", true).getBoolean(true);
		golden_feather = config.get("Misc", "Enables the Golden Feather in dungeon loot", false).getBoolean(false);
		travel_dimension = config.get("Misc", "Dimension below aether", 0).getInt(0);

		disable_trivia = config.get("Trivia", "Disable random trivia", false).getBoolean(false);

		old_mobs = config.get("Misc", "Enable Legacy Visuals", false).getBoolean(false);

		aether_start = config.get("Gameplay", "Spawns Player with Aether Portal Frame", false).getBoolean(false);

		max_life_shards = config.get("Gameplay", "Max Life Shards", 10).getInt(10);

		menu_enabled = config.get("Misc", "Enables the Aether Menu", false);
		menu_button_enabled = config.get("Misc", "Enables the Aether Menu toggle button", true);

		legacy_altar_name = config.get("Misc", "Changes whether the Altar should be named Enchanter or not.", false).getBoolean(false);

		inebriation_id = config.get("Misc", "Sets the id for the Inebriation effect.", 31).getInt(31);

		sun_altar_multiplayer = config.get("Gameplay", "Removes the requirement for a player to be an operator to use the Sun Altar in multiplayer.", false).getBoolean(false);

		repeat_sun_spirit_dialog = config.get("Misc", "If disabed, the Sun Spirit's dialog will only show once per world.", true).getBoolean(true);

		disable_eternal_day = config.get("Misc", "Disables eternal day making time cycle in the Aether without having to kill the Sun Spirit. This is mainly intended for use in modpacks.", false).getBoolean(false);

		config.save();
	}

	public static int getAetherDimensionID() {
		return AetherConfig.aether_dimension_id;
	}

	public static int getAetherBiomeID() {
		return AetherConfig.aether_biome_id;
	}

	public static int getMaxLifeShards() {
		return AetherConfig.max_life_shards;
	}

	public static int getTravelDimensionID() {
		return AetherConfig.travel_dimension;
	}

	public static boolean triviaDisabled() {
		return AetherConfig.disable_trivia;
	}

	public static boolean oldMobsEnabled() {
		return AetherConfig.old_mobs;
	}

	public static boolean shouldLoadHolidayContent() {
		return AetherConfig.christmas_content;
	}

	public static boolean tallgrassEnabled() {
		return AetherConfig.tallgrass;
	}

	public static boolean activateOnlyWithSkyroot() {
		return AetherConfig.skyrootBucketOnly;
	}

	public static boolean valkyrieCapeEnabled() {
		return AetherConfig.valkyrie_cape;
	}

	public static boolean goldenFeatherEnabled() {
		return AetherConfig.golden_feather;
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
		return AetherConfig.legacy_altar_name;
	}

	public static int getInebriationId() {
		return AetherConfig.inebriation_id;
	}

	public static boolean sunAltarMultiplayer() {
		return AetherConfig.sun_altar_multiplayer;
	}

	public static boolean repeatSunSpiritDialogue() {
		return repeat_sun_spirit_dialog;
	}

	public static boolean shouldAetherStart() {
		return aether_start;
	}

	public static boolean eternalDayDisabled() {
		return disable_eternal_day;
	}
}
