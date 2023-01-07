package com.bedmen.odyssey.entity.ai;

import com.bedmen.odyssey.entity.boss.coven.CovenMaster;
import com.bedmen.odyssey.entity.boss.coven.CovenWitch;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.Optional;

public class CovenReturnToMasterGoal extends Goal {

    CovenWitch witch;

    public CovenReturnToMasterGoal(CovenWitch covenWitch) {
        this.witch = covenWitch;
    }

    @Override
    public boolean canUse() {
        return this.witch.isAlive() && this.witch.getMaster().isPresent();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        Optional<CovenMaster> optionalCovenMaster = this.witch.getMaster();
        if (optionalCovenMaster.isPresent()) {
            CovenMaster covenMaster = optionalCovenMaster.get();
            double d0 = this.witch.distanceToSqr(covenMaster.getX(), covenMaster.getY(), covenMaster.getZ());

            if (d0 > (CovenMaster.CENTER_RANGE * CovenMaster.CENTER_RANGE))
                covenMaster.bringWitchesToMe();
        }
    }
}

