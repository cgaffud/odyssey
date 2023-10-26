package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import net.minecraft.util.Mth;

public class MultishotAspect extends FloatAspect {
    protected MultishotAspect() {
        super("multishot", 2.0f, AspectTooltipFunctions.MULTISHOT, AspectItemPredicates.CROSSBOW, true);
    }

    public static int valueToNumberOfSideProjectiles(float strength){
        return 2 * Mth.ceil(strength / 2.0f);
    }

    public static int valueToNumberOfTotalProjectiles(float strength){
        return valueToNumberOfSideProjectiles(strength) + 1;
    }

    public static float valueToDamagePenalty(float strength){
        return strength / ((float) valueToNumberOfSideProjectiles(strength));
    }
}
