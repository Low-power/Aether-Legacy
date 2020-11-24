package com.gildedgames.the_aether.tileentity;

import cpw.mods.fml.common.registry.GameRegistry;

public class AetherTileEntities {

	public static void initialization() {
		GameRegistry.registerTileEntity(EnchanterTileEntity.class, "enchanter");
		GameRegistry.registerTileEntity(FreezerTileEntity.class, "freezer");
		GameRegistry.registerTileEntity(IncubatorTileEntity.class, "incubator");
		GameRegistry.registerTileEntity(TreasureChestTileEntity.class, "treasure_chest");
		GameRegistry.registerTileEntity(TileEntityChestMimic.class, "chest_mimic");
	}

}