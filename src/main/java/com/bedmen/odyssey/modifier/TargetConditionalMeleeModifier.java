package com.bedmen.odyssey.modifier;

import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;

public class TargetConditionalMeleeModifier extends FloatModifier {
    public final Predicate<LivingEntity> livingEntityPredicate;

    protected TargetConditionalMeleeModifier(String id, Predicate<LivingEntity> livingEntityPredicate) {
        super(id);
        this.livingEntityPredicate = livingEntityPredicate;
    }
}