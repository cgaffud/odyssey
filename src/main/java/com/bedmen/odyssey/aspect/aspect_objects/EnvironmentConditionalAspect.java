package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AttackBoostFactorFunction;
import com.bedmen.odyssey.aspect.aspect_objects.FloatAspect;

public class EnvironmentConditionalAspect extends FloatAspect {
    public final AttackBoostFactorFunction attackBoostFactorFunction;

    protected EnvironmentConditionalAspect(String id, AttackBoostFactorFunction attackBoostFactorFunction) {
        super(id);
        this.attackBoostFactorFunction = attackBoostFactorFunction;
    }
}
