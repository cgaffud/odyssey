package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.aspect.aspect_objects.Aspect;
import com.bedmen.odyssey.aspect.aspect_objects.BooleanAspect;
import com.bedmen.odyssey.aspect.aspect_objects.FloatAspect;
import com.bedmen.odyssey.aspect.aspect_objects.IntegerAspect;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;

import java.util.Optional;

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


    public MutableComponent getMutableComponent(Optional<Level> optionalLevel){
        return this.aspect.aspectTooltipFunction.apply(this.strength, optionalLevel);
    }

}
