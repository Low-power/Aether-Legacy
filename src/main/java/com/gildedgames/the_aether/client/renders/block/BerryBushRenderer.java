package com.gildedgames.the_aether.client.renders.block;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.CommonProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BerryBushRenderer implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, -1F, 0F);
		renderer.drawCrossedSquares(AetherBlocks.berry_bush_stem.getIcon(metadata, 0), -0.5D, -0.5D, -0.5D, 1F);
		tessellator.draw();
		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		float f1 = 0.0625F;
		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, -1F, 0F);
		renderer.renderFaceYNeg(block, 0D, 0D, 0D, renderer.getBlockIconFromSide(block, 0));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, 1F, 0F);
		renderer.renderFaceYPos(block, 0D, 0D, 0D, renderer.getBlockIconFromSide(block, 1));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, 0F, -1F);
		tessellator.addTranslation(0F, 0F, f1);
		renderer.renderFaceZNeg(block, 0D, 0D, 0D, renderer.getBlockIconFromSide(block, 2));
		tessellator.addTranslation(0F, 0F, -f1);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, 0F, 1F);
		tessellator.addTranslation(0F, 0F, -f1);
		renderer.renderFaceZPos(block, 0D, 0D, 0D, renderer.getBlockIconFromSide(block, 3));
		tessellator.addTranslation(0F, 0F, f1);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0F, 0F);
		tessellator.addTranslation(f1, 0F, 0F);
		renderer.renderFaceXNeg(block, 0D, 0D, 0D, renderer.getBlockIconFromSide(block, 4));
		tessellator.addTranslation(-f1, 0F, 0F);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1F, 0F, 0F);
		tessellator.addTranslation(-f1, 0F, 0F);
		renderer.renderFaceXPos(block, 0D, 0D, 0D, renderer.getBlockIconFromSide(block, 5));
		tessellator.addTranslation(f1, 0F, 0F);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		renderer.renderCrossedSquares(AetherBlocks.berry_bush_stem, x, y, z);
		renderer.renderStandardBlock(block, x, y, z);

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return CommonProxy.berryBushRenderID;
	}

}
