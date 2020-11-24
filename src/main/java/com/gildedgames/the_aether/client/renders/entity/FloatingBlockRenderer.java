package com.gildedgames.the_aether.client.renders.entity;

import com.gildedgames.the_aether.entities.block.FloatingBlockEntity;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FloatingBlockRenderer extends Render {

	private final RenderBlocks renderBlocks = new RenderBlocks();

	public FloatingBlockRenderer() {
		super();
		this.shadowSize = 0.5F;
	}

	private void renderFloatingBlock(FloatingBlockEntity entity, double x, double y, double z) {
		Block block = entity.get_block();
		if(block == null || block == Blocks.air) return;

		bindTexture(TextureMap.locationBlocksTexture);

		World world = entity.worldObj;
		int block_x = MathHelper.floor_double(entity.posX);
		int block_y = MathHelper.floor_double(entity.posY);
		int block_z = MathHelper.floor_double(entity.posZ);
		if(block == world.getBlock(block_x, block_y, block_z)) return;

		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y + 0.48F, (float)z);
		this.bindEntityTexture(entity);
		GL11.glDisable(GL11.GL_LIGHTING);

		this.renderBlocks.setRenderBoundsFromBlock(block);
		this.renderBlocks.renderBlockSandFalling(block, world, block_x, block_y, block_z, entity.get_metadata());

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float f1, float f2) {
		this.renderFloatingBlock((FloatingBlockEntity)entity, x, y, z);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TextureMap.locationBlocksTexture;
	}

}
