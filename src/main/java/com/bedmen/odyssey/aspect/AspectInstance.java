package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.util.OdysseyChatFormatting;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Locale;

public class AspectInstance {
    private final Aspect aspect;
    private final float strength;
    private final MutableComponent mutableComponent;

    public AspectInstance(Aspect aspect, float strength){
        this.aspect = aspect;
        this.strength = strength;
        this.mutableComponent = new TextComponent("+"+ StringUtil.floatFormat(this.strength)+" ").append(new TranslatableComponent("aspect.oddc."+aspect.name().toLowerCase(Locale.US)));
    }

    public Component getInnateComponent(){
        return this.mutableComponent.withStyle(OdysseyChatFormatting.LAVENDER);
    }
}
