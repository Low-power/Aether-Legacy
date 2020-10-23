package com.gildedgames.the_aether.client.gui.menu;

import com.gildedgames.the_aether.AetherConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class MenuToggleButton extends GuiButton {
	public MenuToggleButton(int x, int y) {
		super(50, x, y, 20, 20, "T");
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		super.drawButton(mc, mouseX, mouseY);

		if (this.visible) {
			FontRenderer font_renderer = mc.fontRenderer;
			int color = 0xffffff;
			if (this.field_146123_n) {
				drawCenteredString(font_renderer,
					AetherConfig.is_aether_menu_enabled() ? "Normal Theme" : "Aether Theme",
					(this.xPosition + this.width) - 34, (this.height / 2) + 18, color);
			}
		}
	}

	public MenuToggleButton setPosition(int x, int y) {
		this.xPosition = x;
		this.yPosition = y;

		return this;
	}

	public void mouseReleased(int mouseX, int mouseY) {
		boolean value = !AetherConfig.is_aether_menu_enabled();
		AetherConfig.set_aether_menu_enabled(value);
		if(value) {
			Minecraft.getMinecraft().displayGuiScreen(new AetherMainMenu());
		}
	}
}
