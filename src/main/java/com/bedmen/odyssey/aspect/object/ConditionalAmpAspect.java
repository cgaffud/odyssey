package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ConditionalAmpAspect extends BonusDamageAspect {

    public interface AttackBoostFactorFunction {
        float getBoostFactor(ItemStack itemStack, BlockPos pos, Level level);
    }

    public final AttackBoostFactorFunction attackBoostFactorFunction;

    protected ConditionalAmpAspect(String id, AttackBoostFactorFunction attackBoostFactorFunction) {
        super(id, AspectItemPredicates.ALL_WEAPON);
        this.attackBoostFactorFunction = attackBoostFactorFunction;
    }
}
