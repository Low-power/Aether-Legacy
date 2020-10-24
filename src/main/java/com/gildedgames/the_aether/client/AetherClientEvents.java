package com.gildedgames.the_aether.client;

import com.gildedgames.the_aether.client.gui.GuiCustomizationScreen;
import com.gildedgames.the_aether.client.gui.GuiEnterAether;
import com.gildedgames.the_aether.client.gui.button.GuiAccessoryButton;
import com.gildedgames.the_aether.client.gui.button.GuiCapeButton;
import com.gildedgames.the_aether.client.gui.button.GuiCustomizationScreenButton;
import com.gildedgames.the_aether.client.gui.button.*;
import com.gildedgames.the_aether.client.gui.menu.AetherMainMenu;
import com.gildedgames.the_aether.client.gui.menu.MenuToggleButton;
import com.gildedgames.the_aether.network.packets.PacketCapeChanged;
import com.gildedgames.the_aether.network.packets.PacketExtendedAttack;
import com.gildedgames.the_aether.player.perks.AetherRankings;
import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.client.gui.AetherLoadingScreen;
import com.gildedgames.the_aether.client.renders.entity.PlayerAetherRenderer;
import com.gildedgames.the_aether.entities.AetherEntities;
import com.gildedgames.the_aether.items.AetherSpawnEgg;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.items.armor.AetherArmor;
import com.gildedgames.the_aether.network.AetherGuiHandler;
import com.gildedgames.the_aether.network.AetherNetwork;
import com.gildedgames.the_aether.network.packets.PacketOpenContainer;
import com.gildedgames.the_aether.player.PlayerAether;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.RenderPlayerEvent.SetArmorModel;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import java.util.List;

public class AetherClientEvents {

