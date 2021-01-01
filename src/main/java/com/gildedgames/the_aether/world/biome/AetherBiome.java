package com.gildedgames.the_aether.world.biome;

import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.entities.hostile.AechorPlant;
import com.gildedgames.the_aether.entities.hostile.Cockatrice;
import com.gildedgames.the_aether.entities.hostile.Whirlwind;
import com.gildedgames.the_aether.entities.hostile.Zephyr;
import com.gildedgames.the_aether.entities.passive.Aerwhale;
import com.gildedgames.the_aether.entities.passive.Sheepuff;
import com.gildedgames.the_aether.entities.passive.mountable.Aerbunny;
import com.gildedgames.the_aether.entities.passive.mountable.FlyingCow;
import com.gildedgames.the_aether.entities.passive.mountable.Moa;
import com.gildedgames.the_aether.entities.passive.mountable.Phyg;
import com.gildedgames.the_aether.entities.hostile.swet.Swet;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.world.biome.decoration.AetherOakTreeFeature;
import com.gildedgames.the_aether.world.biome.decoration.AetherSkyrootTreeFeature;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.ArrayList;
import java.util.Random;

public class AetherBiome extends BiomeGenBase {

	@SuppressWarnings("unchecked")
	public AetherBiome() {
		super(AetherConfig.get_aether_biome_id());

		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();

		ArrayList<SpawnListEntry> list = new ArrayList<SpawnListEntry>();
		add_creature_entry(list);
		this.spawnableCreatureList.addAll(list);
		list.clear();
		add_monster_entry(list);
		this.spawnableMonsterList.addAll(list);
		list.clear();

		this.topBlock = AetherBlocks.aether_grass;
		this.fillerBlock = AetherBlocks.holystone;

		this.setBiomeName("Aether");
		this.setDisableRain();
		this.setColor(0);
	}

	private void add_creature_entry(ArrayList<SpawnListEntry> list) {
		list.add(new SpawnListEntry(Swet.class, 20, 3, 4));
		list.add(new SpawnListEntry(AechorPlant.class, 19, 3, 3));
		list.add(new SpawnListEntry(Phyg.class, 12, 4, 4));
		list.add(new SpawnListEntry(Aerbunny.class, 11, 3, 3));
		list.add(new SpawnListEntry(Sheepuff.class, 10, 4, 4));
		list.add(new SpawnListEntry(Moa.class, 10, 3, 3));
		list.add(new SpawnListEntry(FlyingCow.class, 10, 4, 4));
		list.add(new SpawnListEntry(Aerwhale.class, 2, 1, 1));
	}

	private void add_monster_entry(ArrayList<SpawnListEntry> list) {
		list.add(new SpawnListEntry(Whirlwind.class, 8, 2, 2));
		list.add(new SpawnListEntry(Cockatrice.class, 4, 4, 4));
		list.add(new SpawnListEntry(Zephyr.class, 2, 1, 1));
	}

	@Override
	public void addDefaultFlowers() {
		this.flowers.add(new FlowerEntry(AetherBlocks.white_flower, 0, 20));
		this.flowers.add(new FlowerEntry(AetherBlocks.purple_flower, 0, 10));
	}

	@Override
	public int getWaterColorMultiplier() {
		return 0xffffff;
	}

	@Override
	public int getSkyColorByTemp(float currentTemperature) {
		return 0xBCBCFA; // Lavender Blue
	}

	@Override
	public int getBiomeGrassColor(int x, int y, int z) {
		return 0xb1ffcb;
	}

	@Override
	public int getBiomeFoliageColor(int x, int y, int z) {
		return 0xb1ffcb;
	}

	@Override
	public BiomeDecorator createBiomeDecorator() {
		return new AetherBiomeDecorator();
	}

	@Override
	public WorldGenAbstractTree func_150567_a(Random rand) {
		return rand.nextInt(20) == 0 ? new AetherOakTreeFeature() : new AetherSkyrootTreeFeature(false);
	}

}
