package com.gildedgames.the_aether.network.packets;

import com.gildedgames.the_aether.world.AetherWorldProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldProvider;

public class ShouldCyclePacket extends AetherPacket<ShouldCyclePacket> {
	private boolean should_cycle;

	public ShouldCyclePacket() {
	}

	public ShouldCyclePacket(boolean should_cycle) {
		this.should_cycle = should_cycle;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.should_cycle = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.should_cycle);
	}

	@Override
	public void handleClient(ShouldCyclePacket message, EntityPlayer player) {
		if(player == null || player.worldObj == null) return;
		if(!(player.worldObj.provider instanceof AetherWorldProvider)) return;
		AetherWorldProvider provider = (AetherWorldProvider)player.worldObj.provider;
		provider.setShouldCycleCatchup(message.should_cycle);
	}

	@Override
	public void handleServer(ShouldCyclePacket message, EntityPlayer player) {
	}
}
