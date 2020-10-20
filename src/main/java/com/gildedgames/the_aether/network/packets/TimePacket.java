package com.gildedgames.the_aether.network.packets;

import com.gildedgames.the_aether.world.AetherWorldProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;

public class TimePacket extends AetherPacket<TimePacket> {
	private long time;

	public TimePacket() {
	}

	public TimePacket(long time) {
		this.time = time;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.time = buf.readLong();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.time);
	}

	@Override
	public void handleClient(TimePacket message, EntityPlayer player) {
		if(player == null || player.worldObj == null) return;
		if(!(player.worldObj.provider instanceof AetherWorldProvider)) return;
		AetherWorldProvider provider = (AetherWorldProvider)player.worldObj.provider;
		provider.setAetherTime(message.time);
	}

	@Override
	public void handleServer(TimePacket message, EntityPlayer player) {
	}
}
