package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.AspectHolder;

public interface ThrowableType {
    AspectHolder getAspectHolder();
    float getVelocity();
    double getThrownDamage();
}
