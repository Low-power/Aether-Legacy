package com.gildedgames.the_aether.player;

import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.api.player.IPlayerAether;
import com.gildedgames.the_aether.api.player.util.IAccessoryInventory;
import com.gildedgames.the_aether.api.player.util.IAetherAbility;
import com.gildedgames.the_aether.api.player.util.IAetherBoss;
import com.gildedgames.the_aether.entities.passive.mountable.ParachuteEntity;
import com.gildedgames.the_aether.inventory.AccessoriesInventory;
import com.gildedgames.the_aether.network.packets.*;
import com.gildedgames.the_aether.registry.achievements.AetherAchievements;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.network.AetherNetwork;
import com.gildedgames.the_aether.player.perks.AetherRankings;
import com.gildedgames.the_aether.player.perks.util.EnumAetherPerkType;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.items.tools.ValkyrieTool;
import com.gildedgames.the_aether.player.abilities.AccessoriesAbility;
import com.gildedgames.the_aether.player.abilities.ArmorAbility;
import com.gildedgames.the_aether.player.abilities.AbilityFlight;
import com.gildedgames.the_aether.player.abilities.RepulsionAbility;
import com.gildedgames.the_aether.player.perks.util.DonatorMoaSkin;
import com.gildedgames.the_aether.events.AetherEntityEvents;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

public class PlayerAether implements IPlayerAether {

	private EntityPlayer player;

	private IAetherBoss focusedBoss;

	private IAccessoryInventory accessories = new AccessoriesInventory(this);

	private final ArrayList<IAetherAbility> abilities = new ArrayList<IAetherAbility>();

	public final ArrayList<Entity> clouds = new ArrayList<Entity>(2);

	private int shardCount;

	public DonatorMoaSkin donatorMoaSkin = new DonatorMoaSkin();

	public boolean shouldRenderHalo, shouldRenderGlow, shouldRenderCape;

	public boolean seenSpiritDialog = false;

	private boolean isJumping;

	private boolean isMountSneaking;

	private boolean inPortal;

	private int portalCounter;

	public int teleportDirection;

	private String hammerName = "Hammer of Notch";

	private int cooldown;

	private int cooldownMax;

	public float wingSinage;

	public float timeInPortal;

	public float prevTimeInPortal;

	public Entity ridden_entity;
	public Entity ridden_by_entity;

	private ChunkCoordinates bedLocation;

	public boolean isPoisoned = false, isCured = false;

	public boolean shouldGetPortal;

	public int poisonTime = 0, cureTime = 0;

	public PlayerAether() {
		this.shouldRenderHalo = true;
		this.shouldRenderGlow = false;
		this.shouldRenderCape = true;
		this.shouldGetPortal = true;
		this.abilities.addAll(Arrays.<IAetherAbility>asList(new AccessoriesAbility(this), new ArmorAbility(this), new AbilityFlight(this), new RepulsionAbility(this)));
	}

	public static PlayerAether get(EntityPlayer player) {
		return (PlayerAether) player.getExtendedProperties("aether_legacy:player_aether");
	}

	@Override
	public void init(Entity entity, World world) {
		this.player = (EntityPlayer)entity;
	}

