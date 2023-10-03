package com.bedmen.odyssey.entity.ai;

import com.bedmen.odyssey.entity.monster.SculkMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

// Dummy version of complex Warden AI - Pathfind to disturbance
public class SculkFollowSoundsGoal extends Goal {
    private SculkMob sculkMob;
    private Mob mob;
    private final double speedModifier;

    private Path path;

    public SculkFollowSoundsGoal(SculkMob sculkMob, double speedModifier) {
        this.speedModifier = speedModifier;
        this.sculkMob = sculkMob;
        this.mob = this.sculkMob.asMob();
    }

    @Override
    public boolean canUse() {
        return this.sculkMob.getSourceBlockPos() != null && !this.sculkMob.getAngerLevel().isAngry();
    }

    @Override
    public boolean canContinueToUse() {
        return this.sculkMob.getSourceBlockPos() != null && !this.sculkMob.getAngerLevel().isAngry();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.speedModifier);
    }

    public void stop() {
        this.mob.getNavigation().stop();
    }

    public void tick() {
        Vec3 target = new Vec3(this.sculkMob.getSourceBlockPos().getX(), this.sculkMob.getSourceBlockPos().getY(), this.sculkMob.getSourceBlockPos().getZ());
        this.mob.getLookControl().setLookAt(target);
        if (path == null || !path.getTarget().equals(this.sculkMob.getSourceBlockPos())) {
            path = this.mob.getNavigation().createPath(this.sculkMob.getSourceBlockPos(), 0);
        }
        this.mob.getNavigation().moveTo(path, this.speedModifier);
    }

}
