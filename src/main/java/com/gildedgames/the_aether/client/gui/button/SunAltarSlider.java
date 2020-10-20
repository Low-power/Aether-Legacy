package com.gildedgames.the_aether.client.gui.button;

import com.gildedgames.the_aether.network.AetherNetwork;
import com.gildedgames.the_aether.network.packets.PacketSetTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class SunAltarSlider extends GuiButton {

	public float sliderValue;

	public boolean dragging = false;

	private World world;

	public SunAltarSlider(World world, int x, int y, String text) {
		super(1, x, y, 150, 20, text);

		this.world = world;
	}

	/**
	 * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
	 * this button.
	 */
	public int getHoverState(boolean par1) {
		return 0;
	}

	@Override
	protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			if (this.dragging) {
				this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
				long shouldTime = (long) (24000L * sliderValue);
				long worldTime = world.getWorldInfo().getWorldTime();
				long remainder = worldTime % 24000L;
				long add = shouldTime > remainder ? shouldTime - remainder : shouldTime + 24000 - remainder;
				world.getWorldInfo().setWorldTime(worldTime + add);
				if (this.sliderValue < 0F) this.sliderValue = 0F;
				else if (this.sliderValue > 1F) this.sliderValue = 1F;
			}

			GL11.glColor4f(1F, 1F, 1F, 1F);
			this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
			this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
		}
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		this.sliderValue = (this.world.getWorldInfo().getWorldTime() % 24000) / 24000F;

		super.drawButton(mc, mouseX, mouseY);
	}

	@Override
	public boolean mousePressed(Minecraft mc, int x, int y) {
		if (super.mousePressed(mc, x, y)) {
			this.sliderValue = (float)(x - (this.xPosition + 4)) / (float) (this.width - 8);
			if (this.sliderValue < 0F) this.sliderValue = 0F;
			else if (this.sliderValue > 1F) this.sliderValue = 1F;
			this.dragging = true;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		this.dragging = false;

		AetherNetwork.sendToServer(new PacketSetTime(this.sliderValue, Minecraft.getMinecraft().thePlayer.dimension));
	}

}
