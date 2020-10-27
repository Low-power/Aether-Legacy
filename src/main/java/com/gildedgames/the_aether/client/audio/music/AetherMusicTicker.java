package com.gildedgames.the_aether.client.audio.music;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.AetherConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class AetherMusicTicker implements IUpdatePlayerListBox {

	private final Random rand = new Random();
	private final Minecraft mc;
	private ISound current_music, current_record, menu_music, game_music;
	private int timeUntilNextMusic = 100;

	public AetherMusicTicker(Minecraft mc) {
		this.mc = mc;
	}

	public void update() {
		TrackType tracktype = this.getRandomTrack();

		if (this.mc.thePlayer != null) {
			if (this.mc.gameSettings.getSoundLevel(SoundCategory.MUSIC) == 0F) {
				if (this.current_music != null) {
					this.stopMusic();
					this.current_music = null;
				}

				return;
			}

			if (this.mc.thePlayer.dimension == AetherConfig.get_aether_world_id()) {
				if (this.current_music != null) {
					if (!this.mc.getSoundHandler().isSoundPlaying(this.current_music)) {
						this.current_music = null;
						this.timeUntilNextMusic = Math.min(MathHelper.getRandomIntegerInRange(this.rand, tracktype.getMinDelay(), tracktype.getMaxDelay()), this.timeUntilNextMusic);
					}
				}

				this.timeUntilNextMusic = Math.min(this.timeUntilNextMusic, tracktype.getMaxDelay());

				if (this.current_music == null && this.timeUntilNextMusic-- <= 0) {
					this.playMusic(tracktype);
				}
			} else {
				this.stopMusic();
			}
		}

		if (!this.mc.getSoundHandler().isSoundPlaying(this.menu_music)) {
			this.menu_music = null;
		}
	}

	public boolean playingMusic() {
		return this.current_music != null;
	}

	public boolean playingRecord() {
		return this.current_record != null;
	}

	public boolean playingMenuMusic() {
		return this.menu_music != null;
	}

	public boolean playingMinecraftMusic() {
		return this.game_music != null;
	}

	public ISound getRecord() {
		return this.current_record;
	}

	public AetherMusicTicker.TrackType getRandomTrack() {
		switch(this.rand.nextInt(4)) {
			case 0:
				return TrackType.TRACK_ONE;
			case 1:
				return TrackType.TRACK_TWO;
			case 2:
				return TrackType.TRACK_THREE;
			default:
				return TrackType.TRACK_FOUR;
		}
	}

	public void playMusic(TrackType type) {
		this.current_music = PositionedSoundRecord.func_147673_a(type.getMusicLocation());
		this.mc.getSoundHandler().playSound(this.current_music);
		this.timeUntilNextMusic = Integer.MAX_VALUE;
	}

	public void trackRecord(ISound record) {
		this.current_record = record;
	}

	public void trackMinecraftMusic(ISound record) {
		this.game_music = record;
	}

	public void playMenuMusic() {
		this.menu_music = PositionedSoundRecord.func_147673_a(TrackType.TRACK_MENU.getMusicLocation());
		this.mc.getSoundHandler().playSound(this.menu_music);
	}

	public void stopMusic() {
		if(this.current_music == null) return;
		this.mc.getSoundHandler().stopSound(this.current_music);
		this.current_music = null;
		this.timeUntilNextMusic = 0;
	}

	public void stopMenuMusic() {
		if(this.menu_music == null) return;
		this.mc.getSoundHandler().stopSound(this.menu_music);
		this.menu_music = null;
	}

	public void stopMinecraftMusic() {
		if(this.game_music == null) return;
		this.mc.getSoundHandler().stopSound(this.game_music);
		this.game_music = null;
	}

	@SideOnly(Side.CLIENT)
	public static enum TrackType {
		TRACK_ONE(Aether.locate("music.aether1"), 1200, 1500),
		TRACK_TWO(Aether.locate("music.aether2"), 1200, 1500),
		TRACK_THREE(Aether.locate("music.aether3"), 1200, 1500),
		TRACK_FOUR(Aether.locate("music.aether4"), 1200, 1500),
		TRACK_MENU(Aether.locate("music.menu"), 1200, 1500);

		private final ResourceLocation musicLocation;
		private final int minDelay;
		private final int maxDelay;

		private TrackType(ResourceLocation musicLocationIn, int minDelayIn, int maxDelayIn) {
			this.musicLocation = musicLocationIn;
			this.minDelay = minDelayIn;
			this.maxDelay = maxDelayIn;
		}

		public ResourceLocation getMusicLocation() {
			return this.musicLocation;
		}

		public int getMinDelay() {
			return this.minDelay;
		}

		public int getMaxDelay() {
			return this.maxDelay;
		}
	}

}