	@Override
	public void onUpdate() {
		if (!this.player.worldObj.isRemote) {
			AetherNetwork.sendToAll(new PacketPerkChanged(this.getEntity().getEntityId(), EnumAetherPerkType.Halo, this.shouldRenderHalo));
			AetherNetwork.sendToAll(new PacketPerkChanged(this.getEntity().getEntityId(), EnumAetherPerkType.Glow, this.shouldRenderGlow));
			AetherNetwork.sendToAll(new PacketCapeChanged(this.getEntity().getEntityId(), this.shouldRenderCape));
			AetherNetwork.sendToAll(new PoisonTimePacket(this.getEntity(), this.poisonTime));
			AetherNetwork.sendToAll(new PacketSendSeenDialogue(this.getEntity(), this.seenSpiritDialog));
			AetherNetwork.sendToAll(new PacketPortalItem(this.getEntity(), this.shouldGetPortal));
		}

		if (this.isPoisoned) {
			if (poisonTime > 0) {
				this.poisonTime--;
			} else {
				this.poisonTime = 0;
				this.isPoisoned = false;
			}
		}

		if (this.isCured) {
			if (cureTime > 0) {
				this.cureTime--;
			} else {
				this.cureTime = 0;
				this.isCured = false;
			}
		}

		for (int i = 0; i < this.getAbilities().size(); ++i) {
			IAetherAbility ability = this.getAbilities().get(i);

			if (ability.shouldExecute()) {
				ability.onUpdate();
			}
		}

		Iterator<Entity> iterator = this.clouds.iterator();
		while(iterator.hasNext()) {
			Entity entity = iterator.next();
			if(entity.isDead) iterator.remove();
		}

		if (this.cooldown > 0) {
			this.cooldown -= 2;
		}

		if(isInsideBlock(AetherBlocks.aercloud)) {
			getEntity().fallDistance = 0F;
		}

		if(getEntity().motionY < -2F) {
			activateParachute();
		}

		if(!getEntity().onGround) {
			this.wingSinage += 0.75F;
		} else {
			this.wingSinage += 0.15F;
		}

		if (this.wingSinage > 3.141593F * 2F) {
			this.wingSinage -= 3.141593F * 2F;
		} else {
			this.wingSinage += 0.1F;
		}

		boolean hasJumped = ReflectionHelper.getPrivateValue(EntityLivingBase.class, this.getEntity(), "isJumping", "field_70703_bu");

		this.setJumping(hasJumped);

		this.getEntity().worldObj.theProfiler.startSection("portal");

		if (this.inPortal) {
			if (this.getEntity().timeUntilPortal <= 0) {
				int limit = this.getEntity().getMaxInPortalTime();

				if (this.getEntity().ridingEntity == null) {
					if (this.portalCounter >= limit) {
						this.portalCounter = 0;
						this.getEntity().timeUntilPortal = this.getEntity().getPortalCooldown();

						if (!this.getEntity().worldObj.isRemote) {
							AetherEntityEvents.teleport_entity(true, getEntity());
							getEntity().triggerAchievement(AetherAchievements.enter_aether);
						}
					} else {
						this.portalCounter++;
					}
				}
			} else {
				this.getEntity().timeUntilPortal = this.getEntity().getPortalCooldown();
			}

			if(getEntity().worldObj.getBlock((int)getEntity().posX, (int)getEntity().posY - 1, (int)getEntity().posZ) != Blocks.air) {
				AxisAlignedBB bb = getEntity().boundingBox;
				if(getEntity().worldObj.getBlock((int)bb.minX, (int)bb.minY, (int)bb.minZ) != AetherBlocks.aether_portal) {
					this.inPortal = false;
				}
			}
		} else {
			if (this.portalCounter > 0) {
				this.portalCounter -= 4;
			}

			if (this.portalCounter < 0) {
				this.portalCounter = 0;
			}
		}

		this.getEntity().worldObj.theProfiler.endSection();

		if (!this.getEntity().worldObj.isRemote) {
			ItemStack stack = this.getEntity().getCurrentEquippedItem();

			double distance = this.getEntity().capabilities.isCreativeMode ? 5D : 4.5D;

			if (stack != null && stack.getItem() instanceof ValkyrieTool) {
				distance = 8D;
			}

			((EntityPlayerMP)this.getEntity()).theItemInWorldManager.setBlockReachDistance(distance);
		} else {
			this.prevTimeInPortal = this.timeInPortal;

			if(isInsideBlock(AetherBlocks.aether_portal)) {
				this.timeInPortal += 0.0125F;
				if (this.timeInPortal >= 1F) {
					this.timeInPortal = 1F;
				}
			} else if(getEntity().isPotionActive(Potion.confusion) && this.getEntity().getActivePotionEffect(Potion.confusion).getDuration() > 60) {
				this.timeInPortal += 0.006666667F;
				if (this.timeInPortal > 1F) {
					this.timeInPortal = 1F;
				}
			} else {
				if (this.timeInPortal > 0F) {
					this.timeInPortal -= 0.05F;
				}
				if (this.timeInPortal < 0F) {
					this.timeInPortal = 0F;
				}
			}
		}

		if(!player.worldObj.isRemote && this.bedLocation != null && player.dimension == AetherConfig.get_aether_world_id() && player.worldObj.getBlock(this.bedLocation.posX, this.bedLocation.posY, this.bedLocation.posZ) != AetherBlocks.skyroot_bed) {
			setBedLocation(null);
		}
	}

