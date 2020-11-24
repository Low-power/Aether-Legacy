package com.gildedgames.the_aether.blocks.dungeon;

import com.gildedgames.the_aether.blocks.AetherBlocks;
import com.gildedgames.the_aether.entities.bosses.EntityFireMinion;
import com.gildedgames.the_aether.entities.bosses.Valkyrie;
import com.gildedgames.the_aether.entities.hostile.Sentry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class DungeonTrap extends Block {

	private Block pickBlock;

	public DungeonTrap(Block pickBlock) {
		super(Material.rock);

		this.pickBlock = pickBlock;
		setBlockUnbreakable();
	}

	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		if (entity instanceof EntityPlayer) {
			world.setBlock(x, y, z, this.pickBlock);
			if(!world.isRemote) {
				if(this == AetherBlocks.carved_trap) {
					Sentry sentry = new Sentry(world, x + 2D, y + 1D, z + 2D);
					world.spawnEntityInWorld(sentry);
				} else if(this == AetherBlocks.angelic_trap) {
					Valkyrie valkyrie = new Valkyrie(world);
					valkyrie.setPosition(x + 0.5D, y + 1D, z + 0.5D);
					world.spawnEntityInWorld(valkyrie);
				} else if(this == AetherBlocks.hellfire_trap) {
					EntityFireMinion minion = new EntityFireMinion(world);
					minion.setPosition(x + 0.5D, y + 1D, z + 0.5D);
					world.spawnEntityInWorld(minion);
				}
			}
			world.playSoundEffect(x, y, z, "random.door_close", 2F, world.rand.nextFloat() - world.rand.nextFloat() * 0.2F + 1.2F);
		}
	}

}
