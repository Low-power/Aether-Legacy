package com.gildedgames.the_aether;

import com.gildedgames.the_aether.events.AetherEntityEvents;
import com.gildedgames.the_aether.events.CauldronOnlyEventsListener;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.entities.AetherEntities;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.network.AetherNetwork;
import com.gildedgames.the_aether.player.PlayerAetherEvents;
import com.gildedgames.the_aether.player.perks.AetherRankings;
import com.gildedgames.the_aether.registry.AetherRegistries;
import com.gildedgames.the_aether.registry.achievements.AetherAchievements;
import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import com.gildedgames.the_aether.tileentity.AetherTileEntities;
import com.gildedgames.the_aether.world.AetherWorld;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.SidedProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.util.ResourceLocation;
import java.lang.reflect.InvocationTargetException;

@Mod(modid = Aether.MOD_ID, version = "1.1.1-rivoreo-r5")
public class Aether {

	public static final String MOD_ID = "aether_legacy";

	@Instance(Aether.MOD_ID)
	public static Aether instance;

	@SidedProxy(clientSide = "com.gildedgames.the_aether.client.ClientProxy", serverSide = "com.gildedgames.the_aether.CommonProxy")
	public static CommonProxy proxy;

	private static Boolean cauldron;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		AetherRankings.initialization();
		AetherNetwork.preInitialization();
		AetherConfig.init(event.getModConfigurationDirectory());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		AetherItems.initialization();
		AetherBlocks.initialization();
		AetherBlocks.initializeHarvestLevels();
		AetherRegistries.register();
		AetherEntities.initialization();
		AetherCreativeTabs.initialization();
		AetherTileEntities.initialization();
		AetherWorld.initialization();
		AetherAchievements.initialization();

		proxy.init();

		CommonProxy.registerEvent(new PlayerAetherEvents());
		CommonProxy.registerEvent(new AetherEventHandler());
		CommonProxy.registerEvent(new AetherEntityEvents());

		if(is_running_on_cauldron()) {
			MinecraftForge.EVENT_BUS.register(new CauldronOnlyEventsListener());
		}
	}

	public static ResourceLocation locate(String location) {
		return new ResourceLocation(MOD_ID, location);
	}

	public static String find(String location) {
		return modAddress() + location;
	}

	public static String modAddress() {
		return MOD_ID + ":";
	}

	public static boolean is_running_on_cauldron() {
		if(cauldron == null) try {
			Aether.class.getClassLoader().loadClass("net.minecraftforge.cauldron.CauldronUtils");
			cauldron = Boolean.valueOf(true);
		} catch(ClassNotFoundException e) {
			cauldron = Boolean.valueOf(false);
		}
		return cauldron.booleanValue();
	}
}
