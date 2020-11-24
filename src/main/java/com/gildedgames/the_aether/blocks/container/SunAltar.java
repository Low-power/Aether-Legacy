package com.gildedgames.the_aether.blocks.container;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.world.AetherWorldProvider;
import com.gildedgames.the_aether.Aether;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.command.ICommand;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;

public class SunAltar extends Block {

	@SideOnly(Side.CLIENT)
	private IIcon block_top_icon;

	public SunAltar() {
		super(Material.rock);

		this.setHardness(2.5F);
		this.setStepSound(soundTypeMetal);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister registry) {
		this.blockIcon = registry.registerIcon(Aether.find("sun_altar_side"));
		this.block_top_icon = registry.registerIcon(Aether.find("sun_altar_top"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		switch(side) {
			case 1:
				return this.block_top_icon;
			case 0:
				return AetherBlocks.hellfire_stone.getBlockTextureFromSide(side);
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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.provider instanceof AetherWorldProvider) {
			AetherWorldProvider provider = (AetherWorldProvider)world.provider;
			if (provider.getIsEternalDay() && provider.getShouldCycleCatchup()) {
				Aether.proxy.openSunAltar();
			} else if (!provider.getIsEternalDay()) {
				if (world.isRemote) {
					player.addChatComponentMessage(new ChatComponentText(I18n.format("gui.sun_altar.eternal_day")));
				}
			} else if (!provider.getShouldCycleCatchup()) {
				if (world.isRemote) {
					player.addChatComponentMessage(new ChatComponentText(I18n.format("gui.sun_altar.cycle_catchup")));
				}
			}
		} else if(world.isRemote) {
			player.addChatComponentMessage(new ChatComponentText(I18n.format("gui.sun_altar.message")));
		}

		return true;
	}

}
