package com.gildedgames.the_aether.blocks.container;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.network.AetherGuiHandler;
import com.gildedgames.the_aether.tileentity.EnchanterTileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import java.util.Random;

public class Enchanter extends BlockAetherContainer {

	@SideOnly(Side.CLIENT)
	private IIcon top_icon;
	@SideOnly(Side.CLIENT)
	private IIcon bottom_icon;

	public Enchanter() {
		super(Material.rock);

		this.setHardness(2F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister registry) {
		this.blockIcon = registry.registerIcon(Aether.find("enchanter_side"));
		this.top_icon = registry.registerIcon(Aether.find("enchanter_bottom"));
		this.bottom_icon = registry.registerIcon(Aether.find("enchanter_bottom"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		switch(side) {
			case 1:
				return this.top_icon;
			case 0:
				return this.bottom_icon;
			default:
				return this.blockIcon;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return getIcon(side, 0);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new EnchanterTileEntity();
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune_level) {
		return Item.getItemFromBlock(AetherBlocks.enchanter);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (world.getBlockMetadata(x, y, z) == 1) {
			float fx = (float)x + 0.5F;
			float fy = (float)y + 1F + (random.nextFloat() * 6F) / 16F;
			float fz = (float)z + 0.5F;

			world.spawnParticle("smoke", fx, fy, fz, 0D, 0D, 0D);
			world.spawnParticle("flame", fx, fy, fz, 0D, 0D, 0D);
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		player.openGui(Aether.instance, AetherGuiHandler.enchanter, world, x, y, z);

		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof EnchanterTileEntity) {
			EnchanterTileEntity enchanter_tile = (EnchanterTileEntity)tileentity;
			for (int i = 0; i < enchanter_tile.getSizeInventory(); i++) {
				ItemStack itemstack = enchanter_tile.getStackInSlot(i);
				if (itemstack != null) {
					float x_offset = world.rand.nextFloat() * 0.8F + 0.1F;
					float y_offset = world.rand.nextFloat() * 0.8F + 0.1F;
					float z_offset = world.rand.nextFloat() * 0.8F + 0.1F;

					while (itemstack.stackSize > 0) {
						int count = world.rand.nextInt(21) + 10;
						if(count > itemstack.stackSize) {
							count = itemstack.stackSize;
						}

						itemstack.stackSize -= count;
						EntityItem item_entity = new EntityItem(world,
							(double)((float)x + x_offset), (double)((float)y + y_offset), (double)((float)z + z_offset),
							new ItemStack(itemstack.getItem(), count, itemstack.getItemDamage()));
						if (itemstack.hasTagCompound()) {
							item_entity.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
						}

						float f3 = 0.05F;
						item_entity.motionX = (double)((float)world.rand.nextGaussian() * f3);
						item_entity.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
						item_entity.motionZ = (double)((float)world.rand.nextGaussian() * f3);
						world.spawnEntityInWorld(item_entity);
					}
				}
			}

			world.func_147453_f(x, y, z, this);
		}

		super.breakBlock(world, x, y, z, block, meta);
	}

}
