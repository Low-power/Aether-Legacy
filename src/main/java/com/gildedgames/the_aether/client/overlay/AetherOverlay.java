package com.gildedgames.the_aether.client.overlay;

import com.gildedgames.the_aether.api.player.util.IAetherBoss;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.entities.passive.mountable.Moa;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.Aether;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class AetherOverlay {

	private static final ResourceLocation TEXTURE_JUMPS = Aether.locate("textures/gui/jumps.png");

	private static final ResourceLocation TEXTURE_COOLDOWN_BAR = Aether.locate("textures/gui/cooldown_bar.png");

	private static final ResourceLocation TEXTURE_POISON_VIGNETTE = Aether.locate("textures/blur/poison_vignette.png");

	private static final ResourceLocation TEXTURE_CURE_VIGNETTE = Aether.locate("textures/blur/cure_vignette.png");

	public static void renderPoison(Minecraft mc) {
		PlayerAether playerAether = PlayerAether.get(mc.thePlayer);

		if (playerAether.poisonTime > 0) {
			ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			Tessellator tessellator = Tessellator.instance;

			float alpha = getPoisonAlpha((float) (playerAether.poisonTime % 50) / 50);
			int width = scaledresolution.getScaledWidth();
			int height = scaledresolution.getScaledHeight();

			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glColor4f(0.5F, 0.5F, 0.5F, alpha);

			mc.renderEngine.bindTexture(TEXTURE_POISON_VIGNETTE);

			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0D, (double)height, -90D, 0D, 1D);
			tessellator.addVertexWithUV((double)width, (double)height, -90D, 1D, 1D);
			tessellator.addVertexWithUV((double)width, 0D, -90D, 1D, 0D);
			tessellator.addVertexWithUV(0D, 0D, -90D, 0D, 0D);
			tessellator.draw();

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glPopMatrix();
		}
	}

	public static void renderCure(Minecraft mc) {
		PlayerAether playerAether = PlayerAether.get(mc.thePlayer);
		if (playerAether.isCured()) {
			ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			Tessellator tessellator = Tessellator.instance;

			float alpha = 0.5F;
			int width = scaledresolution.getScaledWidth();
			int height = scaledresolution.getScaledHeight();

			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glColor4f(0.5F, 0.5F, 0.5F, alpha);

			mc.renderEngine.bindTexture(TEXTURE_CURE_VIGNETTE);

			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0D, (double)height, -90D, 0D, 1D);
			tessellator.addVertexWithUV((double)width, (double)height, -90D, 1D, 1D);
			tessellator.addVertexWithUV((double)width, 0D, -90D, 1D, 0D);
			tessellator.addVertexWithUV(0D, 0D, -90D, 0D, 0D);
			tessellator.draw();

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glPopMatrix();
		}
	}

	public static void renderIronBubble(Minecraft mc) {
		ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

		int width = scaledresolution.getScaledWidth();
		int height = scaledresolution.getScaledHeight();

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		mc.renderEngine.bindTexture(Gui.icons);

		int bubbleAmount = PlayerAether.get(mc.thePlayer).getAccessoryInventory().getAccessoryCount(new ItemStack(AetherItems.iron_bubble));

		if (mc.playerController.shouldDrawHUD() && mc.thePlayer.isInWater() && mc.thePlayer.isInsideOfMaterial(Material.water)) {
			for (int i = 0; i < bubbleAmount; ++i) {
				drawTexturedModalRect((width / 2 - 8 * i) + 81, height - 49, 16, 18, 9, 9);
			}
		}

		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glPopMatrix();
	}

	public static void renderCooldown(Minecraft mc) {
		PlayerAether playerInfo = PlayerAether.get(mc.thePlayer);
		if (playerInfo.getHammerCooldown() != 0) {
			ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

			int cooldownRemaining = (int) ((float) (playerInfo.getHammerCooldown()) / (float) (playerInfo.getHammerMaxCooldown()) * 128F);
			int width = scaledresolution.getScaledWidth();

			mc.fontRenderer.drawStringWithShadow(playerInfo.getHammerName() + " Cooldown", (width / 2) - (mc.fontRenderer.getStringWidth(playerInfo.getHammerName() + " Cooldown") / 2), 32, 0xffffffff);

			GL11.glPushMatrix();

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glDisable(GL11.GL_ALPHA_TEST);

			mc.renderEngine.bindTexture(TEXTURE_COOLDOWN_BAR);

			drawTexturedModalRect(width / 2 - 64, 42, 0, 8, 128, 8);

			drawTexturedModalRect(width / 2 - 64, 42, 0, 0, cooldownRemaining, 8);

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glColor4f(1F, 1F, 1F, 1F);

			GL11.glPopMatrix();
		}
	}

	public static void renderJumps(Minecraft mc) {
		EntityPlayer player = mc.thePlayer;
		if (player == null || !(player.ridingEntity instanceof Moa)) {
			return;
		}

		ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

		Moa moa = (Moa)player.ridingEntity;

		int width = scaledresolution.getScaledWidth();

		GL11.glPushMatrix();

		mc.renderEngine.bindTexture(TEXTURE_JUMPS);

		GL11.glColor4f(1F, 1F, 1F, 1F);

		for (int jump = 0; jump < moa.getMaxJumps(); jump++) {
			int yPos = 18;
			int xPos = ((width / 2) + (jump * 8)) - (moa.getMaxJumps() * 8) / 2;

			if (jump < moa.getRemainingJumps()) {
				drawTexturedModalRect(xPos, yPos, 0, 0, 9, 11);
			} else {
				drawTexturedModalRect(xPos, yPos, 10, 0, 9, 11);
			}
		}

		GL11.glColor4f(1F, 1F, 1F, 1F);

		GL11.glPopMatrix();
	}

	public static void renderAetherPortal(float timeInPortal, ScaledResolution scaledRes) {
		if (timeInPortal < 1F) {
			timeInPortal = timeInPortal * timeInPortal;
			timeInPortal = timeInPortal * timeInPortal;
			timeInPortal = timeInPortal * 0.8F + 0.2F;
		}

		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1F, 1F, 1F, timeInPortal);
		IIcon icon = AetherBlocks.aether_portal.getBlockTextureFromSide(1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		float min_u = icon.getMinU();
		float min_v = icon.getMinV();
		float max_u = icon.getMaxU();
		float max_v = icon.getMaxV();
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0D, (double)scaledRes.getScaledHeight(), -90D, (double)min_u, (double)max_v);
		tessellator.addVertexWithUV((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), -90D, (double)max_u, (double)max_v);
		tessellator.addVertexWithUV((double)scaledRes.getScaledWidth(), 0D, -90D, (double)max_u, (double)min_v);
		tessellator.addVertexWithUV(0D, 0D, -90D, (double)min_u, (double)min_v);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1F, 1F, 1F, 1F);
	}

	public static void renderBossHP(Minecraft mc) {
		PlayerAether player = PlayerAether.get(mc.thePlayer);
		IAetherBoss boss = (IAetherBoss) player.getFocusedBoss();
		if (player.getFocusedBoss() != null) {
			if (player.getFocusedBoss().getBossHealth() <= 0F) {
				player.setFocusedBoss(null);
				return;
			}
			String bossTitle = boss.getBossName();
			ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			int healthRemaining = (int) (boss.getBossHealth() / boss.getMaxBossHealth() * 256F);
			int width = scaledresolution.getScaledWidth();
			GL11.glPushMatrix();
			mc.fontRenderer.drawStringWithShadow(bossTitle, width / 2 - (mc.fontRenderer.getStringWidth(bossTitle) / 2), 2, 0xffffffff);
			mc.renderEngine.bindTexture(Aether.locate("textures/gui/boss_bar.png"));
			GL11.glColor4f(1F, 1F, 1F, 1F);
			drawTexturedModalRect(width / 2 - 128, 12, 0, 16, 256, 32);
			drawTexturedModalRect(width / 2 - 128, 12, 0, 0, healthRemaining, 16);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glPopMatrix();
		}
	}

	public static void drawTexturedModalRect(float x, float y, float u, float v, float width, float height) {
		float zLevel = -90F;

		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double) (x + 0), (double) (y + height), (double) zLevel, (double) ((float) (u + 0) * var7), (double) ((float) (v + height) * var8));
		tessellator.addVertexWithUV((double) (x + width), (double) (y + height), (double) zLevel, (double) ((float) (u + width) * var7), (double) ((float) (v + height) * var8));
		tessellator.addVertexWithUV((double) (x + width), (double) (y + 0), (double) zLevel, (double) ((float) (u + width) * var7), (double) ((float) (v + 0) * var8));
		tessellator.addVertexWithUV((double) (x + 0), (double) (y + 0), (double) zLevel, (double) ((float) (u + 0) * var7), (double) ((float) (v + 0) * var8));
		tessellator.draw();
	}

	public static float getPoisonAlpha(float f) {
		return (f * f) / 5F + 0.4F;
	}

	public static float getCureAlpha(float f) {
		return (f * f) / 10F + 0.4F;
	}

}
