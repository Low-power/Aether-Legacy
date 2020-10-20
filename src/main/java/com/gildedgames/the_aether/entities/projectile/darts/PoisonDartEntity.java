package com.gildedgames.the_aether.entities.projectile.darts;

import com.gildedgames.the_aether.entities.effects.InebriationEffect;
import com.gildedgames.the_aether.entities.effects.InebriationPotion;
import com.gildedgames.the_aether.items.AetherItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class PoisonDartEntity extends BaseDartEntity {

	public PoisonDartEntity(World world) {
		super(world);
	}

	public PoisonDartEntity(World world, EntityLivingBase entity, float velocity) {
		super(world, entity, velocity);
	}

	public void entityInit() {
		super.entityInit();
		this.setDamage(0);
	}

	@Override
	public void onDartHit(MovingObjectPosition movingobjectposition) {
		super.onDartHit(movingobjectposition);

		if (!worldObj.isRemote) {
			if (movingobjectposition.entityHit != null) {
				if (movingobjectposition.entityHit instanceof EntityLivingBase) {
					((EntityLivingBase)movingobjectposition.entityHit).addPotionEffect(new InebriationEffect(InebriationPotion.inebriation.id, 500, 0));
				}
			}
		}

		this.isDead = false;
	}

	@Override
	protected ItemStack getStack() {
		return new ItemStack(AetherItems.dart, 1, 1);
	}

}
