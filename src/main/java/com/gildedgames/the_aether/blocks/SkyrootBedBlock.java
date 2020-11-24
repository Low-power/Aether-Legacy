package com.gildedgames.the_aether.blocks;

import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.player.PlayerAether;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockBed;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import java.util.List;
import java.util.Random;

public class SkyrootBedBlock extends BlockBed {
	public static final int[][] field_149981_a = new int[][] {{0, 1}, { -1, 0}, {0, -1}, {1, 0}};
	@SideOnly(Side.CLIENT)
	private IIcon[] field_149980_b;
	@SideOnly(Side.CLIENT)
	private IIcon[] field_149982_M;
	@SideOnly(Side.CLIENT)
	private IIcon[] field_149983_N;

	public SkyrootBedBlock() {
		super();
		this.func_149978_e();
		this.disableStats();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (world.isRemote) return true;

		int metadata = world.getBlockMetadata(x, y, z);
		if (!isBlockHeadOfBed(metadata)) {
			int j1 = getDirection(metadata);
			x += field_149981_a[j1][0];
			z += field_149981_a[j1][1];

			if (world.getBlock(x, y, z) != this) {
				return true;
			}

			metadata = world.getBlockMetadata(x, y, z);
		}

		if (player.dimension == AetherConfig.get_aether_world_id() || player.dimension == 0) {
			if (func_149976_c(metadata)) {
				EntityPlayer sleeping_player = null;
				for(EntityPlayer p : (List<EntityPlayer>)world.playerEntities) {
					if(!p.isPlayerSleeping()) continue;
					ChunkCoordinates chunk_coordinates = p.playerLocation;
					if(chunk_coordinates.posX == x && chunk_coordinates.posY == y && chunk_coordinates.posZ == z) {
						sleeping_player = p;
						break;
					}
				}
				if(sleeping_player != null) {
					player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied", new Object[0]));
					return true;
				}
				func_149979_a(world, x, y, z, false);
			}

			EntityPlayer.EnumStatus status = player.sleepInBedAt(x, y, z);
			switch(status) {
				case OK:
					func_149979_a(world, x, y, z, true);
					break;
				case NOT_POSSIBLE_NOW:
					player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));

					if (player.dimension == AetherConfig.get_aether_world_id()) {
						player.addChatMessage(new ChatComponentTranslation("gui.skyroot_bed.respawn_point"));
						player.setSpawnChunk(new ChunkCoordinates(x, y, z), false, AetherConfig.get_aether_world_id());
						PlayerAether.get(player).setBedLocation(new ChunkCoordinates(x, y, z));
					}
					break;
				case NOT_SAFE:
					player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));
					break;

			}
			return true;
		} else {
			double px = (double)x + 0.5D;
			double py = (double)y + 0.5D;
			double pz = (double)z + 0.5D;
			world.setBlockToAir(x, y, z);
			int k1 = getDirection(metadata);
			x += field_149981_a[k1][0];
			z += field_149981_a[k1][1];

			if (world.getBlock(x, y, z) == this) {
				world.setBlockToAir(x, y, z);
				px = (px + (double)x + 0.5D) / 2D;
				py = (py + (double)y + 0.5D) / 2D;
				pz = (pz + (double)z + 0.5D) / 2D;
			}

			world.newExplosion((Entity)null, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), 5F, true, true);
			return true;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		if (p_149691_1_ == 0) {
			return AetherBlocks.skyroot_planks.getBlockTextureFromSide(p_149691_1_);
		} else {
			int k = getDirection(p_149691_2_);
			int l = Direction.bedDirection[k][p_149691_1_];
			int i1 = isBlockHeadOfBed(p_149691_2_) ? 1 : 0;
			return (i1 != 1 || l != 2) && (i1 != 0 || l != 3) ? (l != 5 && l != 4 ? this.field_149983_N[i1] : this.field_149982_M[i1]) : this.field_149980_b[i1];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.field_149983_N = new IIcon[] { register.registerIcon(this.getTextureName() + "_feet_top"), register.registerIcon(this.getTextureName() + "_head_top") };
		this.field_149980_b = new IIcon[] { register.registerIcon(this.getTextureName() + "_feet_end"), register.registerIcon(this.getTextureName() + "_head_end") };
		this.field_149982_M = new IIcon[] { register.registerIcon(this.getTextureName() + "_feet_side"), register.registerIcon(this.getTextureName() + "_head_side") };
	}

	private void func_149978_e() {
		this.setBlockBounds(0F, 0F, 0F, 1F, 0.5625F, 1F);
	}

	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return isBlockHeadOfBed(p_149650_1_) ? Item.getItemById(0) : AetherItems.skyroot_bed_item;
	}

	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return AetherItems.skyroot_bed_item;
	}

	@Override
	public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean isBedFoot(IBlockAccess world, int x, int y, int z) {
		return SkyrootBedBlock.isBlockHeadOfBed(world.getBlockMetadata(x,  y, z));
	}
}
