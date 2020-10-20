package com.gildedgames.the_aether.entities.effects;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.api.AetherAPI;
import com.gildedgames.the_aether.network.AetherNetwork;
import com.gildedgames.the_aether.network.packets.PoisonPacket;
import com.gildedgames.the_aether.player.PlayerAether;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

public class InebriationPotion extends Potion {
	public static Potion inebriation = new InebriationPotion();

	private int duration;

	public double rotD, motD;

	public InebriationPotion() {
		super(AetherConfig.getInebriationId(), true, 0x51297B);
		this.duration = 0;
		this.setPotionName("Inebriation");
		this.setIconIndex(0, 0);
	}

	public boolean isReady(int duration, int amplifier) {
		this.duration = duration;
		return true;
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		this.distractEntity(entity);

		if (this.duration % 50 == 0) {
			entity.attackEntityFrom(new DamageSource("inebriation"), 1F);
		}

		if (entity instanceof EntityPlayer && this.duration >= 500) {
			((PlayerAether)AetherAPI.get((EntityPlayer)entity)).setPoisoned();
			AetherNetwork.sendToAll(new PoisonPacket((EntityPlayer)entity));
		}

	}

	public void distractEntity(EntityLivingBase entity) {
		double gaussian = entity.worldObj.rand.nextGaussian();
		double newMotD = 0.1D * gaussian;
		double newRotD = (Math.PI / 4D) * gaussian;

		this.motD = 0.2D * newMotD + (0.8D) * this.motD;
		entity.motionX += this.motD;
		entity.motionZ += this.motD;
		this.rotD = 0.125D * newRotD + (1D - 0.125D) * this.rotD;

		entity.rotationYaw = (float)((double)entity.rotationYaw + rotD);
		entity.rotationPitch = (float)((double)entity.rotationPitch + rotD);

		if (entity.worldObj instanceof WorldServer) {
			((WorldServer)entity.worldObj).func_147487_a("iconcrack_" + Item.getIdFromItem(Items.dye) + "_" + 1, entity.posX, entity.boundingBox.minY + entity.height * 0.8D, entity.posZ, 2, 0D, 0D, 0D, 0D);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getStatusIconIndex() {
		Minecraft.getMinecraft().renderEngine.bindTexture(Aether.locate("textures/gui/inventory/inebriation.png"));
		return super.getStatusIconIndex();
	}
}
