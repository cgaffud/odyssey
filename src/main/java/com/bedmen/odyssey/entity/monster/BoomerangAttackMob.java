package com.bedmen.odyssey.entity.monster;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public interface BoomerangAttackMob {
    boolean hasBoomerang();
    InteractionHand getBoomerangHand();
    void performBoomerangAttack(LivingEntity target);
}
