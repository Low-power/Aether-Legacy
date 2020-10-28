package com.gildedgames.the_aether.network.packets;

import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.world.AetherData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.FMLCommonHandler;

public class SetTimePacket extends AetherPacket<SetTimePacket> {
	public SetTimePacket() {
	}

	public SetTimePacket(float time, EntityPlayer player) {
		this.time = time;
		this.world_id = player.dimension;
	}

	public float time;
	public int world_id;

	@Override
	public void fromBytes(ByteBuf buf) {
		this.world_id = buf.readInt();
		this.time = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.world_id);
		buf.writeFloat(this.time);
	}

	@Override
	public void handleClient(SetTimePacket message, EntityPlayer player) {
	}

	@Override
	public void handleServer(SetTimePacket message, EntityPlayer player) {
		message.setTime(message.time, message.world_id);
	}

	public void setTime(float value, int world_id) {
		if(world_id == AetherConfig.get_aether_world_id()) {
			AetherData data = AetherData.getInstance(net.minecraftforge.common.DimensionManager.getWorld(world_id));
			if(!data.isEternalDay() || !data.isShouldCycleCatchup()) return;
		}
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		for(World world : server.worldServers) {
			long should_time = (long)(24000L * value);
			long world_time = world.getWorldInfo().getWorldTime();
			long remainder = world_time % 24000L;
			long add = should_time > remainder ? should_time - remainder : should_time + 24000 - remainder;
			world.setWorldTime(world_time + add);
		}
	}

}
