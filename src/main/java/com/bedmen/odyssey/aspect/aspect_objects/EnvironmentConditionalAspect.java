package com.bedmen.odyssey.aspect.aspect_objects;

public class EnvironmentConditionalAspect extends FloatAspect {
    public final AttackBoostFactorFunction attackBoostFactorFunction;

    protected EnvironmentConditionalAspect(String id, AttackBoostFactorFunction attackBoostFactorFunction) {
        super(id, AspectTooltipFunctions.NUMBER_ADDITION);
        this.attackBoostFactorFunction = attackBoostFactorFunction;
    }
}
