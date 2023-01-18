package com.bedmen.odyssey.aspect;

import net.minecraft.network.chat.MutableComponent;

public class AspectInstance {
    public final Aspect aspect;
    public final float strength;
    public final AspectTooltipDisplaySetting aspectTooltipDisplaySetting;


    public AspectInstance(FloatAspect floatAspect, float strength){
        this((Aspect)floatAspect, strength);
    }

    public AspectInstance(IntegerAspect integerAspect, int strength){
        this(integerAspect, (float)strength);
    }

    public AspectInstance(BooleanAspect booleanAspect){
        this(booleanAspect, 1.0f);
    }

    protected AspectInstance(Aspect aspect, float strength){
        this(aspect, strength, AspectTooltipDisplaySetting.ALWAYS);
    }

    private AspectInstance(Aspect aspect, float strength, AspectTooltipDisplaySetting aspectTooltipDisplaySetting){
        this.aspect = aspect;
        this.strength = strength;
        this.aspectTooltipDisplaySetting = aspectTooltipDisplaySetting;
    }

    public AspectInstance withDisplaySetting(AspectTooltipDisplaySetting aspectTooltipDisplaySetting){
        return new AspectInstance(this.aspect, this.strength, aspectTooltipDisplaySetting);
    }


    public MutableComponent getMutableComponent(){
        return this.aspect.mutableComponentFunction.apply(this.strength);
    }

}
