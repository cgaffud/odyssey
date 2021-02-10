package com.bedmen.odyssey.entity.ai;

import com.bedmen.odyssey.entity.TrooperEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class TrooperAttackGoal extends MeleeAttackGoal {
    private final TrooperEntity trooperEntity;
    private int raiseArmTicks;

    public TrooperAttackGoal(TrooperEntity trooperEntity, double speedIn, boolean longMemoryIn) {
        super(trooperEntity, speedIn, longMemoryIn);
        this.trooperEntity = trooperEntity;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        super.startExecuting();
        this.raiseArmTicks = 0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        super.resetTask();
        this.trooperEntity.setAggroed(false);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        super.tick();
        ++this.raiseArmTicks;
        if (this.raiseArmTicks >= 5 && this.func_234041_j_() < this.func_234042_k_() / 2) {
            this.trooperEntity.setAggroed(true);
        } else {
            this.trooperEntity.setAggroed(false);
        }

    }
}
