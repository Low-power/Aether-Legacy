package com.gildedgames.the_aether.world.dungeon;

import com.gildedgames.the_aether.entities.bosses.slider.Slider;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.world.util.RandomTracker;
import com.gildedgames.the_aether.world.dungeon.util.AetherDungeon;
import com.gildedgames.the_aether.world.dungeon.util.PositionData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class BronzeDungeon extends AetherDungeon {
	private boolean needsCorridor;
	private int roomMaximum;
	private int roomCount;

	public BronzeDungeon() {
		needsCorridor = false;
	}

	@Override
	public boolean generate(World world, Random random, int i, int j, int k) {
		replaceAir = true;
		replaceSolid = true;

		roomMaximum = random.nextInt(4) + 2;
		roomCount = 0;

		generateBossRoom(world, random, i, j, k);

		return true;
	}

	public boolean generateBossRoom(World world, Random random, int x, int y, int z) {
		if (!isBoxSolid(world, new PositionData(x, y - 3, z), new PositionData(16, 18, 16)) || !isBoxSolid(world, new PositionData(x + 20, y, z + 2), new PositionData(12, 12, 12))) {
			return false;
		}

		RandomTracker randomTracker = new RandomTracker();
		if (randomTracker.testRandom(random,15) != 0 && randomTracker.testRandom(random,40) != 0) {
			return false;
		}

		setBlocks(lockedBlock(), lockedLightBlock(), 20);

		addHollowBox(world, random, new PositionData(x, y, z), new PositionData(16, 12, 16));
		addHollowBox(world, random, new PositionData(x + 6, y - 2, z + 6), new PositionData(4, 4, 4));

		if (!world.isRemote) {
			Slider slider = new Slider(world);
			slider.setPosition(x + 8, y + 2, z + 8);
			slider.setDungeon(slider.posX - 8, slider.posY - 2, slider.posZ - 8);
			world.spawnEntityInWorld(slider);
		}

		world.setBlock(x + 7, y - 1, z + 7, AetherBlocks.treasure_chest);
		generateEmptyRoom(world, random, x, y, z);

		return true;
	}

	public boolean generateEmptyRoom(World world, Random random, int i, int j, int k) {
		int x = i;
		int y = j;
		int z = k;

		int rooms = random.nextInt(4);

		switch (rooms) {
			case 0:
			{
				//EAST
				x = i + 20;
				y = j;
				z = k + 2;

				if (!isBoxSolid(world, new PositionData(x, y, z), new PositionData(12, 12, 12))) {
					return true;
				}

				setBlocks(this.mainBlock(), this.mainLightBlock(), 20);
				addHollowBox(world, random, new PositionData(x, y, z), new PositionData(12, 12, 12));

				setBlocks(this.fillerBlock(), this.fillerBlock1(), 5);

				addSquareTube(world, random, new PositionData(x - 5, y, z + 3), new PositionData(6, 6, 6), 0);

				for (int p = x + 2; p < x + 10; p += 3) {
					for (int q = z + 2; q < z + 10; q += 3) {
						world.setBlock(p, j, q, AetherBlocks.carved_trap, 0, 2);
					}
				}

				break;
			}
			case 1:
			{
				//WEST
				x = i - 16;
				y = j;
				z = k + 2;

				if (!isBoxSolid(world, new PositionData(x, y, z), new PositionData(12, 12, 12))) {
					return true;
				}

				setBlocks(this.mainBlock(), this.mainLightBlock(), 20);
				addHollowBox(world, random, new PositionData(x, y, z), new PositionData(12, 12, 12));

				setBlocks(this.fillerBlock(), this.fillerBlock1(), 5);

				addSquareTube(world, random, new PositionData(x + 11, y, z + 3), new PositionData(6, 6, 6), 0);

				for (int p = x + 2; p < x + 10; p += 3) {
					for (int q = z + 2; q < z + 10; q += 3) {
						world.setBlock(p, j, q, AetherBlocks.carved_trap, 0, 2);
					}
				}

				break;
			}
			case 2:
			{
				//SOUTH
				x = i + 2;
				y = j;
				z = k + 20;

				if (!isBoxSolid(world, new PositionData(x, y, z), new PositionData(12, 12, 12))) {
					return true;
				}

				setBlocks(this.mainBlock(), this.mainLightBlock(), 20);
				addHollowBox(world, random, new PositionData(x, y, z), new PositionData(12, 12, 12));

				setBlocks(this.fillerBlock(), this.fillerBlock1(), 5);

				addSquareTube(world, random, new PositionData(x + 3, y, z - 5), new PositionData(6, 6, 6), 2);

				for (int p = x + 2; p < x + 10; p += 3) {
					for (int q = z + 2; q < z + 10; q += 3) {
						world.setBlock(p, j, q, AetherBlocks.carved_trap, 0, 2);
					}
				}

				break;
			}
			case 3:
			{
				//NORTH
				x = i + 2;
				y = j;
				z = k - 16;

				if (!isBoxSolid(world, new PositionData(x, y, z), new PositionData(12, 12, 12))) {
					return true;
				}

				setBlocks(this.mainBlock(), this.mainLightBlock(), 20);
				addHollowBox(world, random, new PositionData(x, y, z), new PositionData(12, 12, 12));

				setBlocks(this.fillerBlock(), this.fillerBlock1(), 5);

				addSquareTube(world, random, new PositionData(x + 3, y, z + 11), new PositionData(6, 6, 6), 2);

				for (int p = x + 2; p < x + 10; p += 3) {
					for (int q = z + 2; q < z + 10; q += 3) {
						world.setBlock(p, j, q, AetherBlocks.carved_trap, 0, 2);
					}
				}
				break;
			}
		}

		if ((!determineRoomPosition(world, random, new PositionData(x, y, z)) && roomCount == 0)) {
			return false;
		}

		if (needsCorridor) {
			endCorridor(world, random, new PositionData(x, y, z));
		}

		return true;
	}

	public boolean determineRoomPosition(World world, Random random, PositionData pos) {
		if (roomCount >= roomMaximum) {
			this.needsCorridor = true;
			return true;
		}

		ArrayList<Integer> sides = new ArrayList<>();
		sides.add(1);
		sides.add(2);
		sides.add(3);
		sides.add(4);

		Collections.shuffle(sides);

		if (generateRoomWithSide(world, random, pos, sides.get(0))) {
			return true;
		} else if (generateRoomWithSide(world, random, pos, sides.get(1))) {
			return true;
		} else if (generateRoomWithSide(world, random, pos, sides.get(2))) {
			return true;
		} else if (generateRoomWithSide(world, random, pos, sides.get(3))) {
			return true;
		} else {
			this.needsCorridor = true;
			return false;
		}
	}

	public boolean generateRoomWithSide(World world, Random random, PositionData pos, int switchCase) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int dir = 0;

		switch (switchCase) {
			case 1:
			{
				x += 16;
				z += 0;
				dir = 0;
				break;
			}
			case 2:
			{
				x += 0;
				z += 16;
				dir = 1;
				break;
			}
			case 3:
			{
				x -= 16;
				z += 0;
				dir = 2;
				break;
			}
			case 4:
			{
				x += 0;
				z -= 16;
				dir = 3;
				break;
			}
		}

		return generateNextRoom(world, random, new PositionData(x, y, z), dir);
	}

	public boolean generateNextRoom(World world, Random random, PositionData pos, int dir) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		if (!isBoxSolid(world, new PositionData(x, y, z), new PositionData(12, 8, 12))) {
			return false;
		}

		setBlocks(this.mainBlock(), this.mainLightBlock(), 20);

		addHollowBox(world, random, new PositionData(x, y, z), new PositionData(12, 8, 12));

		for (int p = x; p < x + 12; p++) {
			for (int q = y; q < y + 8; q++) {
				for (int r = z; r < z + 12; r++) {
					if (world.getBlock(p, q, r) == this.mainBlock() && random.nextInt(100) == 0) {
						world.setBlock(p, q, r, AetherBlocks.carved_trap);
					}
				}
			}
		}

		for (int p = x + 2; p < x + 10; p += 7) {
			for (int q = z + 2; q < z + 10; q += 7) {
				world.setBlock(p, pos.getY(), q, AetherBlocks.carved_trap);
			}
		}

		addPlaneY(world, random, new PositionData(x + 4, y + 1, z + 4), new PositionData(4, 0, 4));
		int type = random.nextInt(2);
		int p = x + 5 + random.nextInt(2);
		int q = z + 5 + random.nextInt(2);

		switch (type) {
			case 0: {
				world.setBlock(p, y + 2, q, AetherBlocks.chest_mimic);
				break;
			}
			case 1: {
				if (world.getBlock(p, y + 2, q) == Blocks.air) {
					world.setBlock(p, y + 2, q, Blocks.chest);
					TileEntityChest chest = (TileEntityChest) world.getTileEntity(p, y + 2, q);

					for (p = 0; p < 3 + random.nextInt(3); p++) {
						chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), this.getNormalLoot(random));
					}
				}
				break;
			}
		}

		setBlocks(this.fillerBlock(), this.fillerBlock1(), 5);

		switch (dir) {
			case 0: {
				addSquareTube(world, random, new PositionData(x - 5, y, z + 3), new PositionData(6, 6, 6), 0);
				break;
			}
			case 1: {
				addSquareTube(world, random, new PositionData(x + 3, y, z - 5), new PositionData(6, 6, 6), 2);
				break;
			}
			case 2: {
				addSquareTube(world, random, new PositionData(x + 11, y, z + 3), new PositionData(6, 6, 6), 0);
				break;
			}
			case 3: {
				addSquareTube(world, random, new PositionData(x + 3, y, z + 11), new PositionData(6, 6, 6), 2);
				break;
			}
		}

		roomCount++;

		if(!determineRoomPosition(world, random,  new PositionData(x, y, z))) {
			return false;
		}

		return determineRoomPosition(world, random, new PositionData(x, y, z));
	}

	public boolean endCorridor(World world, Random random, PositionData pos) {
		ArrayList<Integer> sides = new ArrayList<>();
		sides.add(1);
		sides.add(2);
		sides.add(3);
		sides.add(4);

		Collections.shuffle(sides);

		if (generateEndCorridor(world, random, pos, sides.get(0))) {
			return true;
		} else if (generateEndCorridor(world, random, pos, sides.get(1))) {
			return true;
		} else if (generateEndCorridor(world, random, pos, sides.get(2))) {
			return true;
		} else if (generateEndCorridor(world, random, pos, sides.get(3))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean generateEndCorridor(World world, Random random, PositionData pos, int switchCase) {
		if (!this.needsCorridor) {
			return false;
		}

		replaceAir = false;

		switch (switchCase) {
			case 1:
			{
				//EAST

				boolean tunnelling = true;
				boolean maxLength = false;
				int x = pos.getX();
				int y = pos.getY();
				int z = pos.getZ();

				x += 11;
				z += 3;

				if (!isBoxSolid(world, new PositionData(x + 1, y, z), new PositionData(2, 8, 6))) {
					return false;
				}

				while(tunnelling) {
					if(isBoxEmpty(world, new PositionData(x, y, z), new PositionData(1, 8, 6))) {
						tunnelling = false;
					}

					if(hasBlock(world, new PositionData(x + 1, y, z), new PositionData(2, 8, 6), AetherBlocks.carved_stone) ||
					   hasBlock(world, new PositionData(x + 1, y, z), new PositionData(2, 8, 6), AetherBlocks.locked_carved_stone)) {
						tunnelling = false;
					}

					if (x - pos.getX() > 100) {
						maxLength = true;
						tunnelling = false;
					}

					setBlocks(this.fillerBlock(), this.fillerBlock1(), 5);
					addPlaneX(world, random, new PositionData(x, y, z), new PositionData(0, 8, 6));

					setBlocks(Blocks.air, Blocks.air, 1);
					addPlaneX(world, random, new PositionData(x, y + 1, z + 1), new PositionData(0, 6, 4));

					x++;
				}
				if (maxLength) return false;
				this.needsCorridor = false;
				return true;
			}
			case 2:
			{
				//WEST

				boolean tunnelling = true;
				boolean maxLength = false;
				int x = pos.getX();
				int y = pos.getY();
				int z = pos.getZ();

				x -= 0;
				z += 3;

				if (!isBoxSolid(world, new PositionData(x - 1, y, z), new PositionData(1, 8, 6))) {
					return false;
				}

				while(tunnelling) {
					if(isBoxEmpty(world, new PositionData(x, y, z), new PositionData(1, 8, 6))) {
						tunnelling = false;
					}

					if(hasBlock(world, new PositionData(x - 1, y, z), new PositionData(1, 8, 6), AetherBlocks.carved_stone) ||
					   hasBlock(world, new PositionData(x - 1, y, z), new PositionData(1, 8, 6), AetherBlocks.locked_carved_stone)) {
						tunnelling = false;
					}

					if (pos.getX() - x > 100) {
						maxLength = true;
						tunnelling = false;
					}

					setBlocks(this.fillerBlock(), this.fillerBlock1(), 5);
					addPlaneX(world, random, new PositionData(x, y, z), new PositionData(0, 8, 6));

					setBlocks(Blocks.air, Blocks.air, 1);
					addPlaneX(world, random, new PositionData(x, y + 1, z + 1), new PositionData(0, 6, 4));

					x--;
				}
				if (maxLength) return false;
				this.needsCorridor = false;
				return true;
			}
			case 3:
			{
				//SOUTH
				// BUGGED

				boolean tunnelling = true;
				boolean maxLength = false;
				int x = pos.getX();
				int y = pos.getY();
				int z = pos.getZ();

				x += 3;
				z += 11;

				if (!isBoxSolid(world, new PositionData(x, y, z + 1), new PositionData(6, 8, 2))) {
					return false;
				}

				while(tunnelling) {
					if(isBoxEmpty(world, new PositionData(x, y, z), new PositionData(6, 8, 1))) {
						tunnelling = false;
					}

					if(hasBlock(world, new PositionData(x, y, z + 1), new PositionData(6, 8, 2), AetherBlocks.carved_stone) ||
					   hasBlock(world, new PositionData(x, y, z + 1), new PositionData(6, 8, 2), AetherBlocks.locked_carved_stone)) {
						tunnelling = false;
					}

					if (z - pos.getZ() > 100) {
						maxLength = true;
						tunnelling = false;
					}

					setBlocks(this.fillerBlock(), this.fillerBlock1(), 5);
					addPlaneZ(world, random, new PositionData(x, y, z), new PositionData(6, 8, 0));

					setBlocks(Blocks.air, Blocks.air, 1);
					addPlaneZ(world, random, new PositionData(x + 1, y + 1, z), new PositionData(4, 6, 0));

					z++;
				}
				if (maxLength) return false;
				this.needsCorridor = false;
				return true;
			}
			case 4:
			{
				//NORTH

				boolean tunnelling = true;
				boolean maxLength = false;
				int x = pos.getX();
				int y = pos.getY();
				int z = pos.getZ();

				x += 3;
				z -= 0;

				if (!isBoxSolid(world, new PositionData(x, y, z - 1), new PositionData(6, 8, 1))) {
					return false;
				}

				while(tunnelling) {
					if(isBoxEmpty(world, new PositionData(x, y, z), new PositionData(6, 8, 1))) {
						tunnelling = false;
					}

					if(hasBlock(world, new PositionData(x, y, z - 1), new PositionData(6, 8, 1), AetherBlocks.carved_stone) ||
					   hasBlock(world, new PositionData(x, y, z - 1), new PositionData(6, 8, 1), AetherBlocks.locked_carved_stone)) {
						tunnelling = false;
					}

					if (pos.getZ() - z > 100) {
						maxLength = true;
						tunnelling = false;
					}

					setBlocks(this.fillerBlock(), this.fillerBlock1(), 5);
					addPlaneZ(world, random, new PositionData(x, y, z), new PositionData(6, 8, 0));

					setBlocks(Blocks.air, Blocks.air, 1);
					addPlaneZ(world, random, new PositionData(x + 1, y + 1, z), new PositionData(4, 6, 0));

					z--;
				}
				if (maxLength) return false;
				this.needsCorridor = false;
				return true;
			}
		}

		return false;
	}

	private ItemStack getNormalLoot(Random random) {
		int item = random.nextInt(15);
		switch (item) {
			case 0:
				return new ItemStack(AetherItems.zanite_pickaxe);
			case 1:
				return new ItemStack(AetherItems.zanite_axe);
			case 2:
				return new ItemStack(AetherItems.zanite_sword);
			case 3:
				return new ItemStack(AetherItems.zanite_shovel);
			case 4:
				return new ItemStack(AetherItems.swet_cape);
			case 5:
				return new ItemStack(AetherItems.ambrosium_shard, random.nextInt(10) + 1);
			case 6:
				return new ItemStack(AetherItems.dart, random.nextInt(5) + 1, 0);
			case 7:
				return new ItemStack(AetherItems.dart, random.nextInt(3) + 1, 1);
			case 8:
				return new ItemStack(AetherItems.dart, random.nextInt(3) + 1, 2);
			case 9: {
				if (random.nextInt(20) == 0) {
					return new ItemStack(AetherItems.aether_tune);
				}

				break;
			}
			case 10:
				return new ItemStack(AetherItems.skyroot_bucket, 1, 2);
			case 11: {
				if (random.nextInt(10) == 0) {
					return new ItemStack(Items.record_cat);
				}

				break;
			}
			case 12: {
				if (random.nextInt(4) == 0) {
					return new ItemStack(AetherItems.iron_ring);
				}
				break;
			}
			case 13: {
				if (random.nextInt(10) == 0) {
					return new ItemStack(AetherItems.golden_ring);
				}
				break;
			}
		}
		return new ItemStack(AetherBlocks.ambrosium_torch);
	}

	public static ItemStack getBronzeLoot(Random random) {
		int item = random.nextInt(10);
		switch (item) {
			case 0:
				return new ItemStack(AetherItems.gummy_swet, random.nextInt(7) + 1, random.nextInt(2));
			case 1:
				return new ItemStack(AetherItems.phoenix_bow);
			case 2:
				return new ItemStack(AetherItems.flaming_sword);
			case 3:
				return new ItemStack(AetherItems.notch_hammer);
			case 4:
				return new ItemStack(AetherItems.lightning_knife, random.nextInt(20) + 1);
			case 5:
				return new ItemStack(AetherItems.valkyrie_lance);
			case 6:
				return new ItemStack(AetherItems.agility_cape);
			case 7:
				return new ItemStack(AetherItems.sentry_boots);
			case 8:
				return new ItemStack(AetherItems.repulsion_shield);
		}

		return new ItemStack(AetherItems.cloud_staff);
	}

	public Block lockedLightBlock() {
		return AetherBlocks.locked_sentry_stone;
	}

	public Block lockedBlock() {
		return AetherBlocks.locked_carved_stone;
	}

	public Block mainLightBlock() {
		return AetherBlocks.sentry_stone;
	}

	public Block mainBlock() {
		return AetherBlocks.carved_stone;
	}

	public Block fillerBlock() {
		return AetherBlocks.holystone;
	}

	public Block fillerBlock1() {
		return AetherBlocks.mossy_holystone;
	}

}
