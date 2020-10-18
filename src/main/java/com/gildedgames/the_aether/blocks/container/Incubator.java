package com.gildedgames.the_aether.blocks.container;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.network.AetherGuiHandler;
import com.gildedgames.the_aether.tileentity.IncubatorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;

public class Incubator extends BlockAetherContainer {

	@SideOnly(Side.CLIENT)
	private IIcon blockIconTop;

	@SideOnly(Side.CLIENT)
	private IIcon blockIconBottom;

	public Incubator() {
		super(Material.rock);

		this.setHardness(2.0F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister registry) {
		this.blockIcon = registry.registerIcon(Aether.find("incubator_side"));
		this.blockIconTop = registry.registerIcon(Aether.find("incubator_top"));
		this.blockIconBottom = registry.registerIcon(Aether.find("enchanter_bottom"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? this.blockIconTop : (side == 0 ? this.blockIconBottom : this.blockIcon);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		if (side == 1) {
			return this.blockIconTop;
		} else if (side == 0) {
			return this.blockIconBottom;
		}

		return this.blockIcon;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new IncubatorTileEntity();
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		return Item.getItemFromBlock(BlocksAether.incubator);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (world.getBlockMetadata(x, y, z) == 1) {
			float f = (float) x + 0.5F;
			float f1 = (float) y + 1.0F + (random.nextFloat() * 6F) / 16F;
			float f2 = (float) z + 0.5F;

			world.spawnParticle("smoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", f, f1, f2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		player.openGui(Aether.instance, AetherGuiHandler.incubator, world, x, y, z);

		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		TileEntity tileentity = world.getTileEntity(x, y, z);

		if (tileentity instanceof IncubatorTileEntity) {
			IncubatorTileEntity tile = (IncubatorTileEntity)tileentity;

			for (int i1 = 0; i1 < tile.getSizeInventory(); ++i1) {
				ItemStack itemstack = tile.getStackInSlot(i1);

				if (itemstack != null) {
					float f = world.rand.nextFloat() * 0.8F + 0.1F;
					float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
					float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

					while (itemstack.stackSize > 0) {
						int j1 = world.rand.nextInt(21) + 10;

						if (j1 > itemstack.stackSize) {
							j1 = itemstack.stackSize;
						}

						itemstack.stackSize -= j1;
						EntityItem item_entity = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

						if (itemstack.hasTagCompound()) {
							item_entity.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
						}

						float f3 = 0.05F;
						item_entity.motionX = (double) ((float)world.rand.nextGaussian() * f3);
						item_entity.motionY = (double) ((float)world.rand.nextGaussian() * f3 + 0.2F);
						item_entity.motionZ = (double) ((float)world.rand.nextGaussian() * f3);
						world.spawnEntityInWorld(item_entity);
					}
				}
			}

			world.func_147453_f(x, y, z, this);
		}

		super.breakBlock(world, x, y, z, block, meta);
	}

}
