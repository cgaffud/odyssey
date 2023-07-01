package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

public interface Overgrown {

    default void regenerate(LivingEntity livingEntity, float healAmount) {
        if (GeneralUtil.isHashTick(livingEntity, livingEntity.level, 50) && (livingEntity.getHealth() < livingEntity.getMaxHealth())) {
            livingEntity.heal(healAmount);
            if (!livingEntity.level.isClientSide()) {
                RandomSource randomSource = livingEntity.getRandom();
                for(int i = 0; i < 4; ++i) {
                    ((ServerLevel)livingEntity.level).sendParticles(ParticleTypes.HEART,livingEntity.getX() + randomSource.nextFloat()-0.5f, livingEntity.getY() + randomSource.nextFloat()*2, livingEntity.getZ() + randomSource.nextFloat()-0.5f, 2, 0.2D, 0.2D, 0.2D, 0.0D);
                }
            }

        }
    }
}
