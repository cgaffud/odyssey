package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.network.chat.Component;

public class AspectInstance {
    public final Aspect aspect;
    public final float strength;
    public final Component innateComponenet;

    public AspectInstance(Aspect aspect, float strength){
        this.aspect = aspect;
        this.strength = strength;
        this.innateComponenet = this.aspect.mutableComponentFunction.apply(this).withStyle(OdysseyChatFormatting.LAVENDER);
    }
}
