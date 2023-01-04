package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public class AdditiveConditionalMeleeAspect extends Aspect {
    public final Predicate<LivingEntity> livingEntityPredicate;
    protected AdditiveConditionalMeleeAspect(String name, Predicate<LivingEntity> livingEntityPredicate){
        super(name);
        this.livingEntityPredicate = livingEntityPredicate;
    }

    public MutableComponent getMutableComponent(Object... args){
        return new TextComponent("+"+ StringUtil.floatFormat((Float) args[0])+" ").append(new TranslatableComponent("aspect.oddc."+this.name));
    }
}
