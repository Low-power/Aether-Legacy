package com.gildedgames.the_aether.network;

import com.gildedgames.the_aether.client.gui.EnchanterGui;
import com.gildedgames.the_aether.client.gui.FreezerGui;
import com.gildedgames.the_aether.client.gui.IncubatorGui;
import com.gildedgames.the_aether.client.gui.LoreGui;
import com.gildedgames.the_aether.client.gui.GuiTreasureChest;
import com.gildedgames.the_aether.inventory.ContainerAccessories;
import com.gildedgames.the_aether.inventory.EnchanterContainer;
import com.gildedgames.the_aether.inventory.FreezerContainer;
import com.gildedgames.the_aether.inventory.IncubatorContainer;
import com.gildedgames.the_aether.inventory.LoreContainer;
import com.gildedgames.the_aether.client.gui.inventory.AccessoriesGui;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.tileentity.EnchanterTileEntity;
import com.gildedgames.the_aether.tileentity.FreezerTileEntity;
import com.gildedgames.the_aether.tileentity.IncubatorTileEntity;
import com.gildedgames.the_aether.tileentity.TreasureChestTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AetherGuiHandler implements IGuiHandler {

	public static final int accessories = 1, enchanter = 2, freezer = 3, incubator = 4, treasure_chest = 5, lore = 6;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch(id) {
			case accessories:
				return new ContainerAccessories(PlayerAether.get(player).getAccessoryInventory(), player);
			case enchanter:
				return new EnchanterContainer(player.inventory, (EnchanterTileEntity)world.getTileEntity(x, y, z));
			case freezer:
				return new FreezerContainer(player.inventory, (FreezerTileEntity)world.getTileEntity(x, y, z));
			case incubator:
				return new IncubatorContainer(player, player.inventory, (IncubatorTileEntity)world.getTileEntity(x, y, z));
			case treasure_chest:
				return new ContainerChest(player.inventory, (IInventory) world.getTileEntity(x, y, z));
			case lore:
				return new LoreContainer(player.inventory);
			default:
				return null;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch(id) {
			case accessories:
				return new AccessoriesGui(PlayerAether.get(player));
			case enchanter:
				return new EnchanterGui(player.inventory, (EnchanterTileEntity)world.getTileEntity(x, y, z));
			case freezer:
				return new FreezerGui(player.inventory, (FreezerTileEntity)world.getTileEntity(x, y, z));
			case incubator:
				return new IncubatorGui(player, player.inventory, (IncubatorTileEntity)world.getTileEntity(x, y, z));
			case treasure_chest:
				return new GuiTreasureChest(player.inventory, (TreasureChestTileEntity)world.getTileEntity(x, y, z));
			case lore:
				return new LoreGui(player.inventory);
			default:
				return null;
		}
	}

}
