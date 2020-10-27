package com.gildedgames.the_aether;

import com.gildedgames.the_aether.api.player.IPlayerAether;
import com.gildedgames.the_aether.entities.passive.mountable.Aerbunny;
import com.gildedgames.the_aether.network.AetherNetwork;
import com.gildedgames.the_aether.network.packets.EternalDayPacket;
import com.gildedgames.the_aether.network.packets.ShouldCyclePacket;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.world.AetherData;
import com.gildedgames.the_aether.world.AetherWorldProvider;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.*;
import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.blocks.portal.BlockAetherPortal;
import com.gildedgames.the_aether.entities.AetherEntities;
import com.gildedgames.the_aether.entities.bosses.Valkyrie;
import com.gildedgames.the_aether.entities.passive.mountable.FlyingCow;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.items.dungeon.DungeonKey;
import com.gildedgames.the_aether.items.util.SkyrootBucketType;
import com.gildedgames.the_aether.items.weapons.ItemSkyrootSword;
import com.gildedgames.the_aether.registry.achievements.AetherAchievements;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.event.world.WorldEvent;
import java.util.Random;

public class AetherEventHandler {

	@SubscribeEvent
	public void checkBlockBannedEvent(PlayerInteractEvent event) {
		if(event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;
		EntityPlayer player = event.entityPlayer;
		if (player.dimension == AetherConfig.get_aether_world_id()) {
			ItemStack currentStack = player.getCurrentEquippedItem();
			if (currentStack != null) {
				if (currentStack.getItem() == Items.flint_and_steel || currentStack.getItem() == Item.getItemFromBlock(Blocks.torch) || currentStack.getItem() == Items.fire_charge) {
					for (int i = 0; i < 10; ++i) {
						event.world.spawnParticle("smoke", event.x, event.y, event.z, 0D, 0D, 0D);
					}

					event.setCanceled(true);
				}
			} else if (event.world.getBlock(event.x, event.y, event.z) == Blocks.bed) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntityInteract(EntityInteractEvent event) {
		if (event.target instanceof EntityAgeable) {
			ItemStack itemstack = event.entityPlayer.inventory.getCurrentItem();

			if (itemstack != null && itemstack.getItem() == AetherItems.aether_spawn_egg) {
				if (!event.entityPlayer.worldObj.isRemote) {
					Class<?> oclass = AetherEntities.getClassFromID(itemstack.getItemDamage());

					if (oclass != null && oclass.isAssignableFrom(this.getClass())) {
						EntityAgeable child = ((EntityAgeable)event.target).createChild((EntityAgeable) event.target);

						if (child != null) {
							child.setGrowingAge(-24000);
							child.setLocationAndAngles(event.target.posX, event.target.posY, event.target.posZ, 0F, 0F);
							event.entityPlayer.worldObj.spawnEntityInWorld(child);

							if (itemstack.hasDisplayName()) {
								child.setCustomNameTag(itemstack.getDisplayName());
							}

							if (!event.entityPlayer.capabilities.isCreativeMode) {
								--itemstack.stackSize;

								if (itemstack.stackSize <= 0) {
									event.entityPlayer.inventory.setInventorySlotContents(event.entityPlayer.inventory.currentItem, (ItemStack)null);
								}
							}
						}
					}
				}
			}
		}

		if (event.target instanceof EntityCow || event.target instanceof FlyingCow) {
			EntityPlayer player = event.entityPlayer;
			ItemStack heldItem = player.getCurrentEquippedItem();

			if (heldItem != null && heldItem.getItem() == AetherItems.skyroot_bucket && SkyrootBucketType.getType(heldItem.getItemDamage()) == SkyrootBucketType.Empty) {
				if (!player.capabilities.isCreativeMode) {
					--heldItem.stackSize;
				}

				if (heldItem.stackSize <= 0) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(AetherItems.skyroot_bucket, 1, SkyrootBucketType.Milk.meta));
				} else if (!player.inventory.addItemStackToInventory(new ItemStack(AetherItems.skyroot_bucket, 1, SkyrootBucketType.Milk.meta))) {
					player.dropPlayerItemWithRandomChoice(new ItemStack(AetherItems.skyroot_bucket, 1, SkyrootBucketType.Milk.meta), false);
				}
			}
		}
	}

	@SubscribeEvent
	public void onFillBucket(FillBucketEvent event) {
		MovingObjectPosition target = event.target;
		if(target == null || target.typeOfHit != MovingObjectType.BLOCK) return;

		World world = event.world;
		if(!(world.provider instanceof AetherWorldProvider) && !world.provider.getDimensionName().equalsIgnoreCase(AetherConfig.get_travel_world_name())) {
			return;
		}

		int x = target.blockX;
		int y = target.blockY;
		int z = target.blockZ;
		switch(target.sideHit) {
			case 0:
				y--;
				break;
			case 1:
				y++;
				break;
			case 2:
				z--;
				break;
			case 3:
				z++;
				break;
			case 4:
				x--;
				break;
		}

		EntityPlayer player = event.entityPlayer;
		ItemStack stack = event.current;
		Item item = stack.getItem();

		if((!AetherConfig.activateOnlyWithSkyroot() && item == Items.water_bucket) || (item == AetherItems.skyroot_bucket && stack.getItemDamage() == 1)) {
			// Water
			if (((BlockAetherPortal)BlocksAether.aether_portal).trySpawnPortal(world, x, y, z)) {
				if (!player.capabilities.isCreativeMode) {
					if(item == AetherItems.skyroot_bucket && stack.getItemDamage() == 1) {
						event.result = new ItemStack(AetherItems.skyroot_bucket);
					} else if(item == Items.water_bucket) {
						event.result = new ItemStack(Items.bucket);
					}
				}

				event.setResult(Event.Result.ALLOW);
			}
		} else if(item == Items.lava_bucket && world.provider instanceof AetherWorldProvider) {
			// Lava
			if (player.capabilities.isCreativeMode && player.isSneaking()) {
				return;
			}

			if (world.isAirBlock(x, y, z)) {
				world.setBlock(x, y, z, BlocksAether.aerogel);

				if (!player.capabilities.isCreativeMode) {
					event.result = new ItemStack(Items.bucket);
				}
			}

			event.setResult(Event.Result.ALLOW);
		}
	}

	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event) {
		if (this.isGravititeTool(event.crafting.getItem())) {
			event.player.triggerAchievement(AetherAchievements.grav_tools);
		} else if (event.crafting.getItem() == Item.getItemFromBlock(BlocksAether.enchanter)) {
			event.player.triggerAchievement(AetherAchievements.enchanter);
		}
	}

