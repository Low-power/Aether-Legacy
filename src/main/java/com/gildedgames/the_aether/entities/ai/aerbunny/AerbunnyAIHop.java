package com.gildedgames.the_aether.entities.ai.aerbunny;

import com.gildedgames.the_aether.entities.passive.mountable.Aerbunny;
import net.minecraft.entity.ai.EntityAIBase;

public class AerbunnyAIHop extends EntityAIBase {

    private Aerbunny aerbunny;

    public AerbunnyAIHop(Aerbunny aerbunny) {
        this.aerbunny = aerbunny;
        this.setMutexBits(8);
    }

    public boolean shouldExecute() {
        return this.aerbunny.motionZ > 0.0D || this.aerbunny.motionX > 0.0D || this.aerbunny.onGround;
    }

    public void updateTask() {
        if (this.aerbunny.moveForward != 0.0F) {
            this.aerbunny.getJumpHelper().setJumping();
        }
    }

}
