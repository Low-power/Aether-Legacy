package com.gildedgames.the_aether.client.renders.entity;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.client.models.entities.AerbunnyModel;
import com.gildedgames.the_aether.entities.passive.mountable.Aerbunny;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class AerbunnyRenderer extends RenderLiving {

	private static final ResourceLocation TEXTURE = Aether.locate("textures/entities/aerbunny/aerbunny.png");

	public AerbunnyModel model;

	public AerbunnyRenderer() {
		super(new AerbunnyModel(), 0.3F);
		this.model = (AerbunnyModel)this.mainModel;
	}

	protected void rotateAerbunny(Aerbunny bunny) {
		if (!bunny.isRiding()) {
			GL11.glTranslated(0D, 0.2D, 0D);
		} else if (bunny.ridingEntity == Minecraft.getMinecraft().thePlayer) {
			GL11.glTranslated(0D, 1.7D, 0D);
		}

		if (!bunny.onGround) {
			if (bunny.motionY > 0.5D) {
				GL11.glRotatef(15F, -1F, 0F, 0F);
			} else if (bunny.motionY < -0.5D) {
				GL11.glRotatef(-15F, -1F, 0F, 0F);
			} else {
				GL11.glRotatef((float)(bunny.motionY * 30D), -1F, 0F, 0F);
			}
		}

		this.model.puffiness = (float)(bunny.ridingEntity != null ? bunny.getPuffinessClient() : bunny.getPuffiness()) / 10F;
	}

	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f) {
		this.rotateAerbunny((Aerbunny)entity);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TEXTURE;
	}

}
