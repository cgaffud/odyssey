package com.bedmen.odyssey.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.EnumSet;

public class ReturnToPosGoal extends Goal {
    private final PathfinderMob mob;
    private final BlockPos dungeonPos;
    private final double speedModifier;
    private final float range;

    public ReturnToPosGoal(PathfinderMob mob, @Nonnull BlockPos dungeonPos, double speedModifier, float range) {
        this.mob = mob;
        this.dungeonPos = dungeonPos;
        this.speedModifier = speedModifier;
        this.range = range;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        return mob.distanceToSqr(this.dungeonPos.getX() + 0.5d, this.mob.getY(), this.dungeonPos.getZ() + 0.5d) > (double) (this.range * this.range);
    }

    public void start() {
        this.mob.getNavigation().moveTo(this.dungeonPos.getX(), this.dungeonPos.getY(), this.dungeonPos.getZ(), this.speedModifier);
    }
}
