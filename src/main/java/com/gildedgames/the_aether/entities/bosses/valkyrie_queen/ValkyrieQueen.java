package com.gildedgames.the_aether.entities.bosses.valkyrie_queen;

import com.gildedgames.the_aether.entities.ai.valkyrie_queen.ValkyrieQueenAIWander;
import com.gildedgames.the_aether.entities.ai.EntityAIAttackContinuously;
import com.gildedgames.the_aether.entities.util.BossMob;
import com.gildedgames.the_aether.entities.util.AetherNameGen;
import com.gildedgames.the_aether.entities.util.AetherItemEntity;
import com.gildedgames.the_aether.entities.projectile.crystals.EntityCrystal;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.blocks.dungeon.DungeonBaseBlock;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.api.player.util.IAetherBoss;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.registry.achievements.AetherAchievements;
import com.gildedgames.the_aether.client.gui.dialogue.entity.ValkyrieDialogueGui;
import com.gildedgames.the_aether.Aether;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import java.util.List;

public class ValkyrieQueen extends BossMob implements IAetherBoss {

	private EntityAIAttackContinuously enhancedCombat = new EntityAIAttackContinuously(this, 0.65D);

	public int angerLevel;

	public int timeLeft, timeUntilTeleport, chatTime, timeUntilTeleportToPlayer;

	public int dungeonX, dungeonY, dungeonZ;

	public int dungeonEntranceZ;

	public double safeX, safeY, safeZ;

	public float sinage;

	public double lastMotionY;

	public ValkyrieQueen(World world) {
		super(world);

		this.timeUntilTeleport = this.rand.nextInt(250);
		registerEntityAI();
		this.dataWatcher.updateObject(19, AetherNameGen.valkGen());
		this.safeX = posX;
		this.safeY = posY;
		this.safeZ = posZ;
	}

	public ValkyrieQueen(World world, double x, double y, double z) {
		this(world);
		this.safeX = posX = x;
		this.safeY = posY = y;
		this.safeZ = posZ = z;
	}

	@Override
	public void entityInit() {
		super.entityInit();

		this.dataWatcher.addObject(18, new Byte((byte) 0));
		this.dataWatcher.addObject(19, AetherNameGen.valkGen());
	}

	public void registerEntityAI() {
		this.targetTasks.addTask(0, this.enhancedCombat);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new ValkyrieQueenAIWander(this, 0.5D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8F, 200F));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(28D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.85D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(13D);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(500D);
	}

	@Override
	public void moveEntityWithHeading(float slide, float forward) {
		if(isBossReady()) super.moveEntityWithHeading(slide, forward);
		else if(worldObj.isRemote) onGround = true;
	}

	@Override
	public void addVelocity(double x, double y, double z) {
		if(isBossReady()) {
			super.addVelocity(x, y, z);
		}
	}

	public void swingArm() {
		if (!this.isSwingInProgress) {
			this.isSwingInProgress = true;
		}
	}

	private void becomeAngryAt(EntityLivingBase entity) {
		setTarget(entity);
		this.angerLevel = 200 + this.rand.nextInt(200);
	}

	public void setDungeon(int x, int y, int z) {
		this.dungeonX = x;
		this.dungeonY = y;
		this.dungeonZ = z - 19;
	}

