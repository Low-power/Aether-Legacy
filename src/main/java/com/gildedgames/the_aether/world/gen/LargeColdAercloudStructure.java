package com.gildedgames.the_aether.world.gen;

import com.gildedgames.the_aether.world.gen.components.LargeColdAercloudComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;
import java.util.Random;

public class LargeColdAercloudStructure extends MapGenStructure {

	public LargeColdAercloudStructure() {
	}

	@Override
	public String func_143025_a() {
		return "aether_legacy:cold_aercloud";
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		return this.rand.nextInt(50) == 0;
	}

	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new Start(this.worldObj, this.rand, chunkX, chunkZ);
	}

	public static class Start extends StructureStart {
		public Start() {
		}

		public Start(World world, Random random, int chunkX, int chunkZ) {
			super(chunkX, chunkZ);

			create(world, random, chunkX, chunkZ);
		}

		@SuppressWarnings("unchecked")
		private void create(World world, Random random, int chunkX, int chunkZ) {
			this.components.add(new LargeColdAercloudComponent(random, (chunkX << 4) + 2, 0, (chunkZ << 4) + 2));
			this.updateBoundingBox();
		}

	}

}
