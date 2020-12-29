package com.gildedgames.the_aether.network.packets;

import com.gildedgames.the_aether.api.AetherAPI;
import com.gildedgames.the_aether.player.PlayerAether;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import io.netty.buffer.ByteBuf;

public class PoisonPacket extends AetherPacket<PoisonPacket> {
	private int entity_id;

	public PoisonPacket() {
	}

	public PoisonPacket(EntityPlayer player) {
		this.entity_id = player.getEntityId();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entity_id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entity_id);
	}

	@Override
	public void handleClient(PoisonPacket message, EntityPlayer player) {
		if(player == null || player.worldObj == null) return;
		Entity entity = player.worldObj.getEntityByID(message.entity_id);
		if(!(entity instanceof EntityPlayer)) return;
		EntityPlayer parent = (EntityPlayer)entity;
		((PlayerAether)AetherAPI.get(parent)).setPoisoned();
	}

	@Override
	public void handleServer(PoisonPacket message, EntityPlayer player) {
	}

}
