package com.bedmen.odyssey.entity.ai;

import com.bedmen.odyssey.entity.monster.OdysseyCreeper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class OdysseyCreeperSwellGoal extends Goal {
    private final OdysseyCreeper creeper;
    private LivingEntity target;

    public OdysseyCreeperSwellGoal(OdysseyCreeper p_i1655_1_) {
        this.creeper = p_i1655_1_;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        LivingEntity livingentity = this.creeper.getTarget();
        return this.creeper.getSwellDir() > 0 || livingentity != null && this.creeper.distanceToSqr(livingentity) < 9.0D;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.creeper.getNavigation().stop();
        this.target = this.creeper.getTarget();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.target = null;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (this.target == null) {
            this.creeper.setSwellDir(-1);
        } else if (this.creeper.distanceToSqr(this.target) > 49.0D) {
            this.creeper.setSwellDir(-1);
        } else if (!this.creeper.getSensing().hasLineOfSight(this.target)) {
            this.creeper.setSwellDir(-1);
        } else {
            this.creeper.setSwellDir(1);
        }
    }
}