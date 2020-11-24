package com.gildedgames.the_aether.items.accessories;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.api.accessories.AccessoryType;
import com.gildedgames.the_aether.client.ClientProxy;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;

public class Accessory extends Item {

	public static final String ROOT = Aether.modAddress() + "textures/slots/slot_";

	protected final AccessoryType accessoryType;

	protected final AccessoryType extraType;

	public ResourceLocation texture, texture_inactive;

	private int colorHex = 0xdddddd;

	private boolean isDungeonLoot = false;

	private boolean hasInactiveTexture = false;

	public static final IBehaviorDispenseItem DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem() {
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			ItemStack itemstack = Accessory.dispenseAccessory(source, stack);
			return itemstack != null ? itemstack : super.dispenseStack(source, stack);
		}
	};

	public Accessory(AccessoryType type) {
		this.accessoryType = type;
		this.extraType = type == AccessoryType.RING ? AccessoryType.EXTRA_RING : type == AccessoryType.MISC ? AccessoryType.EXTRA_MISC : null;
		this.texture = Aether.locate("textures/armor/accessory_base.png");

		this.setMaxStackSize(1);
		this.setCreativeTab(AetherCreativeTabs.accessories);
		BlockDispenser.dispenseBehaviorRegistry.putObject(this, DISPENSER_BEHAVIOR);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister registry) {
		super.registerIcons(registry);

		ObjectIntIdentityMap orderedList = AccessoryType.createCompleteList();

		for (int i = 0; i < ClientProxy.ACCESSORY_ICONS.length; ++i) {
			ClientProxy.ACCESSORY_ICONS[i] = registry.registerIcon(Aether.find("slots/" + ((AccessoryType) orderedList.func_148745_a(i)).getDisplayName()));
		}
	}

	@SuppressWarnings("unchecked")
	public static ItemStack dispenseAccessory(IBlockSource blockSource, ItemStack stack) {
		EnumFacing facing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
		int x = blockSource.getXInt() + facing.getFrontOffsetX();
		int y = blockSource.getYInt() + facing.getFrontOffsetY();
		int z = blockSource.getZInt() + facing.getFrontOffsetZ();
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1));
		List<EntityPlayer> player_list = (List<EntityPlayer>)blockSource.getWorld().getEntitiesWithinAABB(EntityPlayer.class, aabb);
		if(player_list.isEmpty()) return null;
		EntityPlayer player = player_list.get(0);

		ItemStack itemstack = stack.copy();
		itemstack.stackSize = 1;

		PlayerAether player_data = PlayerAether.get((EntityPlayer)player);
		if(!player_data.getAccessoryInventory().setAccessorySlot(itemstack)) {
			return null;
		}

		--stack.stackSize;

		return stack;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		ItemStack heldItem = player.getHeldItem();
		if (heldItem != null) {
			if (PlayerAether.get(player).getAccessoryInventory().setAccessorySlot(heldItem.copy())) {
				--heldItem.stackSize;
				return heldItem;
			}
		}
		return super.onItemRightClick(stack, world, player);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		if(repair.getItem() != AetherItems.zanite_gemstone) return false;
		return toRepair.getItem() == AetherItems.zanite_ring ||
			toRepair.getItem() == AetherItems.zanite_pendant;
	}

	public AccessoryType getExtraType() {
		return this.extraType;
	}

	public AccessoryType getType() {
		return this.accessoryType;
	}

	public Item setColor(int color) {
		this.colorHex = color;
		return this;
	}

	public int getColor() {
		return this.colorHex;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return this.isDungeonLoot ? AetherItems.aether_loot : super.getRarity(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int meta) {
		return this.colorHex;
	}

	public Accessory setDungeonLoot() {
		this.isDungeonLoot = true;

		return this;
	}

	public Accessory setTexture(String location) {
		this.texture = Aether.locate("textures/armor/accessory_" + location + ".png");

		return this;
	}

	public Accessory setInactiveTexture(String location) {
		this.texture_inactive = new ResourceLocation("aether_legacy", "textures/armor/accessory_" + location + ".png");
		this.hasInactiveTexture = true;

		return this;
	}

	public boolean hasInactiveTexture() {
		return this.hasInactiveTexture;
	}

}
