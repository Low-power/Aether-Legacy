package com.gildedgames.the_aether.client.renders.entity;

import com.gildedgames.the_aether.entities.block.EntityTNTPresent;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TNTPresentRenderer extends Render {
	public TNTPresentRenderer() {
		this.shadowSize = 0.5F;
	}

	private RenderBlocks blockRenderer = new RenderBlocks();

	public void doRender(EntityTNTPresent tnt_present, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y + 0.48F, (float)z);

		if ((float)tnt_present.fuse - p_76986_9_ + 1F < 10F) {
			float f2 = 1F - ((float)tnt_present.fuse - p_76986_9_ + 1F) / 10F;
			if(f2 < 0F) f2 = 0F; else if(f2 > 1F) f2 = 1F;
			f2 *= f2;
			f2 *= f2;
			float f3 = 1F + f2 * 0.3F;
			GL11.glScalef(f3, f3, f3);
		}

		float f2 = (1F - ((float)tnt_present.fuse - p_76986_9_ + 1F) / 100F) * 0.8F;
		bindEntityTexture(tnt_present);
		this.blockRenderer.renderBlockAsItem(AetherBlocks.present, 0, tnt_present.getBrightness(p_76986_9_));

		if (tnt_present.fuse / 5 % 2 == 0) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
			GL11.glColor4f(1F, 1F, 1F, f2);
			this.blockRenderer.renderBlockAsItem(AetherBlocks.present, 0, 1F);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
		this.doRender((EntityTNTPresent)entity, x, y, z, p_76986_8_, p_76986_9_);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TextureMap.locationBlocksTexture;
	}
}
