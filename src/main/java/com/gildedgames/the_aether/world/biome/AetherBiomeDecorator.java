package com.gildedgames.the_aether.world.biome;

import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.world.biome.decoration.*;
import com.gildedgames.the_aether.world.biome.decoration.*;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;

public class AetherBiomeDecorator extends BiomeDecorator {

	public World world;

	public Random rand;

	public BiomeGenBase aetherBiome;

	private AetherFoilageFeature foilage = new AetherFoilageFeature();
	private AetherMinableFeature ores = new AetherMinableFeature();
	private AetherSkyrootTreeFeature skyroot_tree = new AetherSkyrootTreeFeature(false);
	private AetherDungeonOakTreeFeature golden_oak_tree_dungeon = new AetherDungeonOakTreeFeature();
	private AetherQuicksoilFeature quicksoil_patches = new AetherQuicksoilFeature();
	private AetherFloatingIslandFeature crystal_island = new AetherFloatingIslandFeature();
	private AetherLiquidsFeature liquid_overhang = new AetherLiquidsFeature();
	private AetherHolidayTreeFeature holiday_tree = new AetherHolidayTreeFeature();
	private AetherLakesFeature aether_lakes = new AetherLakesFeature();
	private AetherCloudsFeature clouds = new AetherCloudsFeature();
	private final WorldGenDoublePlant double_grass = new WorldGenDoublePlant();

	public AetherBiomeDecorator() {
		super();
	}

	@Override
	public void decorateChunk(World world, Random random, BiomeGenBase biome, int x, int z) {
		if (this.world != null) {
			System.err.println("Already decorating");
		} else {
			this.world = world;
			this.rand = random;
			this.chunk_X = x;
			this.chunk_Z = z;
			this.aetherBiome = biome;
			genDecorations(biome);
			this.world = null;
			this.rand = null;
		}
	}

	@Override
	protected void genDecorations(BiomeGenBase biome) {
		generateClouds(2, 4, 50, this.nextInt(64) + 96);
		generateClouds(1, 8, 26, this.nextInt(64) + 32);
		generateClouds(0, 16, 14, this.nextInt(64) + 64);

		if(shouldSpawn(37)) {
			this.crystal_island.generate(this.world, this.rand, this.chunk_X + 8, this.nextInt(64) + 32, this.chunk_Z + 8);
		}

		if(shouldSpawn(3)) {
			spawnOre(AetherBlocks.aether_dirt, 32, 20, 128);
		}

		generateFoilage(AetherBlocks.white_flower);
		generateFoilage(AetherBlocks.purple_flower);

		spawnOre(AetherBlocks.icestone, 16, 10, 128);
		spawnOre(AetherBlocks.ambrosium_ore, 16, 15, 128);
		spawnOre(AetherBlocks.zanite_ore, 8, 15, 64);
		spawnOre(AetherBlocks.gravitite_ore, 6, 8, 32);

		generateFoilage(AetherBlocks.berry_bush);

		for (int i3 = 0; i3 < 3; ++i3) {
			int x = this.chunk_X + this.nextInt(16) + 8;
			int z = this.chunk_Z + this.nextInt(16) + 8;
			int y = this.world.getHeightValue(x, z);
			getTree().generate(this.world, this.rand, x, y, z);
		}

		if (AetherConfig.shouldLoadHolidayContent()) {
			if (this.shouldSpawn(15)) {
				int x = this.chunk_X + 8;
				int z = this.chunk_Z + 8;
				int y = this.world.getHeightValue(x, z);
				this.holiday_tree.generate(this.world, this.rand, x, y, z);
			}
		}

		for (int i = 0; i < 25; i++) {
			int x = this.chunk_X + this.nextInt(16);
			int z = this.chunk_Z + this.nextInt(16);
			int y = this.world.getHeightValue(x, z);
			this.golden_oak_tree_dungeon.generate(this.world, this.rand, x, y, z);
		}

		if (AetherConfig.tallgrassEnabled()) {
			for (int i3 = 0; i3 < 10; ++i3) {
				int j7 = this.chunk_X + this.rand.nextInt(16) + 8;
				int i11 = this.chunk_Z + this.rand.nextInt(16) + 8;
				int k14 = this.world.getHeight() * 2;
				if (k14 > 0) {
					int l17 = this.rand.nextInt(k14);
					this.aetherBiome.getRandomWorldGenForGrass(this.rand).generate(this.world, this.rand, j7, l17, i11);
				}
			}

			if(TerrainGen.decorate(this.world, this.rand, this.chunk_X, this.chunk_Z, DecorateBiomeEvent.Decorate.EventType.GRASS)) {
				for (int i = 0; i < 7; ++i) {
					int x = this.chunk_X + this.rand.nextInt(16) + 8;
					int z = this.chunk_Z + this.rand.nextInt(16) + 8;
					int y = this.rand.nextInt(this.world.getHeight() + 32);
					this.double_grass.func_150548_a(2);
					this.double_grass.generate(this.world, rand, x, y, z);
				}
			}
		}

		if(shouldSpawn(10)) {
			(new WorldGenLakes(Blocks.water)).generate(this.world, this.rand, this.chunk_X + this.rand.nextInt(16) + 8, this.rand.nextInt(256), this.chunk_Z + this.rand.nextInt(16) + 8);
		}
	}

	public int nextInt(int max) {
		return this.rand.nextInt(max);
	}

	public boolean shouldSpawn(int chance) {
		return this.nextInt(chance) == 0;
	}

	public WorldGenerator getTree() {
		return shouldSpawn(30) ? new AetherOakTreeFeature() : new AetherSkyrootTreeFeature(true);
	}

	public void generateFoilage(Block block) {
		this.foilage.setPlantBlock(block);

		for (int n = 0; n < 2; n++) {
			this.foilage.generate(this.world, this.rand, this.chunk_X + this.nextInt(16) + 8, this.nextInt(128), this.chunk_Z + this.nextInt(16) + 8);
		}
	}

	public void generateClouds(int meta, int amount, int chance, int y) {
		if (this.shouldSpawn(chance)) {
			this.clouds.setCloudMeta(meta);
			this.clouds.setCloudAmount(amount);

			this.clouds.generate(this.world, this.rand, this.chunk_X + this.nextInt(16), this.nextInt(y), this.chunk_Z + this.nextInt(16));
		}
	}

	public void spawnOre(Block state, int size, int chance, int y) {
		this.ores.setSize(size);
		this.ores.setBlock(state);

		for (int chances = 0; chances < chance; chances++) {
			this.ores.generate(this.world, this.rand, this.chunk_X + this.nextInt(16), this.nextInt(y), this.chunk_Z + this.nextInt(16));
		}
	}

}
