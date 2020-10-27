package com.gildedgames.the_aether.world;

import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.world.dungeon.BronzeDungeon;
import com.gildedgames.the_aether.world.dungeon.util.AetherDungeon;
import com.gildedgames.the_aether.world.gen.GoldenDungeonStructure;
import com.gildedgames.the_aether.world.gen.MapGenLargeColdAercloud;
import com.gildedgames.the_aether.world.gen.MapGenQuicksoil;
import com.gildedgames.the_aether.world.gen.SilverDungeonStructure;
import com.gildedgames.the_aether.AetherConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Random;

public class AetherChunkProvider implements IChunkProvider {

	private Random rand;

	private World world;

	private NoiseGeneratorOctaves noisegen1;
	private NoiseGeneratorOctaves noisegen2;
	private NoiseGeneratorOctaves perlinnoisegen1;	// 3
	private NoiseGeneratorOctaves noisegen4;
	private NoiseGeneratorOctaves noisegen5;
	private NoiseGeneratorOctaves scalenoisegen1;	// 6
	private NoiseGeneratorOctaves noisegen7;

	private double buffer[];
	private double pnr[], ar[], br[];
	private double r4p1[];
	private double r4p2[];
	private double r5[];
	private double r6[];
	private double r7[];

	protected AetherDungeon dungeon_bronze = new BronzeDungeon();

	private MapGenQuicksoil quicksoilGen = new MapGenQuicksoil();

	private SilverDungeonStructure silverDungeonStructure = new SilverDungeonStructure();

	private GoldenDungeonStructure goldenDungeonStructure = new GoldenDungeonStructure();

	private MapGenLargeColdAercloud largeColdAercloudStructure = new MapGenLargeColdAercloud();

	public AetherChunkProvider(World world, long seed) {
		this.world = world;

		this.rand = new Random(seed);
/*
		this.pnr = new double[256];
		this.ar = new double[256];
		this.br = new double[256];
		this.r5 = new double[256];
*/
		this.noisegen1 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noisegen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.perlinnoisegen1 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noisegen4 = new NoiseGeneratorOctaves(this.rand, 4);
		this.noisegen5 = new NoiseGeneratorOctaves(this.rand, 4);
		this.scalenoisegen1 = new NoiseGeneratorOctaves(this.rand, 10);
		this.noisegen7 = new NoiseGeneratorOctaves(this.rand, 16);

		check_old_save_directory();
		check_cauldron_save_directory_bug();
	}

	public void setBlocksInChunk(int x, int z, Block[] blocks) {
		this.buffer = this.setupNoiseGenerators(this.buffer, x * 2, z * 2);

		for (int i1 = 0; i1 < 2; i1++) {
			for (int j1 = 0; j1 < 2; j1++) {
				for (int k1 = 0; k1 < 32; k1++) {
					double d1 = this.buffer[(i1 * 3 + j1) * 33 + k1];
					double d2 = this.buffer[(i1 * 3 + (j1 + 1)) * 33 + k1];
					double d3 = this.buffer[((i1 + 1) * 3 + j1) * 33 + k1];
					double d4 = this.buffer[((i1 + 1) * 3 + (j1 + 1)) * 33 + k1];

					double d5 = (this.buffer[(i1 * 3 + j1) * 33 + (k1 + 1)] - d1) * 0.25D;
					double d6 = (this.buffer[(i1 * 3 + (j1 + 1)) * 33 + (k1 + 1)] - d2) * 0.25D;
					double d7 = (this.buffer[((i1 + 1) * 3 + j1) * 33 + (k1 + 1)] - d3) * 0.25D;
					double d8 = (this.buffer[((i1 + 1) * 3 + (j1 + 1)) * 33 + (k1 + 1)] - d4) * 0.25D;

					for (int l1 = 0; l1 < 4; l1++) {
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * 0.125D;
						double d13 = (d4 - d2) * 0.125D;

						for (int i2 = 0; i2 < 8; i2++) {
							int j2 = i2 + i1 * 8 << 11 | 0 + j1 * 8 << 7 | k1 * 4 + l1;
							double d15 = d10;
							double d16 = (d11 - d10) * 0.125D;

							for (int k2 = 0; k2 < 8; k2++) {
								Block filler = Blocks.air;

								if (d15 > 0.0D) {
									filler = BlocksAether.holystone;
								}

								blocks[j2] = filler;
								j2 += 128;
								d15 += d16;
							}

							d10 += d12;
							d11 += d13;
						}

						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}

				}

			}

		}

	}