	@SubscribeEvent
	public void onEntityDropLoot(LivingDropsEvent event) {
		if (event.source instanceof EntityDamageSource) {
			EntityLivingBase entity = event.entityLiving;
			EntityDamageSource source = (EntityDamageSource) event.source;

			if (source.getEntity() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) source.getEntity();
				ItemStack currentItem = player.inventory.getCurrentItem();

				if (currentItem != null && currentItem.getItem() instanceof ItemSkyrootSword && !(entity instanceof EntityPlayer) && !(entity instanceof EntityWither) && !(entity instanceof Valkyrie)) {
					for (EntityItem items : event.drops) {
						ItemStack stack = items.getEntityItem();

						if (!(stack.getItem() instanceof DungeonKey) && stack.getItem() != AetherItems.victory_medal && stack.getItem() != Items.skull) {
							EntityItem item = new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, items.getEntityItem());

							entity.worldObj.spawnEntityInWorld(item);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entity;
			if (player.inventory.getCurrentItem() != null) {
				if (player.inventory.getCurrentItem().getItem() == AetherItems.lightning_sword || player.inventory.getCurrentItem().getItem() == AetherItems.lightning_knife) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityDamage(LivingAttackEvent event) {
		if (event.entityLiving instanceof Aerbunny) {
			Aerbunny aerbunny = (Aerbunny)event.entityLiving;
			if (aerbunny.isRiding() && aerbunny.ridingEntity instanceof EntityPlayer) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntityAttack(AttackEntityEvent event) {
		if (event.entityPlayer.getHeldItem() == null) return;
		if (event.entityPlayer.getHeldItem().getItem() == AetherItems.flaming_sword) {
			if (event.target.canAttackWithItem()) {
				if (!event.target.hitByEntity(event.entityPlayer)) {
					if (event.target instanceof EntityLivingBase) {
						int defaultTime = 30;
						int fireAspectModifier = EnchantmentHelper.getFireAspectModifier(event.entityPlayer);
						if (fireAspectModifier > 0) {
							defaultTime += (fireAspectModifier * 4);
						}
						event.target.setFire(defaultTime);
					}
				}
			}
		} else if (event.entityPlayer.getHeldItem().getItem() == AetherItems.pig_slayer) {
			String s = EntityList.getEntityString((Entity)event.target);

			if (s != null && (s.toLowerCase().contains("pig") || s.toLowerCase().contains("phyg") || s.toLowerCase().contains("taegore") || event.target.getUniqueID().toString().equals("1d680bb6-2a9a-4f25-bf2f-a1af74361d69"))) {
				if (event.target.worldObj.isRemote) {
					for (int j = 0; j < 20; j++) {
						Random itemRand = new Random();
						double d = itemRand.nextGaussian() * 0.02D;
						double d1 = itemRand.nextGaussian() * 0.02D;
						double d2 = itemRand.nextGaussian() * 0.02D;
						double d3 = 5D;
						event.target.worldObj.spawnParticle("flame", (event.target.posX + (double) (itemRand.nextFloat() * event.target.width * 2F)) - (double) event.target.width - d * d3, (event.target.posY + (double) (itemRand.nextFloat() * event.target.height)) - d1 * d3, (event.target.posZ + (double) (itemRand.nextFloat() * event.target.width * 2F)) - (double) event.target.width - d2 * d3, d, d1, d2);
					}
				}
			}
		}
	}

	public boolean isGravititeTool(Item stackID) {
		return stackID == AetherItems.gravitite_shovel || stackID == AetherItems.gravitite_axe || stackID == AetherItems.gravitite_pickaxe;
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if (!event.world.isRemote) {
			AetherData data = AetherData.getInstance(event.world);

			WorldProvider provider = event.world.provider;

			if (provider instanceof AetherWorldProvider) {
				AetherWorldProvider providerAether = (AetherWorldProvider)provider;

				providerAether.setIsEternalDay(data.isEternalDay());
				AetherNetwork.sendToAll(new EternalDayPacket(providerAether.getIsEternalDay()));

				providerAether.setShouldCycleCatchup(data.isShouldCycleCatchup());
				AetherNetwork.sendToAll(new ShouldCyclePacket(providerAether.getShouldCycleCatchup()));
			}
		}

		for (Object entity : event.world.loadedEntityList) {
			if (entity instanceof EntityItem) {
				EntityItem item_entity = (EntityItem)entity;

				if (item_entity.getEntityItem().getItem() == AetherItems.dungeon_key) {
					ObfuscationReflectionHelper.setPrivateValue(Entity.class, item_entity, true, "invulnerable", "field_83001_bt");
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerSleepInBed(PlayerWakeUpEvent event) {
		final World world = event.entityPlayer.worldObj;

		if (!world.isRemote && event.entityPlayer.dimension == AetherConfig.get_aether_world_id()) {
			final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

			final WorldServer server_world = server.worldServerForDimension(0);

			if (server_world.playerEntities.size() > 0) {
				if (server_world.areAllPlayersAsleep()) {
					performTimeSet(event, world, server_world);
				}
			} else {
				performTimeSet(event, world, server_world);
			}
		}
	}

	@SubscribeEvent
	public void onFall(LivingFallEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			IPlayerAether playerAether = PlayerAether.get((EntityPlayer) event.entityLiving);

			if (playerAether.getAccessoryInventory().wearingArmor(new ItemStack(AetherItems.sentry_boots)) || playerAether.getAccessoryInventory().isWearingGravititeSet() || playerAether.getAccessoryInventory().isWearingValkyrieSet()) {
				event.setCanceled(true);
			}
		}
	}

	private void performTimeSet(PlayerWakeUpEvent event, World world, WorldServer server_world) {
		if (world.getGameRules().getGameRuleBooleanValue("doDaylightCycle") && event.entityPlayer.isPlayerFullyAsleep()) {
			final long i = server_world.getWorldInfo().getWorldTime() + 24000L;

			server_world.getWorldInfo().setWorldTime(i - i % 24000L);

			PlayerAether.get(event.entityPlayer).setBedLocation(event.entityPlayer.getBedLocation(AetherConfig.get_aether_world_id()));
		}
	}
}
