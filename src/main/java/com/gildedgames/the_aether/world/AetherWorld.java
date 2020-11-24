package com.gildedgames.the_aether.world;

import com.gildedgames.the_aether.world.biome.AetherBiome;
import com.gildedgames.the_aether.world.gen.GoldenDungeonStructure;
import com.gildedgames.the_aether.world.gen.SilverDungeonStructure;
import com.gildedgames.the_aether.world.gen.LargeColdAercloudStructure;
import com.gildedgames.the_aether.world.gen.components.GoldenIslandComponent;
import com.gildedgames.the_aether.world.gen.components.StubGoldenIslandComponent;
import com.gildedgames.the_aether.world.gen.components.GoldenDungeonComponent;
import com.gildedgames.the_aether.world.gen.components.SilverDungeonComponent;
import com.gildedgames.the_aether.world.gen.components.LargeColdAercloudComponent;
import com.gildedgames.the_aether.AetherConfig;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;

public class AetherWorld {

	public static BiomeGenBase aether_biome = new AetherBiome();

	public static void initialization() {
		MapGenStructureIO.registerStructure(SilverDungeonStructure.Start.class, "aether_legacy:silver_dungeon_start");
		MapGenStructureIO.registerStructure(GoldenDungeonStructure.Start.class, "aether_legacy:golden_dungeon_start");
		MapGenStructureIO.registerStructure(LargeColdAercloudStructure.Start.class, "aether_legacy:large_cold_aercloud_start");

		MapGenStructureIO.func_143031_a(LargeColdAercloudComponent.class, "aether_legacy:large_cold_aercloud_component");
		MapGenStructureIO.func_143031_a(SilverDungeonComponent.class, "aether_legacy:silver_dungeon_component");
		MapGenStructureIO.func_143031_a(GoldenDungeonComponent.class, "aether_legacy:golden_dungeon_component");
		MapGenStructureIO.func_143031_a(GoldenIslandComponent.class, "aether_legacy:golden_island_component");
		MapGenStructureIO.func_143031_a(StubGoldenIslandComponent.class, "aether_legacy:golden_island_stub_component");

		DimensionManager.registerProviderType(AetherConfig.get_aether_world_id(), AetherWorldProvider.class, false);
		DimensionManager.registerDimension(AetherConfig.get_aether_world_id(), AetherConfig.get_aether_world_id());
	}
}
