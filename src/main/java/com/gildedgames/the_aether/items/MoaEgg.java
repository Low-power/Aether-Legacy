package com.gildedgames.the_aether.items;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.api.AetherAPI;
import com.gildedgames.the_aether.api.moa.AetherMoaType;
import com.gildedgames.the_aether.entities.passive.mountable.Moa;
import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;

public class MoaEgg extends Item {

	@SideOnly(Side.CLIENT)
	private IIcon spot_icon;

	public MoaEgg() {
		setMaxStackSize(1);
		setCreativeTab(AetherCreativeTabs.misc);
		setTextureName(Aether.find("misc/egg/moa_egg"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int metadata, int pass) {
		return pass > 0 ? this.spot_icon : super.getIconFromDamageForRenderPass(metadata, pass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister registry) {
		super.registerIcons(registry);

		this.spot_icon = registry.registerIcon(Aether.find("misc/egg/moa_egg_spot"));
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return CreativeTabs.creativeTabArray;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int facing, float hitX, float hitY, float hitZ) {
		if(!world.isRemote && player.capabilities.isCreativeMode) {
			Moa moa = new Moa(world, AetherAPI.instance().getMoaType(stack.getTagCompound().getInteger("typeId")));
			moa.setPositionAndRotation(x, y + 1, z, 1F, 1F);
			moa.setPlayerGrown(true);
			world.spawnEntityInWorld(moa);
			return true;
		}

		return super.onItemUse(stack, player, world, x, y, z, facing, hitX, hitY, hitZ);
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void getSubItems(Item item, CreativeTabs tab, List subitems) {
		for (int i = 0; i < AetherAPI.instance().getMoaTypeSize(); i++) {
			ItemStack stack = new ItemStack(this);
			NBTTagCompound compound = new NBTTagCompound();
			AetherMoaType moa_type = AetherAPI.instance().getMoaType(i);
			if(moa_type.getCreativeTab() == tab || tab == null || tab == CreativeTabs.tabAllSearch) {
				compound.setInteger("typeId", i);
				stack.setTagCompound(compound);
				subitems.add(stack);
			}
		}
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			AetherMoaType moaType = AetherAPI.instance().getMoaType(tag.getInteger("typeId"));
			return moaType.getMoaEggColor();
		}
		return AetherAPI.instance().getMoaType(0).getMoaEggColor();
	}

	public AetherMoaType getMoaTypeFromItemStack(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			AetherMoaType moaType = AetherAPI.instance().getMoaType(tag.getInteger("typeId"));
			return moaType;
		}
		return AetherAPI.instance().getMoaType(0);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null && stack.getTagCompound().hasKey("typeId")) {
			AetherMoaType moaType = AetherAPI.instance().getMoaType(tag.getInteger("typeId"));
			return "item." + moaType.getRegistryName().getResourcePath().replace(" ", "_").toLowerCase() + "_moa_egg.name";
		}
		return super.getUnlocalizedName();
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return super.getItemStackDisplayName(stack).replace(".name", "");
	}

	public static ItemStack getStackFromType(AetherMoaType type) {
		ItemStack stack = new ItemStack(AetherItems.moa_egg);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("typeId", AetherAPI.instance().getMoaTypeId(type));
		stack.setTagCompound(tag);
		return stack;
	}

}
