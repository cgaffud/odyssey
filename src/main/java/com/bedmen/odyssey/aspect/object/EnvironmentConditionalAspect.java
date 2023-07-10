package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

// Want this to be slowly phased out by ConditionalAmpAspect
public class EnvironmentConditionalAspect extends BonusDamageAspect {

    public interface AttackBoostFactorFunction {
        float getBoostFactor(BlockPos pos, Level level);
    }

    public final AttackBoostFactorFunction attackBoostFactorFunction;

    protected EnvironmentConditionalAspect(String id, AttackBoostFactorFunction attackBoostFactorFunction) {
        super(id, AspectItemPredicates.ALL_WEAPON);
        this.attackBoostFactorFunction = attackBoostFactorFunction;
    }
}
