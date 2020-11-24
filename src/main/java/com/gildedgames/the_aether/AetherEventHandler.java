package com.gildedgames.the_aether;

import com.gildedgames.the_aether.api.player.IPlayerAether;
import com.gildedgames.the_aether.entities.passive.mountable.Aerbunny;
import com.gildedgames.the_aether.network.AetherNetwork;
import com.gildedgames.the_aether.network.packets.EternalDayPacket;
import com.gildedgames.the_aether.network.packets.ShouldCyclePacket;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.world.AetherData;
import com.gildedgames.the_aether.world.AetherWorldProvider;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.blocks.portal.BlockAetherPortal;
import com.gildedgames.the_aether.entities.AetherEntities;
import com.gildedgames.the_aether.entities.bosses.Valkyrie;
import com.gildedgames.the_aether.entities.passive.mountable.FlyingCow;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.items.dungeon.DungeonKey;
import com.gildedgames.the_aether.items.util.SkyrootBucketType;
import com.gildedgames.the_aether.items.weapons.SkyrootSword;
import com.gildedgames.the_aether.registry.achievements.AetherAchievements;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.WorldEvent;
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
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.World;
import java.util.Random;

public class AetherEventHandler {

	@SubscribeEvent
	public void on_player_interact(PlayerInteractEvent event) {
		if(event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;
		EntityPlayer player = event.entityPlayer;
		if (player.dimension == AetherConfig.get_aether_world_id()) {
			ItemStack current_item_stack = player.getCurrentEquippedItem();
			if(current_item_stack != null) {
				Item item = current_item_stack.getItem();
				if(item == Items.flint_and_steel || item == Item.getItemFromBlock(Blocks.torch) || item == Items.fire_charge) {
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
	public void on_fill_bucket(FillBucketEvent event) {
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
			case 5:
				x++;
				break;
		}

		EntityPlayer player = event.entityPlayer;
		ItemStack stack = event.current;
		Item item = stack.getItem();

		if((!AetherConfig.activateOnlyWithSkyroot() && item == Items.water_bucket) || (item == AetherItems.skyroot_bucket && stack.getItemDamage() == 1)) {
			// Water
			if(((BlockAetherPortal)AetherBlocks.aether_portal).trySpawnPortal(world, x, y, z)) {
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
				world.setBlock(x, y, z, AetherBlocks.aerogel);
				if (!player.capabilities.isCreativeMode) {
					event.result = new ItemStack(Items.bucket);
				}
				event.setResult(Event.Result.ALLOW);
			}
		}
	}

	@SubscribeEvent
	public void on_crafting(ItemCraftedEvent event) {
		Item item = event.crafting.getItem();
		if(is_gravitite_tool(item)) {
			event.player.triggerAchievement(AetherAchievements.grav_tools);
		} else if(item == Item.getItemFromBlock(AetherBlocks.enchanter)) {
			event.player.triggerAchievement(AetherAchievements.enchanter);
		}
	}

	@SubscribeEvent
	public void on_living_entity_drop_loot(LivingDropsEvent event) {
		if(!(event.source instanceof EntityDamageSource)) return;

		EntityLivingBase entity = event.entityLiving;
		EntityDamageSource source = (EntityDamageSource)event.source;

		if (source.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) source.getEntity();
			ItemStack current_item = player.inventory.getCurrentItem();
			if(current_item != null && current_item.getItem() instanceof SkyrootSword && !(entity instanceof EntityPlayer) && !(entity instanceof EntityWither) && !(entity instanceof Valkyrie)) {
				for (EntityItem item_entity : event.drops) {
					ItemStack item_stack = item_entity.getEntityItem();
					Item item = item_stack.getItem();
					if(!(item instanceof DungeonKey) && item != AetherItems.victory_medal && item != Items.skull) {
						EntityItem another_item_entity = new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, item_stack);
						entity.worldObj.spawnEntityInWorld(another_item_entity);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void on_entity_struck_by_lighting(EntityStruckByLightningEvent event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entity;
			ItemStack current_item_stack = player.inventory.getCurrentItem();
			if(current_item_stack != null) {
				Item item = current_item_stack.getItem();
				if(item == AetherItems.lightning_sword || item == AetherItems.lightning_knife) {
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
		ItemStack held_item_stack = event.entityPlayer.getHeldItem();
		if(held_item_stack == null) return;

		Item held_item = held_item_stack.getItem();
		if(held_item == AetherItems.flaming_sword) {
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
		} else if (held_item == AetherItems.pig_slayer) {
			String s = EntityList.getEntityString((Entity)event.target);
			if (s != null && (s.toLowerCase().contains("pig") || s.toLowerCase().contains("phyg") || s.toLowerCase().contains("taegore") || event.target.getUniqueID().toString().equals("1d680bb6-2a9a-4f25-bf2f-a1af74361d69"))) {
				World world = event.target.worldObj;
				if(world.isRemote) {
					for (int j = 0; j < 20; j++) {
						Random random = new Random();
						double x = random.nextGaussian() * 0.02D;
						double y = random.nextGaussian() * 0.02D;
						double z = random.nextGaussian() * 0.02D;
						double size = 5D;
						world.spawnParticle("flame", (event.target.posX + (double)(random.nextFloat() * event.target.width * 2F)) - (double)event.target.width - x * size, (event.target.posY + (double)(random.nextFloat() * event.target.height)) - y * size, (event.target.posZ + (double)(random.nextFloat() * event.target.width * 2F)) - (double)event.target.width - z * size, z, y, z);
					}
				}
			}
		}
	}

	public boolean is_gravitite_tool(Item id) {
		return id == AetherItems.gravitite_shovel || id == AetherItems.gravitite_axe || id == AetherItems.gravitite_pickaxe;
	}

	@SubscribeEvent
	public void on_world_tick(TickEvent.WorldTickEvent event) {
		World world = event.world;

		if(!world.isRemote && world.provider instanceof AetherWorldProvider) {
			AetherWorldProvider provider = (AetherWorldProvider)world.provider;
			AetherData data = AetherData.getInstance(world);
			provider.setIsEternalDay(data.isEternalDay());
			AetherNetwork.sendToAll(new EternalDayPacket(provider.getIsEternalDay()));
			provider.setShouldCycleCatchup(data.isShouldCycleCatchup());
			AetherNetwork.sendToAll(new ShouldCyclePacket(provider.getShouldCycleCatchup()));
		}

		for (Object entity : world.loadedEntityList) {
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
	public void on_living_entity_fall(LivingFallEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			IPlayerAether aplayer = PlayerAether.get((EntityPlayer)event.entityLiving);
			if(aplayer.getAccessoryInventory().wearingArmor(new ItemStack(AetherItems.sentry_boots)) || aplayer.getAccessoryInventory().isWearingGravititeSet() || aplayer.getAccessoryInventory().isWearingValkyrieSet()) {
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
