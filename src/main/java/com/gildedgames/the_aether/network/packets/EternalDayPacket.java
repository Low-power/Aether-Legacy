package com.gildedgames.the_aether.network.packets;

import com.gildedgames.the_aether.world.AetherWorldProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldProvider;

public class EternalDayPacket extends AetherPacket<EternalDayPacket> {
	private boolean is_eternal_day;

	public EternalDayPacket() {
	}

	public EternalDayPacket(boolean is_eternal_day) {
		this.is_eternal_day = is_eternal_day;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.is_eternal_day = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.is_eternal_day);
	}

	@Override
	public void handleClient(EternalDayPacket message, EntityPlayer player) {
		if(player == null || player.worldObj == null) return;
		if(!(player.worldObj.provider instanceof AetherWorldProvider)) return;
		AetherWorldProvider provider = (AetherWorldProvider)player.worldObj.provider;
		provider.setIsEternalDay(message.is_eternal_day);
	}

	@Override
	public void handleServer(EternalDayPacket message, EntityPlayer player) {
	}
}
