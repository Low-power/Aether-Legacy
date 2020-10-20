package com.gildedgames.the_aether.client.gui.menu;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;

public class AetherMainMenu extends GuiMainMenu {
	private static final Logger logger = LogManager.getLogger();
	private static final Random rand = new Random();
	private String splashText;
	private GuiButton buttonResetDemo;
	private int panoramaTimer;
	private DynamicTexture viewportTexture;
	private final Object field_104025_t = new Object();
	private String field_92025_p;
	private String field_146972_A;
	private String field_104024_v;
	private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
	private static final ResourceLocation aetherTitleTextures = new ResourceLocation("aether_legacy", "textures/gui/menu/aether.png");
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {new ResourceLocation("aether_legacy", "textures/gui/menu/panorama/panorama_0.png"), new ResourceLocation("aether_legacy", "textures/gui/menu/panorama/panorama_1.png"), new ResourceLocation("aether_legacy", "textures/gui/menu/panorama/panorama_2.png"), new ResourceLocation("aether_legacy", "textures/gui/menu/panorama/panorama_3.png"), new ResourceLocation("aether_legacy", "textures/gui/menu/panorama/panorama_4.png"), new ResourceLocation("aether_legacy", "textures/gui/menu/panorama/panorama_5.png")};
	public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
	private int field_92024_r;
	private int field_92023_s;
	private int field_92022_t;
	private int field_92021_u;
	private int field_92020_v;
	private int field_92019_w;
	private ResourceLocation field_110351_G;
	private GuiButton selectedButton;

	public AetherMainMenu() {
		this.field_146972_A = field_96138_a;
		this.splashText = "missingno";
		BufferedReader reader = null;

		try {
			ArrayList<String> splash_text_list = new ArrayList<String>();
			reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));
			String s;

			while ((s = reader.readLine()) != null) {
				s = s.trim();

				if (!s.isEmpty()) {
					splash_text_list.add(s);
				}
			}

