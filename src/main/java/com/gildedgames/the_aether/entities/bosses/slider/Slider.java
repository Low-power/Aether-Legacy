package com.gildedgames.the_aether.entities.bosses.slider;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.api.player.util.IAetherBoss;
import com.gildedgames.the_aether.entities.util.AetherNameGen;
import com.gildedgames.the_aether.entities.util.AetherItemEntity;
import com.gildedgames.the_aether.items.AetherItems;
import com.gildedgames.the_aether.player.PlayerAether;
import com.gildedgames.the_aether.registry.achievements.AetherAchievements;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.blocks.dungeon.DungeonBaseBlock;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.command.IEntitySelector;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Slider extends EntityFlying implements IAetherBoss {

	private int dungeonX, dungeonY, dungeonZ;
	private int[] doorStart = new int[3], doorEnd = new int[3];
	public float hurtAngle, hurtAngleX, hurtAngleZ;
	public int chatTime, moveTime;
	public boolean crushedBlock;
	public float velocity;
	public EnumFacing direction;

	public Slider(World world) {
		super(world);

		this.rotationYaw = this.rotationPitch = 0F;

		this.setSize(2F, 2F);
		this.dataWatcher.updateObject(19, AetherNameGen.gen());
	}

	@Override
	public void entityInit() {
		super.entityInit();

		this.dataWatcher.addObject(18, new Byte((byte) 0));
		this.dataWatcher.addObject(19, AetherNameGen.gen());
		this.dataWatcher.addObject(20, new Byte((byte) 0));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(500D);
	}

	@Override
	protected String getLivingSound() {
		return "ambient.cave.cave";
	}

	@Override
	protected String getHurtSound() {
		return "step.stone";
	}

	@Override
	protected String getDeathSound() {
		return null;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);

		nbttagcompound.setInteger("dungeonX", this.dungeonX);
		nbttagcompound.setInteger("dungeonY", this.dungeonY);
		nbttagcompound.setInteger("dungeonZ", this.dungeonZ);

		nbttagcompound.setIntArray("doorStart", this.doorStart);
		nbttagcompound.setIntArray("doorEnd", this.doorEnd);

		nbttagcompound.setBoolean("isAwake", this.isAwake());
		nbttagcompound.setString("bossName", this.getName());
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);

		this.dungeonX = nbttagcompound.getInteger("dungeonX");
		this.dungeonY = nbttagcompound.getInteger("dungeonY");
		this.dungeonZ = nbttagcompound.getInteger("dungeonZ");

		this.doorStart = nbttagcompound.getIntArray("doorStart");
		this.doorEnd = nbttagcompound.getIntArray("doorEnd");

		set_awake(nbttagcompound.getBoolean("isAwake"));
		set_boss_name(nbttagcompound.getString("bossName"));
	}

	public boolean criticalCondition() {
		return this.getHealth() <= 80F;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (this.hurtAngle > 0.01F) {
			this.hurtAngle *= 0.8F;
		}

		if (this.chatTime > 0) {
			this.chatTime--;
		}

		this.renderYawOffset = this.rotationPitch = this.rotationYaw = 0F;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

	@Override
	public void updateEntityActionState() {
		if (!this.isAwake()) {
			this.setAttackTarget(null);
			return;
		}

		if (!this.worldObj.isRemote) {
			if (this.getAttackTarget() == null || this.getAttackTarget().isDead || this.getAttackTarget().getHealth() <= 0F) {
				this.reset();

				return;
			}

			if (this.isMoving()) {
				if (this.isCollided) {
					double x, y, z;
					x = this.posX - 0.5D;
					y = this.boundingBox.minY + 0.75D;
					z = this.posZ - 0.5D;

					this.crushedBlock = false;

					if (y > 4D) switch(this.direction) {
						case UP:
							for (int i = 0; i < 25; i++) {
								double a = (double) ((i / 5) - 2) * 0.75D;
								double b = (double) ((i % 5) - 2) * 0.75D;

								this.destroyBlock(x + a, y + 1.5D, z + b);
							}
							break;
						case DOWN:
							for (int i = 0; i < 25; i++) {
								double a = (double) ((i / 5) - 2) * 0.75D;
								double b = (double) ((i % 5) - 2) * 0.75D;

								this.destroyBlock(x + a, y - 1.5D, z + b);
							}
							break;
						case EAST:
							for (int i = 0; i < 25; i++) {
								double a = (double) ((i / 5) - 2) * 0.75D;
								double b = (double) ((i % 5) - 2) * 0.75D;

								this.destroyBlock(x + 1.5D, y + a, z + b);
							}
							break;
						case WEST:
							for (int i = 0; i < 25; i++) {
								double a = (double) ((i / 5) - 2) * 0.75D;
								double b = (double) ((i % 5) - 2) * 0.75D;

								this.destroyBlock(x - 1.5D, y + a, z + b);
							}
							break;
						case SOUTH:
							for (int i = 0; i < 25; i++) {
								double a = (double) ((i / 5) - 2) * 0.75D;
								double b = (double) ((i % 5) - 2) * 0.75D;

								this.destroyBlock(x + a, y + b, z + 1.5D);
							}
							break;
						case NORTH:
							for (int i = 0; i < 25; i++) {
								double a = (double) ((i / 5) - 2) * 0.75D;
								double b = (double) ((i % 5) - 2) * 0.75D;

								this.destroyBlock(x + a, y + b, z - 1.5D);
							}
							break;
					}

					if (this.crushedBlock) {
						this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 3F, (0.625F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
						this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "aether_legacy:aeboss.slider.collide", 2.5F, 1F / (this.rand.nextFloat() * 0.2F + 0.9F));
					}

					this.stop();
				} else {
					if (this.velocity < 2F) {
						this.velocity += (this.criticalCondition() ? 0.07F : 0.035F);
					}

					this.motionX = this.motionY = this.motionZ = 0F;

					if (this.direction == EnumFacing.UP) {
						this.motionY = this.velocity;

						if (this.boundingBox.minY > this.getAttackTarget().boundingBox.minY + 0.35D) {
							this.stop();
							this.moveTime = this.criticalCondition() ? 4 : 8;
						}
					} else if (this.direction == EnumFacing.DOWN) {
						this.motionY = -this.velocity;

						if (this.boundingBox.minY < this.getAttackTarget().boundingBox.minY - 0.25D) {
							this.stop();
							this.moveTime = this.criticalCondition() ? 4 : 8;
						}
					} else if (this.direction == EnumFacing.EAST) {
						this.motionX = this.velocity;

						if (this.posX > this.getAttackTarget().posX + 0.125D) {
							this.stop();
							this.moveTime = this.criticalCondition() ? 4 : 8;
						}
					} else if (this.direction == EnumFacing.WEST) {
						this.motionX = -this.velocity;

						if (this.posX < this.getAttackTarget().posX - 0.125D) {
							this.stop();
							this.moveTime = this.criticalCondition() ? 4 : 8;
						}
					} else if (this.direction == EnumFacing.SOUTH) {
						this.motionZ = this.velocity;

						if (this.posZ > this.getAttackTarget().posZ + 0.125D) {
							this.stop();
							this.moveTime = this.criticalCondition() ? 4 : 8;
						}
					} else if (this.direction == EnumFacing.NORTH) {
						this.motionZ = -this.velocity;

						if (this.posZ < this.getAttackTarget().posZ - 0.125D) {
							this.stop();
							this.moveTime = this.criticalCondition() ? 4 : 8;
						}
					}
				}
			} else {
				if (this.moveTime > 0) {
					this.moveTime--;

					if (this.criticalCondition() && this.rand.nextInt(2) == 0) {
						this.moveTime--;
					}

					this.motionX = this.motionY = this.motionZ = 0F;
				} else {
					if (this.getAttackTarget() == null) return;

					double a, b, c;
					a = Math.abs(this.posX - this.getAttackTarget().posX);
					b = Math.abs(this.boundingBox.minY - this.getAttackTarget().boundingBox.minY);
					c = Math.abs(this.posZ - this.getAttackTarget().posZ);

					if (a > c) {
						this.direction = EnumFacing.EAST;

						if (this.posX > this.getAttackTarget().posX) {
							this.direction = EnumFacing.WEST;
						}
					} else {
						this.direction = EnumFacing.SOUTH;

						if (this.posZ > this.getAttackTarget().posZ) {
							this.direction = EnumFacing.NORTH;
						}
					}

					if ((b > a && b > c) || (b > 0.25F && this.rand.nextInt(5) == 0)) {
						this.direction = EnumFacing.UP;

						if (this.posY > this.getAttackTarget().posY) {
							this.direction = EnumFacing.DOWN;
						}
					}

					this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "aether_legacy:aeboss.slider.move", 2.5F, 1F / (this.rand.nextFloat() * 0.2F + 0.9F));
					this.setMoving(true);
				}
			}
		}
	}

	private void destroyBlock(double x, double y, double z) {
		Block block = this.worldObj.getBlock((int) x, (int) y, (int) z);
		int metadata = this.worldObj.getBlockMetadata((int) x, (int) y, (int) z);

		if (block == Blocks.air || block instanceof DungeonBaseBlock || block.getBlockHardness(this.worldObj, (int)x, (int)y, (int)z) < 0F) {
			return;
		}

		for (int j = 0; j < 4; ++j) {
			for (int k = 0; k < 4; ++k) {
				for (int l = 0; l < 4; ++l) {
					double d0 = ((double) j + 0.5D) / 4D;
					double d1 = ((double) k + 0.5D) / 4D;
					double d2 = ((double) l + 0.5D) / 4D;
					this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_" + metadata, (double) x + d0, (double) y + d1, (double) z + d2, d0 - 0.5D, d1 - 0.5D, d2 - 0.5D);
				}
			}
		}

		block.breakBlock(this.worldObj, (int) x, (int) y, (int) z, block, metadata);
		block.dropBlockAsItem(this.worldObj, (int) x, (int) y, (int) z, metadata, 0);

		this.worldObj.setBlockToAir((int) x, (int) y, (int) z);

		this.crushedBlock = true;

		double a = x + 0.5D + ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D);
		double b = y + 0.5D + ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D);
		double c = z + 0.5D + ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D);

		this.worldObj.spawnParticle("smoke", a, b, c, 0D, 0D, 0D);
	}

	private boolean checkIsAir(int x1, int y1, int z1, int x2, int y2, int z2) {
		ArrayList<Block> blockList = Lists.newArrayListWithCapacity(9);

		for (int x = x1; x < x2 + 1; x++) {
			for (int z = z1; z < z2 + 1; z++) {
				for (int y = y1; y < y2 + 1; y++) {
					blockList.add(this.worldObj.getBlock(x, y, z));
				}
			}
		}

		Set<Block> blockSet = new HashSet<>(blockList);

		if (blockSet.size() == 1) {
			return blockList.get(1) == Blocks.air;
		}

		return false;
	}

	private void open_door() {
		for (int x = this.doorStart[0]; x < this.doorEnd[0] + 1; x++) {
			for (int y = this.doorStart[1]; y < this.doorEnd[1] + 1; y++) {
				for (int z = this.doorStart[2]; z < this.doorEnd[2] + 1; z++) {
					this.worldObj.setBlock(x, y, z, Blocks.air);
				}
			}
		}
	}

	private void close_door() {
		if (checkIsAir(this.dungeonX + 15, this.dungeonY + 1, this.dungeonZ + 6, this.dungeonX + 15, this.dungeonY + 4, this.dungeonZ + 9)) {
			//EAST
			this.doorStart = new int[] {this.dungeonX + 15, this.dungeonY + 1, this.dungeonZ + 6};
			this.doorEnd = new int[] {this.dungeonX + 15, this.dungeonY + 4, this.dungeonZ + 9};

			int x = this.dungeonX + 15;

			for(int y = this.dungeonY + 1; y < this.dungeonY + 8; y++) {
				for(int z = this.dungeonZ + 5; z < this.dungeonZ + 11; z++) {
					this.worldObj.setBlock(x, y, z, AetherBlocks.locked_carved_stone);
				}
			}
		} else if (checkIsAir(this.dungeonX, this.dungeonY + 1, this.dungeonZ + 6, this.dungeonX, this.dungeonY + 4, this.dungeonZ + 9)) {
			//WEST
			this.doorStart = new int[] {this.dungeonX, this.dungeonY + 1, this.dungeonZ + 6};
			this.doorEnd = new int[] {this.dungeonX, this.dungeonY + 4, this.dungeonZ + 9};

			int x = this.dungeonX;

			for(int y = this.dungeonY + 1; y < this.dungeonY + 8; y++) {
				for(int z = this.dungeonZ + 5; z < this.dungeonZ + 11; z++) {
					this.worldObj.setBlock(x, y, z, AetherBlocks.locked_carved_stone);
				}
			}
		} else if (checkIsAir(this.dungeonX + 6, this.dungeonY + 1, this.dungeonZ + 15, this.dungeonX + 9, this.dungeonY + 4, this.dungeonZ + 15)) {
			//SOUTH
			this.doorStart = new int[] {this.dungeonX + 6, this.dungeonY + 1, this.dungeonZ + 15};
			this.doorEnd = new int[] {this.dungeonX + 9, this.dungeonY + 4, this.dungeonZ + 15};

			int z = this.dungeonZ + 15;

			for(int y = this.dungeonY + 1; y < this.dungeonY + 8; y++) {
				for(int x = this.dungeonX + 5; x < this.dungeonX + 11; x++) {
					this.worldObj.setBlock(x, y, z, AetherBlocks.locked_carved_stone);
				}
			}
		} else if (checkIsAir(this.dungeonX + 6, this.dungeonY + 1, this.dungeonZ, this.dungeonX + 9, this.dungeonY + 4, this.dungeonZ)) {
			//NORTH
			this.doorStart = new int[] {this.dungeonX + 6, this.dungeonY + 1, this.dungeonZ};
			this.doorEnd = new int[] {this.dungeonX + 9, this.dungeonY + 4, this.dungeonZ};

			int z = this.dungeonZ;

			for(int y = this.dungeonY + 1; y < this.dungeonY + 8; y++) {
				for(int x = this.dungeonX + 5; x < this.dungeonX + 11; x++) {
					this.worldObj.setBlock(x, y, z, AetherBlocks.locked_carved_stone);
				}
			}
		}
	}

	@Override
	public void applyEntityCollision(Entity entity) {
		if(!isAwake() || !isMoving()) return;
		boolean attack_ok = entity.attackEntityFrom(new EntityDamageSource("crush", this), 6);
		if(attack_ok && entity instanceof EntityLivingBase) {
			EntityLivingBase collidedEntity = (EntityLivingBase)entity;
			collidedEntity.addVelocity(collidedEntity.motionY, 0.35D, collidedEntity.motionZ);
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "aether_legacy:aeboss.slider.collide", 2.5F, 1F / (this.rand.nextFloat() * 0.2F + 0.9F));
			stop();
		}
	}

	@Override
	protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
		dropItem(Item.getItemFromBlock(AetherBlocks.carved_stone), 7 + rand.nextInt(3));
		entityDropItem(new ItemStack(AetherItems.dungeon_key), 0.5F);
	}

	@Override
	public EntityItem entityDropItem(ItemStack stack, float offsetY) {
		if (stack.stackSize != 0 && stack.getItem() != null) {
			AetherItemEntity item_entity = new AetherItemEntity(this.worldObj, this.posX, this.posY + (double) offsetY, this.posZ, stack);
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

	public void stop() {
		setMoving(false);
		this.moveTime = 12;
		this.direction = EnumFacing.UP;
		this.motionX = this.motionY = this.motionZ = this.velocity = 0F;
	}

	private void sendMessage(EntityPlayer player, String s) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();

		if (this.chatTime <= 0) {
			if (side.isClient()) {
				Aether.proxy.sendMessage(player, s);
			}

			this.chatTime = 60;
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource ds, float damage) {
		if (ds.getEntity() == null || !(ds.getEntity() instanceof EntityPlayer) || ds.isProjectile() || ds.isMagicDamage() || ds.isExplosion() || ds.isFireDamage()) {
			return false;
		}

		EntityPlayer player = (EntityPlayer)ds.getEntity();
		ItemStack stack = player.inventory.getCurrentItem();
		if (stack == null || stack.getItem() == null) {
			return false;
		}

		if (!stack.getItem().canHarvestBlock(Blocks.stone, stack)) {
			this.sendMessage(player, "Hmm. Perhaps I need to attack it with a Pickaxe?");
			return false;
		}

		boolean r = super.attackEntityFrom(ds, Math.max(0, damage));
		if(r) {
			for (int size = 0; size < (this.getHealth() <= 0 ? 2 : 4); size++) {
				double a = this.posX + ((this.rand.nextFloat() - this.rand.nextFloat()) * 1.5D);
				double b = this.boundingBox.minY + 1.75D;
				double c = this.posZ + ((this.rand.nextFloat() - this.rand.nextFloat()) * 1.5D);

				if(getHealth() <= 0) {
					this.worldObj.spawnParticle("explode", a, b, c, 0D, 0D, 0D);
				}

				for (int j = 0; j < 4; ++j) {
					for (int k = 0; k < 4; ++k) {
						for (int l = 0; l < 4; ++l) {
							double d0 = ((double) j + 0.5D) / 4D;
							double d1 = ((double) k + 0.5D) / 4D;
							double d2 = ((double) l + 0.5D) / 4D;
							this.worldObj.spawnParticle("blockcrack_" + Blocks.stone + "_0", (double) a + d0, (double) b + d1, (double) c + d2, d0 - 0.5D, d1 - 0.5D, d2 - 0.5D);
						}
					}
				}
			}

			if(getHealth() <= 0 || this.isDead) {
				open_door();
				unlock_block(this.dungeonX, this.dungeonY, this.dungeonZ);

				this.worldObj.setBlock(this.dungeonX + 7, this.dungeonY + 1, this.dungeonZ + 7, Blocks.trapdoor, 3, 2);
				this.worldObj.setBlock(this.dungeonX + 8, this.dungeonY + 1, this.dungeonZ + 7, Blocks.trapdoor, 2, 2);
				this.worldObj.setBlock(this.dungeonX + 7, this.dungeonY + 1, this.dungeonZ + 8, Blocks.trapdoor, 3, 2);
				this.worldObj.setBlock(this.dungeonX + 8, this.dungeonY + 1, this.dungeonZ + 8, Blocks.trapdoor, 2, 2);

				PlayerAether.get(player).setFocusedBoss(null);

				for(EntityPlayer p : get_dungeon_players_excluding(player)) {
					p.triggerAchievement(AetherAchievements.defeat_bronze);
				}
				player.triggerAchievement(AetherAchievements.defeat_bronze);

				this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "aether_legacy:aeboss.slider.death", 2.5F, 1F / (this.rand.nextFloat() * 0.2F + 0.9F));
				setDead();
			}

			if (!this.isAwake()) {
				this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "aether_legacy:aeboss_slider.awaken", 2.5F, 1F / (this.rand.nextFloat() * 0.2F + 0.9F));
				this.setAttackTarget(player);
				close_door();
				set_awake(true);
			}

			if (this.isMoving()) {
				this.velocity *= 0.75F;
			}
		}

		double a, c;

		a = Math.abs(this.posX - player.posX);
		c = Math.abs(this.posZ - player.posZ);

		if (a > c) {
			this.hurtAngleZ = 1;
			this.hurtAngleX = 0;

			if (this.posX > player.posX) {
				this.hurtAngleZ = -1;
			}
		} else {
			this.hurtAngleX = 1;
			this.hurtAngleZ = 0;

			if (this.posZ > player.posZ) {
				this.hurtAngleX = -1;
			}
		}

		this.hurtAngle = 0.7F - (this.getHealth() / 875F);

		PlayerAether.get(player).setFocusedBoss(this);

		return r;
	}

	private void unlock_block(int x, int y, int z) {
		Block block = this.worldObj.getBlock(x, y, z);
		if(block == AetherBlocks.locked_carved_stone || block == AetherBlocks.locked_sentry_stone) {
			this.worldObj.setBlock(x, y, z, ((DungeonBaseBlock)block).getUnlockedBlock());
			unlock_block(x + 1, y, z);
			unlock_block(x - 1, y, z);
			unlock_block(x, y + 1, z);
			unlock_block(x, y - 1, z);
			unlock_block(x, y, z + 1);
			unlock_block(x, y, z - 1);
		}
	}

	@Override
	protected void collideWithNearbyEntities() {
		List<Entity> nearby_entities = (List<Entity>)this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0D, 0.20000000298023224D));
		if(nearby_entities == null || nearby_entities.isEmpty()) return;
		for(Entity entity : nearby_entities) {
			applyEntityCollision(entity);
		}
	}

	@Override
	public boolean canDespawn() {
		return false;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	@Override
	public void addVelocity(double d, double d1, double d2) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
	}

	@Override
	public void knockBack(Entity par1Entity, float par2, double par3, double par5) {
	}

	public void reset() {
		this.moveTime = 0;
		stop();
		open_door();
		set_awake(false);
		setAttackTarget(null);
		setHealth(this.getMaxHealth());
		setPositionAndUpdate(this.dungeonX + 8, this.dungeonY + 2, this.dungeonZ + 8);
	}

	private List<EntityPlayer> get_dungeon_players_excluding(final EntityPlayer player) {
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(this.dungeonX, this.dungeonY, this.dungeonZ, this.dungeonX, this.dungeonY, this.dungeonZ).expand(10, 10, 10);
		IEntitySelector selector = new IEntitySelector() {
			public boolean isEntityApplicable(Entity entity) {
				return entity != player;
			}
		};
		return (List<EntityPlayer>)this.worldObj.selectEntitiesWithinAABB(EntityPlayer.class, aabb, selector);
	}

	public void setDungeon(double posX, double posY, double posZ) {
		this.dungeonX = (int) posX;
		this.dungeonY = (int) posY;
		this.dungeonZ = (int) posZ;
	}

	private void set_awake(boolean value) {
		this.dataWatcher.updateObject(18, new Byte(value ? (byte)1 : (byte)0));
	}

	public boolean isAwake() {
		return this.dataWatcher.getWatchableObjectByte(18) == (byte) 1;
	}

	public void setMoving(boolean moving) {
		this.dataWatcher.updateObject(20, new Byte(moving ? (byte) 1 : (byte) 0));
	}

	public boolean isMoving() {
		return this.dataWatcher.getWatchableObjectByte(20) == (byte) 1;
	}

	public String getName() {
		return this.dataWatcher.getWatchableObjectString(19);
	}

	private void set_boss_name(String name) {
		this.dataWatcher.updateObject(19, name);
	}

	@Override
	public String getBossName() {
		return this.dataWatcher.getWatchableObjectString(19) + ", the Slider";
	}

	@Override
	public float getBossHealth() {
		return this.getHealth();
	}

	@Override
	public float getMaxBossHealth() {
		return this.getMaxHealth();
	}

	@Override
	public boolean canRenderOnFire() {
		return false;
	}

}
