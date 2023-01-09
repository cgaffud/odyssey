package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.items.innate_aspect_items.InnateAspectItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class AspectUtil {
    //TODO: anvil aspects
    public static float getTotalAspectStrength(InnateAspectItem innateAspectItem, Aspect aspect){
        return getTotalAspectStrength(innateAspectItem, aspect1 -> aspect1 == aspect);
    }

    public static float getTotalAspectStrength(InnateAspectItem innateAspectItem, Predicate<Aspect> aspectPredicate){
        return innateAspectItem.getInnateAspectInstanceList().stream()
                .filter(aspectInstance -> aspectPredicate.test(aspectInstance.aspect))
                .reduce(0.0f,
                        (aFloat, aspectInstance) ->
                                aspectInstance.strength + aFloat,
                        Float::sum);
    }

    public static float getEnvironmentalAspectStrength(InnateAspectItem innateAspectItem, BlockPos blockPos, Level level){
        return innateAspectItem.getInnateAspectInstanceList().stream()
                .filter(aspectInstance -> aspectInstance.aspect instanceof EnvironmentConditionalMeleeAspect)
                .reduce(0.0f,
                        (aFloat, aspectInstance) ->
                                aspectInstance.strength
                                        * ((EnvironmentConditionalMeleeAspect)aspectInstance.aspect).attackBoostFactorFunction.getBoostFactor(blockPos, level)
                                        + aFloat,
                        Float::sum);
    }
}
