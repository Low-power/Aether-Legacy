package com.gildedgames.the_aether.client.models.entities;

import com.gildedgames.the_aether.entities.passive.Sheepuff;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class SheepuffModel extends ModelQuadruped {

	private float headRotationAngleX;

	public SheepuffModel() {
		super(12, 0F);

		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-3F, -4F, -4F, 6, 6, 6, 0.6F);
		this.head.setRotationPoint(0F, 6F, -8F);

		this.body = new ModelRenderer(this, 28, 8);
		this.body.addBox(-4F, -10F, -7F, 8, 16, 6, 1.75F);
		this.body.setRotationPoint(0F, 5F, 2F);

		this.leg1 = new ModelRenderer(this, 0, 16);
		this.leg1.addBox(-2F, 0F, -2F, 4, 6, 4, 0.5F);
		this.leg1.setRotationPoint(-3F, 12F, 7F);

		this.leg2 = new ModelRenderer(this, 0, 16);
		this.leg2.addBox(-2F, 0F, -2F, 4, 6, 4, 0.5F);
		this.leg2.setRotationPoint(3F, 12F, 7F);

		this.leg3 = new ModelRenderer(this, 0, 16);
		this.leg3.addBox(-2F, 0F, -2F, 4, 6, 4, 0.5F);
		this.leg3.setRotationPoint(-3F, 12F, -5F);

		this.leg4 = new ModelRenderer(this, 0, 16);
		this.leg4.addBox(-2F, 0F, -2F, 4, 6, 4, 0.5F);
		this.leg4.setRotationPoint(3F, 12F, -5F);
	}

	@Override
	public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float prevLimbSwing, float partialTickTime) {
		super.setLivingAnimations(entity, limbSwing, prevLimbSwing, partialTickTime);

		this.head.rotationPointY = 6F + ((Sheepuff)entity).getHeadRotationPointY(partialTickTime) * 9F;
		this.headRotationAngleX = ((Sheepuff)entity).getHeadRotationAngleX(partialTickTime);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);

		this.head.rotateAngleX = this.headRotationAngleX;
	}

}
