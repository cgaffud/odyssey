package com.bedmen.odyssey.modifier;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;

public class MultishotModifier extends FloatModifier {
    protected MultishotModifier(String id) {
        super(id, f -> new TranslatableComponent("modifier.oddc."+id, strengthToNumberOfSideArrows(f), StringUtil.percentFormat(strengthToDamagePenalty(f))));
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
