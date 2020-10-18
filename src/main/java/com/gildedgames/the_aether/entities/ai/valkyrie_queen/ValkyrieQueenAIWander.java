package com.gildedgames.the_aether.entities.ai.valkyrie_queen;

import com.gildedgames.the_aether.entities.bosses.valkyrie_queen.ValkyrieQueen;
import net.minecraft.entity.ai.EntityAIWander;

public class ValkyrieQueenAIWander extends EntityAIWander {

    private ValkyrieQueen theQueen;

    public ValkyrieQueenAIWander(ValkyrieQueen creatureIn, double speedIn) {
        super(creatureIn, speedIn);

        this.theQueen = creatureIn;
    }

    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && this.theQueen.isBossReady();
    }

}
