package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public class AdditiveConditionalMeleeAspect extends AdditiveMeleeAspect {
    public final Predicate<LivingEntity> livingEntityPredicate;
    protected AdditiveConditionalMeleeAspect(String id, Predicate<LivingEntity> livingEntityPredicate){
        super(id);
        this.livingEntityPredicate = livingEntityPredicate;
    }
}
