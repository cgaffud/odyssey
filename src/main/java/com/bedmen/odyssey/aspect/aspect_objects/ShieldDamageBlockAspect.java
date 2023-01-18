package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.items.odyssey_versions.AspectShieldItem;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.LevelAccessor;

import java.util.function.Predicate;

public class ShieldDamageBlockAspect extends DamageSourcePredicateAspect {
    protected ShieldDamageBlockAspect(String id, Predicate<DamageSource> damageSourcePredicate) {
        super(id, (strength, optionalLevel) -> new TranslatableComponent("aspect.oddc."+id, StringUtil.floatFormat(AspectShieldItem.getDifficultyAdjustedDamageBlock(strength, optionalLevel.map(LevelAccessor::getDifficulty).orElse(Difficulty.NORMAL)))), damageSourcePredicate);
    }
}
