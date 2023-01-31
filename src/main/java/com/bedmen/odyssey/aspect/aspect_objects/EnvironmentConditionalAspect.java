package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AttackBoostFactorFunction;

public class EnvironmentConditionalAspect extends FloatAspect {
    public final AttackBoostFactorFunction attackBoostFactorFunction;

    protected EnvironmentConditionalAspect(String id, AttackBoostFactorFunction attackBoostFactorFunction) {
        super(id, AspectTooltipFunctions.NUMBER_ADDITION);
        this.attackBoostFactorFunction = attackBoostFactorFunction;
    }
}