	public void buildSurfaces(int i, int j, Block[] blocks) {
		final double d = 0.03125D;
		this.r4p1 = noisegen4.generateNoiseOctaves(this.r4p1, i * 16, j * 16, 0, 16, 16, 1, d, d, 1D);
		this.r4p2 = noisegen4.generateNoiseOctaves(this.r4p2, i * 16, 109, j * 16, 16, 1, 16, d, 1D, d);
		this.r5 = noisegen5.generateNoiseOctaves(this.r5, i * 16, j * 16, 0, 16, 16, 1, d * 2D, d * 2D, d * 2D);
		for (int k = 0; k < 16; k++) {
			for (int l = 0; l < 16; l++) {
				int i1 = (int)(this.r5[k + l * 16] / 3D + 3D + this.rand.nextDouble() * 0.25D);
				int j1 = -1;

				Block top = BlocksAether.aether_grass;
				Block filler = BlocksAether.aether_dirt;

				for (int k1 = 127; k1 >= 0; k1--) {
					int l1 = (l * 16 + k) * 128 + k1;

					Block block = blocks[l1];

					if (block == Blocks.air) {
						j1 = -1;
					} else if (block == BlocksAether.holystone) {
						if (j1 == -1) {
							if (i1 <= 0) {
								top = Blocks.air;
								filler = BlocksAether.holystone;
							}

							j1 = i1;

							if (k1 >= 0) {
								blocks[l1] = top;
							} else {
								blocks[l1] = filler;
							}
						} else if (j1 > 0) {
							--j1;
							blocks[l1] = filler;
						}
					}
				}
			}
		}
	}

	private double[] setupNoiseGenerators(double buffer[], int x, int z) {
		if (buffer == null) {
			buffer = new double[3 * 33 * 3];
		}

		double d1 = 684.412D;
		double d2 = 684.412D;

		this.r6 = this.scalenoisegen1.generateNoiseOctaves(this.r6, x, z, 3, 3, 1.121D, 1.121D, 0.5D);
		this.r7 = this.noisegen7.generateNoiseOctaves(this.r7, x, z, 3, 3, 200D, 200D, 0.5D);

		d1 *= 2D;

		this.pnr = this.perlinnoisegen1.generateNoiseOctaves(this.pnr, x, 0, z, 3, 33, 3, d1 / 80D, d2 / 160D, d1 / 80D);
		this.ar = this.noisegen1.generateNoiseOctaves(this.ar, x, 0, z, 3, 33, 3, d1, d2, d1);
		this.br = this.noisegen2.generateNoiseOctaves(this.br, x, 0, z, 3, 33, 3, d1, d2, d1);

		int i1 = 0;
		//int i2 = 0;

		for (int j1 = 0; j1 < 3; j1++) {
			for (int j2 = 0; j2 < 3; j2++) {
/*
				double d4 = 0.5D;
				double d5 = 1D - d4;
				d5 *= d5;
				d5 *= d5;
				d5 = 1D - d5;
				double d6 = (this.r6[i2] + 256D) / 512D;
				d6 *= d5;
				if(d6 > 1D) d6 = 1D;
				else if(d6 < 0D) d6 = 0D;
				i2++;
*/
				for (int j3 = 0; j3 < 33; j3++) {
					double d8;

					double d10 = this.ar[i1] / 512D;
					double d11 = this.br[i1] / 512D;
					double d12 = (this.pnr[i1] / 10D + 1D) / 2D;

					if (d12 < 0D) {
						d8 = d10;
					} else if (d12 > 1D) {
						d8 = d11;
					} else {
						d8 = d10 + (d11 - d10) * d12;
					}

					d8 -= 8D;

					if (j3 > 33 - 32) {
						double d13 = (float) (j3 - (33 - 32)) / ((float) 32 - 1.0F);
						d8 = d8 * (1D - d13) + -30D * d13;
					}

					if (j3 < 8) {
						double d14 = (float) (8 - j3) / ((float) 8 - 1.0F);
						d8 = d8 * (1D - d14) + -30D * d14;
					}

					buffer[i1] = d8;

					i1++;
				}

			}

		}

		return buffer;
	}

