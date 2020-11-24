package com.gildedgames.the_aether.client.gui;

import com.gildedgames.the_aether.inventory.EnchanterContainer;
import com.gildedgames.the_aether.tileentity.EnchanterTileEntity;
import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.Aether;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EnchanterGui extends GuiContainer {

	private static final ResourceLocation TEXTURE = Aether.locate("textures/gui/altar.png");

	private EnchanterTileEntity enchanter;

	public EnchanterGui(InventoryPlayer inventory, EnchanterTileEntity enchanter) {
		super(new EnchanterContainer(inventory, enchanter));
		this.enchanter = enchanter;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		String name = AetherConfig.should_use_legacy_altar_name() ?
			I18n.format("container.aether_legacy.enchanter") :
			I18n.format("container.aether_legacy.altar");

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 0x404040);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float ticks, int x, int y) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		x = (this.width - this.xSize) / 2;
		y = (this.height - this.ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		if (this.enchanter.isEnchanting()) {
			int remaining = this.enchanter.getEnchantmentTimeRemaining(12);
			drawTexturedModalRect(x + 56, y + 36 + 12 - remaining, 176, 12 - remaining, 14, remaining + 2);
		}

		int progress = this.enchanter.getEnchantmentProgressScaled(24);
		drawTexturedModalRect(x + 79, y + 34, 176, 14, progress + 1, 16);
	}

}
