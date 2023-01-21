package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.AspectHolder;

public interface ThrowableType {
    AspectHolder getAspectHolder();
    float getVelocity();
    double getThrownDamage();
    String getName();

    static ThrowableType getThrowableType(String name){
        if(name.startsWith(BoomerangType.BOOMERANG_TYPE_PREFIX_TAG)){
            String substring = name.substring(BoomerangType.BOOMERANG_TYPE_PREFIX_TAG.length());
            return BoomerangType.valueOf(substring);
        } else {
            String substring = name.substring(SpearType.SPEAR_TYPE_TAG_PREFIX.length());
            return BoomerangType.valueOf(substring);
        }
    }
}
