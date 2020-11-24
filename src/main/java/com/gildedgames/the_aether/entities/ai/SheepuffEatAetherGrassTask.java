package com.gildedgames.the_aether.entities.ai;

import com.gildedgames.the_aether.entities.passive.Sheepuff;
import com.gildedgames.the_aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class SheepuffEatAetherGrassTask extends EntityAIBase {

	private Sheepuff sheepuff;

	private World entityWorld;

	int eatingGrassTimer;

	public SheepuffEatAetherGrassTask(Sheepuff sheepuff) {
		this.sheepuff = sheepuff;
		this.entityWorld = sheepuff.worldObj;
		setMutexBits(7);
	}

	public boolean shouldExecute() {
		if (this.sheepuff.getRNG().nextInt(1000) != 0) return false;

		int x = MathHelper.floor_double(this.sheepuff.posX);
		int y = MathHelper.floor_double(this.sheepuff.posY);
		int z = MathHelper.floor_double(this.sheepuff.posZ);
		return this.entityWorld.getBlock(x, y - 1, z) == AetherBlocks.aether_grass;
	}

	public void startExecuting() {
		this.eatingGrassTimer = 40;
		this.entityWorld.setEntityState(this.sheepuff, (byte) 10);
		this.sheepuff.getNavigator().clearPathEntity();
	}

	public void resetTask() {
		this.eatingGrassTimer = 0;
	}

	public boolean continueExecuting() {
		return this.eatingGrassTimer > 0;
	}

	public int getEatingGrassTimer() {
		return this.eatingGrassTimer;
	}

	public void updateTask() {
		this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
		if (this.eatingGrassTimer == 4) {
			int x = MathHelper.floor_double(this.sheepuff.posX);
			int y = MathHelper.floor_double(this.sheepuff.posY);
			int z = MathHelper.floor_double(this.sheepuff.posZ);
			if(this.entityWorld.getBlock(x, y - 1, z) == AetherBlocks.aether_grass) {
				if (this.entityWorld.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
					this.entityWorld.playAuxSFX(2001, x, y - 1, z, Block.getIdFromBlock(AetherBlocks.aether_grass));
	 				this.entityWorld.setBlock(x, y - 1, z, AetherBlocks.aether_dirt);
				}
				this.sheepuff.eatGrassBonus();
			}
		}
	}

}
