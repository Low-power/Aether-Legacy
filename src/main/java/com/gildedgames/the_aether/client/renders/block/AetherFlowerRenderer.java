package com.gildedgames.the_aether.client.renders.block;

import com.gildedgames.the_aether.blocks.natural.AetherFlower;
import com.gildedgames.the_aether.CommonProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

public class AetherFlowerRenderer implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, -1F, 0F);
		IIcon iicon = renderer.getBlockIconFromSideAndMetadata(block, 0, metadata);
		renderer.drawCrossedSquares(iicon, -0.5D, -0.5D, -0.5D, 1F);
		tessellator.draw();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
		int l = block.colorMultiplier(world, x, y, z);
		float f = (float)(l >> 16 & 255) / 255F;
		float f1 = (float)(l >> 8 & 255) / 255F;
		float f2 = (float)(l & 255) / 255F;

		if (EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30F + f1 * 59F + f2 * 11F) / 100F;
			float f4 = (f * 30F + f1 * 70F) / 100F;
			float f5 = (f * 30F + f2 * 70F) / 100F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		tessellator.setColorOpaque_F(f, f1, f2);
		double d1 = (double) x;
		double d2 = (double) y;
		double d0 = (double) z;
		long i1;

		if (block instanceof AetherFlower) {
			i1 = (long) (x * 3129871) ^ (long) z * 116129781L ^ (long) y;
			i1 = i1 * i1 * 42317861L + i1 * 11L;
			d1 += ((double)((float) (i1 >> 16 & 15L) / 15F) - 0.5D) * 0.3D;
			d0 += ((double)((float) (i1 >> 24 & 15L) / 15F) - 0.5D) * 0.3D;
		}

		IIcon iicon = renderer.getBlockIconFromSideAndMetadata(block, 0, world.getBlockMetadata(x, y, z));

		renderer.drawCrossedSquares(iicon, d1, d2, d0, 1F);

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return CommonProxy.aetherFlowerRenderID;
	}

}
