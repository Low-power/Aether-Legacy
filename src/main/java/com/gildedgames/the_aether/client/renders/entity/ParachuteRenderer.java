package com.gildedgames.the_aether.client.renders.entity;

import com.gildedgames.the_aether.entities.passive.mountable.ParachuteEntity;
import com.gildedgames.the_aether.blocks.BlocksAether;
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

	public void renderParachute(ParachuteEntity parachute, double d, double d1, double d2, float f, float f1) {
		this.bindTexture(TextureMap.locationBlocksTexture);

		int meta = parachute.isGoldenParachute ? 2 : 0;

		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1 + 0.5F, (float) d2);
		this.bindEntityTexture(parachute);
		this.renderBlocks.renderBlockAsItem(BlocksAether.aercloud, meta, parachute.getBrightness(f1));
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.renderParachute((ParachuteEntity) entity, d, d1, d2, f, f1);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TextureMap.locationBlocksTexture;
	}

}
