package com.gildedgames.the_aether.blocks.portal;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;

public class AetherPortalSize {

	private final World world;
	private final int axis;
	public final int rightDir;
	public final int leftDir;
	public int portalBlockCount = 0;
	public ChunkCoordinates bottomLeft;
	public int height;
	public int width;

	public AetherPortalSize(World world, int x, int y, int z, int axis) {
		this.world = world;
		this.axis = axis;
		this.leftDir = BlockPortal.field_150001_a[axis][0];
		this.rightDir = BlockPortal.field_150001_a[axis][1];

		int orig_y = y;
		while(y > orig_y - 21 && y > 0 && is_empty_block(world.getBlock(x, y - 1, z))) y--;

		int i = get_distance_until_edge(x, y, z, this.leftDir) - 1;
		if (i >= 0) {
			this.bottomLeft = new ChunkCoordinates(x + i * Direction.offsetX[this.leftDir], y, z + i * Direction.offsetZ[this.leftDir]);
			this.width = get_distance_until_edge(this.bottomLeft.posX, this.bottomLeft.posY, this.bottomLeft.posZ, this.rightDir);
			if (this.width < 2 || this.width > 21) {
				this.bottomLeft = null;
				this.width = 0;
			}
		}

		if (this.bottomLeft != null) {
			this.height = calculate_portal_height();
		}
	}

	private int get_distance_until_edge(int x, int y, int z, int left_dir) {
		int j1 = Direction.offsetX[left_dir];
		int k1 = Direction.offsetZ[left_dir];
		int i1;
		Block block;

		for (i1 = 0; i1 < 22; ++i1) {
			block = this.world.getBlock(x + j1 * i1, y, z + k1 * i1);
			if(!is_empty_block(block)) break;

			block = this.world.getBlock(x + j1 * i1, y - 1, z + k1 * i1);
			if(block != Blocks.glowstone) break;
		}

		block = this.world.getBlock(x + j1 * i1, y, z + k1 * i1);
		return block == Blocks.glowstone ? i1 : 0;
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	private int calculate_portal_height() {
		label24:
		for (this.height = 0; this.height < 21; ++this.height) {
			for (int i = 0; i < this.width; ++i) {
				int k = this.bottomLeft.posX + i * Direction.offsetX[BlockPortal.field_150001_a[this.axis][1]];
				int l = this.bottomLeft.posZ + i * Direction.offsetZ[BlockPortal.field_150001_a[this.axis][1]];
				Block block = this.world.getBlock(k, this.bottomLeft.posY + this.height, l);
				if(!is_empty_block(block)) break label24;
				if(block == AetherBlocks.aether_portal) {
					++this.portalBlockCount;
				}

				if (i == 0) {
					block = this.world.getBlock(k + Direction.offsetX[BlockPortal.field_150001_a[this.axis][0]], this.bottomLeft.posY + this.height, l + Direction.offsetZ[BlockPortal.field_150001_a[this.axis][0]]);
					if (block != Blocks.glowstone) {
						break label24;
					}
				} else if (i == this.width - 1) {
					block = this.world.getBlock(k + Direction.offsetX[BlockPortal.field_150001_a[this.axis][1]], this.bottomLeft.posY + this.height, l + Direction.offsetZ[BlockPortal.field_150001_a[this.axis][1]]);
					if (block != Blocks.glowstone) {
						break label24;
					}
				}
			}
		}

		for (int j = 0; j < this.width; ++j) {
			int x = this.bottomLeft.posX + j * Direction.offsetX[BlockPortal.field_150001_a[this.axis][1]];
			int y = this.bottomLeft.posY + this.height;
			int z = this.bottomLeft.posZ + j * Direction.offsetZ[BlockPortal.field_150001_a[this.axis][1]];
			if (this.world.getBlock(x, y, z) != Blocks.glowstone) {
				this.height = 0;
				break;
			}
		}

		if (this.height <= 21 && this.height >= 3) {
			return this.height;
		} else {
			this.bottomLeft = null;
			this.width = 0;
			this.height = 0;
			return 0;
		}
	}

	private boolean is_empty_block(Block block) {
		return block.getMaterial() == Material.air || block == Blocks.fire || block == AetherBlocks.aether_portal;
	}

	public boolean isValid() {
		return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
	}

	public void placePortalBlocks() {
		for (int i = 0; i < this.width; i++) {
			int x = this.bottomLeft.posX + Direction.offsetX[this.rightDir] * i;
			int z = this.bottomLeft.posZ + Direction.offsetZ[this.rightDir] * i;
			for (int j = 0; j < this.height; j++) {
				int y = this.bottomLeft.posY + j;
				this.world.setBlock(x, y, z, AetherBlocks.aether_portal, this.axis, 2);
			}
		}
	}

}