			if (!splash_text_list.isEmpty()) {
				do {
					this.splashText = splash_text_list.get(rand.nextInt(splash_text_list.size()));
				}
				while (this.splashText.hashCode() == 125780783);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) try {
				reader.close();
			} catch (IOException e) {
				;
			}
		}

		this.field_92025_p = "";

		if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.func_153193_b()) {
			this.field_92025_p = I18n.format("title.oldgl1", new Object[0]);
			this.field_146972_A = I18n.format("title.oldgl2", new Object[0]);
			this.field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
		}
	}

	@Override
	public void updateScreen() {
		++this.panoramaTimer;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {}

	@Override
	public void initGui() {
		this.viewportTexture = new DynamicTexture(256, 256);
		this.field_110351_G = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		if (calendar.get(2) + 1 == 11 && calendar.get(5) == 9) {
			this.splashText = "Happy birthday, ez!";
		} else if (calendar.get(2) + 1 == 6 && calendar.get(5) == 1) {
			this.splashText = "Happy birthday, Notch!";
		} else if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
			this.splashText = "Merry X-mas!";
		} else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
			this.splashText = "Happy new year!";
		} else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
			this.splashText = "OOoooOOOoooo! Spooky!";
		}

		boolean flag = true;
		int i = this.height / 4 + 48;

		this.addSingleplayerMultiplayerButtons(80, 24);

		synchronized (this.field_104025_t) {
			this.field_92023_s = this.fontRendererObj.getStringWidth(this.field_92025_p);
			this.field_92024_r = this.fontRendererObj.getStringWidth(this.field_146972_A);
			int j = Math.max(this.field_92023_s, this.field_92024_r);
			this.field_92022_t = (this.width - j) / 2;
			this.field_92021_u = ((GuiButton)this.buttonList.get(0)).yPosition - 24;
			this.field_92020_v = this.field_92022_t + j;
			this.field_92019_w = this.field_92021_u + 24;
		}
	}

	private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
		this.buttonList.add(new AetherMainMenuButton(1, 30, p_73969_1_, I18n.format("menu.singleplayer", new Object[0])));
		this.buttonList.add(new AetherMainMenuButton(2, 30, p_73969_1_ + p_73969_2_ * 1, I18n.format("menu.multiplayer", new Object[0])));
		GuiButton realmsButton = new AetherMainMenuButton(14, 30, p_73969_1_ + p_73969_2_ * 2, I18n.format("menu.online", new Object[0]));
		GuiButton fmlModButton = new AetherMainMenuButton(6,  30, p_73969_1_ + p_73969_2_ * 3, "Mods");
		this.buttonList.add(realmsButton);
		this.buttonList.add(fmlModButton);
		this.buttonList.add(new AetherMainMenuButton(0, 30, p_73969_1_ + p_73969_2_ * 4, I18n.format("menu.options", new Object[0])));
		this.buttonList.add(new AetherMainMenuButton(4,30, p_73969_1_ + p_73969_2_ * 5, I18n.format("menu.quit", new Object[0])));
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		switch(p_146284_1_.id) {
			case 0:
				this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
				break;

			case 5:
				this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
				break;

			case 1:
				this.mc.displayGuiScreen(new GuiSelectWorld(this));
				break;

			case 2:
				this.mc.displayGuiScreen(new GuiMultiplayer(this));
				break;

			case 14:
				func_140005_i();
				break;

			case 4:
				this.mc.shutdown();
				break;

			case 6:
				this.mc.displayGuiScreen(new GuiModList(this));
				break;

			case 11:
				this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
				break;

			case 12:
				ISaveFormat saveformat = this.mc.getSaveLoader();
				WorldInfo world_info = saveformat.getWorldInfo("Demo_World");
				if (world_info != null) {
					GuiYesNo yesno_gui = GuiSelectWorld.func_152129_a(this, world_info.getWorldName(), 12);
					this.mc.displayGuiScreen(yesno_gui);
				}
				break;
		}
	}

	private void func_140005_i() {
		RealmsBridge realmsbridge = new RealmsBridge();
		realmsbridge.switchToRealms(this);
	}

	@Override
	public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
		if (p_73878_1_ && p_73878_2_ == 12) {
			ISaveFormat saveformat = this.mc.getSaveLoader();
			saveformat.flushCache();
			saveformat.deleteWorldDirectory("Demo_World");
			this.mc.displayGuiScreen(this);
		} else if (p_73878_2_ == 13) {
			if (p_73878_1_) {
				try {
					Class oclass = Class.forName("java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
					oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI(this.field_104024_v)});
				} catch (Exception e) {
					logger.error("Couldn\'t open link", e);
				}
			}

			this.mc.displayGuiScreen(this);
		}
	}

	private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
		Tessellator tessellator = Tessellator.instance;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		Project.gluPerspective(120F, 1F, 0.05F, 10F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glRotatef(180F, 1F, 0F, 0F);
		GL11.glRotatef(90F, 0F, 0F, 1F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		byte b0 = 8;

		for (int k = 0; k < b0 * b0; ++k) {
			GL11.glPushMatrix();
			float f1 = ((float)(k % b0) / (float)b0 - 0.5F) / 64F;
			float f2 = ((float)(k / b0) / (float)b0 - 0.5F) / 64F;
			float f3 = 0F;
			GL11.glTranslatef(f1, f2, f3);
			GL11.glRotatef(MathHelper.sin(((float)this.panoramaTimer + p_73970_3_) / 400F) * 25F + 20F, 1F, 0F, 0F);
			GL11.glRotatef(-((float)this.panoramaTimer + p_73970_3_) * 0.1F, 0F, 1F, 0F);

			for (int l = 0; l < 6; ++l) {
				GL11.glPushMatrix();

				switch(l) {
					case 1:
						GL11.glRotatef(90F, 0F, 1F, 0F);
						break;
					case 2:
						GL11.glRotatef(180F, 0F, 1F, 0F);
						break;
					case 3:
						GL11.glRotatef(-90F, 0F, 1F, 0F);
						break;
					case 4:
						GL11.glRotatef(90F, 1F, 0F, 0F);
						break;
					case 5:
						GL11.glRotatef(-90F, 1F, 0F, 0F);
						break;
				}

				this.mc.getTextureManager().bindTexture(titlePanoramaPaths[l]);
				tessellator.startDrawingQuads();
				tessellator.setColorRGBA_I(16777215, 255 / (k + 1));
				float f4 = 0F;
				tessellator.addVertexWithUV(-1D, -1D, 1D, (double)(0F + f4), (double)(0F + f4));
				tessellator.addVertexWithUV(1D, -1D, 1D, (double)(1F - f4), (double)(0F + f4));
				tessellator.addVertexWithUV(1D, 1D, 1D, (double)(1F - f4), (double)(1F - f4));
				tessellator.addVertexWithUV(-1D, 1D, 1D, (double)(0F + f4), (double)(1F - f4));
				tessellator.draw();
				GL11.glPopMatrix();
			}

			GL11.glPopMatrix();
			GL11.glColorMask(true, true, true, false);
		}

		tessellator.setTranslation(0D, 0D, 0D);
		GL11.glColorMask(true, true, true, true);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void rotateAndBlurSkybox(float p_73968_1_) {
		this.mc.getTextureManager().bindTexture(this.field_110351_G);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		byte b0 = 3;

		for (int i = 0; i < b0; i++) {
			tessellator.setColorRGBA_F(1F, 1F, 1F, 1F / (float)(i + 1));
			int j = this.width;
			int k = this.height;
			float f1 = (float)(i - b0 / 2) / 256F;
			tessellator.addVertexWithUV((double)j, (double)k, (double)this.zLevel, (double)(0F + f1), 1D);
			tessellator.addVertexWithUV((double)j, 0D, (double)this.zLevel, (double)(1F + f1), 1D);
			tessellator.addVertexWithUV(0D, 0D, (double)this.zLevel, (double)(1F + f1), 0D);
			tessellator.addVertexWithUV(0D, (double)k, (double)this.zLevel, (double)(0F + f1), 0D);
		}

		tessellator.draw();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColorMask(true, true, true, true);
	}

	private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
		this.mc.getFramebuffer().unbindFramebuffer();
		GL11.glViewport(0, 0, 256, 256);
		this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.mc.getFramebuffer().bindFramebuffer(true);
		GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		float f1 = this.width > this.height ? 120F / (float)this.width : 120F / (float)this.height;
		float f2 = (float)this.height * f1 / 256F;
		float f3 = (float)this.width * f1 / 256F;
		tessellator.setColorRGBA_F(1F, 1F, 1F, 1F);
		int k = this.width;
		int l = this.height;
		tessellator.addVertexWithUV(0D, (double)l, (double)this.zLevel, (double)(0.5F - f2), (double)(0.5F + f3));
		tessellator.addVertexWithUV((double)k, (double)l, (double)this.zLevel, (double)(0.5F - f2), (double)(0.5F - f3));
		tessellator.addVertexWithUV((double)k, 0D, (double)this.zLevel, (double)(0.5F + f2), (double)(0.5F - f3));
		tessellator.addVertexWithUV(0D, 0D, (double)this.zLevel, (double)(0.5F + f2), (double)(0.5F + f3));
		tessellator.draw();
	}

	private static boolean is_hovering_aether_menu_button(AetherMainMenuButton button, int x, int y) {
		int button_x_pos = button.xPosition;
		if(button_x_pos < 46 && button_x_pos >= 30) button_x_pos = 46;
		if(x < button_x_pos || x >= button.xPosition + button.width) return false;
		return y >= button.yPosition && y < button.yPosition + button.height;
	}

	@Override
	public void drawScreen(int x, int y, float p_73863_3_) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		this.renderSkybox(x, y, p_73863_3_);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		Tessellator tessellator = Tessellator.instance;
		short short1 = 274;
		int k = this.width / 2 - short1 / 2;
		byte b0 = 30;
		this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
		this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
		this.mc.getTextureManager().bindTexture(aetherTitleTextures);
		GL11.glColor4f(1F, 1F, 1F, 1F);

		this.drawTexturedModalRect(-30, 15, 0, 0, 155, 44);
		this.drawTexturedModalRect(-30 + 155, 15, 0, 45, 155, 44);

		tessellator.setColorOpaque_I(-1);
		GL11.glPushMatrix();
		GL11.glTranslatef(200F, 55F, 0F);
		GL11.glRotatef(-20F, 0F, 0F, 1F);
		float f1 = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000F * (float)Math.PI * 2F) * 0.1F);
		f1 = f1 * 100F / (float)(this.fontRendererObj.getStringWidth(this.splashText) + 32);
		GL11.glScalef(f1, f1, f1);
		this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
		GL11.glPopMatrix();
		String s = "Minecraft 1.7.10";
		if (this.mc.isDemo()) s = s + " Demo";

		List<String> brandings = Lists.reverse(FMLCommonHandler.instance().getBrandings(true));
		for (int i = 0; i < brandings.size(); i++) {
			String brd = brandings.get(i);
			if (!Strings.isNullOrEmpty(brd)) {
				this.drawString(this.fontRendererObj, brd, this.width - this.fontRendererObj.getStringWidth(brd) - 2, this.height - (30 + i * (this.fontRendererObj.FONT_HEIGHT + 1)), 16777215);
			}
		}
		ForgeHooksClient.renderMainMenu(this, fontRendererObj, width, height);
		String s1 = "Copyright Mojang AB. Do not distribute!";
		this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2, this.height - 10, -1);

		if (this.field_92025_p != null && this.field_92025_p.length() > 0) {
			drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
			this.drawString(this.fontRendererObj, this.field_92025_p, this.field_92022_t, this.field_92021_u, -1);
			this.drawString(this.fontRendererObj, this.field_146972_A, (this.width - this.field_92024_r) / 2, ((GuiButton)this.buttonList.get(0)).yPosition - 12, -1);
		}

		for (int j = 0; j < this.buttonList.size(); ++j) {
			GuiButton button = (GuiButton)this.buttonList.get(j);
			button.drawButton(this.mc, x, y);
			if(button.getClass() == AetherMainMenuButton.class) {
				if(is_hovering_aether_menu_button((AetherMainMenuButton)button, x, y)) {
					if(button.xPosition < 45) button.xPosition += 4;
				} else {
					if(button.xPosition > 30) button.xPosition -= 4;
				}
			}
		}

		for (int j = 0; j < this.labelList.size(); ++j) {
			((GuiLabel)this.labelList.get(j)).func_146159_a(this.mc, x, y);
		}
	}

	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		if (p_73864_3_ == 0) {
			for (int l = 0; l < this.buttonList.size(); ++l) {
				GuiButton button = (GuiButton)this.buttonList.get(l);

				if (button.mousePressed(this.mc, p_73864_1_, p_73864_2_)) {
					GuiScreenEvent.ActionPerformedEvent.Pre event = new GuiScreenEvent.ActionPerformedEvent.Pre(this, button, this.buttonList);
					if (MinecraftForge.EVENT_BUS.post(event)) break;
					this.selectedButton = event.button;
					event.button.func_146113_a(this.mc.getSoundHandler());
					this.actionPerformed(event.button);
					if (this.equals(this.mc.currentScreen)) {
						MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(this, event.button, this.buttonList));
					}
				}
			}
		}

		synchronized (this.field_104025_t) {
			if (this.field_92025_p.length() > 0 && p_73864_1_ >= this.field_92022_t && p_73864_1_ <= this.field_92020_v && p_73864_2_ >= this.field_92021_u && p_73864_2_ <= this.field_92019_w) {
				GuiConfirmOpenLink confirm_gui = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
				confirm_gui.func_146358_g();
				this.mc.displayGuiScreen(confirm_gui);
			}
		}
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_) {
		if (this.selectedButton != null && p_146286_3_ == 0) {
			this.selectedButton.mouseReleased(p_146286_1_, p_146286_2_);
			this.selectedButton = null;
		}
	}
}
