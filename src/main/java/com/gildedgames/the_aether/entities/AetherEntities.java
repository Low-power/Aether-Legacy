package com.gildedgames.the_aether.entities;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.entities.bosses.valkyrie_queen.ValkyrieQueen;
import com.gildedgames.the_aether.entities.passive.Aerwhale;
import com.gildedgames.the_aether.entities.passive.EntityMiniCloud;
import com.gildedgames.the_aether.entities.passive.Sheepuff;
import com.gildedgames.the_aether.entities.projectile.crystals.EntityCrystal;
import com.gildedgames.the_aether.entities.projectile.darts.EnchantedDartEntity;
import com.gildedgames.the_aether.entities.projectile.darts.GoldenDartEntity;
import com.gildedgames.the_aether.entities.projectile.darts.PoisonDartEntity;
import com.gildedgames.the_aether.items.AetherSpawnEgg;
import com.gildedgames.the_aether.entities.block.FloatingBlockEntity;
import com.gildedgames.the_aether.entities.block.EntityTNTPresent;
import com.gildedgames.the_aether.entities.bosses.EntityFireMinion;
import com.gildedgames.the_aether.entities.bosses.Valkyrie;
import com.gildedgames.the_aether.entities.bosses.slider.Slider;
import com.gildedgames.the_aether.entities.bosses.sun_spirit.SunSpirit;
import com.gildedgames.the_aether.entities.hostile.AechorPlant;
import com.gildedgames.the_aether.entities.hostile.Cockatrice;
import com.gildedgames.the_aether.entities.hostile.Mimic;
import com.gildedgames.the_aether.entities.hostile.Sentry;
import com.gildedgames.the_aether.entities.hostile.Whirlwind;
import com.gildedgames.the_aether.entities.hostile.Zephyr;
import com.gildedgames.the_aether.entities.passive.mountable.Aerbunny;
import com.gildedgames.the_aether.entities.passive.mountable.FlyingCow;
import com.gildedgames.the_aether.entities.passive.mountable.Moa;
import com.gildedgames.the_aether.entities.passive.mountable.ParachuteEntity;
import com.gildedgames.the_aether.entities.passive.mountable.Phyg;
import com.gildedgames.the_aether.entities.passive.mountable.Swet;
import com.gildedgames.the_aether.entities.projectile.HammerProjectileEntity;
import com.gildedgames.the_aether.entities.projectile.LightningKnifeEntity;
import com.gildedgames.the_aether.entities.projectile.EntityPhoenixArrow;
import com.gildedgames.the_aether.entities.projectile.PoisonNeedleEntity;
import com.gildedgames.the_aether.entities.projectile.ZephyrSnowballEntity;
import com.gildedgames.the_aether.entities.util.AetherMoaTypes;
import com.gildedgames.the_aether.entities.util.AetherItemEntity;
import com.gildedgames.the_aether.entities.effects.InebriationPotion;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

public class AetherEntities {
	public static Map<Class<? extends Entity>, String> class_to_name_mapping = new HashMap<Class<? extends Entity>, String>();
	public static Map<Integer, Class<? extends Entity>> id_to_class_mapping = new HashMap<Integer, Class<? extends Entity>>();
	private static Map<Class<? extends Entity>, Integer> class_to_id_mapping = new HashMap<Class<? extends Entity>, Integer>();
	private static Map<String, Integer> name_to_id_mapping = new HashMap<String, Integer>();

	private static final Logger logger = LogManager.getLogger();