	@Override
	public void setInPortal() {
		double d0 = this.getEntity().prevPosX - this.getEntity().posX;
		double d1 = this.getEntity().prevPosZ - this.getEntity().posZ;

		if (!this.getEntity().worldObj.isRemote && !this.inPortal) {
			this.teleportDirection = Direction.getMovementDirection(d0, d1);
		}

		this.inPortal = true;
	}

	private void activateParachute() {
		if(this.player.capabilities.isCreativeMode) return;

		ParachuteEntity parachute = null;
		ItemStack itemstack = null;

		for (int i = 0; i < this.getEntity().inventory.getSizeInventory(); i++) {
			ItemStack stackInSlot = this.getEntity().inventory.getStackInSlot(i);

			if(stackInSlot != null && stackInSlot.getItem() == AetherItems.cloud_parachute) {
				itemstack = stackInSlot;
				break;
			} else {
				if (stackInSlot != null && stackInSlot.getItem() == AetherItems.golden_parachute) {
					itemstack = stackInSlot;
					break;
				}
			}
		}

		if (itemstack != null) {
			Item item = itemstack.getItem();
			if(item == AetherItems.cloud_parachute) {
				parachute = new ParachuteEntity(this.getEntity().worldObj, this.getEntity(), false);
				parachute.setPosition(this.getEntity().posX, this.getEntity().posY, this.getEntity().posZ);
				this.getEntity().worldObj.spawnEntityInWorld(parachute);
				this.getEntity().inventory.consumeInventoryItem(itemstack.getItem());
			} else if(item == AetherItems.golden_parachute) {
				itemstack.damageItem(1, this.getEntity());
				parachute = new ParachuteEntity(this.getEntity().worldObj, this.getEntity(), true);
				parachute.setPosition(this.getEntity().posX, this.getEntity().posY, this.getEntity().posZ);
				this.getEntity().worldObj.spawnEntityInWorld(parachute);
			}
		}
	}

