package com.gildedgames.the_aether.entities.ai.aerwhale;

import com.gildedgames.the_aether.entities.passive.Aerwhale;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import java.util.*;

public class AerwhaleUnstuckTask extends EntityAIBase {
	private Aerwhale aerwhale;
	private World world;
	private boolean isStuckWarning = false;
	private long checkTime = 0L;
	private double[] checkPos = new double[3];

	public AerwhaleUnstuckTask(Aerwhale aerwhale) {
		this.aerwhale = aerwhale;
		this.world = aerwhale.worldObj;
		setMutexBits(4);
	}

	@Override
	public boolean shouldExecute() {
		return !this.aerwhale.isDead;
	}

	private boolean check_region(double[] pos1, double[] pos2) {
		ArrayList<Block> blockList = Lists.newArrayListWithCapacity(9);
		for (int x = (int) pos1[0]; x < (int) pos2[0] + 1; x++) {
			for (int z = (int) pos1[2]; z < (int) pos2[2] + 1; z++) {
				for (int y = (int) pos1[1]; y < (int) pos2[1] + 1; y++) {
					blockList.add(this.world.getBlock(x, y, z));
				}
			}
		}

		Set<Block> block_set = new HashSet<>(blockList);
		switch(block_set.size()) {
			case 0:
				return false;
			case 1:
				return blockList.get(1) != Blocks.air;
			default:
				return block_set.size() > 1;
		}
	}

	@Override
	public void updateTask() {
		double[] posUp = new double[] { this.aerwhale.posX, this.aerwhale.posY + 3, this.aerwhale.posZ };
		double[] posDown = new double[] { this.aerwhale.posX, this.aerwhale.posY - 1, this.aerwhale.posZ };
		double[] posEast = new double[] { this.aerwhale.posX + 1, this.aerwhale.posY + 2, this.aerwhale.posZ };
		double[] posWest = new double[] { this.aerwhale.posX - 3, this.aerwhale.posY + 2, this.aerwhale.posZ };
		double[] posSouth = new double[] { this.aerwhale.posX, this.aerwhale.posY + 2, this.aerwhale.posZ + 1 };
		double[] posNorth = new double[] { this.aerwhale.posX, this.aerwhale.posY + 2, this.aerwhale.posZ - 3 };

		if(check_region(new double[] { posUp[0] - 2, posUp[1], posUp[2] - 2 }, posUp)) {
			this.aerwhale.motionY -= 0.002;
		}
		if(check_region(new double[] { posDown[0] - 2, posDown[1], posDown[2] - 2 }, posDown)) {
			this.aerwhale.motionY += 0.002;
		}
		if(check_region(new double[] { posEast[0], posEast[1] - 2, posEast[2] - 2 }, posEast)) {
			this.aerwhale.motionX -= 0.002;
			this.aerwhale.motionY += 0.002;
		}
		if(check_region(new double[] { posWest[0], posWest[1] - 2, posWest[2] - 2 }, posWest)) {
			this.aerwhale.motionX += 0.002;
			this.aerwhale.motionY += 0.002;
		}
		if(check_region(new double[] { posSouth[0] - 2, posSouth[1] - 2, posSouth[2] }, posSouth)) {
			this.aerwhale.motionZ -= 0.002;
			this.aerwhale.motionY += 0.002;
		}
		if(check_region(new double[] { posNorth[0] - 2, posNorth[1] - 2, posNorth[2] }, posNorth)) {
			this.aerwhale.motionZ += 0.002;
			this.aerwhale.motionY += 0.002;
		}
	}
}
