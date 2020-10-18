package com.gildedgames.the_aether.client.renders;

import com.gildedgames.the_aether.blocks.dungeon.TreasureChest;
import com.gildedgames.the_aether.tileentity.TreasureChestTileEntity;
import com.gildedgames.the_aether.Aether;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TreasureChestRenderer extends TileEntitySpecialRenderer {

	private static final ResourceLocation TEXTURE_DOUBLE = Aether.locate("textures/tile_entities/treasure_chest_large.png");

	private static final ResourceLocation TEXTURE_SINGLE = Aether.locate("textures/tile_entities/treasure_chest.png");

	private final ModelChest chestModel = new ModelChest();

	private final ModelChest largeChestModel = new ModelLargeChest();

	@Override
	public void renderTileEntityAt(TileEntity par1TileEntityChest, double posX, double posY, double posZ, float partialTicks) {
		int var9;

		if (par1TileEntityChest == null) {
			TileEntityRendererDispatcher.instance.renderTileEntityAt(new TreasureChestTileEntity(), 0D, 0D, 0D, 0F);
			return;
		}

		if (!par1TileEntityChest.hasWorldObj()) {
			var9 = 0;
		} else {
			Block var10 = par1TileEntityChest.getBlockType();
			var9 = par1TileEntityChest.getBlockMetadata();

			if (var10 != null && var10 instanceof TreasureChest) {
				((TreasureChest)var10).func_149954_e(par1TileEntityChest.getWorldObj(), par1TileEntityChest.xCoord, par1TileEntityChest.yCoord, par1TileEntityChest.zCoord);
				var9 = par1TileEntityChest.getBlockMetadata();
			}

			((TreasureChestTileEntity)par1TileEntityChest).checkForAdjacentChests();
		}

		if (((TreasureChestTileEntity)par1TileEntityChest).adjacentChestZNeg == null && ((TreasureChestTileEntity)par1TileEntityChest).adjacentChestXNeg == null) {
			ModelChest var14;

			if (((TreasureChestTileEntity)par1TileEntityChest).adjacentChestXPos == null && ((TreasureChestTileEntity)par1TileEntityChest).adjacentChestZPos == null) {
				var14 = this.chestModel;
				this.bindTexture(TEXTURE_SINGLE);
			} else {
				var14 = this.largeChestModel;
				this.bindTexture(TEXTURE_DOUBLE);
			}

			GL11.glPushMatrix();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef((float) posX, (float) posY + 1F, (float) posZ + 1F);
			GL11.glScalef(1F, -1F, -1F);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			short var11 = 0;

			if (var9 == 2) {
				var11 = 180;
			}

			if (var9 == 3) {
				var11 = 0;
			}

			if (var9 == 4) {
				var11 = 90;
			}

			if (var9 == 5) {
				var11 = -90;
			}

			if (var9 == 2 && ((TreasureChestTileEntity)par1TileEntityChest).adjacentChestXPos != null) {
				GL11.glTranslatef(1F, 0F, 0F);
			}

			if (var9 == 5 && ((TreasureChestTileEntity)par1TileEntityChest).adjacentChestZPos != null) {
				GL11.glTranslatef(0F, 0F, -1F);
			}

			GL11.glRotatef(var11, 0F, 1F, 0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			float var12 = ((TreasureChestTileEntity)par1TileEntityChest).prevLidAngle + (((TreasureChestTileEntity)par1TileEntityChest).lidAngle - ((TreasureChestTileEntity)par1TileEntityChest).prevLidAngle) * partialTicks;
			float var13;

			if (((TreasureChestTileEntity)par1TileEntityChest).adjacentChestZNeg != null) {
				var13 = ((TreasureChestTileEntity)par1TileEntityChest).adjacentChestZNeg.prevLidAngle + (((TreasureChestTileEntity)par1TileEntityChest).adjacentChestZNeg.lidAngle - ((TreasureChestTileEntity)par1TileEntityChest).adjacentChestZNeg.prevLidAngle) * partialTicks;

				if (var13 > var12) {
					var12 = var13;
				}
			}

			if (((TreasureChestTileEntity)par1TileEntityChest).adjacentChestXNeg != null) {
				var13 = ((TreasureChestTileEntity)par1TileEntityChest).adjacentChestXNeg.prevLidAngle + (((TreasureChestTileEntity)par1TileEntityChest).adjacentChestXNeg.lidAngle - ((TreasureChestTileEntity)par1TileEntityChest).adjacentChestXNeg.prevLidAngle) * partialTicks;

				if (var13 > var12) {
					var12 = var13;
				}
			}

			var12 = 1F - var12;
			var12 = 1F - var12 * var12 * var12;
			var14.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 2F);
			var14.renderAll();
			GL11.glPopMatrix();
		}
	}

}
