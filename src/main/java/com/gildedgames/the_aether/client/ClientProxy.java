package com.gildedgames.the_aether.client;

import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.CommonProxy;
import com.gildedgames.the_aether.client.audio.AetherMusicHandler;
import com.gildedgames.the_aether.client.gui.AetherLoadingScreen;
import com.gildedgames.the_aether.client.gui.GuiAetherInGame;
import com.gildedgames.the_aether.client.gui.GuiSunAltar;
import com.gildedgames.the_aether.client.renders.AetherEntityRenderer;
import com.gildedgames.the_aether.client.renders.RendersAether;
import com.gildedgames.the_aether.compatibility.client.AetherClientCompatibility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	public static final IIcon[] ACCESSORY_ICONS = new IIcon[8];

	@Override
	public void init() {
		berryBushRenderID = RenderingRegistry.getNextAvailableRenderId();
		treasureChestRenderID = RenderingRegistry.getNextAvailableRenderId();
		aetherFlowerRenderID = RenderingRegistry.getNextAvailableRenderId();

		Minecraft.getMinecraft().loadingScreen = new AetherLoadingScreen(Minecraft.getMinecraft());

		EntityRenderer previousRenderer = Minecraft.getMinecraft().entityRenderer;

		Minecraft.getMinecraft().entityRenderer = new AetherEntityRenderer(Minecraft.getMinecraft(), previousRenderer, Minecraft.getMinecraft().getResourceManager());

		RendersAether.initialization();

		registerEvent(new AetherMusicHandler());
		registerEvent(new AetherClientEvents());
		registerEvent(new GuiAetherInGame(Minecraft.getMinecraft()));

		AetherClientCompatibility.initialization();
	}

	@Override
	public void sendMessage(EntityPlayer player, String text) {
		if (this.getPlayer() == player)
		{
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(text));
		}
	}

	@Override
	public void openSunAltar() {
		FMLClientHandler.instance().getClient().displayGuiScreen(new GuiSunAltar());
	}

	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

}
