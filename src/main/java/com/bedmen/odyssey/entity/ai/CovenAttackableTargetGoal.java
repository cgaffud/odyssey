package com.bedmen.odyssey.entity.ai;

import com.bedmen.odyssey.entity.boss.coven.CovenWitch;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

// Generally needs to be able to request a target from its corresponding coven witch
public class CovenAttackableTargetGoal extends TargetGoal {

    private static final int DEFAULT_RANDOM_INTERVAL = 10;
    private CovenWitch covenWitch;
    protected final int randomInterval;
    @Nullable
    protected LivingEntity target;
    protected TargetingConditions targetConditions;

    public CovenAttackableTargetGoal(CovenWitch p_26060_, boolean p_26062_) {
        this(p_26060_, 10, p_26062_, false, (Predicate<LivingEntity>)null);
    }

    public CovenAttackableTargetGoal(CovenWitch p_199891_, boolean p_199893_, Predicate<LivingEntity> p_199894_) {
        this(p_199891_, 10, p_199893_, false, p_199894_);
    }

    public CovenAttackableTargetGoal(CovenWitch p_26064_, boolean p_26066_, boolean p_26067_) {
        this(p_26064_, 10, p_26066_, p_26067_, (Predicate<LivingEntity>)null);
    }

    public CovenAttackableTargetGoal(CovenWitch p_26053_, int p_26055_, boolean p_26056_, boolean p_26057_, @Nullable Predicate<LivingEntity> p_26058_) {
        super(p_26053_, p_26056_, p_26057_);
        this.covenWitch = p_26053_;
        this.randomInterval = reducedTickDelay(p_26055_);
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(p_26058_);
    }

    public boolean canUse() {
        if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
            return false;
        } else {
            this.covenWitch.getTarget();
            return this.target != null;
        }
    }

    public void start() {
        this.mob.setTarget(this.target);
        super.start();
    }

    public void setTarget(@Nullable LivingEntity p_26071_) {
        this.target = p_26071_;
    }
}
