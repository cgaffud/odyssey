package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.util.OdysseyChatFormatting;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class AspectInstance {
    public final Aspect aspect;
    public final float strength;
    private final MutableComponent mutableComponent;

    public AspectInstance(Aspect aspect, float strength){
        this.aspect = aspect;
        this.strength = strength;
        this.mutableComponent = new TextComponent("+"+ StringUtil.floatFormat(this.strength)+" ").append(new TranslatableComponent("aspect.oddc."+aspect.id));
    }

    public Component getInnateComponent(){
        return this.aspect.getMutableComponent(this.strength).withStyle(OdysseyChatFormatting.LAVENDER);
    }
}
