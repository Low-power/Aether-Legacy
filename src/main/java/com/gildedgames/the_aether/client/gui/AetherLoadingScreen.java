package com.gildedgames.the_aether.client.gui;

import com.gildedgames.the_aether.client.gui.trivia.AetherTrivia;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.Framebuffer;
import cpw.mods.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

public class AetherLoadingScreen extends LoadingScreenRenderer {
	public AetherLoadingScreen(Minecraft mc) {
		super(mc);

		this.mc = mc;
		this.time = Minecraft.getSystemTime();
		this.fb = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
		this.fb.setFramebufferFilter(9728);
	}

	private Minecraft mc;
	private String message = "";
	private String trivia = "";
	private long time;
	private Framebuffer fb;

	@Override
	public void resetProgressAndMessage(String message) {
		super.resetProgressAndMessage(message);

		this.trivia = AetherTrivia.getNewTrivia();
	}

	@Override
	public void displayProgressMessage(String message) {
		this.time = 0L;
		this.message = message;
		this.setLoadingProgress(-1);
		this.time = 0L;
	}

	@Override
	public void setLoadingProgress(int progress) {
		long j = Minecraft.getSystemTime();
		if (j - this.time >= 100L) {
			this.time = j;
			ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
			int scale_factor = scaledresolution.getScaleFactor();
			int width = scaledresolution.getScaledWidth();
			int height = scaledresolution.getScaledHeight();

			if (OpenGlHelper.isFramebufferEnabled()) {
				this.fb.framebufferClear();
			} else {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			}

			this.fb.bindFramebuffer(false);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0D, 100D, 300D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0F, 0F, -200F);

			if (!OpenGlHelper.isFramebufferEnabled()) {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			}

			if (!FMLClientHandler.instance().handleLoadingScreen(scaledresolution)) {
				Tessellator tessellator = Tessellator.instance;
				this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
				float f = 32F;
				tessellator.startDrawingQuads();
				tessellator.setColorOpaque_I(4210752);
				tessellator.addVertexWithUV(0D, (double)height, 0D, 0D, (double)((float)height / f));
				tessellator.addVertexWithUV((double)width, (double)height, 0D, (double) ((float)width / f), (double)((float)height / f));
				tessellator.addVertexWithUV((double)width, 0D, 0D, (double)((float)width / f), 0D);
				tessellator.addVertexWithUV(0D, 0D, 0D, 0D, 0D);
				tessellator.draw();

				if (progress >= 0) {
					byte b0 = 100;
					byte b1 = 2;
					int j1 = width / 2 - b0 / 2;
					int k1 = height / 2 + 16;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					tessellator.startDrawingQuads();
					tessellator.setColorOpaque_I(8421504);
					tessellator.addVertex((double)j1, (double)k1, 0D);
					tessellator.addVertex((double)j1, (double)(k1 + b1), 0D);
					tessellator.addVertex((double)(j1 + b0), (double)(k1 + b1), 0D);
					tessellator.addVertex((double)(j1 + b0), (double)k1, 0D);
					tessellator.setColorOpaque_I(8454016);
					tessellator.addVertex((double)j1, (double)k1, 0D);
					tessellator.addVertex((double)j1, (double)(k1 + b1), 0D);
					tessellator.addVertex((double)(j1 + progress), (double)(k1 + b1), 0D);
					tessellator.addVertex((double)(j1 + progress), (double)k1, 0D);
					tessellator.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				this.mc.fontRenderer.drawStringWithShadow(this.message, (width - this.mc.fontRenderer.getStringWidth(this.message)) / 2, height / 2 - 4 + 8, 0xffffff);
				this.mc.fontRenderer.drawStringWithShadow(this.trivia, (width - this.mc.fontRenderer.getStringWidth(this.trivia)) / 2, height - 16, 0xffff99);
			}

			this.fb.unbindFramebuffer();

			if (OpenGlHelper.isFramebufferEnabled()) {
				this.fb.framebufferRender(width * scale_factor, height * scale_factor);
			}

			this.mc.func_147120_f();

			try {
				Thread.yield();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
