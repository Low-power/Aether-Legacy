package com.gildedgames.the_aether.items.weapons.projectile;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.entities.projectile.darts.BaseDartEntity;
import com.gildedgames.the_aether.entities.projectile.darts.EnchantedDartEntity;
import com.gildedgames.the_aether.entities.projectile.darts.GoldenDartEntity;
import com.gildedgames.the_aether.entities.projectile.darts.PoisonDartEntity;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.items.util.EnumDartShooterType;
import com.gildedgames.the_aether.registry.creative_tabs.AetherCreativeTabs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;

public class DartShooter extends Item {

	@SideOnly(Side.CLIENT)
	private IIcon goldenIcon;

	@SideOnly(Side.CLIENT)
	private IIcon poisonIcon;

	@SideOnly(Side.CLIENT)
	private IIcon enchantedIcon;

	public DartShooter() {
		super();

		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setCreativeTab(AetherCreativeTabs.weapons);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister registry) {
		this.goldenIcon = registry.registerIcon(Aether.find("projectile/golden_dart_shooter"));
		this.poisonIcon = registry.registerIcon(Aether.find("projectile/poison_dart_shooter"));
		this.enchantedIcon = registry.registerIcon(Aether.find("projectile/enchanted_dart_shooter"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return meta == 1 ? this.poisonIcon : meta == 2 ? this.enchantedIcon : this.goldenIcon;
	}

	@Override
	public boolean isFull3D() {
		return false;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return stack.getItemDamage() == 2 ? EnumRarity.rare : super.getRarity(stack);
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void getSubItems(Item item, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < EnumDartShooterType.values().length; ++var4) {
			subItems.add(new ItemStack(this, 1, var4));
		}
	}

	private int consumeItem(EntityPlayer player, Item item_id, int max_damage) {
		IInventory inv = player.inventory;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);

			if (stack == null) {
				continue;
			}

			if(stack.getItem() != item_id) continue;
			int damage = stack.getItemDamage();
			if(damage == max_damage) {
				if (!player.capabilities.isCreativeMode) {
					--stack.stackSize;
				}

				if (stack.stackSize == 0) {
					stack = null;
				}

				inv.setInventorySlotContents(i, stack);

				return damage;
			}
		}

		return -1;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return this.getUnlocalizedName() + "_" + EnumDartShooterType.getType(itemstack.getItemDamage()).toString();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack heldItem, World world, EntityPlayer player) {
		int consume;

		if (!player.capabilities.isCreativeMode) {
			consume = this.consumeItem(player, AetherItems.dart, heldItem.getItemDamage());
		} else {
			consume = heldItem.getItemDamage();
		}

		if (consume != -1) {
			world.playSoundEffect(player.posX, player.posY, player.posZ, "aether_legacy:projectile.dart_shooter.shoot", 1F, 1F / (itemRand.nextFloat() * 0.4F + 0.8F));

			BaseDartEntity dart = null;

			if (consume == 1) {
				dart = new PoisonDartEntity(world, player, 1F);
			} else if (consume == 2) {
				dart = new EnchantedDartEntity(world, player, 1F);
			} else {
				dart = new GoldenDartEntity(world, player, 1F);
			}

			if (!world.isRemote) {
				world.spawnEntityInWorld(dart);
				if (dart != null) {
					dart.setGravityVelocity(0.99F);
				}
				dart.canBePickedUp = player.capabilities.isCreativeMode ? 2 : 1;
			}
		}

		return heldItem;
	}

}
