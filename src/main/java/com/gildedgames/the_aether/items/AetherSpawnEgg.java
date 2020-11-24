package com.gildedgames.the_aether.items;

import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import com.gildedgames.the_aether.entities.AetherEntities;
import com.gildedgames.the_aether.entities.AetherEntities.AetherEggInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLiving;	// Bad MCP name: should be 'Mob'
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class AetherSpawnEgg extends Item {

	public static HashMap<Integer, AetherEggInfo> egg_info_map = new LinkedHashMap<Integer, AetherEggInfo>();

	@SideOnly(Side.CLIENT)
	private IIcon icon;

	public AetherSpawnEgg() {
		setHasSubtypes(true);
		setCreativeTab(AetherCreativeTabs.misc);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		StringBuilder name = new StringBuilder(StatCollector.translateToLocal(this.getUnlocalizedName() + ".name").trim());
		String creature_name = AetherEntities.getStringFromID(stack.getItemDamage());
		if(creature_name != null) {
			name.append(' ').append(StatCollector.translateToLocal("entity." + creature_name + ".name"));
		}
		return name.toString();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack item_stack, int pass) {
		AetherEggInfo egginfo = egg_info_map.get(Integer.valueOf(item_stack.getItemDamage()));
		if(egginfo == null) return 0xffffff;
		return pass == 0 ? egginfo.primary_color : egginfo.secondary_color;
	}

	@Override
	public boolean onItemUse(ItemStack item_stack, EntityPlayer player, World world, int x, int y, int z, int side, float fx, float fy, float fz) {
		if (world.isRemote) return true;

		Block block = world.getBlock(x, y, z);
		x += Facing.offsetsXForSide[side];
		y += Facing.offsetsYForSide[side];
		z += Facing.offsetsZForSide[side];

		double y_offset = (side == 1 && block.getRenderType() == 11) ? 0.5D : 0D;
		Entity entity = spawn_creature(world, item_stack.getItemDamage(), (double)x + 0.5D, (double)y + y_offset, (double)z + 0.5D);
		if (entity != null) {
			if(entity instanceof EntityLiving && item_stack.hasDisplayName()) {
				((EntityLiving)entity).setCustomNameTag(item_stack.getDisplayName());
			}
			if (!player.capabilities.isCreativeMode) {
				--item_stack.stackSize;
			}
		}

		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item_stack, World world, EntityPlayer player) {
		if(world.isRemote) return item_stack;

		MovingObjectPosition moving_obj_pos = getMovingObjectPositionFromPlayer(world, player, true);
		if(moving_obj_pos == null) return item_stack;
		if(moving_obj_pos.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			int x = moving_obj_pos.blockX;
			int y = moving_obj_pos.blockY;
			int z = moving_obj_pos.blockZ;
			if(!world.canMineBlock(player, x, y, z)) {
				return item_stack;
			}
			if(!player.canPlayerEdit(x, y, z, moving_obj_pos.sideHit, item_stack)) {
				return item_stack;
			}
			if(world.getBlock(x, y, z) instanceof BlockLiquid) {
				Entity entity = spawn_creature(world, item_stack.getItemDamage(), (double)x, (double)y, (double)z);
				if (entity != null) {
					if (entity instanceof EntityLiving && item_stack.hasDisplayName()) {
						((EntityLiving)entity).setCustomNameTag(item_stack.getDisplayName());
					}
					if (!player.capabilities.isCreativeMode) {
						--item_stack.stackSize;
					}
				}
			}
		}

		return item_stack;
	}

	private static Entity spawn_creature(World world, int id, double x, double y, double z) {
		if (!egg_info_map.containsKey(Integer.valueOf(id))) {
			return null;
		}

		Entity entity = AetherEntities.createEntityByID(id, world);
		if(entity instanceof EntityLivingBase) {
			EntityLivingBase living_entity = (EntityLivingBase)entity;
			entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360F), 0F);
			living_entity.rotationYawHead = living_entity.rotationYaw;
			living_entity.renderYawOffset = living_entity.rotationYaw;
			if(living_entity instanceof EntityLiving) {
				EntityLiving mob = (EntityLiving)living_entity;
				mob.onSpawnWithEgg((IEntityLivingData)null);
				mob.playLivingSound();
			}
		}
		world.spawnEntityInWorld(entity);
		return entity;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int metadata, int pass) {
		return pass > 0 ? this.icon : super.getIconFromDamageForRenderPass(metadata, pass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		for(AetherEggInfo egg_info : egg_info_map.values()) {
			list.add(new ItemStack(item, 1, egg_info.spawn_entity_type_id));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		super.registerIcons(register);

		this.icon = register.registerIcon(this.getIconString() + "_overlay");
	}

}
