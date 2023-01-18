package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;

public class MultishotAspect extends FloatAspect {
    protected MultishotAspect(String id) {
        super(id, (strength, optionalLevel) -> new TranslatableComponent("aspect.oddc."+id, strengthToNumberOfSideArrows(strength), StringUtil.percentFormat(strengthToDamagePenalty(strength))));
    }

    public static int strengthToNumberOfSideArrows(float strength){
        return 2 * Mth.ceil(strength / 2.0f);
    }

    public static int strengthToNumberOfTotalArrows(float strength){
        return strengthToNumberOfSideArrows(strength) + 1;
    }

    public static float strengthToDamagePenalty(float strength){
        return strength / ((float)strengthToNumberOfSideArrows(strength));
    }
}