	public static void initialization() {
		register(Moa.class, "moa", 0, 0x87bfef, 0x7a7a7a);
		register(Phyg.class, "phyg", 1, 0xffc1d0, 0xffd939);
		register(FlyingCow.class, "flying_cow", 2, 0xd8d8d8, 0xffd939);
		register(Sheepuff.class, "sheepuff", 3, 0xe2fcff, 0xcb9090);
		register(Aerbunny.class, "aerbunny", 4, 0xe2fcff, 0xffdff9);
		register(Aerwhale.class, "aerwhale", 5, 0x79b7d1, 0xe0d25c);
		register(Swet.class, "swet", 6, 0xcdda4f, 0x4fb1da);
		register(Cockatrice.class, "cockatrice", 7, 0x6cb15c, 0x6c579d);
		register(Sentry.class, "sentry", 8, 0x838c9a, 0x2561ba);
		register(Zephyr.class, "zephyr", 9, 0xdfdfdf, 0x99cfe8);
		register(AechorPlant.class, "aechor_plant", 10, 0x076178, 0x4bc69e);
		register(Mimic.class, "mimic", 11, 0xb18132, 0x605a4e);

		register(Slider.class, "slider", 12, 80, 3, true);
		register(ValkyrieQueen.class, "valkyrie_queen", 13, 80, 3, true);
		register(SunSpirit.class, "sun_spirit", 14, 80, 3, true);

		register(GoldenDartEntity.class, "golden_dart", 15, 64, 20, false);
		register(PoisonDartEntity.class, "poison_dart", 16, 64, 20, false);
		register(EnchantedDartEntity.class, "enchanted_dart", 17, 64, 20, false);
		register(PoisonNeedleEntity.class, "poison_needle", 18, 64, 20, false);

		register(EntityCrystal.class, "crystal", 19, 64, 4, false);
		register(Whirlwind.class, "whirlwind", 20, 80, 3, false);
		register(Valkyrie.class, "valkyrie", 22, 80, 3, true);
		register(EntityFireMinion.class, "fire_minion", 23, 80, 3, true);
		register(EntityMiniCloud.class, "mini_cloud", 24, 80, 3, false);

		register(FloatingBlockEntity.class, "floating_block", 25, 160, 20, true);
		register(EntityTNTPresent.class, "tnt_present", 26, 160, 20, true);

		register(EntityPhoenixArrow.class, "phoenix_arrow", 27, 64, 20, false);
		register(ZephyrSnowballEntity.class, "zephyr_snowball", 28, 64, 20, false);
		register(HammerProjectileEntity.class, "hammer_projectile", 29, 64, 20, false);
		register(LightningKnifeEntity.class, "lightning_knife", 30, 64, 10, true);
		register(ParachuteEntity.class, "parachute", 31, 160, 20, true);
		register(AetherItemEntity.class, "aether_item", 32, 160, 20, true);

		AetherMoaTypes.initialization();

		InebriationPotion.static_initialize();
	}

	public static void register(Class<? extends Entity> entity_class, String entity_name, int entity_id, int tracking_range, int update_frequency, boolean has_velocity_updates) {
		EntityRegistry.registerModEntity(entity_class, entity_name, entity_id, Aether.instance, tracking_range, update_frequency, has_velocity_updates);
	}

	public static void register(Class<? extends Entity> entity_class, String entity_name, int entity_id, int primary_egg_color, int secondary_egg_color) {
		add_mapping(entity_class, entity_name, entity_id, primary_egg_color, secondary_egg_color);
		EntityRegistry.registerModEntity(entity_class, entity_name, entity_id, Aether.instance, 80, 3, true);
	}

	private static void add_mapping(Class<? extends Entity> entity_class, String entity_name, int entity_id, int primary_egg_color, int secondary_egg_color) {
		if(id_to_class_mapping.containsKey(Integer.valueOf(entity_id))) {
			throw new IllegalArgumentException(String.format("ID %d is already registered", entity_id));
		} else {
			Integer id = Integer.valueOf(entity_id);
			class_to_name_mapping.put(entity_class, entity_name);
			id_to_class_mapping.put(id, entity_class);
			class_to_id_mapping.put(entity_class, id);
			name_to_id_mapping.put(entity_name, id);
			AetherEggInfo egg_info = new AetherEggInfo(entity_id, primary_egg_color, secondary_egg_color);
			AetherSpawnEgg.egg_info_map.put(id, egg_info);
		}
	}

	public static Entity createEntityByID(int id, World world) {
		Entity entity = null;
		Class<?> c = getClassFromID(id);
		if(c != null) try {
			entity = (Entity)c.getConstructor(World.class).newInstance(world);
		} catch(Exception e) {
			e.printStackTrace();
		}
		if (entity == null) {
			logger.warn("Skipping Aether Entity with id " + String.valueOf(id));
		}
		return entity;
	}

	public static int getEntityID(Entity entity) {
		Class<?> c = entity.getClass();
		Integer id = class_to_id_mapping.get(c);
		return id == null ? -1 : id.intValue();
	}

	public static Class<?> getClassFromID(int id) {
		return id_to_class_mapping.get(Integer.valueOf(id));
	}

	public static String getStringFromID(int id) {
		Class<?> c = getClassFromID(id);
		return c != null ? class_to_name_mapping.get(c) : null;
	}

	public static class AetherEggInfo {
		public final int spawn_entity_type_id;
		public final int primary_color;
		public final int secondary_color;

		public AetherEggInfo(int spawn_entity_type_id, int primary_color, int secondary_color) {
			this.spawn_entity_type_id = spawn_entity_type_id;
			this.primary_color = primary_color;
			this.secondary_color = secondary_color;
		}
	}

}
