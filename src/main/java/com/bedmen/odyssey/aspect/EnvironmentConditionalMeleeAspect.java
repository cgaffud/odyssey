package com.bedmen.odyssey.aspect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.function.Function;
import java.util.function.Predicate;

public class EnvironmentConditionalMeleeAspect extends Aspect {
    public final AttackBoostFactorFunction attackBoostFactorFunction;

    protected EnvironmentConditionalMeleeAspect(String id, AttackBoostFactorFunction attackBoostFactorFunction) {
        super(id);
        this.attackBoostFactorFunction = attackBoostFactorFunction;
    }
}
