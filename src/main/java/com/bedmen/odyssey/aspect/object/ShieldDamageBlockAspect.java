package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import net.minecraft.world.damagesource.DamageSource;

import java.util.function.Predicate;

public class ShieldDamageBlockAspect extends DamageSourcePredicateAspect {
    protected ShieldDamageBlockAspect(String id, Predicate<DamageSource> damageSourcePredicate) {
        super(id, AspectTooltipFunctions.ADDITIONAL_SHIELD_DAMAGE_BLOCK, damageSourcePredicate, AspectItemPredicates.SHIELD);
    }
}
