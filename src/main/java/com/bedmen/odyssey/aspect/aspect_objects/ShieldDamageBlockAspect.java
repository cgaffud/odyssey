package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.AspectTooltipFunctions;
import net.minecraft.world.damagesource.DamageSource;

import java.util.function.Predicate;

public class ShieldDamageBlockAspect extends DamageSourcePredicateAspect {
    protected ShieldDamageBlockAspect(String id, Predicate<DamageSource> damageSourcePredicate) {
        super(id, AspectTooltipFunctions.ADDITIONAL_SHIELD_DAMAGE_BLOCK, damageSourcePredicate, AspectItemPredicates.SHIELD);
    }
}
