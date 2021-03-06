package com.gildedgames.the_aether.world.gen;

import com.gildedgames.the_aether.world.gen.components.GoldenDungeonComponent;
import com.gildedgames.the_aether.world.gen.components.GoldenIslandComponent;
import com.gildedgames.the_aether.world.gen.components.StubGoldenIslandComponent;
import com.gildedgames.the_aether.world.util.RandomTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureComponent;
import java.util.List;
import java.util.Random;

public class GoldenDungeonStructure extends MapGenStructure {

	public GoldenDungeonStructure() {
	}

	@Override
	public String func_143025_a() {
		return "aether_legacy:golden_dungeon";
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		RandomTracker randomTracker = new RandomTracker();

		if (randomTracker.testRandom(this.rand,140) != 0) {
			if (randomTracker.testRandom(this.rand,170) != 0) {
				return false;
			}
		}

		return chunkX % 10 == 0 && chunkZ % 10 == 0;
	}

	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new Start(this.worldObj, this.rand, chunkX, chunkZ);
	}

	public static class Start extends StructureStart {
		private int dungeonDirection;
		private int stubIslandCount;

		public Start() {
		}

		public Start(World world, Random random, int chunkX, int chunkZ) {
			super(chunkX, chunkZ);
			this.create(world, random, chunkX, chunkZ);
		}

		@SuppressWarnings("unchecked")
		private void create(World world, Random random, int chunkX, int chunkZ) {
			random.setSeed(world.getSeed());
			long x = (long)chunkX * random.nextLong();
			long z = (long)chunkZ * random.nextLong();
			random.setSeed(x ^ z ^ world.getSeed());

			GoldenIslandComponent dungeon = new GoldenIslandComponent((chunkX << 4) + 2, (chunkZ << 4) + 2);

			this.dungeonDirection = random.nextInt(4);
			this.stubIslandCount = 8 + random.nextInt(5);

			this.components.add(dungeon);

			for (int stubIslands = 0; stubIslands < this.stubIslandCount; ++stubIslands) {
				float f1 = 0.01745329F;
				float f2 = random.nextFloat() * 360F;
				float f3 = ((random.nextFloat() * 0.125F) + 0.7F) * 24F;
				int l4 = MathHelper.floor_double(Math.cos(f1 * f2) * (double) f3);
				int k5 = -MathHelper.floor_double(24D * (double)random.nextFloat() * 0.3D);
				int i6 = MathHelper.floor_double(-Math.sin(f1 * f2) * (double) f3);

				this.components.add(new StubGoldenIslandComponent((chunkX << 4) + 2, (chunkZ << 4) + 2, l4, k5, i6, 8));
			}

			this.components.add(new GoldenDungeonComponent((chunkX << 4) + 2, (chunkZ << 4) + 2, this.dungeonDirection));

			customOffset(random);
			updateBoundingBox();
		}

		private void customOffset(Random random) {
			int offset = random.nextInt(64);

			for(StructureComponent component : (List<StructureComponent>)this.components) {
				component.getBoundingBox().offset(0, offset, 0);
			}
		}

		@Override
		public void func_143022_a(NBTTagCompound tagCompound) {
			super.func_143022_a(tagCompound);

			tagCompound.setInteger("stubIslandCount", this.stubIslandCount);
			tagCompound.setInteger("dungeonDirection", this.dungeonDirection);
		}

		@Override
		public void func_143017_b(NBTTagCompound tagCompound) {
			super.func_143017_b(tagCompound);

			this.stubIslandCount = tagCompound.getInteger("stubIslandCount");
			this.dungeonDirection = tagCompound.getInteger("dungeonDirection");
		}

	}

}