	private static boolean wasInAether = false;

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) throws Exception {
		Minecraft mc = Minecraft.getMinecraft();
		TickEvent.Phase phase = event.phase;
		TickEvent.Type type = event.type;

		if (phase == TickEvent.Phase.END) {
			if (type.equals(TickEvent.Type.CLIENT)) {
				if (!AetherConfig.triviaDisabled()) {
					if (!(mc.loadingScreen instanceof AetherLoadingScreen)) {
						mc.loadingScreen = new AetherLoadingScreen(mc);
					}
				}

				if (mc.thePlayer != null && !(mc.thePlayer.movementInput instanceof AetherMovementInput)) {
					mc.thePlayer.movementInput = new AetherMovementInput(mc, mc.gameSettings);
				}

				handleExtendedReach(mc);
			}
		}

		if (phase == TickEvent.Phase.START) {
			if (type.equals(TickEvent.Type.CLIENT)) {
				if (mc.currentScreen == null || mc.currentScreen.allowUserInput) {
					if (!mc.thePlayer.isUsingItem()) {
						if (GameSettings.isKeyDown(mc.gameSettings.keyBindPickBlock)) {
							this.sendPickupPacket(mc);
						}
					}
				}
			}
		}
	}

	private void handleExtendedReach(Minecraft mc) {
		EntityPlayer player = mc.thePlayer;
		if(player == null) return;

		if (Mouse.getEventButton() == 0) {
			if (Mouse.getEventButtonState()) {
				ItemStack stack = player.getHeldItem();

				if (stack != null) {
					if (isValkyrieItem(stack.getItem())) {
						Vec3 playerVision = player.getLookVec();
						AxisAlignedBB reachDistance = player.boundingBox.expand(10D, 10D, 10D);

						List<Entity> locatedEntities = player.worldObj.getEntitiesWithinAABB(Entity.class, reachDistance);

						Entity found = null;
						double foundLen = 0D;

						for (Entity ent : locatedEntities) {
							if (ent == player) {
								continue;
							}

							if (!ent.canBeCollidedWith() && !(ent instanceof EntityDragon)) {
								continue;
							}

							Vec3 vec = Vec3.createVectorHelper(ent.posX - player.posX, ent.boundingBox.minY + ent.height / 2f - player.posY - player.getEyeHeight(), ent.posZ - player.posZ);
							double len = vec.lengthVector();

							if (len > 8F) {
								continue;
							}

							vec = vec.normalize();
							double dot = playerVision.dotProduct(vec);

							if (dot < 1D - 0.125 / len || !player.canEntityBeSeen(ent)) {
								continue;
							}

							if (foundLen == 0D || len < foundLen) {
								found = ent;
								foundLen = len;
							}
						}

						if (found != null && player.ridingEntity != found) {
							stack.damageItem(1, player);

							AetherNetwork.sendToServer(new PacketExtendedAttack(found.getEntityId()));
						}
					}
				}
			}
		}
	}

	public boolean isValkyrieItem(Item id) {
		return id == AetherItems.valkyrie_shovel || id == AetherItems.valkyrie_axe || id == AetherItems.valkyrie_pickaxe || id == AetherItems.valkyrie_lance;
	}

	@SubscribeEvent
	public void onOpenGui(GuiOpenEvent event) {
		Minecraft mc = FMLClientHandler.instance().getClient();

		if (mc.thePlayer != null && event.gui instanceof GuiDownloadTerrain) {
			GuiEnterAether enterAether = new GuiEnterAether(true);
			GuiEnterAether exitAether = new GuiEnterAether(false);

			if (mc.thePlayer.dimension == AetherConfig.getAetherDimensionID()) {
				event.gui = enterAether;
				wasInAether = true;
			} else if (wasInAether) {
				event.gui = exitAether;
				wasInAether = false;
			}
		}
	}


	private void sendPickupPacket(Minecraft mc) {
		if (mc.objectMouseOver != null) {
			if (!this.onPickEntity(mc.objectMouseOver, mc.thePlayer, mc.theWorld)) {
				return;
			}

			if (mc.thePlayer.capabilities.isCreativeMode) {
				int index = mc.thePlayer.inventoryContainer.inventorySlots.size() - 9 + mc.thePlayer.inventory.currentItem;

				mc.playerController.sendSlotPacket(mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem), index);
			}
		}
	}

	private boolean onPickEntity(MovingObjectPosition target, EntityPlayer player, World world) {
		ItemStack result = null;
		boolean isCreative = player.capabilities.isCreativeMode;

		if (!isCreative) {
			return false;
		}

		if (target.entityHit != null) {
			int id = AetherEntities.getEntityID(target.entityHit);

			if (id >= 0 && AetherSpawnEgg.entityEggs.containsKey(id)) {
				result = new ItemStack(AetherItems.aether_spawn_egg, 1, id);
			}
		}

		if (result == null) {
			return false;
		}

		for (int x = 0; x < 9; x++) {
			ItemStack stack = player.inventory.getStackInSlot(x);

			if (stack != null && stack.isItemEqual(result) && ItemStack.areItemStackTagsEqual(stack, result)) {
				player.inventory.currentItem = x;

				return true;
			}
		}

		int slot = player.inventory.getFirstEmptyStack();

		if (slot < 0 || slot >= 9) {
			slot = player.inventory.currentItem;
		}

		player.inventory.setInventorySlotContents(slot, result);
		player.inventory.currentItem = slot;

		return true;
	}

	@SubscribeEvent
	public void onBowPulled(FOVUpdateEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if (player == null || player.getCurrentEquippedItem() == null) {
			return;
		}

		Item item = player.getCurrentEquippedItem().getItem();
		if (item == AetherItems.phoenix_bow) {
			int i = player.getItemInUseDuration();
			float f1 = (float) i / 20F;

			if (f1 > 1F) {
				f1 = 1F;
			} else {
				f1 = f1 * f1;
			}

			float original = event.fov;

			original *= 1F - f1 * 0.15F;

			event.newfov = original;
		}
	}

	private static final GuiAccessoryButton ACCESSORY_BUTTON = new GuiAccessoryButton(0, 0);

	private static final MenuToggleButton MAIN_MENU_BUTTON = new MenuToggleButton(0, 0);

	private static int previousSelectedTabIndex = -1;

	@SubscribeEvent
	@SuppressWarnings("unchecked")
	public void onGuiOpened(GuiScreenEvent.InitGuiEvent.Post event) {
		if (event.gui instanceof GuiContainer) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			Class<?> clazz = event.gui.getClass();

			int guiLeft = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, (GuiContainer) event.gui, "guiLeft", "field_147003_i");
			int guiTop = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, (GuiContainer) event.gui, "guiTop", "field_147009_r");

			if (player.capabilities.isCreativeMode) {
				if (event.gui instanceof GuiContainerCreative) {
					if (((GuiContainerCreative) event.gui).func_147056_g() == CreativeTabs.tabInventory.getTabIndex()) {
						event.buttonList.add(ACCESSORY_BUTTON.setPosition(guiLeft + 28, guiTop + 38));
						previousSelectedTabIndex = CreativeTabs.tabInventory.getTabIndex();
					}
				}
			} else if (clazz == GuiInventory.class) {
				event.buttonList.add(ACCESSORY_BUTTON.setPosition(guiLeft + 26, guiTop + 65));
			}
		}

		if(event.gui instanceof GuiMainMenu) {
			if(AetherConfig.is_menu_toggle_button_enabled()) {
				event.buttonList.add(MAIN_MENU_BUTTON.setPosition(event.gui.width - 24, 4));
			}
			if(!(event.gui instanceof AetherMainMenu) && AetherConfig.is_aether_menu_enabled()) {
				Minecraft.getMinecraft().displayGuiScreen(new AetherMainMenu());
			}
		}

		if(event.gui instanceof GuiOptions) {
			if (Minecraft.getMinecraft().thePlayer != null) {
				if (AetherRankings.isRankedPlayer(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
					event.buttonList.add(new GuiCustomizationScreenButton(545, event.gui.width / 2 - 155, event.gui.height / 6 + 48 - 6, 150, 20, I18n.format("gui.options.perk_customization")));
				}
			}
		}

		if(event.gui instanceof ScreenChatOptions) {
			if (Minecraft.getMinecraft().thePlayer != null) {
				int i = 13;
				event.buttonList.add(new GuiCapeButton(event.gui.width / 2 - 155 + i % 2 * 160, event.gui.height / 6 + 24 * (i >> 1)));
			}
		}
	}

	@SubscribeEvent
	public void onMouseClicked(DrawScreenEvent.Post event) {
		if (Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative) {
			GuiContainerCreative guiScreen = (GuiContainerCreative) Minecraft.getMinecraft().currentScreen;

			if (previousSelectedTabIndex != guiScreen.func_147056_g()) {
				List<GuiButton> buttonList = ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, (GuiScreen) guiScreen, 4);

				if (guiScreen.func_147056_g() == CreativeTabs.tabInventory.getTabIndex() && !buttonList.contains(ACCESSORY_BUTTON)) {
					int guiLeft = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, (GuiContainer) guiScreen, "guiLeft", "field_147003_i");
					int guiTop = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, (GuiContainer) guiScreen, "guiTop", "field_147009_r");

					buttonList.add(ACCESSORY_BUTTON.setPosition(guiLeft + 28, guiTop + 38));
				} else if (previousSelectedTabIndex == CreativeTabs.tabInventory.getTabIndex()) {
					buttonList.remove(ACCESSORY_BUTTON);
				}

				previousSelectedTabIndex = guiScreen.func_147056_g();
			}
		}
	}

	@SubscribeEvent
	public void on_draw_gui(GuiScreenEvent.DrawScreenEvent.Pre event) {
		if(event.gui instanceof AetherMainMenu && !AetherConfig.is_aether_menu_enabled()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
		}
	}

	@SubscribeEvent
	public void onButtonPressed(GuiScreenEvent.ActionPerformedEvent.Pre event) {
		Class<?> clazz = event.gui.getClass();

		if ((clazz == GuiInventory.class || clazz == GuiContainerCreative.class) && event.button.id == 18067) {
			AetherNetwork.sendToServer(new PacketOpenContainer(AetherGuiHandler.accessories));
		}

		if(event.button instanceof GuiCustomizationScreenButton) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiCustomizationScreen(event.gui));
		} else if(event.button instanceof GuiCapeButton) {
			PlayerAether player = PlayerAether.get(Minecraft.getMinecraft().thePlayer);

			boolean enableCape = !player.shouldRenderCape;

			player.shouldRenderCape = enableCape;
			AetherNetwork.sendToServer(new PacketCapeChanged(player.getEntity().getEntityId(), player.shouldRenderCape));
		}
	}

	@SubscribeEvent
	public void onRenderInvisibility(RenderPlayerEvent.Pre event) {
		EntityPlayer player = event.entityPlayer;
		PlayerAether playerAether = PlayerAether.get(player);

		if (playerAether != null) {
			if (playerAether.getAccessoryInventory().wearingAccessory(new ItemStack(AetherItems.invisibility_cape))) {
				event.setCanceled(true);
			}
		}

		PlayerAetherRenderer.instance().setPartialTicks(event.partialRenderTick);
	}

	@SubscribeEvent
	public void onRenderAetherCape(RenderPlayerEvent.Specials.Pre event) {
		event.renderCape = !PlayerAetherRenderer.instance().isCapeRendering();
	}

	@SubscribeEvent
	public void onRenderAetherArmor(SetArmorModel event) {
		if (event.stack != null && event.stack.getItem() instanceof AetherArmor) {
			event.result = PlayerAetherRenderer.instance().renderAetherArmor(PlayerAether.get(event.entityPlayer), event.renderer, event.stack, 3 - event.slot);
		}
	}

	@SubscribeEvent
	public void onRenderAccessories(RenderLivingEvent.Post event) {
		if (event.entity instanceof EntityPlayer) {
			PlayerAether playerAether = PlayerAether.get((EntityPlayer) event.entity);

			if (event.renderer instanceof RenderPlayer) {
				PlayerAetherRenderer.instance().renderAccessories(playerAether, (RenderPlayer) event.renderer, event.x, event.y, event.z, PlayerAetherRenderer.instance().getPartialTicks());
			}
		}
	}

}
