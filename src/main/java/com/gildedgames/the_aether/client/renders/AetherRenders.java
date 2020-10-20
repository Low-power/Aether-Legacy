package com.gildedgames.the_aether.client.renders;

import com.gildedgames.the_aether.entities.bosses.valkyrie_queen.ValkyrieQueen;
import com.gildedgames.the_aether.entities.passive.EntityAerwhale;
import com.gildedgames.the_aether.entities.passive.EntityMiniCloud;
import com.gildedgames.the_aether.entities.passive.Sheepuff;
import com.gildedgames.the_aether.client.renders.block.AetherFlowerRenderer;
import com.gildedgames.the_aether.client.renders.block.BerryBushRenderer;
import com.gildedgames.the_aether.client.renders.block.TreasureChestBlockRenderer;
import com.gildedgames.the_aether.client.renders.entity.AechorPlantRenderer;
import com.gildedgames.the_aether.client.renders.entity.AerbunnyRenderer;
import com.gildedgames.the_aether.client.renders.entity.AerwhaleRenderer;
import com.gildedgames.the_aether.client.renders.entity.CockatriceRenderer;
import com.gildedgames.the_aether.client.renders.entity.CrystalRenderer;
import com.gildedgames.the_aether.client.renders.entity.DartBaseRenderer;
import com.gildedgames.the_aether.client.renders.entity.FireMinionRenderer;
import com.gildedgames.the_aether.client.renders.entity.FloatingBlockRenderer;
import com.gildedgames.the_aether.client.renders.entity.FlyingCowRenderer;
import com.gildedgames.the_aether.client.renders.entity.HammerProjectileRenderer;
import com.gildedgames.the_aether.client.renders.entity.LightningKnifeRenderer;
import com.gildedgames.the_aether.client.renders.entity.MimicRenderer;
import com.gildedgames.the_aether.client.renders.entity.MiniCloudRenderer;
import com.gildedgames.the_aether.client.renders.entity.MoaRenderer;
import com.gildedgames.the_aether.client.renders.entity.ParachuteRenderer;
import com.gildedgames.the_aether.client.renders.entity.PhoenixArrowRenderer;
import com.gildedgames.the_aether.client.renders.entity.PhygRenderer;
import com.gildedgames.the_aether.client.renders.entity.SentryRenderer;
import com.gildedgames.the_aether.client.renders.entity.SheepuffRenderer;
import com.gildedgames.the_aether.client.renders.entity.SliderRenderer;
import com.gildedgames.the_aether.client.renders.entity.SunSpiritRenderer;
import com.gildedgames.the_aether.client.renders.entity.SwetRenderer;
import com.gildedgames.the_aether.client.renders.entity.TNTPresentRenderer;
import com.gildedgames.the_aether.client.renders.entity.ValkyrieQueenRenderer;
import com.gildedgames.the_aether.client.renders.entity.ValkyrieRenderer;
import com.gildedgames.the_aether.client.renders.entity.WhirlwindRenderer;
import com.gildedgames.the_aether.client.renders.entity.ZephyrRenderer;
import com.gildedgames.the_aether.client.renders.entity.ZephyrSnowballRenderer;
import com.gildedgames.the_aether.client.renders.items.PhoenixBowRenderer;
import com.gildedgames.the_aether.entities.block.EntityFloatingBlock;
import com.gildedgames.the_aether.entities.block.EntityTNTPresent;
import com.gildedgames.the_aether.entities.bosses.EntityFireMinion;
import com.gildedgames.the_aether.entities.bosses.Valkyrie;
import com.gildedgames.the_aether.entities.bosses.slider.Slider;
import com.gildedgames.the_aether.entities.bosses.sun_spirit.SunSpirit;
import com.gildedgames.the_aether.entities.hostile.AechorPlant;
import com.gildedgames.the_aether.entities.hostile.Cockatrice;
import com.gildedgames.the_aether.entities.hostile.EntityMimic;
import com.gildedgames.the_aether.entities.hostile.EntitySentry;
import com.gildedgames.the_aether.entities.hostile.EntityWhirlwind;
import com.gildedgames.the_aether.entities.hostile.EntityZephyr;
import com.gildedgames.the_aether.entities.passive.mountable.Aerbunny;
import com.gildedgames.the_aether.entities.passive.mountable.FlyingCow;
import com.gildedgames.the_aether.entities.passive.mountable.Moa;
import com.gildedgames.the_aether.entities.passive.mountable.ParachuteEntity;
import com.gildedgames.the_aether.entities.passive.mountable.Phyg;
import com.gildedgames.the_aether.entities.passive.mountable.Swet;
import com.gildedgames.the_aether.entities.projectile.EntityHammerProjectile;
import com.gildedgames.the_aether.entities.projectile.EntityLightningKnife;
import com.gildedgames.the_aether.entities.projectile.EntityPhoenixArrow;
import com.gildedgames.the_aether.entities.projectile.ZephyrSnowballEntity;
import com.gildedgames.the_aether.entities.projectile.crystals.EntityCrystal;
import com.gildedgames.the_aether.entities.projectile.darts.BaseDartEntity;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.tileentity.TreasureChestTileEntity;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class AetherRenders {

	public static void initialization() {
		/* Misc */
		register(EntityHammerProjectile.class, new HammerProjectileRenderer());
		register(EntityFloatingBlock.class, new FloatingBlockRenderer());
		register(ParachuteEntity.class, new ParachuteRenderer());
		register(ZephyrSnowballEntity.class, new ZephyrSnowballRenderer());
		register(EntityPhoenixArrow.class, new PhoenixArrowRenderer());
		register(EntityLightningKnife.class, new LightningKnifeRenderer());

		/* Darts */
		register(BaseDartEntity.class, new DartBaseRenderer());

		/* Crystals */
		register(EntityCrystal.class, new CrystalRenderer());

		/* Bosses */
		register(Slider.class, new SliderRenderer());
		register(ValkyrieQueen.class, new ValkyrieQueenRenderer());
		register(SunSpirit.class, new SunSpiritRenderer());

		/* Hostile */
		register(EntityMimic.class, new MimicRenderer());
		register(EntitySentry.class, new SentryRenderer());
		register(AechorPlant.class, new AechorPlantRenderer());
		register(EntityFireMinion.class, new FireMinionRenderer());
		register(EntityZephyr.class, new ZephyrRenderer());
		register(Valkyrie.class, new ValkyrieRenderer());
		register(Cockatrice.class, new CockatriceRenderer());

		/* Passive */
		register(Moa.class, new MoaRenderer());
		register(Phyg.class, new PhygRenderer());
		register(FlyingCow.class, new FlyingCowRenderer());
		register(Sheepuff.class, new SheepuffRenderer());
		register(EntityAerwhale.class, new AerwhaleRenderer());
		register(Aerbunny.class, new AerbunnyRenderer());
		register(Swet.class, new SwetRenderer());
		register(EntityMiniCloud.class, new MiniCloudRenderer());
		register(EntityTNTPresent.class, new TNTPresentRenderer());
		register(EntityWhirlwind.class, new WhirlwindRenderer());

		MinecraftForgeClient.registerItemRenderer(AetherItems.phoenix_bow, new PhoenixBowRenderer());
		RenderingRegistry.registerBlockHandler(new BerryBushRenderer());
		RenderingRegistry.registerBlockHandler(new TreasureChestBlockRenderer());
		RenderingRegistry.registerBlockHandler(new AetherFlowerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TreasureChestTileEntity.class, new TreasureChestRenderer());
	}

	public static void register(Class<? extends Entity> entityClass, Render render) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, render);
	}
}
