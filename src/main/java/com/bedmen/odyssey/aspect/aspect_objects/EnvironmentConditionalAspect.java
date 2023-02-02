package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.AspectTooltipFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class EnvironmentConditionalAspect extends FloatAspect {

    public interface AttackBoostFactorFunction {
        float getBoostFactor(BlockPos pos, Level level);
    }

    public final AttackBoostFactorFunction attackBoostFactorFunction;

    protected EnvironmentConditionalAspect(String id, AttackBoostFactorFunction attackBoostFactorFunction) {
        super(id, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.ALL_WEAPON);
        this.attackBoostFactorFunction = attackBoostFactorFunction;
    }
}
