package com.gildedgames.the_aether.client.renders.entity;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.entities.projectile.HammerProjectileEntity;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class HammerProjectileRenderer extends Render {

	public HammerProjectileRenderer() {
		super();

		this.shadowSize = 0F;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return Aether.locate("textures/entities/projectile/notch_wave.png");
	}

	public void doRenderNotchWave(HammerProjectileEntity notchwave, double x, double y, double z, float yaw, float ticks) {
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslated(x, y, z);

		bindTexture(getEntityTexture(notchwave));

		Tessellator tessellator = Tessellator.instance;

		GL11.glRotatef(180F - this.renderManager.playerViewY, 0F, 1F, 0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1F, 0F, 0F);

		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(-0.5F, -0.25F, 0F, 0F, 0F);
		tessellator.setNormal(0F, 1F, 0F);
		tessellator.addVertexWithUV(0.5F, -0.25F, 0F, 0F, 1F);
		tessellator.setNormal(0F, 1F, 0F);
		tessellator.addVertexWithUV(0.5F, 0.75F, 0F, 1F, 1F);
		tessellator.setNormal(0F, 1F, 0F);
		tessellator.addVertexWithUV(-0.5F, 0.75F, 0F, 1F, 0F);
		tessellator.setNormal(0F, 1F, 0F);
		tessellator.draw();

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float ticks) {
		doRenderNotchWave((HammerProjectileEntity)entity, x, y, z, yaw, ticks);
	}
}
