package com.gildedgames.the_aether.client.gui;

import com.gildedgames.the_aether.client.gui.button.SunAltarSlider;
import com.gildedgames.the_aether.Aether;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class SunAltarGui extends GuiScreen {

	private static final ResourceLocation TEXTURE = Aether.locate("textures/gui/sun_altar.png");

	private World world;

	public SunAltarGui() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();

		this.world = this.mc.theWorld;
		this.buttonList.add(new SunAltarSlider(this.world, this.width / 2 - 75, this.height / 2, "Select Time"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.mc.renderEngine.bindTexture(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int xSize = 175;
		int ySize = 78;
		int j = (this.width - xSize) / 2;
		int k = (this.height - ySize) / 2;

		this.drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
		this.fontRendererObj.drawString("Sun Altar", (this.width - this.fontRendererObj.getStringWidth("Sun Altar")) / 2, k + 20, 0x404040);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	protected void keyTyped(char c, int key_code) {
		if (key_code == 1 || key_code == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
			this.mc.thePlayer.closeScreen();
		}
	}
}
