package com.gildedgames.the_aether.blocks.natural;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.items.util.DoubleDropHelper;
import com.gildedgames.the_aether.world.AetherWorld;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.EnumPlantType;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import java.util.Random;

public class AetherGrass extends Block implements IGrowable {

	@SideOnly(Side.CLIENT)
	private IIcon block_top_icon;

	@SideOnly(Side.CLIENT)
	private IIcon block_snowy_icon;

	public AetherGrass() {
		super(Material.grass);

		this.setHardness(0.2F);
		this.setTickRandomly(true);
		this.setStepSound(soundTypeGrass);
		this.setHarvestLevel("shovel", 0);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		if(world.isRemote) return;

		if (world.getBlockLightValue(x, y + 1, z) < 4 && world.getBlockLightOpacity(x, y + 1, z) > 2) {
			world.setBlock(x, y, z, AetherBlocks.aether_dirt);
		} else if (world.getBlockLightValue(x, y + 1, z) >= 9) {
			for (int i = 0; i < 4; i++) {
				int x1 = x + rand.nextInt(3) - 1;
				int y1 = y + rand.nextInt(5) - 3;
				int z1 = z + rand.nextInt(3) - 1;

				if(world.getBlock(x1, y1, z1) == AetherBlocks.aether_dirt && world.getBlockMetadata(x1, y1, z1) == 0 && world.getBlockLightValue(x1, y1 + 1, z1) >= 4 && world.getBlockLightOpacity(x1, y1 + 1, z1) <= 2) {
					world.setBlock(x1, y1, z1, AetherBlocks.aether_grass);
				}
			}
		}
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return Item.getItemFromBlock(AetherBlocks.aether_dirt);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
		DoubleDropHelper.drop_block(player, x, y, z, this, meta, null);
	}

	@Override
	public int damageDropped(int meta) {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister registry) {
		this.blockIcon = registry.registerIcon("aether_legacy:aether_grass_side");
		this.block_snowy_icon = registry.registerIcon("aether_legacy:aether_grass_side_snowy");
		this.block_top_icon = registry.registerIcon("aether_legacy:aether_grass_top");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		switch(side) {
			case 1:
				return this.block_top_icon;
			case 0:
				return AetherBlocks.aether_dirt.getBlockTextureFromSide(side);
			default:
				return this.blockIcon;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		switch(side) {
			case 1:
				return this.block_top_icon;
			case 0:
				return AetherBlocks.aether_dirt.getBlockTextureFromSide(side);
			default:
				Material material = world.getBlock(x, y + 1, z).getMaterial();
				return material == Material.snow || material == Material.craftedSnow ? this.block_snowy_icon : this.blockIcon;
		}
	}

	@Override
	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
		return plantable.getPlantType(world, x, y + 1, z) == EnumPlantType.Plains;
	}

	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean p_149851_5_) {
		return true;
	}

	@Override
	public boolean func_149852_a(World world, Random random, int x, int y, int z) {
		return true;
	}

	@Override
	public void func_149853_b(World world, Random random, int x, int y, int z) {
		int i = 0;
		while (i < 128) {
			int x1 = x;
			int y1 = y + 1;
			int z1 = z;
			int j = 0;
			while (true) {
				if (j < i / 16) {
					x1 += random.nextInt(3) - 1;
					y1 += (random.nextInt(3) - 1) * random.nextInt(3) / 2;
					z1 += random.nextInt(3) - 1;
					if(world.getBlock(x1, y1 - 1, z1) == AetherBlocks.aether_grass && !world.getBlock(x1, y1, z1).isNormalCube()) {
						j++;
						continue;
					}
				} else if (world.isAirBlock(x1, y1, z1)) {
					if (random.nextInt(8) != 0) {
						if (Blocks.tallgrass.canBlockStay(world, x1, y1, z1)) {
							world.setBlock(x1, y1, z1, Blocks.tallgrass, 1, 3);
						}
					} else if (random.nextInt(12) == 0) {
						if(AetherBlocks.berry_bush_stem.canBlockStay(world, x1, y1, z1)) {
							world.setBlock(x1, y1, z1, AetherBlocks.berry_bush_stem, 0, 3);
						}
					} else {
						AetherWorld.aether_biome.plantFlower(world, random, x1, y1, z1);
					}
				}
				break;
			}
			i++;
		}
	}

}