	@Override
	public Chunk provideChunk(int x, int z) {
		this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
		Block[] ablock = new Block[32768];

		this.setBlocksInChunk(x, z, ablock);
		this.buildSurfaces(x, z, ablock);

		this.quicksoilGen.func_151539_a(this, this.world, x, z, ablock);

		this.largeColdAercloudStructure.func_151539_a(this, this.world, x, z, ablock);

		this.silverDungeonStructure.func_151539_a(this, this.world, x, z, ablock);
		this.goldenDungeonStructure.func_151539_a(this, this.world, x, z, ablock);

		Chunk chunk = new Chunk(this.world, ablock, x, z);
		chunk.generateSkylightMap();

		return chunk;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List getPossibleCreatures(EnumCreatureType creatureType, int x, int y, int z) {
		return this.world.getBiomeGenForCoords(x, z).getSpawnableList(creatureType);
	}

	@Override
	public void recreateStructures(int x, int z) {
		this.largeColdAercloudStructure.func_151539_a(this, this.world, x, z, (Block[]) null);
		this.silverDungeonStructure.func_151539_a(this, this.world, x, z, (Block[]) null);
		this.goldenDungeonStructure.func_151539_a(this, this.world, x, z, (Block[]) null);
	}

	@Override
	public ChunkPosition func_147416_a(World worldIn, String structureName, int x, int y, int z) //getNearestStructurePos
	{
		return null;
	}

	@Override
	public void populate(IChunkProvider provider, int chunkX, int chunkZ) {
		int x = chunkX * 16;
		int z = chunkZ * 16;

		BiomeGenBase biome = this.world.getBiomeGenForCoords(x + 16, z + 16);

		this.rand.setSeed(this.world.getSeed());
		long k = this.rand.nextLong() / 2L * 2L + 1L;
		long l = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long) x * k + (long) z * l ^ this.world.getSeed());

		this.largeColdAercloudStructure.generateStructuresInChunk(this.world, this.rand, chunkX, chunkZ);
		this.silverDungeonStructure.generateStructuresInChunk(this.world, this.rand, chunkX, chunkZ);
		this.goldenDungeonStructure.generateStructuresInChunk(this.world, this.rand, chunkX, chunkZ);

		biome.decorate(this.world, this.rand, x, z);

		this.dungeon_bronze.generate(this.world, this.rand, x, this.rand.nextInt(48) + 24, z);

