package com.bedmen.odyssey.aspect;

public class EnvironmentConditionalAspect extends FloatAspect {
    public final AttackBoostFactorFunction attackBoostFactorFunction;

    protected EnvironmentConditionalAspect(String id, AttackBoostFactorFunction attackBoostFactorFunction) {
        super(id);
        this.attackBoostFactorFunction = attackBoostFactorFunction;
    }
}
