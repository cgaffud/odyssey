package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.aspect.object.Aspect;
import com.bedmen.odyssey.aspect.object.BooleanAspect;
import com.bedmen.odyssey.aspect.object.FloatAspect;
import com.bedmen.odyssey.aspect.object.IntegerAspect;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipContext;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipDisplaySetting;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctionInput;
import net.minecraft.network.chat.MutableComponent;

public class AspectInstance {
    public final Aspect aspect;
    public final float strength;
    public final AspectTooltipDisplaySetting aspectTooltipDisplaySetting;
    public final boolean obfuscated;


    public AspectInstance(FloatAspect floatAspect, float strength){
        this((Aspect)floatAspect, strength);
    }

    public AspectInstance(IntegerAspect integerAspect, int strength){
        this(integerAspect, (float)strength);
    }

    public AspectInstance(BooleanAspect booleanAspect){
        this(booleanAspect, 1.0f);
    }

    public AspectInstance(Aspect aspect, float strength){
        this(aspect, strength, AspectTooltipDisplaySetting.ALWAYS, false);
    }

    public AspectInstance(Aspect aspect, float strength, AspectTooltipDisplaySetting aspectTooltipDisplaySetting, boolean obfuscated){
        this.aspect = aspect;
        this.strength = strength;
        this.aspectTooltipDisplaySetting = aspectTooltipDisplaySetting;
        this.obfuscated = obfuscated;
    }

    public AspectInstance withDisplaySetting(AspectTooltipDisplaySetting aspectTooltipDisplaySetting){
        return new AspectInstance(this.aspect, this.strength, aspectTooltipDisplaySetting, this.obfuscated);
    }

    public AspectInstance withObfuscation(){
        return new AspectInstance(this.aspect, this.strength, aspectTooltipDisplaySetting, true);
    }

    public MutableComponent getMutableComponent(AspectTooltipContext aspectTooltipContext){
        return this.aspect.aspectTooltipFunction.apply(new AspectTooltipFunctionInput(this, aspectTooltipContext.optionalLevel, aspectTooltipContext.itemStack));
    }

}
