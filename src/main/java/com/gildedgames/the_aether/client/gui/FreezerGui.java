package com.gildedgames.the_aether.client.gui;

import com.gildedgames.the_aether.inventory.FreezerContainer;
import com.gildedgames.the_aether.tileentity.FreezerTileEntity;
import com.gildedgames.the_aether.Aether;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class FreezerGui extends GuiContainer {

	private static final ResourceLocation TEXTURE = Aether.locate("textures/gui/altar.png");

	private FreezerTileEntity freezer;

	public FreezerGui(InventoryPlayer inventory, FreezerTileEntity freezer) {
		super(new FreezerContainer(inventory, freezer));
		this.freezer = freezer;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		String freezer_name = this.freezer.getInventoryName();
		this.fontRendererObj.drawString(freezer_name, this.xSize / 2 - this.fontRendererObj.getStringWidth(freezer_name) / 2, 6, 0x404040);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float ticks, int x, int y) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		x = (this.width - this.xSize) / 2;
		y = (this.height - this.ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		if (this.freezer.isFreezing()) {
			int remaining = this.freezer.getFreezingTimeRemaining(12);
			this.drawTexturedModalRect(x + 56, y + 36 + 12 - remaining, 176, 12 - remaining, 14, remaining + 2);
		}

		int progress = this.freezer.getFreezingProgressScaled(24);
		this.drawTexturedModalRect(x + 79, y + 34, 176, 14, progress + 1, 16);
	}

}
