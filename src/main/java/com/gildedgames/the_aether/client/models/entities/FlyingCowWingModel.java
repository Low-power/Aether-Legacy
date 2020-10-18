package com.gildedgames.the_aether.client.models.entities;

import com.gildedgames.the_aether.entities.passive.mountable.FlyingCow;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class FlyingCowWingModel extends ModelBase {

	private ModelRenderer leftWingInner = new ModelRenderer(this, 0, 0);
	private ModelRenderer leftWingOuter = new ModelRenderer(this, 20, 0);
	private ModelRenderer rightWingInner = new ModelRenderer(this, 0, 0);
	private ModelRenderer rightWingOuter = new ModelRenderer(this, 40, 0);

	public FlyingCowWingModel() {
		this.leftWingInner.addBox(-1F, -8F, -4F, 2, 16, 8, 0F);
		this.leftWingOuter.addBox(-1F, -8F, -4F, 2, 16, 8, 0F);
		this.rightWingInner.addBox(-1F, -8F, -4F, 2, 16, 8, 0F);
		this.rightWingOuter.addBox(-1F, -8F, -4F, 2, 16, 8, 0F);

		this.rightWingOuter.rotateAngleY = (float) Math.PI;
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GL11.glPushMatrix();
		FlyingCow flyingcow = (FlyingCow)entity;
		GL11.glTranslatef(0F, -10F * scale, 0F);

		float wingBend = -((float) Math.acos((double) flyingcow.wingFold));

		float x = 32F * flyingcow.wingFold / 4F;
		float y = -32F * (float) Math.sqrt((double) (1F - flyingcow.wingFold * flyingcow.wingFold)) / 4F;

		float x2 = x * (float) Math.cos((double) flyingcow.wingAngle) - y * (float) Math.sin((double) flyingcow.wingAngle);
		float y2 = x * (float) Math.sin((double) flyingcow.wingAngle) + y * (float) Math.cos((double) flyingcow.wingAngle);

		this.leftWingInner.setRotationPoint(4F + x2, y2 + 12F, 0F);
		this.rightWingInner.setRotationPoint(-4F - x2, y2 + 12F, 0F);

		x *= 3F;
		x2 = x * (float) Math.cos((double) flyingcow.wingAngle) - y * (float) Math.sin((double) flyingcow.wingAngle);
		y2 = x * (float) Math.sin((double) flyingcow.wingAngle) + y * (float) Math.cos((double) flyingcow.wingAngle);

		this.leftWingOuter.setRotationPoint(4F + x2, y2 + 12F, 0F);
		this.rightWingOuter.setRotationPoint(-4F - x2, y2 + 12F, 0F);

		this.leftWingInner.rotateAngleZ = flyingcow.wingAngle + wingBend + ((float) Math.PI / 2F);
		this.leftWingOuter.rotateAngleZ = flyingcow.wingAngle - wingBend + ((float) Math.PI / 2F);
		this.rightWingInner.rotateAngleZ = -(flyingcow.wingAngle + wingBend - ((float) Math.PI / 2F));
		this.rightWingOuter.rotateAngleZ = -(flyingcow.wingAngle - wingBend + ((float) Math.PI / 2F));

		this.leftWingOuter.render(scale);
		this.leftWingInner.render(scale);
		this.rightWingOuter.render(scale);
		this.rightWingInner.render(scale);

		GL11.glPopMatrix();
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
	}

}
