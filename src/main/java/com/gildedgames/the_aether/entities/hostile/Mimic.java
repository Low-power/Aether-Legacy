package com.gildedgames.the_aether.entities.hostile;

import com.gildedgames.the_aether.items.tools.AetherTool;
import com.gildedgames.the_aether.items.util.AetherToolType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityMob;	// Bad MCP name: should be 'HostileMob' or 'Monster'
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class Mimic extends EntityMob {

	public float mouth, legs;

	private float legsDirection = 1;

	public Mimic(World world) {
		super(world);
		this.setSize(1F, 2F);
		this.applyEntityAI();
	}

	protected void applyEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1D, false));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1D));
		this.tasks.addTask(7, new EntityAIWander(this, 1D));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(8D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.68000000417232513D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3D);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40D);
	}

	public void onUpdate() {
		super.onUpdate();

		this.mouth = (float) ((Math.cos((float) ticksExisted / 10F * 3.14159265F)) + 1F) * 0.6F;
		this.legs *= 0.9F;

		if (this.prevPosX - this.posX != 0 || this.prevPosZ - this.posZ != 0) {
			this.legs += legsDirection * 0.2F;

			if (this.legs > 1F) {
				this.legsDirection = -1;
			}

			if (this.legs < -1F) {
				this.legsDirection = 1;
			}
		} else {
			this.legs = 0F;
		}
	}

	@Override
	protected String getHurtSound() {
		return "mob.slime.small";
	}

	@Override
	protected String getDeathSound() {
		return "mob.slime.small";
	}

	protected float getSoundVolume() {
		return 0.6F;
	}

	@Override
	protected void dropFewItems(boolean var1, int var2) {
		dropItem(Item.getItemFromBlock(Blocks.chest), 1);
	}

	@Override
	public boolean attackEntityFrom(DamageSource ds, float var2) {
		if (ds.getEntity() instanceof Mimic) {
			return false;
		}

		if (ds.getEntity() instanceof EntityLivingBase) {
			this.setAttackTarget((EntityLivingBase)ds.getEntity());
		}

		if (ds.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) ds.getEntity();
			ItemStack stack = player.inventory.getCurrentItem();

			if (stack == null || stack.getItem() == null) {
				return super.attackEntityFrom(ds, var2);
			}

			Item item = stack.getItem();
			if (!(item instanceof ItemAxe) && !(item instanceof AetherTool)) {
				return super.attackEntityFrom(ds, var2);
			}
			if (item instanceof AetherTool && ((AetherTool)item).tool_type != AetherToolType.AXE) {
				return super.attackEntityFrom(ds, var2);
			}

			return super.attackEntityFrom(ds, var2 * 1.5F);
		}

		return super.attackEntityFrom(ds, var2);
	}

}
