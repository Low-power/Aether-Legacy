package com.gildedgames.the_aether.items.tools;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.entities.effects.InebriationEffect;
import com.gildedgames.the_aether.entities.effects.InebriationPotion;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.items.util.SkyrootBucketType;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.registry.AetherCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;

public class SkyrootBucket extends Item {

	@SideOnly(Side.CLIENT)
	private IIcon waterBucket;

	@SideOnly(Side.CLIENT)
	private IIcon poisonBucket;

	@SideOnly(Side.CLIENT)
	private IIcon remedyBucket;

	@SideOnly(Side.CLIENT)
	private IIcon milkBucket;

	public SkyrootBucket() {
		super();

		this.setHasSubtypes(true);
		this.setContainerItem(this);
		this.setCreativeTab(AetherCreativeTabs.misc);
		this.setTextureName(Aether.find("misc/buckets/skyroot_bucket"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister registry) {
		super.registerIcons(registry);

		this.waterBucket = registry.registerIcon(Aether.find("misc/buckets/skyroot_water_bucket"));
		this.poisonBucket = registry.registerIcon(Aether.find("misc/buckets/skyroot_poison_bucket"));
		this.remedyBucket = registry.registerIcon(Aether.find("misc/buckets/skyroot_remedy_bucket"));
		this.milkBucket = registry.registerIcon(Aether.find("misc/buckets/skyroot_milk_bucket"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return meta == 1 ? this.waterBucket : meta == 2 ? this.poisonBucket : meta == 3 ? this.remedyBucket : meta == 4 ? this.milkBucket : this.itemIcon;
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void getSubItems(Item item, CreativeTabs tab, List subItems) {
		for (int meta = 0; meta < SkyrootBucketType.values().length; ++meta) {
			subItems.add(new ItemStack(this, 1, meta));
		}
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return stack.getItemDamage() == 3 ? EnumRarity.rare : super.getRarity(stack);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return SkyrootBucketType.getType(stack.getItemDamage()) == SkyrootBucketType.Empty ? 16 : 1;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		int meta = itemstack.getItemDamage();
		return getUnlocalizedName() + "_" + SkyrootBucketType.getType(meta).toString();
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return false;
		MovingObjectPosition moving_obj_pos = this.getMovingObjectPositionFromPlayer(world, player, true);
		if(world.getBlock(moving_obj_pos.blockX, moving_obj_pos.blockY, moving_obj_pos.blockZ) != Blocks.cauldron) return false;
		int meta = stack.getItemDamage();
		BlockCauldron cauldron = (BlockCauldron)world.getBlock(moving_obj_pos.blockX, moving_obj_pos.blockY, moving_obj_pos.blockZ);
		int waterLevel = BlockCauldron.func_150027_b(world.getBlockMetadata(moving_obj_pos.blockX, moving_obj_pos.blockY, moving_obj_pos.blockZ));
		if (SkyrootBucketType.getType(meta) == SkyrootBucketType.Water) {
			if (waterLevel < 3) {
				if (!player.capabilities.isCreativeMode) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(AetherItems.skyroot_bucket, 1, SkyrootBucketType.Empty.meta));
				}
				cauldron.func_150024_a(world, moving_obj_pos.blockX, moving_obj_pos.blockY, moving_obj_pos.blockZ, 3);
			}
		}
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack held_item, World world, EntityPlayer player) {
		int meta = held_item.getItemDamage();

		/* Remedy and Poison Bucket checker */
		if (SkyrootBucketType.getType(meta) != SkyrootBucketType.Water && SkyrootBucketType.getType(meta) != SkyrootBucketType.Empty) {
			player.setItemInUse(held_item, getMaxItemUseDuration(held_item));
			return held_item;
		}

		boolean is_empty = SkyrootBucketType.getType(meta) == SkyrootBucketType.Empty;
		MovingObjectPosition moving_obj_pos = this.getMovingObjectPositionFromPlayer(world, player, is_empty);
		if(moving_obj_pos == null) return held_item;

		FillBucketEvent event = new FillBucketEvent(player, held_item, world, moving_obj_pos);
		if (MinecraftForge.EVENT_BUS.post(event)) return held_item;
		if (event.getResult() == Event.Result.ALLOW) {
			if (player.capabilities.isCreativeMode) return held_item;
			if (--held_item.stackSize <= 0) return event.result;

			if (!player.inventory.addItemStackToInventory(event.result)) {
				player.dropPlayerItemWithRandomChoice(event.result, false);
			}

			return held_item;
		}
		if (moving_obj_pos.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			int x = moving_obj_pos.blockX;
			int y = moving_obj_pos.blockY;
			int z = moving_obj_pos.blockZ;

			if(!world.canMineBlock(player, x, y, z)) return held_item;

			if (is_empty) {
				if (!player.canPlayerEdit(x, y, z, moving_obj_pos.sideHit, held_item)) {
					return held_item;
				}

				Material material = world.getBlock(x, y, z).getMaterial();
				int block_metadata = world.getBlockMetadata(x, y, z);
				if(material == Material.water && block_metadata == 0) {
					world.setBlockToAir(x, y, z);
					return fill_bucket(held_item, player, AetherItems.skyroot_bucket);
				}
			} else {
				if (SkyrootBucketType.getType(meta) == SkyrootBucketType.Empty) {
					return new ItemStack(AetherItems.skyroot_bucket);
				}

				switch(moving_obj_pos.sideHit) {
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

				if(!player.canPlayerEdit(x, y, z, moving_obj_pos.sideHit, held_item)) {
					return held_item;
				}

				if(try_place_liquid(player, world, held_item, x, y, z) && !player.capabilities.isCreativeMode) {
					return new ItemStack(AetherItems.skyroot_bucket);
				}
			}
		}

		return held_item;
	}

	private ItemStack fill_bucket(ItemStack emptyBuckets, EntityPlayer player, Item fullBucket) {
		if (player.capabilities.isCreativeMode) {
			return emptyBuckets;
		} else {
			ItemStack result = new ItemStack(fullBucket, 1, 1);
			--emptyBuckets.stackSize;

			if (emptyBuckets.stackSize <= 0) {
				return result;
			} else {
				if (!player.inventory.addItemStackToInventory(result)) {
					player.dropPlayerItemWithRandomChoice(result, false);
				}
			}

			return emptyBuckets;
		}
	}

	private boolean try_place_liquid(EntityPlayer player, World world, ItemStack stack, int x, int y, int z) {
		if (SkyrootBucketType.getType(stack.getItemDamage()) != SkyrootBucketType.Water) {
			return false;
		}

		Material material = world.getBlock(x, y, z).getMaterial();
		boolean is_not_solid = !material.isSolid();

		if (!world.isAirBlock(x, y, z) && !is_not_solid) {
			return false;
		} else {
			if (world.provider.isHellWorld) {
				world.playSoundEffect((double) x, (double) y, (double) z, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

				for (int l = 0; l < 8; ++l) {
					world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + Math.random(), (double)z + Math.random(), 0D, 0D, 0D);
				}
			} else {
				if (!world.isRemote && is_not_solid && !material.isLiquid()) {
					world.func_147480_a(x, y, z, true);
				}

				world.setBlock(x, y, z, Blocks.flowing_water, 0, 11);
			}

			return true;
		}
	}

	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		return this.onBucketUsed(stack, world, player);
	}

	public ItemStack onBucketUsed(ItemStack itemstack, World world, EntityPlayer player) {
		int meta = itemstack.getItemDamage();

		if (!player.capabilities.isCreativeMode) {
			--itemstack.stackSize;
		}

		if (SkyrootBucketType.getType(meta) == SkyrootBucketType.Poison) {
			player.addPotionEffect(new InebriationEffect(InebriationPotion.inebriation.id, 500, 0));
		} else if (SkyrootBucketType.getType(meta) == SkyrootBucketType.Remedy) {
			PlayerAether aplayer = PlayerAether.get(player);
			aplayer.setCured(200);
			if (!world.isRemote) {
				player.curePotionEffects(new ItemStack(AetherItems.skyroot_bucket, SkyrootBucketType.Remedy.meta));
			}
		} else if (SkyrootBucketType.getType(meta) == SkyrootBucketType.Milk) {
			if (!world.isRemote) {
				player.curePotionEffects(new ItemStack(Items.milk_bucket));
			}
		}

		return itemstack.stackSize <= 0 ? new ItemStack(this, 1, 0) : itemstack;
	}

	public int getMaxItemUseDuration(ItemStack itemstack) {
		int meta = itemstack.getItemDamage();

		if (SkyrootBucketType.getType(meta) != SkyrootBucketType.Water) {
			return 32;
		} else {
			return 0;
		}
	}

	public EnumAction getItemUseAction(ItemStack itemstack) {
		int meta = itemstack.getItemDamage();

		if (SkyrootBucketType.getType(meta) != SkyrootBucketType.Water) {
			return EnumAction.drink;
		} else {
			return EnumAction.none;
		}
	}

}
