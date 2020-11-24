package com.gildedgames.the_aether.client.renders.entity;

import com.gildedgames.the_aether.entities.passive.mountable.ParachuteEntity;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ParachuteRenderer extends Render {

	private final RenderBlocks renderBlocks = new RenderBlocks();

	public ParachuteRenderer() {
		super();
	}

	public void renderParachute(ParachuteEntity parachute, double x, double y, double z, float f, float ticks) {
		bindTexture(TextureMap.locationBlocksTexture);
		int meta = parachute.isGoldenParachute ? 2 : 0;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y + 0.5F, (float)z);
		bindEntityTexture(parachute);
		this.renderBlocks.renderBlockAsItem(AetherBlocks.aercloud, meta, parachute.getBrightness(ticks));
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float f1, float f2) {
		renderParachute((ParachuteEntity)entity, x, y, z, f1, f2);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TextureMap.locationBlocksTexture;
	}

}
