package com.gildedgames.the_aether.world;

import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.network.AetherNetwork;
import com.gildedgames.the_aether.network.packets.TimePacket;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.player.PlayerAether;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.block.Block;
import net.minecraftforge.client.IRenderHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AetherWorldProvider extends WorldProvider {

	private float[] colorsSunriseSunset = new float[4];

	private boolean eternalDay;
	private boolean shouldCycleCatchup;
	private long aether_time = 6000;

	public AetherWorldProvider() {
		super();
	}

	@Override
	protected void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerAether();
	}

	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		if (!AetherConfig.eternalDayDisabled()) {
			if (!this.worldObj.isRemote) {
				AetherData data = AetherData.getInstance(this.worldObj);
				if (data.isEternalDay()) {
					if (!data.isShouldCycleCatchup()) {
						long aether_time = data.getAetherTime();
						if(aether_time != (worldTime % 24000L) && aether_time != ((worldTime + 1) % 24000L) && aether_time != ((worldTime - 1) % 24000L)) {
							if(aether_time < 1) aether_time = 24000;
							aether_time -= 1;
							data.setAetherTime(aether_time % 24000L);
						} else {
							data.setShouldCycleCatchup(true);
						}
					} else {
						data.setAetherTime(worldTime);
					}

					this.aether_time = data.getAetherTime();
					AetherNetwork.sendToAll(new TimePacket(this.aether_time));
					data.setAetherTime(this.aether_time);
				} else {
					data.setAetherTime(6000);
				}
			}
		}

		int i = (int)(AetherConfig.eternalDayDisabled() ? worldTime : this.aether_time % 24000L);

		float f = ((float)i + partialTicks) / 24000F - 0.25F;

		if(f < 0F) f++;
		else if(f > 1F) f--;

		float f1 = 1F - (float)((Math.cos((double)f * Math.PI) + 1D) / 2D);
		f = f + (f1 - f) / 3F;
		return f;
	}

	public void setIsEternalDay(boolean value) {
		this.eternalDay = value;
	}

	public boolean getIsEternalDay() {
		return this.eternalDay;
	}

	public void setShouldCycleCatchup(boolean value) {
		this.shouldCycleCatchup = value;
	}

	public boolean getShouldCycleCatchup() {
		return this.shouldCycleCatchup;
	}

	public void setAetherTime(long time) {
		this.aether_time = time;
	}

	public long getAetherTime() {
		return this.aether_time;
	}

	@Override
	public float[] calcSunriseSunsetColors(float f, float f1) {
		float f2 = 0.4F;
		float f3 = MathHelper.cos(f * 3.141593F * 2F) - 0F;
		float f4 = -0F;

		if (f3 >= f4 - f2 && f3 <= f4 + f2) {
			float f5 = (f3 - f4) / f2 * 0.5F + 0.5F;
			float f6 = 1F - (1F - MathHelper.sin(f5 * 3.141593F)) * 0.99F;
			f6 *= f6;
			this.colorsSunriseSunset[0] = f5 * 0.3F + 0.1F;
			this.colorsSunriseSunset[1] = f5 * f5 * 0.7F + 0.2F;
			this.colorsSunriseSunset[2] = f5 * f5 * 0.7F + 0.2F;
			this.colorsSunriseSunset[3] = f6;
			return this.colorsSunriseSunset;
		} else {
			return null;
		}
	}

	@Override
	public int getRespawnDimension(EntityPlayerMP player) {
		return AetherConfig.should_always_respawn_in_aether() ||
			PlayerAether.get(player).getBedLocation() != null ?
				AetherConfig.get_aether_world_id() : 0;
	}

	@Override
	public boolean canCoordinateBeSpawn(int x, int z) {
		if(!AetherConfig.should_always_respawn_in_aether()) return false;
		Block block = worldObj.getTopBlock(x, z);
		return block == AetherBlocks.aether_grass || block == AetherBlocks.enchanted_aether_grass || block == AetherBlocks.aether_dirt;
	}

	@Override
	public boolean canRespawnHere() {
		return AetherConfig.should_always_respawn_in_aether();
	}

	@Override
	public IChunkProvider createChunkGenerator() {
		return new AetherChunkProvider(this.worldObj, this.worldObj.getSeed());
	}

	public boolean canDoLightning(Chunk chunk) {
		return false;
	}

	public boolean canDoRainSnowIce(Chunk chunk) {
		return false;
	}

	@Override
	public Vec3 getFogColor(float f, float f1) {
		int i = 0x9393BC;

		float f2 = MathHelper.cos(f * 3.141593F * 2F) * 2F + 0.5F;
		if(f2 < 0F) f2 = 0F;
		else if(f2 > 1F) f2 = 1F;
		float f3 = (i >> 16 & 0xff) / 255F;
		float f4 = (i >> 8 & 0xff) / 255F;
		float f5 = (i & 0xff) / 255F;
		f3 *= f2 * 0.94F + 0.06F;
		f4 *= f2 * 0.94F + 0.06F;
		f5 *= f2 * 0.91F + 0.09F;

		return Vec3.createVectorHelper(f3, f4, f5);
	}

	@Override
	public String getSaveFolder() {
		return "AETHER";
	}

	@Override
	public double getVoidFogYFactor() {
		return 100;
	}

	@Override
	public boolean doesXZShowFog(int x, int z) {
		return false;
	}

	@Override
	public boolean isSkyColored() {
		return false;
	}

	@Override
	public double getHorizon() {
		return 0;
	}

	@Override
	public float getCloudHeight() {
		return 8F;
	}

	@Override
	public String getDimensionName() {
		return "the_aether";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean getWorldHasVoidParticles() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public net.minecraftforge.client.IRenderHandler getWeatherRenderer() {
		return new IRenderHandler() {
			@Override
			public void render(float partialTicks, net.minecraft.client.multiplayer.WorldClient world, net.minecraft.client.Minecraft mc) {
			}
		};
	}
}
