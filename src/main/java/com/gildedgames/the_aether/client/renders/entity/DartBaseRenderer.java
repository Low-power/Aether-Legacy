package com.gildedgames.the_aether.client.renders.entity;

import com.gildedgames.the_aether.entities.projectile.darts.BaseDartEntity;
import com.gildedgames.the_aether.entities.projectile.darts.PoisonDartEntity;
import com.gildedgames.the_aether.entities.projectile.darts.EnchantedDartEntity;
import com.gildedgames.the_aether.entities.projectile.PoisonNeedleEntity;
import com.gildedgames.the_aether.Aether;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class DartBaseRenderer extends Render {

	public DartBaseRenderer() {
		super();

		this.shadowSize = 0F;
	}

	public void renderDart(BaseDartEntity dart, double d, double d1, double d2, float f, float f1) {
		if (dart.isInvisible() && dart.prevRotationYaw == 0F || dart.prevRotationPitch == 0F) {
			return;
		}

		this.bindEntityTexture(dart);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glRotatef(dart.prevRotationYaw + (dart.rotationYaw - dart.prevRotationYaw) * f1 - 90F, 0F, 1F, 0F);
		GL11.glRotatef(dart.prevRotationPitch + (dart.rotationPitch - dart.prevRotationPitch) * f1, 0F, 0F, 1F);
		Tessellator tessellator = Tessellator.instance;

		byte i = 1;
		float f2 = 0F;
		float f3 = 0.5F;
		float f4 = (float) (0 + i * 10) / 32F;
		float f5 = (float) (5 + i * 10) / 32F;
		float f6 = 0F;
		float f7 = 0.15625F;
		float f8 = (float) (5 + i * 10) / 32F;
		float f9 = (float) (10 + i * 10) / 32F;
		float f10 = 0.05625F;
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		float f11 = (float) dart.dartShake - f1;

		if (f11 > 0F) {
			float j = -MathHelper.sin(f11 * 3F) * f11;
			GL11.glRotatef(j, 0F, 0F, 1F);
		}

		GL11.glRotatef(45F, 1F, 0F, 0F);
		GL11.glScalef(f10, f10, f10);
		GL11.glTranslatef(-4F, 0F, 0F);
		GL11.glNormal3f(f10, 0F, 0F);

		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(-7D, -2D, -2D, (double) f6, (double) f8);
		tessellator.addVertexWithUV(-7D, -2D, 2D, (double) f7, (double) f8);
		tessellator.addVertexWithUV(-7D, 2D, 2D, (double) f7, (double) f9);
		tessellator.addVertexWithUV(-7D, 2D, -2D, (double) f6, (double) f9);
		tessellator.draw();

		GL11.glNormal3f(-f10, 0F, 0F);

		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(-7D, 2D, -2D, (double) f6, (double) f8);
		tessellator.addVertexWithUV(-7D, 2D, 2D, (double) f7, (double) f8);
		tessellator.addVertexWithUV(-7D, -2D, 2D, (double) f7, (double) f9);
		tessellator.addVertexWithUV(-7D, -2D, -2D, (double) f6, (double) f9);
		tessellator.draw();

		for (int var23 = 0; var23 < 5; ++var23) {
			GL11.glRotatef(72F, 1F, 0F, 0F);
			GL11.glNormal3f(0F, 0F, f10);

			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(-8D, -2D, 0D, (double) f2, (double) f4);
			tessellator.addVertexWithUV(8D, -2D, 0D, (double) f3, (double) f4);
			tessellator.addVertexWithUV(8D, 2D, 0D, (double) f3, (double) f5);
			tessellator.addVertexWithUV(-8D, 2D, 0D, (double) f2, (double) f5);
			tessellator.draw();
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.renderDart((BaseDartEntity)entity, d, d1, d2, f, f1);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		String base;
		if(entity instanceof EnchantedDartEntity) base = "enchanted";
		else if(entity instanceof PoisonDartEntity) base = "poison";
		else base = "golden";
		return Aether.locate("textures/entities/projectile/dart/" + base + (entity instanceof PoisonNeedleEntity ? "_needle" : "_dart") + ".png");
	}

}
