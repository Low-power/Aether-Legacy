package com.gildedgames.the_aether.world;

import com.gildedgames.the_aether.world.util.AetherPortalPosition;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.blocks.BlocksAether;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AetherTeleporter extends Teleporter {
	public AetherTeleporter(boolean should_spawn_portal, WorldServer world) {
		super(world);

		this.should_spawn_portal = should_spawn_portal;
		this.world = world;
		this.random = new Random(world.getSeed());
		this.destination_coordinate_cache = new LongHashMap();
		this.destination_coordinate_keys = new ArrayList<Long>();
	}

	private final boolean should_spawn_portal;
	private final WorldServer world;
	private final Random random;
	private final LongHashMap destination_coordinate_cache;
	private final List<Long> destination_coordinate_keys;

	public void placeInPortal(Entity entity, double x, double y, double z, float rotationYaw) {
		if (!this.should_spawn_portal) {
			entity.setPosition(entity.posX, 256, entity.posZ);
			return;
		}

		if (!this.placeInExistingPortal(entity, x, y, z, rotationYaw)) {
			this.makePortal(entity);
			this.placeInExistingPortal(entity, x, y, z, rotationYaw);
		}
	}

	@Override
	public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float rotationYaw) {
		short short1 = 128;
		double d3 = -1D;
		int i = 0;
		int j = 0;
		int k = 0;
		int l = MathHelper.floor_double(entity.posX);
		int i1 = MathHelper.floor_double(entity.posZ);
		long j1 = ChunkCoordIntPair.chunkXZ2Int(l, i1);
		boolean flag = true;
		double d7;
		int l3;

		if (this.destination_coordinate_cache.containsItem(j1)) {
			AetherPortalPosition portal_pos = (AetherPortalPosition)this.destination_coordinate_cache.getValueByKey(j1);
			d3 = 0D;
			i = portal_pos.posX;
			j = portal_pos.posY;
			k = portal_pos.posZ;
			portal_pos.lastUpdateTime = this.world.getTotalWorldTime();
			flag = false;
		} else {
			for (l3 = l - short1; l3 <= l + short1; ++l3) {
				double d4 = (double)l3 + 0.5D - entity.posX;

				for (int l1 = i1 - short1; l1 <= i1 + short1; ++l1) {
					double d5 = (double)l1 + 0.5D - entity.posZ;

					for (int i2 = this.world.getActualHeight() - 1; i2 >= 0; --i2) {
						if (this.world.getBlock(l3, i2, l1) == BlocksAether.aether_portal) {
							while (this.world.getBlock(l3, i2 - 1, l1) == BlocksAether.aether_portal) {
								--i2;
							}

							d7 = (double)i2 + 0.5D - entity.posY;
							double d8 = d4 * d4 + d7 * d7 + d5 * d5;

							if (d3 < 0D || d8 < d3) {
								d3 = d8;
								i = l3;
								j = i2;
								k = l1;
							}
						}
					}
				}
			}
		}

		if (d3 >= 0D) {
			if (flag) {
				this.destination_coordinate_cache.add(j1, new AetherPortalPosition(i, j, k, this.world.getTotalWorldTime()));
				this.destination_coordinate_keys.add(Long.valueOf(j1));
			}

			double d11 = (double) i + 0.5D;
			double d6 = (double) j + 0.5D;
			d7 = (double) k + 0.5D;
			int i4 = -1;

			if (this.world.getBlock(i - 1, j, k) == BlocksAether.aether_portal) {
				i4 = 2;
			}

			if (this.world.getBlock(i + 1, j, k) == BlocksAether.aether_portal) {
				i4 = 0;
			}

			if (this.world.getBlock(i, j, k - 1) == BlocksAether.aether_portal) {
				i4 = 3;
			}

			if (this.world.getBlock(i, j, k + 1) == BlocksAether.aether_portal) {
				i4 = 1;
			}

			int j2 = entity.getTeleportDirection();

			if (entity instanceof EntityPlayer) {
				j2 = PlayerAether.get((EntityPlayer)entity).teleportDirection;
			}

			if (i4 > -1) {
				int k2 = Direction.rotateLeft[i4];
				int l2 = Direction.offsetX[i4];
				int i3 = Direction.offsetZ[i4];
				int j3 = Direction.offsetX[k2];
				int k3 = Direction.offsetZ[k2];
				boolean flag1 = !this.world.isAirBlock(i + l2 + j3, j, k + i3 + k3) || !this.world.isAirBlock(i + l2 + j3, j + 1, k + i3 + k3);
				boolean flag2 = !this.world.isAirBlock(i + l2, j, k + i3) || !this.world.isAirBlock(i + l2, j + 1, k + i3);

				if (flag1 && flag2) {
					i4 = Direction.rotateOpposite[i4];
					k2 = Direction.rotateOpposite[k2];
					l2 = Direction.offsetX[i4];
					i3 = Direction.offsetZ[i4];
					j3 = Direction.offsetX[k2];
					k3 = Direction.offsetZ[k2];
					l3 = i - j3;
					d11 -= (double) j3;
					int k1 = k - k3;
					d7 -= (double) k3;
					flag1 = !this.world.isAirBlock(l3 + l2 + j3, j, k1 + i3 + k3) || !this.world.isAirBlock(l3 + l2 + j3, j + 1, k1 + i3 + k3);
					flag2 = !this.world.isAirBlock(l3 + l2, j, k1 + i3) || !this.world.isAirBlock(l3 + l2, j + 1, k1 + i3);
				}

				float f1 = 0.5F;
				float f2 = 0.5F;

				if (!flag1 && flag2) {
					f1 = 1F;
				} else if (flag1 && !flag2) {
					f1 = 0F;
				} else if (flag1 && flag2) {
					f2 = 0F;
				}

				d11 += (double) ((float) j3 * f1 + f2 * (float) l2);
				d7 += (double) ((float) k3 * f1 + f2 * (float) i3);
				float f3 = 0F;
				float f4 = 0F;
				float f5 = 0F;
				float f6 = 0F;

				if (i4 == j2) {
					f3 = 1F;
					f4 = 1F;
				} else if (i4 == Direction.rotateOpposite[j2]) {
					f3 = -1F;
					f4 = -1F;
				} else if (i4 == Direction.rotateRight[j2]) {
					f5 = 1F;
					f6 = -1F;
				} else {
					f5 = -1F;
					f6 = 1F;
				}

				double d9 = entity.motionX;
				double d10 = entity.motionZ;
				entity.motionX = d9 * (double)f3 + d10 * (double)f6;
				entity.motionZ = d9 * (double)f5 + d10 * (double)f4;
				entity.rotationYaw = rotationYaw - (float)(j2 * 90) + (float)(i4 * 90);
			} else {
				entity.motionX = entity.motionY = entity.motionZ = 0D;
			}

			entity.setLocationAndAngles(d11, d6, d7, entity.rotationYaw, entity.rotationPitch);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean makePortal(Entity entity) {
		byte b0 = 16;
		double d0 = -1D;
		int i = MathHelper.floor_double(entity.posX);
		int j = MathHelper.floor_double(entity.posY);
		int k = MathHelper.floor_double(entity.posZ);
		int l = i;
		int i1 = j;
		int j1 = k;
		int k1 = 0;
		int l1 = this.random.nextInt(4);
		int i2;
		double d1;
		int k2;
		double d2;
		int i3;
		int j3;
		int k3;
		int l3;
		int i4;
		int j4;
		int k4;
		int l4;
		int i5;
		double d3;
		double d4;

		for (i2 = i - b0; i2 <= i + b0; ++i2) {
			d1 = (double)i2 + 0.5D - entity.posX;

			for (k2 = k - b0; k2 <= k + b0; ++k2) {
				d2 = (double)k2 + 0.5D - entity.posZ;
				label274:

				for (i3 = this.world.getActualHeight() - 1; i3 >= 0; --i3) {
					if (this.world.isAirBlock(i2, i3, k2)) {
						while (i3 > 0 && this.world.isAirBlock(i2, i3 - 1, k2)) {
							--i3;
						}

						for (j3 = l1; j3 < l1 + 4; ++j3) {
							k3 = j3 % 2;
							l3 = 1 - k3;

							if (j3 % 4 >= 2) {
								k3 = -k3;
								l3 = -l3;
							}

							for (i4 = 0; i4 < 3; ++i4) {
								for (j4 = 0; j4 < 4; ++j4) {
									for (k4 = -1; k4 < 4; ++k4) {
										l4 = i2 + (j4 - 1) * k3 + i4 * l3;
										i5 = i3 + k4;
										int j5 = k2 + (j4 - 1) * l3 - i4 * k3;

										if (k4 < 0 && !this.world.getBlock(l4, i5, j5).getMaterial().isSolid() || k4 >= 0 && !this.world.isAirBlock(l4, i5, j5)) {
											continue label274;
										}
									}
								}
							}

							d3 = (double)i3 + 0.5D - entity.posY;
							d4 = d1 * d1 + d3 * d3 + d2 * d2;

							if (d0 < 0D || d4 < d0) {
								d0 = d4;
								l = i2;
								i1 = i3;
								j1 = k2;
								k1 = j3 % 4;
							}
						}
					}
				}
			}
		}

		if (d0 < 0D) {
			for (i2 = i - b0; i2 <= i + b0; ++i2) {
				d1 = (double)i2 + 0.5D - entity.posX;

				for (k2 = k - b0; k2 <= k + b0; ++k2) {
					d2 = (double)k2 + 0.5D - entity.posZ;
					label222:

					for (i3 = this.world.getActualHeight() - 1; i3 >= 0; --i3) {
						if (this.world.isAirBlock(i2, i3, k2)) {
							while (i3 > 0 && this.world.isAirBlock(i2, i3 - 1, k2)) {
								--i3;
							}

							for (j3 = l1; j3 < l1 + 2; ++j3) {
								k3 = j3 % 2;
								l3 = 1 - k3;

								for (i4 = 0; i4 < 4; ++i4) {
									for (j4 = -1; j4 < 4; ++j4) {
										k4 = i2 + (i4 - 1) * k3;
										l4 = i3 + j4;
										i5 = k2 + (i4 - 1) * l3;

										if (j4 < 0 && !this.world.getBlock(k4, l4, i5).getMaterial().isSolid() || j4 >= 0 && !this.world.isAirBlock(k4, l4, i5)) {
											continue label222;
										}
									}
								}

								d3 = (double)i3 + 0.5D - entity.posY;
								d4 = d1 * d1 + d3 * d3 + d2 * d2;

								if (d0 < 0D || d4 < d0) {
									d0 = d4;
									l = i2;
									i1 = i3;
									j1 = k2;
									k1 = j3 % 2;
								}
							}
						}
					}
				}
			}
		}

		int k5 = l;
		int j2 = i1;
		k2 = j1;
		int l5 = k1 % 2;
		int l2 = 1 - l5;

		if (k1 % 4 >= 2) {
			l5 = -l5;
			l2 = -l2;
		}

		boolean flag;

		if (d0 < 0D) {
			if (i1 < 70) {
				i1 = 70;
			}

			if (i1 > this.world.getActualHeight() - 10) {
				i1 = this.world.getActualHeight() - 10;
			}

			j2 = i1;

			for (i3 = -1; i3 <= 1; ++i3) {
				for (j3 = 1; j3 < 3; ++j3) {
					for (k3 = -1; k3 < 3; ++k3) {
						l3 = k5 + (j3 - 1) * l5 + i3 * l2;
						i4 = j2 + k3;
						j4 = k2 + (j3 - 1) * l2 - i3 * l5;
						flag = k3 < 0;
						this.world.setBlock(l3, i4, j4, flag ? Blocks.glowstone : Blocks.air);
					}
				}
			}
		}

		for (i3 = 0; i3 < 4; ++i3) {
			for (j3 = 0; j3 < 4; ++j3) {
				for (k3 = -1; k3 < 4; ++k3) {
					l3 = k5 + (j3 - 1) * l5;
					i4 = j2 + k3;
					j4 = k2 + (j3 - 1) * l2;
					flag = j3 == 0 || j3 == 3 || k3 == -1 || k3 == 3;
					this.world.setBlock(l3, i4, j4, (Block) (flag ? Blocks.glowstone : BlocksAether.aether_portal), 0, 2);
				}
			}

			for (j3 = 0; j3 < 4; ++j3) {
				for (k3 = -1; k3 < 4; ++k3) {
					l3 = k5 + (j3 - 1) * l5;
					i4 = j2 + k3;
					j4 = k2 + (j3 - 1) * l2;
					this.world.notifyBlocksOfNeighborChange(l3, i4, j4, this.world.getBlock(l3, i4, j4));
				}
			}
		}

		return true;
	}

	@Override
	public void removeStalePortalLocations(long world_time) {
		if(world_time % 100L != 0L) return;

		Iterator<Long> iterator = this.destination_coordinate_keys.iterator();
		long j = world_time - 600L;
		while (iterator.hasNext()) {
			Long pos = iterator.next();
			Teleporter.PortalPosition portal_pos = (Teleporter.PortalPosition)this.destination_coordinate_cache.getValueByKey(pos.longValue());
			if(portal_pos == null || portal_pos.lastUpdateTime < j) {
				iterator.remove();
				this.destination_coordinate_cache.remove(pos.longValue());
			}
		}
	}

}
