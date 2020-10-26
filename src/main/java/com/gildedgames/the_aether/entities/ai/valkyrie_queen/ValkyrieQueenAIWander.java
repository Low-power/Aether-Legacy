package com.gildedgames.the_aether.entities.ai.valkyrie_queen;

import com.gildedgames.the_aether.entities.bosses.valkyrie_queen.ValkyrieQueen;
import net.minecraft.entity.ai.EntityAIWander;

public class ValkyrieQueenAIWander extends EntityAIWander {

    private ValkyrieQueen queen;

    public ValkyrieQueenAIWander(ValkyrieQueen queen, double speed) {
        super(queen, speed);

        this.queen = queen;
    }

    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && queen.isBossReady();
    }

}
