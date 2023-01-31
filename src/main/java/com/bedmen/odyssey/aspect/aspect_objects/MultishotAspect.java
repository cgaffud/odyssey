package com.bedmen.odyssey.aspect.aspect_objects;

import net.minecraft.util.Mth;

public class MultishotAspect extends FloatAspect {
    protected MultishotAspect(String id) {
        super(id, AspectTooltipFunctions.MULTISHOT);
    }

    public static int strengthToNumberOfSideProjectiles(float strength){
        return 2 * Mth.ceil(strength / 2.0f);
    }

    public static int strengthToNumberOfTotalProjectiles(float strength){
        return strengthToNumberOfSideProjectiles(strength) + 1;
    }

    public static float strengthToDamagePenalty(float strength){
        return strength / ((float) strengthToNumberOfSideProjectiles(strength));
    }
}