	private void unlockDoor() {
		this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY, this.dungeonEntranceZ, Blocks.air);
		this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY, this.dungeonEntranceZ + 1, Blocks.air);
		this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, this.dungeonEntranceZ + 1, Blocks.air);
		this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, this.dungeonEntranceZ, Blocks.air);
	}

	private void unlockTreasure() {
		this.worldObj.setBlock(this.dungeonX + 16, dungeonY + 1, dungeonZ + 9, Blocks.trapdoor, 3, 2);
		this.worldObj.setBlock(this.dungeonX + 17, dungeonY + 1, dungeonZ + 9, Blocks.trapdoor, 2, 2);
		this.worldObj.setBlock(this.dungeonX + 16, dungeonY + 1, dungeonZ + 10, Blocks.trapdoor, 3, 2);
		this.worldObj.setBlock(this.dungeonX + 17, dungeonY + 1, dungeonZ + 10, Blocks.trapdoor, 2, 2);

		for (int x = this.dungeonX - 27; x < this.dungeonX + 30; x++) {
			for (int y = this.dungeonY - 1; y < this.dungeonY + 22; y++) {
				for (int z = this.dungeonZ - 6; z < this.dungeonZ + 26; z++) {
					Block block = this.worldObj.getBlock(x, y, z);
					if(block == AetherBlocks.locked_angelic_stone || block == AetherBlocks.locked_light_angelic_stone) {
						this.worldObj.setBlock(x, y, z, ((DungeonBaseBlock)block).getUnlockedBlock());
					}
				}
			}
		}
	}

	private void chatItUp(EntityPlayer player, String s) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();

		if (this.chatTime <= 0) {
			if (side.isClient()) {
				Aether.proxy.sendMessage(player, s);
			}

			this.chatTime = 60;
		}
	}

	public void makeHomeShot(int shots, EntityPlayer player) {
		if(this.worldObj.isRemote) return;
		for (int i = 0; i < shots; i++) {
			EntityCrystal crystal = new EntityCrystal(this.worldObj, this.posX - (this.motionX / 2D), this.posY, this.posZ - (this.motionZ / 2D), player);
			this.worldObj.spawnEntityInWorld(crystal);
		}
	}

	@SideOnly(Side.CLIENT)
	public void displayValkyrieDialogue(EntityPlayer player) {
		if (this.worldObj.isRemote) {
			FMLClientHandler.instance().getClient().displayGuiScreen(new ValkyrieDialogueGui(this, player));
		}
	}

	@Override
	public boolean interact(EntityPlayer player) {
		this.faceEntity(player, 180F, 180F);

		if(isBossReady()) {
			chatItUp(player, "If you wish to challenge me, strike at any time.");
		} else if (this.worldObj.isRemote) {
			displayValkyrieDialogue(player);
			return true;
		}

		return super.interact(player);
	}

	@Override
	public void updateEntityActionState() {
		super.updateEntityActionState();

		if (!this.isBossReady()) {
			this.motionY *= 0.5f;
			this.moveStrafing = this.moveForward = 0;
		} else {
			if(getEntityToAttack() != null) {
				if(getEntityToAttack() instanceof EntityPlayer) {
					EntityPlayer target = (EntityPlayer)getEntityToAttack();
					if (target != null) {
						if (target.posY > this.posY) {
							timeUntilTeleportToPlayer++;
							if (timeUntilTeleportToPlayer >= 75 && !this.worldObj.isRemote) {
								teleportToPlayer();
							}
						} else {
							timeUntilTeleportToPlayer = 0;
						}

						if (this.timeUntilTeleport++ >= 450) {
							if (this.onGround && this.rand.nextInt(5) == 0) {
								makeHomeShot(1, target);
							} else {
								teleport(target.posX, target.posY, target.posZ, 4);
							}
						} else if (this.timeUntilTeleport < 446 && (this.posY <= 0D || this.posY <= (this.safeY - 16D))) {
							this.timeUntilTeleport = 446;
						} else if ((this.timeUntilTeleport % 5) == 0 && !canEntityBeSeen(target)) {
							this.timeUntilTeleport += 100;
						}
					}
				}
			}

			if (!this.worldObj.isRemote) {
				for (int k = 2; k < 23; k += 7) {
					Block block = this.worldObj.getBlock(this.dungeonX - 1, this.dungeonY, this.dungeonZ + k);
					if(block != AetherBlocks.locked_angelic_stone || block != AetherBlocks.locked_light_angelic_stone) {
						this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY, this.dungeonZ + k, AetherBlocks.locked_angelic_stone);
						this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY, this.dungeonZ + k + 1, AetherBlocks.locked_angelic_stone);
						this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, this.dungeonZ + k + 1, AetherBlocks.locked_angelic_stone);
						this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, this.dungeonZ + k, AetherBlocks.locked_angelic_stone);
						this.dungeonEntranceZ = this.dungeonZ + k;
					}
				}
			}
		}

		if(getEntityToAttack() != null && getEntityToAttack().isDead) {
			this.setTarget(null);
			unlockDoor();
			this.angerLevel = 0;
		}

		if (this.chatTime > 0) {
			this.chatTime--;
		}
	}

	@Override
	public void onUpdate() {
		this.lastMotionY = motionY;
		super.onUpdate();

		Entity attack_target = getEntityToAttack();

		if(!this.onGround && attack_target != null && this.lastMotionY >= 0D && motionY < 0D && getDistanceToEntity(attack_target) <= 16F && canEntityBeSeen(attack_target)) {
			double x_diff = attack_target.posX - posX;
			double z_diff = attack_target.posZ - posZ;
			double angle = Math.atan2(x_diff, z_diff);
			this.motionX = Math.sin(angle) * 0.25D;
			this.motionZ = Math.cos(angle) * 0.25D;
		}

		if (!this.onGround && !isOnLadder() && Math.abs(this.motionY - this.lastMotionY) > 0.07D && Math.abs(this.motionY - this.lastMotionY) < 0.09D) {
			this.motionY += 0.055F;

			if (this.motionY < -0.275F) {
				this.motionY = -0.275F;
			}
		}

		if (this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL && (attack_target != null || this.angerLevel > 0)) {
			this.angerLevel = 0;
			setTarget(null);
		}

		if (!this.onGround) {
			this.sinage += 0.75F;
		} else {
			this.sinage += 0.15F;
		}

		if (this.sinage > 3.141593F * 2F) {
			this.sinage -= (3.141593F * 2F);
		}

		if (this.getHealth() <= 0 || this.isDead) {
			if (!this.worldObj.isRemote) {
				unlockDoor();
				unlockTreasure();
			}

			if(attack_target instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)attack_target;
				chatItUp(player, "You are truly... a mighty warrior...");
				player.triggerAchievement(AetherAchievements.defeat_silver);
				PlayerAether.get(player).setFocusedBoss(null);
			}

			spawnExplosionParticle();
			setDead();
		}
	}

	@Override
	protected Entity findPlayerToAttack() {
		return null;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setShort("Anger", (short) angerLevel);
		nbttagcompound.setShort("TimeLeft", (short) timeLeft);
		nbttagcompound.setBoolean("Duel", this.isBossReady());
		nbttagcompound.setInteger("DungeonX", this.dungeonX);
		nbttagcompound.setInteger("DungeonY", this.dungeonY);
		nbttagcompound.setInteger("DungeonZ", this.dungeonZ);
		nbttagcompound.setInteger("DungeonEntranceZ", this.dungeonEntranceZ);
		nbttagcompound.setTag("SafePos", newDoubleNBTList(new double[]{this.safeX, this.safeY, this.safeZ}));
		nbttagcompound.setString("BossName", this.getName());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.angerLevel = nbttagcompound.getShort("Anger");
		this.timeLeft = nbttagcompound.getShort("TimeLeft");
		this.setBossReady(nbttagcompound.getBoolean("Duel"));
		this.dungeonX = nbttagcompound.getInteger("DungeonX");
		this.dungeonY = nbttagcompound.getInteger("DungeonY");
		this.dungeonZ = nbttagcompound.getInteger("DungeonZ");
		this.dungeonEntranceZ = nbttagcompound.getInteger("DungeonEntranceZ");
		NBTTagList nbttaglist = nbttagcompound.getTagList("SafePos", 10);
		this.setBossName(nbttagcompound.getString("BossName"));

		this.safeX = nbttaglist.func_150309_d(0);
		this.safeY = nbttaglist.func_150309_d(1);
		this.safeZ = nbttaglist.func_150309_d(2);
	}

	@Override
	public boolean attackEntityFrom(DamageSource ds, float i) {
		if (ds.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) ds.getEntity();

			if (this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
				spawnExplosionParticle();
				chatItUp(player, "Sorry, I don't fight with weaklings.");
				return false;
			}

			if(!isBossReady()) {
				spawnExplosionParticle();
				chatItUp(player, "Try defeating some weaker valkyries first.");
				return false;
			}

			PlayerAether player_data = PlayerAether.get(player);
			if(player_data != null) {
				if(this.isDead || getHealth() <= 0F) {
					player_data.setFocusedBoss(null);
				} else if(!player.isDead) {
					player_data.setFocusedBoss(this);
				}
			}

			if(getEntityToAttack() == null) {
				this.chatTime = 0;
				chatItUp(player, "This will be your final battle!");
				becomeAngryAt(player);
			} else {
				this.timeUntilTeleport += 60;
			}
		} else {
			extinguish();
			return false;
		}

		return super.attackEntityFrom(ds, i);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		swingArm();

		boolean attack_ok = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 8);

		if(entity instanceof EntityPlayer && entity == getEntityToAttack()) {
			EntityPlayer player = (EntityPlayer)entity;
			if (player.getHealth() <= 0 || player.isDead) {
				setTarget(null);
				this.angerLevel = this.chatTime = 0;
				chatItUp(player, "As expected of a human.");
				unlockDoor();
			}
		}

		return attack_ok;
	}

	@Override
	protected void dropFewItems(boolean loot_enabled, int looting_modifier) {
		entityDropItem(new ItemStack(AetherItems.dungeon_key, 1, 1), 0.5F);
		dropItem(Items.golden_sword, 1);
	}

	@Override
	public EntityItem entityDropItem(ItemStack stack, float y_offset) {
		if (stack.stackSize != 0 && stack.getItem() != null) {
			AetherItemEntity item_entity = new AetherItemEntity(this.worldObj, this.posX, this.posY + (double)y_offset, this.posZ, stack);
			if (captureDrops) {
				this.capturedDrops.add(item_entity);
			} else {
				this.worldObj.spawnEntityInWorld(item_entity);
			}
			return item_entity;
		} else {
			return null;
		}
	}

	@Override
	public void fall(float distance) {
	}

	public void teleport(double x, double y, double z, int rad) {
		int x_offset = this.rand.nextInt(rad + 1);
		int y_offset = this.rand.nextInt(rad / 2);
		int z_offset = rad - x_offset;

		x_offset *= (rand.nextInt(2) * 2) - 1;	// Negate or Not
		y_offset *= (rand.nextInt(2) * 2) - 1;	// Negate or Not
		z_offset *= (rand.nextInt(2) * 2) - 1;	// Negate or Not

		x += (double)x_offset;
		y += (double)y_offset;
		z += (double)z_offset;

		int new_x = (int)Math.floor(x - 0.5D);
		int new_y = (int)Math.floor(y - 0.5D);
		int new_z = (int)Math.floor(z - 0.5D);

		boolean can_teleport = false;

		for(int i = 0; i < 32; i++) {
			int tx = new_x + (this.rand.nextInt(rad / 2) - this.rand.nextInt(rad / 2));
			int ty = new_y + (this.rand.nextInt(rad / 2) - this.rand.nextInt(rad / 2));
			int tz = new_z + (this.rand.nextInt(rad / 2) - this.rand.nextInt(rad / 2));
			if(is_airy_space(tx, ty, tz) && is_airy_space(tx, ty + 1, tz) && !is_airy_space(tx, ty - 1, tz) &&
			   tx > dungeonX && tx < dungeonX + 20 && ty > dungeonY && ty < dungeonY + 12 && tz > dungeonZ && tz < dungeonZ + 20) {
				new_x = tx;
				new_y = ty;
				new_z = tz;
				break;
			}
		}

		if(!can_teleport) {
			if (this.posY <= 0D) {
				this.timeUntilTeleport = 446;
			} else {
				this.timeUntilTeleport -= this.rand.nextInt(40) + 40;
			}
		} else {
			spawnExplosionParticle();
			this.enhancedCombat.resetTask();
			setPosition((double)new_x + 0.5D, (double)new_y + 0.5D, (double)new_z + 0.5D);
			this.isJumping = false;
			this.renderYawOffset = this.rand.nextFloat() * 360F;
			this.timeUntilTeleport = this.rand.nextInt(40);
			this.motionX = this.motionY = this.motionZ = this.moveForward = this.moveStrafing = this.rotationPitch = this.rotationYaw = 0;
		}
	}

	public void teleportToPlayer() {
		if(getEntityToAttack() instanceof EntityPlayer) {
			spawnExplosionParticle();
			this.enhancedCombat.resetTask();
			setPosition(getEntityToAttack().posX + 0.5D, getEntityToAttack().posY + 0.5D, getEntityToAttack().posZ + 0.5D);
			this.isJumping = false;
			this.renderYawOffset = this.rand.nextFloat() * 360F;
			this.timeUntilTeleportToPlayer = 0;
			this.motionX = this.motionY = this.motionZ = this.moveForward = this.moveStrafing = this.rotationPitch = this.rotationYaw = 0;
		}
	}

	public boolean is_airy_space(int x, int y, int z) {
		Block block = this.worldObj.getBlock(x, y, z);
		return block == Blocks.air || block.getCollisionBoundingBoxFromPool(this.worldObj, x, y, z) == null;
	}

	public boolean canDespawn() {
		return false;
	}

	@Override
	public boolean getCanSpawnHere() {
		int x = MathHelper.floor_double(this.posX);
		int y = MathHelper.floor_double(this.boundingBox.minY);
		int z = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBlockLightValue(x, y, z) > 8 && this.worldObj.checkBlockCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox);
	}

	@Override
	protected String getHurtSound() {
		return "game.player.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "game.player.die";
	}

	public String getName() {
		return this.dataWatcher.getWatchableObjectString(19);
	}

	@Override
	public String getBossName() {
		return this.dataWatcher.getWatchableObjectString(19) + ", the Valkyrie Queen";
	}

	public void setBossName(String name) {
		this.dataWatcher.updateObject(19, name);
	}

	@Override
	public float getBossHealth() {
		return getHealth();
	}

	@Override
	public float getMaxBossHealth() {
		return getMaxHealth();
	}

	public void setBossReady(boolean isReady) {
		this.dataWatcher.updateObject(18, new Byte(isReady ? (byte) 1 : (byte) 0));
	}

	public boolean isBossReady() {
		return this.dataWatcher.getWatchableObjectByte(18) == (byte) 1;
	}

}
