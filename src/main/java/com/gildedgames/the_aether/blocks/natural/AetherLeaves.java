package com.gildedgames.the_aether.blocks.natural;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.entities.particles.ParticleCrystalLeaves;
import com.gildedgames.the_aether.entities.particles.ParticleGoldenOakLeaves;
import com.gildedgames.the_aether.entities.particles.ParticleHolidayLeaves;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockLeaves;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.Random;

public class AetherLeaves extends BlockLeaves {

	@SideOnly(Side.CLIENT)
	private IIcon fastIcon;

	@SideOnly(Side.CLIENT)
	private IIcon fancyIcon;

	public AetherLeaves() {
		super();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		return 16777215;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta) {
		return 16777215;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		return 16777215;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		super.randomDisplayTick(world, x, y, z, rand);
		if (!world.isRemote) {
			return;
		}

		if (Minecraft.getMinecraft().gameSettings.particleSetting == 2) {
			return;
		}

		if(this == AetherBlocks.golden_oak_leaves) {
			for (int ammount = 0; ammount < 2; ammount++) {
				double d = x + (rand.nextFloat() - 0.5D) * 10;
				double d1 = y + (rand.nextFloat() - 0.5D) * 10;
				double d2 = z + (rand.nextFloat() - 0.5D) * 10;
				double d3 = (rand.nextFloat() - 0.5D) * 0.5D;
				double d4 = (rand.nextFloat() - 0.5D) * 0.5D;
				double d5 = (rand.nextFloat() - 0.5D) * 0.5D;

				ParticleGoldenOakLeaves obj = new ParticleGoldenOakLeaves(world, d, d1, d2, d3, d4, d5);
				FMLClientHandler.instance().getClient().effectRenderer.addEffect(obj);
			}
		}

		if(this == AetherBlocks.holiday_leaves || this == AetherBlocks.decorated_holiday_leaves) {
			if (rand.nextInt(5) == 0) {
				for (int l = 0; l < 6; ++l) {
					double d = (double)x + ((double)rand.nextFloat() - 0.5D) * 8D;
					double d1 = (double)y + ((double)rand.nextFloat() - 0.5D) * 8D;
					double d2 = (double)z + ((double)rand.nextFloat() - 0.5D) * 8D;
					double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
					double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
					double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;

					ParticleHolidayLeaves particle = new ParticleHolidayLeaves(world, d, d1, d2, d3, d4, d5);
					FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
				}
			}
		}

		if(this == AetherBlocks.crystal_leaves || this == AetherBlocks.crystal_fruit_leaves) {
			if (rand.nextInt(5) == 0) {
				for (int l = 0; l < 6; ++l) {
					double d = (double)x + ((double)rand.nextFloat() - 0.5D) * 6D;
					double d1 = (double)y + ((double)rand.nextFloat() - 0.5D) * 6D;
					double d2 = (double)z + ((double)rand.nextFloat() - 0.5D) * 6D;
					double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
					double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
					double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;

					ParticleCrystalLeaves particle = new ParticleCrystalLeaves(world, d, d1, d2, d3, d4, d5);
					FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
				}
			}
		}
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return this == AetherBlocks.skyroot_leaves ? Item.getItemFromBlock(AetherBlocks.skyroot_sapling) : this == AetherBlocks.golden_oak_leaves ? Item.getItemFromBlock(AetherBlocks.golden_oak_sapling) : null;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> drops = super.getDrops(world, x, y, z, metadata, fortune);

		if(this == AetherBlocks.crystal_fruit_leaves) {
			drops.add(new ItemStack(AetherItems.white_apple));
		}

		return drops;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		this.fancyIcon = p_149651_1_.registerIcon(this.getTextureName());
		this.fastIcon = p_149651_1_.registerIcon(this.getTextureName() + "_opaque");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return Minecraft.getMinecraft().gameSettings.fancyGraphics ? fancyIcon : fastIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		return true;
	}

	@Override
	public String[] func_150125_e() {
		return new String[]{this.getUnlocalizedName()};
	}

}