	public boolean isInsideBlock(Block block) {
		AxisAlignedBB boundingBox = this.getEntity().boundingBox;
		int i = MathHelper.floor_double(boundingBox.minX);
		int j = MathHelper.floor_double(boundingBox.maxX + 1D);
		int k = MathHelper.floor_double(boundingBox.minY);
		int l = MathHelper.floor_double(boundingBox.maxY + 1D);
		int i1 = MathHelper.floor_double(boundingBox.minZ);
		int j1 = MathHelper.floor_double(boundingBox.maxZ + 1D);

		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					if (this.getEntity().worldObj.getBlock(k1, l1, i2) == block) {
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound aetherTag = new NBTTagCompound();

		if (AetherRankings.isRankedPlayer(this.player.getUniqueID())) {
			aetherTag.setBoolean("halo", this.shouldRenderHalo);
		}

		if (AetherRankings.isDeveloper(this.player.getUniqueID())) {
			aetherTag.setBoolean("glow", this.shouldRenderGlow);
		}

		aetherTag.setBoolean("poisoned", this.isPoisoned);
		aetherTag.setInteger("poison_time", this.poisonTime);
		aetherTag.setBoolean("cape", this.shouldRenderCape);
		aetherTag.setInteger("shardCount", this.shardCount);
		aetherTag.setTag("accessories", this.getAccessoryInventory().writeToNBT(aetherTag));
		aetherTag.setBoolean("seen_spirit_dialog", this.seenSpiritDialog);
		aetherTag.setBoolean("get_portal", this.shouldGetPortal);

		if (this.bedLocation != null) {
			aetherTag.setInteger("bedX", this.bedLocation.posX);
			aetherTag.setInteger("bedY", this.bedLocation.posY);
			aetherTag.setInteger("bedZ", this.bedLocation.posZ);
		}

		compound.setTag("aetherI", aetherTag);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound aetherTag = compound.getCompoundTag("aetherI");

		if (aetherTag.hasKey("halo")) {
			this.shouldRenderHalo = aetherTag.getBoolean("halo");
		}

		if (aetherTag.hasKey("glow")) {
			this.shouldRenderGlow = aetherTag.getBoolean("glow");
		}

		if (aetherTag.hasKey("cape")) {
			this.shouldRenderCape = aetherTag.getBoolean("cape");
		}

		if (aetherTag.hasKey("poisoned")) {
			this.isPoisoned = aetherTag.getBoolean("poisoned");
		}

		if (aetherTag.hasKey("poison_time")) {
			this.poisonTime = aetherTag.getInteger("poison_time");
		}

		if (aetherTag.hasKey("seen_spirit_dialog")) {
			this.seenSpiritDialog = aetherTag.getBoolean("seen_spirit_dialog");
		}

		if (aetherTag.hasKey("get_portal")) {
			this.shouldGetPortal = aetherTag.getBoolean("get_portal");
		}

		this.updateShardCount(aetherTag.getInteger("shardCount"));
		this.getAccessoryInventory().readFromNBT(aetherTag.getTagList("accessories", 10));
		this.setBedLocation(new ChunkCoordinates(aetherTag.getInteger("bedX"), aetherTag.getInteger("bedY"), aetherTag.getInteger("bedZ")));
	}

	@Override
	public void setFocusedBoss(IAetherBoss boss) {
		this.focusedBoss = boss;
	}

	@Override
	public IAetherBoss getFocusedBoss() {
		return this.focusedBoss;
	}

	@Override
	public void setAccessoryInventory(IAccessoryInventory inventory) {
		this.accessories = inventory;
	}

	@Override
	public IAccessoryInventory getAccessoryInventory() {
		return this.accessories;
	}

	@Override
	public ArrayList<IAetherAbility> getAbilities() {
		return this.abilities;
	}

	@Override
	public EntityPlayer getEntity() {
		return this.player;
	}

	@Override
	public void updateShardCount(int amount) {
		UUID uuid = UUID.fromString("df6eabe7-6947-4a56-9099-002f90370706");
		AttributeModifier healthModifier = new AttributeModifier(uuid, "Aether Health Modifier", amount * 2D, 0);

		this.shardCount = amount;

		if (this.getEntity().getEntityAttribute(SharedMonsterAttributes.maxHealth).getModifier(uuid) != null) {
			this.getEntity().getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(healthModifier);
		}

		this.getEntity().getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(healthModifier);
	}

	@Override
	public int getShardsUsed() {
		return this.shardCount;
	}

	@Override
	public int getMaxShardCount() {
		return AetherConfig.getMaxLifeShards();
	}

	@Override
	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}

	@Override
	public boolean isJumping() {
		return this.isJumping;
	}

	@Override
	public void setMountSneaking(boolean isSneaking) {
		this.isMountSneaking = isSneaking;
	}

	@Override
	public boolean isMountSneaking() {
		return this.isMountSneaking;
	}

	@Override
	public boolean isDonator() {
		return true;
	}

	public boolean setHammerCooldown(int cooldown, String hammerName) {
		if (this.cooldown <= 0) {
			this.cooldown = cooldown;
			this.cooldownMax = cooldown;
			this.hammerName = hammerName;

			return true;
		}

		return false;
	}

	@Override
	public String getHammerName() {
		return this.hammerName;
	}

	@Override
	public int getHammerCooldown() {
		return this.cooldown;
	}

	@Override
	public int getHammerMaxCooldown() {
		return this.cooldownMax;
	}

	public void setBedLocation(ChunkCoordinates bedLocation) {
		this.bedLocation = bedLocation;
	}

	public ChunkCoordinates getBedLocation() {
		return bedLocation;
	}

	public boolean isPoisoned() {
		return this.isPoisoned;
	}

	public void setPoisoned() {
		this.isPoisoned = true;
		this.poisonTime = 500;
	}

	public boolean isCured() {
		return this.isCured;
	}

	public void setCured(int time) {
		this.isCured = true;
		this.cureTime = time;

		this.isPoisoned = false;
		this.poisonTime = 0;
	}

	public void givePortalFrame() {
		if (this.shouldGetPortal) {
			this.player.inventory.addItemStackToInventory(new ItemStack(AetherItems.aether_portal_frame));
			this.shouldGetPortal = false;
		}
	}
}
