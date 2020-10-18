package com.gildedgames.the_aether.client.renders;

import com.gildedgames.the_aether.api.accessories.AccessoryType;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.items.accessories.Accessory;
import com.gildedgames.the_aether.player.PlayerAether;
import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;
import static net.minecraftforge.client.IItemRenderer.ItemRenderType.FIRST_PERSON_MAP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.common.Loader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class AetherItemRenderer extends ItemRenderer {

	private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");

	private Minecraft mc;

	private ItemStack itemToRender;

	private float equippedProgress;

	private float prevEquippedProgress;

	private int equippedItemSlot = -1;

	public AetherItemRenderer(Minecraft mc) {
		super(mc);

		this.mc = mc;
	}

	public void renderFirstPersonArm(RenderPlayer player_render, EntityClientPlayerMP player) {
		PlayerAether playerAether = PlayerAether.get(player);
		ItemStack gloves = playerAether.getAccessoryInventory().getStackInSlot(AccessoryType.GLOVES);

		this.mc.getTextureManager().bindTexture(player.getLocationSkin());

		if (gloves == null) {
			player_render.renderFirstPersonArm(player);

			return;
		}

		player_render.renderFirstPersonArm(player);

		if (gloves.getItem() instanceof Accessory) {
			this.mc.getTextureManager().bindTexture(((Accessory)gloves.getItem()).texture);

			int colour = gloves.getItem().getColorFromItemStack(gloves, 0);
			float red = ((colour >> 16) & 0xff) / 255F;
			float green = ((colour >> 8) & 0xff) / 255F;
			float blue = (colour & 0xff) / 255F;

			if (gloves.getItem() != AetherItems.phoenix_gloves) {
				GL11.glColor3f(red, green, blue);
			}

			GL11.glEnable(GL11.GL_BLEND);

			player_render.modelBipedMain.onGround = 0F;
			player_render.modelBipedMain.setRotationAngles(0F, 0F, 0F, 0F, 0F, 0.0625F, player);
			player_render.modelBipedMain.bipedRightArm.render(0.0625F);

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor3f(1F, 1F, 1F);
		}
	}

	@Override
	public void renderItemInFirstPerson(float partialTicks) {
		float f1 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks;
		EntityClientPlayerMP client_player = this.mc.thePlayer;
		float f2 = client_player.prevRotationPitch + (client_player.rotationPitch - client_player.prevRotationPitch) * partialTicks;
		GL11.glPushMatrix();
		GL11.glRotatef(f2, 1F, 0F, 0F);
		GL11.glRotatef(client_player.prevRotationYaw + (client_player.rotationYaw - client_player.prevRotationYaw) * partialTicks, 0F, 1F, 0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		float f3 = client_player.prevRenderArmPitch + (client_player.renderArmPitch - client_player.prevRenderArmPitch) * partialTicks;
		float f4 = client_player.prevRenderArmYaw + (client_player.renderArmYaw - client_player.prevRenderArmYaw) * partialTicks;
		GL11.glRotatef((client_player.rotationPitch - f3) * 0.1F, 1F, 0F, 0F);
		GL11.glRotatef((client_player.rotationYaw - f4) * 0.1F, 0F, 1F, 0F);
		ItemStack itemstack = this.itemToRender;

		if (itemstack != null && itemstack.getItem() instanceof ItemCloth) {
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		}

		int i = this.mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(client_player.posX), MathHelper.floor_double(client_player.posY), MathHelper.floor_double(client_player.posZ), 0);
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1F, (float)k / 1F);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		float f5;
		float f6;
		float f7;

		if (itemstack != null) {
			int l = itemstack.getItem().getColorFromItemStack(itemstack, 0);
			f5 = (float)(l >> 16 & 255) / 255F;
			f6 = (float)(l >> 8 & 255) / 255F;
			f7 = (float)(l & 255) / 255F;
			GL11.glColor4f(f5, f6, f7, 1F);
		} else {
			GL11.glColor4f(1F, 1F, 1F, 1F);
		}

		float f8;
		float f9;
		float f10;
		float f13;
		Render render;
		RenderPlayer renderplayer;

		if (itemstack != null && itemstack.getItem() instanceof ItemMap) {
			GL11.glPushMatrix();
			f13 = 0.8F;
			f5 = client_player.getSwingProgress(partialTicks);
			f6 = MathHelper.sin(f5 * (float)Math.PI);
			f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI);
			GL11.glTranslatef(-f7 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI * 2F) * 0.2F, -f6 * 0.2F);
			f5 = 1F - f2 / 45F + 0.1F;
			if (f5 < 0F) f5 = 0F;
			else if(f5 > 1F) f5 = 1F;
			f5 = -MathHelper.cos(f5 * (float)Math.PI) * 0.5F + 0.5F;
			GL11.glTranslatef(0F, 0F * f13 - (1F - f1) * 1.2F - f5 * 0.5F + 0.04F, -0.9F * f13);
			GL11.glRotatef(90F, 0F, 1F, 0F);
			GL11.glRotatef(f5 * -85F, 0F, 0F, 1F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			this.mc.getTextureManager().bindTexture(client_player.getLocationSkin());

			for (int i1 = 0; i1 < 2; ++i1) {
				int j1 = i1 * 2 - 1;
				GL11.glPushMatrix();
				GL11.glTranslatef(-0F, -0.6F, 1.1F * (float)j1);
				GL11.glRotatef((float)(-45 * j1), 1F, 0F, 0F);
				GL11.glRotatef(-90F, 0F, 0F, 1F);
				GL11.glRotatef(59F, 0F, 0F, 1F);
				GL11.glRotatef((float)(-65 * j1), 0F, 1F, 0F);
				render = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
				renderplayer = (RenderPlayer)render;
				f10 = 1F;
				GL11.glScalef(f10, f10, f10);
				this.renderFirstPersonArm(renderplayer, this.mc.thePlayer);
				GL11.glPopMatrix();
			}

			f6 = client_player.getSwingProgress(partialTicks);
			f7 = MathHelper.sin(f6 * f6 * (float)Math.PI);
			f8 = MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI);
			GL11.glRotatef(-f7 * 20F, 0F, 1F, 0F);
			GL11.glRotatef(-f8 * 20F, 0F, 0F, 1F);
			GL11.glRotatef(-f8 * 80F, 1F, 0F, 0F);
			f9 = 0.38F;
			GL11.glScalef(f9, f9, f9);
			GL11.glRotatef(90F, 0F, 1F, 0F);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glTranslatef(-1F, -1F, 0F);
			f10 = 0.015625F;
			GL11.glScalef(f10, f10, f10);
			this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
			Tessellator tessellator = Tessellator.instance;
			GL11.glNormal3f(0F, 0F, -1F);
			tessellator.startDrawingQuads();
			byte b0 = 7;
			tessellator.addVertexWithUV((double)(0 - b0), (double)(128 + b0), 0D, 0D, 1D);
			tessellator.addVertexWithUV((double)(128 + b0), (double)(128 + b0), 0D, 1D, 1D);
			tessellator.addVertexWithUV((double)(128 + b0), (double)(0 - b0), 0D, 1D, 0D);
			tessellator.addVertexWithUV((double)(0 - b0), (double)(0 - b0), 0D, 0D, 0D);
			tessellator.draw();

			IItemRenderer custom = MinecraftForgeClient.getItemRenderer(itemstack, FIRST_PERSON_MAP);
			MapData mapdata = ((ItemMap)itemstack.getItem()).getMapData(itemstack, this.mc.theWorld);

			if (custom == null) {
				if (mapdata != null) {
					this.mc.entityRenderer.getMapItemRenderer().func_148250_a(mapdata, false);
				}
			} else {
				custom.renderItem(FIRST_PERSON_MAP, itemstack, mc.thePlayer, mc.getTextureManager(), mapdata);
			}

			GL11.glPopMatrix();
		} else if (itemstack != null) {
			GL11.glPushMatrix();
			f13 = 0.8F;

			if (client_player.getItemInUseCount() > 0) {
				EnumAction action = itemstack.getItemUseAction();

				if (action == EnumAction.eat || action == EnumAction.drink) {
					f6 = (float)client_player.getItemInUseCount() - partialTicks + 1F;
					f7 = 1F - f6 / (float)itemstack.getMaxItemUseDuration();
					f8 = 1F - f7;
					f8 = f8 * f8 * f8;
					f8 = f8 * f8 * f8;
					f8 = f8 * f8 * f8;
					f9 = 1F - f8;
					GL11.glTranslatef(0F, MathHelper.abs(MathHelper.cos(f6 / 4F * (float)Math.PI) * 0.1F) * (float)((double)f7 > 0.2D ? 1 : 0), 0F);
					GL11.glTranslatef(f9 * 0.6F, -f9 * 0.5F, 0F);
					GL11.glRotatef(f9 * 90F, 0F, 1F, 0F);
					GL11.glRotatef(f9 * 10F, 1F, 0F, 0F);
					GL11.glRotatef(f9 * 30F, 0F, 0F, 1F);
				}
			} else {
				f5 = client_player.getSwingProgress(partialTicks);
				f6 = MathHelper.sin(f5 * (float)Math.PI);
					f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI);
				GL11.glTranslatef(-f7 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI * 2F) * 0.2F, -f6 * 0.2F);
			}

			GL11.glTranslatef(0.7F * f13, -0.65F * f13 - (1F - f1) * 0.6F, -0.9F * f13);
			GL11.glRotatef(45F, 0F, 1F, 0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			f5 = client_player.getSwingProgress(partialTicks);
			f6 = MathHelper.sin(f5 * f5 * (float)Math.PI);
			f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI);
			GL11.glRotatef(-f6 * 20F, 0F, 1F, 0F);
			GL11.glRotatef(-f7 * 20F, 0F, 0F, 1F);
			GL11.glRotatef(-f7 * 80F, 1F, 0F, 0F);
			f8 = 0.4F;
			GL11.glScalef(f8, f8, f8);
			float f11;
			float f12;

			if (client_player.getItemInUseCount() > 0) {
				switch(itemstack.getItemUseAction()) {
					case block:
						GL11.glTranslatef(-0.5F, 0.2F, 0F);
						GL11.glRotatef(30F, 0F, 1F, 0F);
						GL11.glRotatef(-80F, 1F, 0F, 0F);
						GL11.glRotatef(60F, 0F, 1F, 0F);
						break;
					case bow:
						GL11.glRotatef(-18F, 0F, 0F, 1F);
						GL11.glRotatef(-12F, 0F, 1F, 0F);
						GL11.glRotatef(-8F, 1F, 0F, 0F);
						GL11.glTranslatef(-0.9F, 0.2F, 0F);
						f10 = (float)itemstack.getMaxItemUseDuration() - ((float)client_player.getItemInUseCount() - partialTicks + 1F);
						f11 = f10 / 20F;
						f11 = (f11 * f11 + f11 * 2F) / 3F;

						if (f11 > 1F) {
							f11 = 1F;
						}

						if (f11 > 0.1F) {
							GL11.glTranslatef(0F, MathHelper.sin((f10 - 0.1F) * 1.3F) * 0.01F * (f11 - 0.1F), 0F);
						}

						GL11.glTranslatef(0F, 0F, f11 * 0.1F);
						GL11.glRotatef(-335F, 0F, 0F, 1F);
						GL11.glRotatef(-50F, 0F, 1F, 0F);
						GL11.glTranslatef(0F, 0.5F, 0F);
						f12 = 1F + f11 * 0.2F;
						GL11.glScalef(1F, 1F, f12);
						GL11.glTranslatef(0F, -0.5F, 0F);
						GL11.glRotatef(50F, 0F, 1F, 0F);
						GL11.glRotatef(335F, 0F, 0F, 1F);
						break;
				}
			}

			if (itemstack.getItem().shouldRotateAroundWhenRendering()) {
				GL11.glRotatef(180F, 0F, 1F, 0F);
			}

			if (itemstack.getItem().requiresMultipleRenderPasses()) {
				this.renderItem(client_player, itemstack, 0, EQUIPPED_FIRST_PERSON);
				for (int x = 1; x < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); x++) {
					int k1 = itemstack.getItem().getColorFromItemStack(itemstack, x);
					f10 = (float)(k1 >> 16 & 255) / 255F;
					f11 = (float)(k1 >> 8 & 255) / 255F;
					f12 = (float)(k1 & 255) / 255F;
					GL11.glColor4f(1F * f10, 1F * f11, 1F * f12, 1F);
					this.renderItem(client_player, itemstack, x, EQUIPPED_FIRST_PERSON);
				}
			} else {
				this.renderItem(client_player, itemstack, 0, EQUIPPED_FIRST_PERSON);
			}

			GL11.glPopMatrix();
		} else if (!client_player.isInvisible()) {
			GL11.glPushMatrix();
			f13 = 0.8F;
			f5 = client_player.getSwingProgress(partialTicks);
			f6 = MathHelper.sin(f5 * (float)Math.PI);
			f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI);
			GL11.glTranslatef(-f7 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI * 2F) * 0.4F, -f6 * 0.4F);
			GL11.glTranslatef(0.8F * f13, -0.75F * f13 - (1F - f1) * 0.6F, -0.9F * f13);
			GL11.glRotatef(45F, 0F, 1F, 0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			f5 = client_player.getSwingProgress(partialTicks);
			f6 = MathHelper.sin(f5 * f5 * (float)Math.PI);
			f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float)Math.PI);
			GL11.glRotatef(f7 * 70F, 0F, 1F, 0F);
			GL11.glRotatef(-f6 * 20F, 0F, 0F, 1F);
			this.mc.getTextureManager().bindTexture(client_player.getLocationSkin());
			GL11.glTranslatef(-1F, 3.6F, 3.5F);
			GL11.glRotatef(120F, 0F, 0F, 1F);
			GL11.glRotatef(200F, 1F, 0F, 0F);
			GL11.glRotatef(-135F, 0F, 1F, 0F);
			GL11.glScalef(1F, 1F, 1F);
			GL11.glTranslatef(5.6F, 0F, 0F);
			render = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
			renderplayer = (RenderPlayer)render;
			f10 = 1F;
			GL11.glScalef(f10, f10, f10);
			this.renderFirstPersonArm(renderplayer, this.mc.thePlayer);
			GL11.glPopMatrix();
		}

		if (itemstack != null && itemstack.getItem() instanceof ItemCloth) {
			GL11.glDisable(GL11.GL_BLEND);
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();

		if (Loader.isModLoaded("battlegear2")) {
			mods.battlegear2.client.utils.BattlegearRenderHelper.renderItemInFirstPerson(partialTicks, mc, this);
		}
	}

	@Override
	public void updateEquippedItem() {
		super.updateEquippedItem();

		this.prevEquippedProgress = this.equippedProgress;
		EntityClientPlayerMP client_player = this.mc.thePlayer;
		ItemStack itemstack = client_player.inventory.getCurrentItem();
		boolean flag = this.equippedItemSlot == client_player.inventory.currentItem && itemstack == this.itemToRender;

		if (this.itemToRender == null && itemstack == null) {
			flag = true;
		}

		if (itemstack != null && this.itemToRender != null && itemstack != this.itemToRender && itemstack.getItem() == this.itemToRender.getItem() && itemstack.getItemDamage() == this.itemToRender.getItemDamage()) {
			this.itemToRender = itemstack;
			flag = true;
		}

		float f = 0.4F;
		float f1 = flag ? 1F : 0F;
		float f2 = f1 - this.equippedProgress;

		if (f2 < -f) f2 = -f;
		else if (f2 > f) f2 = f;

		this.equippedProgress += f2;

		if (this.equippedProgress < 0.1F) {
			this.itemToRender = itemstack;
			this.equippedItemSlot = client_player.inventory.currentItem;
		}
	}

	@Override
	public void resetEquippedProgress() {
		super.resetEquippedProgress();

		this.equippedProgress = 0F;
	}

	@Override
	public void resetEquippedProgress2() {
		super.resetEquippedProgress2();

		this.equippedProgress = 0F;
	}

}