		SpawnerAnimals.performWorldGenSpawning(this.world, biome, x + 8, z + 8, 16, 16, this.rand);
	}

	@Override
	public Chunk loadChunk(int chunkX, int chunkZ) {
		return this.provideChunk(chunkX, chunkZ);
	}

	@Override
	public boolean chunkExists(int chunkX, int chunkZ) {
		return true;
	}

	@Override
	public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
		return true;
	}

	@Override
	public boolean unloadQueuedChunks() {
		return true;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public String makeString() {
		return "AetherRandomLevelSource";
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	@Override
	public void saveExtraData() {
	}

	private void check_old_save_directory() {
		Logger log = LogManager.getLogger();
		File save_dir = world.getSaveHandler().getWorldDirectory();
		File old_aether_save_dir = new File(save_dir, "Dim-Aether");
		if(!old_aether_save_dir.isDirectory()) return;
		Path old_aether_save_dir_path = old_aether_save_dir.toPath();
		if(Files.isSymbolicLink(old_aether_save_dir_path)) return;
		File aether_save_dir = new File(save_dir, "AETHER");
/*
		if(aether_save_dir.exists() && !aether_save_dir.delete()) {
			if(!aether_save_dir.isDirectory()) throw new RuntimeException(String.format("Failed to remove non-directory file '%s'", aether_save_dir));
			FMLLog.log(Level.WARN, "Old Aether save directory '%s' exists, but new directory '%s' cannot be removed (not empty?); old save will not be migrated", old_aether_save_dir, aether_save_dir);
			return;
		}
*/
		Path aether_save_dir_path = aether_save_dir.toPath();
/*
		try {
			Files.deleteIfExists(aether_save_dir_path);
		} catch(DirectoryNotEmptyException e) {
			LogManager.getLogger().log(Level.WARN, String.format("Old Aether save directory '%s' exists, but new directory '%s' is not empty; old save will not be migrated", old_aether_save_dir_path, aether_save_dir_path), e);
			return;
		} catch(IOException e) {
			throw new RuntimeException(String.format("Failed to remove '%s'", aether_save_dir_path), e);
		}
*/
		try {
			Files.move(old_aether_save_dir_path, aether_save_dir_path, StandardCopyOption.REPLACE_EXISTING);
		} catch(DirectoryNotEmptyException e) {
			log.log(Level.WARN, String.format("Old Aether save directory '%s' exists, but new directory '%s' is not empty; old save will not be migrated", old_aether_save_dir_path, aether_save_dir_path), e);
			return;
		} catch(IOException e) {
			throw new RuntimeException(String.format("Failed to move '%s' to '%s'", old_aether_save_dir_path, aether_save_dir_path), e);
		}
		try {
			Files.createSymbolicLink(old_aether_save_dir_path, aether_save_dir_path.getFileName());
		} catch(UnsupportedOperationException e) {
			e.printStackTrace();
		} catch(IOException e) {
			log.log(Level.WARN, String.format("Failed to create symbolic link '%s'", old_aether_save_dir_path), e);
		}
	}

	private void check_cauldron_save_directory_bug() {
		try {
			getClass().getClassLoader().loadClass("net.minecraftforge.cauldron.CauldronUtils");
		} catch(ClassNotFoundException e) {
			return;
		}
		Logger log = LogManager.getLogger();
		File save_dir;
		try {
			save_dir = world.getSaveHandler().getWorldDirectory().getCanonicalFile();
		} catch(IOException e) {
			e.printStackTrace();
			return;
		}
		File cauldron_erroneous_save_dir = null;
		String cauldron_erroneous_save_dir_name = "DIM" + String.valueOf(AetherConfig.get_aether_world_id());
		if(save_dir.getName().equals(cauldron_erroneous_save_dir_name)) {
			cauldron_erroneous_save_dir = save_dir;
			save_dir = save_dir.getParentFile();
			log.warn(String.format("Cauldron bug detected: the current world save path is '%s', which didn't match the configuration from AetherWorldProvider!", cauldron_erroneous_save_dir.toString()));
		} else {
			cauldron_erroneous_save_dir = new File(save_dir, cauldron_erroneous_save_dir_name);
			if(!cauldron_erroneous_save_dir.isDirectory()) return;
			log.warn(String.format("Cauldron bug detected: found erroneous save directory '%s' which shouldn't exist!", cauldron_erroneous_save_dir.toString()));
		}

		Path cauldron_erroneous_save_dir_path = cauldron_erroneous_save_dir.toPath();
		if(Files.isSymbolicLink(cauldron_erroneous_save_dir_path)) return;

		// Try to remove this directory in case it is empty
		cauldron_erroneous_save_dir.delete();

		File aether_save_dir = new File(save_dir, "AETHER");
		Path aether_save_dir_path = aether_save_dir.toPath();
		if(Files.isDirectory(cauldron_erroneous_save_dir_path, LinkOption.NOFOLLOW_LINKS)) try {
			Files.move(cauldron_erroneous_save_dir_path, aether_save_dir_path, StandardCopyOption.REPLACE_EXISTING);
		} catch(DirectoryNotEmptyException e) {
			throw new RuntimeException(String.format("Failed to rename '%s', target save directory '%s' exists and it is not empty; please manually remove one, or merge the two", cauldron_erroneous_save_dir_path, aether_save_dir_path), e);
		} catch(IOException e) {
			throw new RuntimeException(String.format("Failed to move '%s' to '%s'", cauldron_erroneous_save_dir_path, aether_save_dir_path), e);
		} else if(!Files.isDirectory(aether_save_dir_path)) try {
			Files.createDirectory(aether_save_dir_path);
		} catch(IOException e) {
			throw new RuntimeException(String.format("Failed to create Aether save directory '%s'", aether_save_dir_path), e);
		}
		try {
			Files.createSymbolicLink(cauldron_erroneous_save_dir_path, aether_save_dir_path.getFileName());
		} catch(Exception e) {
			log.log(Level.WARN, String.format("Failed to create symbolic link '%s'", cauldron_erroneous_save_dir_path), e);
			try {
				Files.createFile(cauldron_erroneous_save_dir_path);
			} catch(IOException ee) {
				throw new RuntimeException(String.format("Failed to create either a symbolic link or a regular file with path '%s'", cauldron_erroneous_save_dir_path), ee);
			}
		}
	}
}
