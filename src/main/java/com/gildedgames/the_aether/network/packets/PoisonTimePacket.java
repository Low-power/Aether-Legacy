package com.gildedgames.the_aether.network.packets;

import com.gildedgames.the_aether.api.AetherAPI;
import com.gildedgames.the_aether.player.PlayerAether;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import io.netty.buffer.ByteBuf;

public class PoisonTimePacket extends AetherPacket<PoisonTimePacket> {
	private int entity_id;
	private int time;

	public PoisonTimePacket() {
	}

	public PoisonTimePacket(EntityPlayer player, int time) {
		this.entity_id = player.getEntityId();
		this.time = time;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entity_id = buf.readInt();
		this.time = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entity_id);
		buf.writeInt(this.time);
	}

	@Override
	public void handleClient(PoisonTimePacket message, EntityPlayer player) {
		if(player == null || player.worldObj == null) return;
		Entity entity = player.worldObj.getEntityByID(message.entity_id);
		if(!(entity instanceof EntityPlayer)) return;
		EntityPlayer parent = (EntityPlayer)entity;
		((PlayerAether)AetherAPI.get(parent)).poisonTime = message.time;
	}

	@Override
	public void handleServer(PoisonTimePacket message, EntityPlayer player) {
	}
}
