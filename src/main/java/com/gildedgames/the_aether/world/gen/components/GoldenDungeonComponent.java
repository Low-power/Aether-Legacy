package com.gildedgames.the_aether.world.gen.components;

import com.gildedgames.the_aether.entities.bosses.sun_spirit.SunSpirit;
import com.gildedgames.the_aether.world.gen.AetherStructure;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.items.AetherItems;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import java.util.Random;

public class GoldenDungeonComponent extends AetherStructure {

	private int direction;

	public GoldenDungeonComponent() {
	}

	public GoldenDungeonComponent(int chunkX, int chunkZ, int direction) {
		this.coordBaseMode = 0;
		this.direction = direction;
		this.boundingBox = new StructureBoundingBox(chunkX, 80, chunkZ, chunkX + 100, 220, chunkZ + 100);
	}

	@Override
	public boolean generate() {
		this.replaceAir = true;
		this.replaceSolid = true;
		this.setStructureOffset(60, 0, 60);

		int r = 24;
		r = (int) Math.floor((double) r * 0.8D);
		int wid = (int) Math.sqrt((r * r) / 2);

		setBlocks(AetherBlocks.locked_hellfire_stone, AetherBlocks.locked_light_hellfire_stone, 10);

		for (int j = 4; j > -5; j--) {
			int a = wid;

			if (j >= 3 || j <= -3) {
				a--;
			}

			if (j == 4 || j == -4) {
				a--;
			}

			for (int i = -a; i <= a; i++) {
				for (int k = -a; k <= a; k++) {
					if (j == 4) {
						setBlockWithOffset(i, j, k);
					} else if (j > -4) {
						if (i == a || -i == a || k == a || -k == a) {
							setBlockWithOffset(i, j, k);
						} else {
							setBlockWithOffset(i, j, k, Blocks.air, 0);
						}
					} else {
						this.setBlockWithOffset(i, j, k);

						if ((i == (a - 2) || -i == (a - 2)) && (k == (a - 2) || -k == (a - 2))) {
							setBlockWithOffset(i, j + 1, k, Blocks.netherrack, 0);
							setBlockWithOffset(i, j + 2, k, Blocks.fire, 0);
						}
					}
				}
			}
		}

		for (int i = wid; i < wid + 32; i++) {
			for (int j = -3; j < 2; j++) {
				for (int k = -3; k < 4; k++) {
					int a, b;
					if (this.direction / 2 == 0) {
						a = i;
						b = k;
					} else {
						a = k;
						b = i;
					}

					if (this.direction % 2 == 0) {
						a = -a;
						b = -b;
					}

					if(!AetherBlocks.is_empty(getBlockStateWithOffset(a, j, b))) {
						setBlocks(AetherBlocks.holystone, AetherBlocks.mossy_holystone, 5);

						if (j == -3) {
							setBlockWithOffset(a, j, b);
						} else if (j < 1) {
							if (i == wid) {
								if (k < 2 && k > -2 && j < 0) {
									setBlockWithOffset(a, j, b, Blocks.air, 0);
								} else {
									setBlocks(AetherBlocks.locked_hellfire_stone, AetherBlocks.locked_light_hellfire_stone, 10);
									setBlockWithOffset(a, j, b);
								}
							} else {
								if (k == 3 || k == -3) {
									setBlockWithOffset(a, j, b);
								} else {
									setBlockWithOffset(a, j, b, Blocks.air, 0);
								}
							}
						} else if (i == wid) {
							setBlocks(AetherBlocks.locked_hellfire_stone, AetherBlocks.locked_light_hellfire_stone, 10);
							setBlockWithOffset(a, j, b);
						} else {
							setBlocks(AetherBlocks.holystone, AetherBlocks.mossy_holystone, 5);
							setBlockWithOffset(a, j, b);
						}
					}

					a = -a;
					b = -b;

					setBlocks(AetherBlocks.locked_hellfire_stone, AetherBlocks.locked_light_hellfire_stone, 10);

					if (i < wid + 6) {
						if (j == -3) {
							this.setBlockWithOffset(a, j, b);
						} else if (j < 1) {
							if (i == wid) {
								if (k < 2 && k > -2 && j < 0) {
									setBlockWithOffset(a, j, b);
								} else {
									setBlockWithOffset(a, j, b);
								}
							} else if (i == wid + 5) {
								setBlockWithOffset(a, j, b);
							} else {
								if (i == wid + 4 && k == 0 && j == -2) {
									setBlockWithOffset(a, j, b, AetherBlocks.treasure_chest, 0);
								} else if (k == 3 || k == -3) {
									setBlockWithOffset(a, j, b);
								} else {
									setBlockWithOffset(a, j, b, Blocks.air, 0);
								}
							}
						} else {
							setBlockWithOffset(a, j, b);
						}
					}
				}
			}

		}

		SunSpirit boss = new SunSpirit(this.worldObj, this.getActualX(0, 0), this.getActualY(-1), this.getActualZ(0, 0), this.direction);

		this.spawnEntity(boss, 0, -1, 0);

		return true;
	}

	public static ItemStack getGoldLoot(Random random) {
		int item = random.nextInt(10);

		switch (item) {
			case 0:
				return new ItemStack(AetherItems.iron_bubble);
			case 1:
				return new ItemStack(AetherItems.vampire_blade);
			case 2:
				return new ItemStack(AetherItems.pig_slayer);
			case 3: {
				if (random.nextBoolean()) {
					return new ItemStack(AetherItems.phoenix_helmet);
				}

				if (random.nextBoolean()) {
					return new ItemStack(AetherItems.phoenix_leggings);
				}

				if (random.nextBoolean()) {
					return new ItemStack(AetherItems.phoenix_chestplate);
				}

				break;
			}
			case 4: {
				if (random.nextBoolean()) {
					return new ItemStack(AetherItems.phoenix_boots);
				}

				return new ItemStack(AetherItems.phoenix_gloves);
			}
			case 5: {
				return new ItemStack(AetherItems.life_shard);
			}
			case 6: {
				if (random.nextBoolean()) {
					return new ItemStack(AetherItems.gravitite_helmet);
				}

				if (random.nextBoolean()) {
					return new ItemStack(AetherItems.gravitite_leggings);
				}

				if (random.nextBoolean()) {
					return new ItemStack(AetherItems.gravitite_chestplate);
				}

				break;
			}
			case 7: {
				if (random.nextBoolean()) {
					return new ItemStack(AetherItems.gravitite_boots);
				}

				return new ItemStack(AetherItems.gravitite_gloves);
			}

			case 8:
				return new ItemStack(AetherItems.chain_gloves);

		}

		return new ItemStack(AetherItems.obsidian_chestplate);
	}

}
