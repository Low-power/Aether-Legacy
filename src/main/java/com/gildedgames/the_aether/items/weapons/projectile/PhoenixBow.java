package com.gildedgames.the_aether.items.weapons.projectile;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.entities.projectile.EntityPhoenixArrow;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.registry.creative_tabs.AetherCreativeTabs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PhoenixBow extends ItemBow {

	@SideOnly(Side.CLIENT)
	private IIcon[] iconArray;

	public PhoenixBow() {
		super();

		this.maxStackSize = 1;
		this.setMaxDamage(384);
		this.setFull3D();
		this.setTextureName(Aether.find("weapons/bow"));
		this.setCreativeTab(AetherCreativeTabs.weapons);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getItemIconForUseDuration(int p_94599_1_) {
		return this.iconArray[p_94599_1_];
	}

	@Override
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		if (usingItem != null && stack.getItem() == AetherItems.phoenix_bow) {
			int j = stack.getMaxItemUseDuration() - useRemaining;

			if (j >= 18) {
				return this.getItemIconForUseDuration(2);
			}

			if (j > 13) {
				return this.getItemIconForUseDuration(1);
			}

			if (j > 0) {
				return this.getItemIconForUseDuration(0);
			}
		}

		return getIcon(stack, renderPass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister p_94581_1_) {
		this.itemIcon = p_94581_1_.registerIcon(Aether.find("weapons/phoenix_bow"));
		this.iconArray = new IIcon[bowPullIconNameArray.length];

		for (int i = 0; i < this.iconArray.length; ++i) {
			this.iconArray[i] = p_94581_1_.registerIcon(this.getIconString() + "_" + bowPullIconNameArray[i]);
		}
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return AetherItems.aether_loot;
	}

	private ItemStack findAmmo(EntityPlayer player) {
		for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
			ItemStack itemstack = player.inventory.getStackInSlot(i);

			if (this.isArrow(itemstack)) {
				return itemstack;
			}
		}

		return null;
	}

	protected boolean isArrow(ItemStack stack) {
		return stack != null && stack.getItem() == Items.arrow;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int time_left) {
		boolean infinity_arrow = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;
		ItemStack arrow_item_stack = findAmmo(player);

		int charge = this.getMaxItemUseDuration(stack) - time_left;
		ArrowLooseEvent event = new ArrowLooseEvent(player, stack, charge);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) return;
		charge = event.charge;
		if(charge < 0) return;

		if (arrow_item_stack != null || infinity_arrow) {
			if (arrow_item_stack == null) {
				arrow_item_stack = new ItemStack(Items.arrow);
			}

			float f = getArrowVelocity(charge);

			if ((double)f >= 0.1D) {
				if (!world.isRemote) {
					EntityPhoenixArrow arrow_entity = createArrow(world, f * 2F, arrow_item_stack, player);

					if (f == 1F) {
						arrow_entity.setIsCritical(true);
					}

					int power_level = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
					if(power_level > 0) {
						arrow_entity.setDamage(arrow_entity.getDamage() + (double)power_level * 0.5D + 0.5D);
					}

					int punch_level = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
					if(punch_level > 0) {
						arrow_entity.setKnockbackStrength(punch_level);
					}

					int flame_level = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
					if(flame_level > 0) {
						arrow_entity.setFire(1200);
					}

					stack.damageItem(1, player);

					if (infinity_arrow) {
						arrow_entity.canBePickedUp = 2;
					}

					world.spawnEntityInWorld(arrow_entity);
				}

				world.playSoundAtEntity(player, "random.bow", 1F, 1F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

				if (!infinity_arrow) {
					if (--arrow_item_stack.stackSize == 0) {
						player.inventory.consumeInventoryItem(arrow_item_stack.getItem());
					}
				}
			}
		}
	}

	public EntityPhoenixArrow createArrow(World world, float distance, ItemStack stack, EntityLivingBase shooter) {
		EntityPhoenixArrow arrow_entity = new EntityPhoenixArrow(world, shooter, distance);
		return arrow_entity;
	}

	public static float getArrowVelocity(int charge) {
		float f = (float) charge / 20F;
		f = (f * f + f * 2F) / 3F;

		if (f > 1F) {
			f = 1F;
		}

		return f;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.bow;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack heldItem, World world, EntityPlayer player) {
		boolean have_arrow = findAmmo(player) != null;

		ArrowNockEvent event = new ArrowNockEvent(player, heldItem);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return event.result;
		}

		if (player.capabilities.isCreativeMode || have_arrow) {
			player.setItemInUse(heldItem, this.getMaxItemUseDuration(heldItem));
		}

		return heldItem;
	}

	@Override
	public int getItemEnchantability() {
		return 1;
	}

}
