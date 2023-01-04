package com.bedmen.odyssey.aspect;

import net.minecraft.world.entity.MobType;

public class Aspects {
    public static final Aspect DAMAGE_AGAINST_ARTHROPOD = new AdditiveConditionalMeleeAspect("damage_against_arthropod", livingEntity -> livingEntity.getMobType() == MobType.ARTHROPOD);
    public static final Aspect DAMAGE_AGAINST_UNDEAD = new AdditiveConditionalMeleeAspect("damage_against_undead", livingEntity -> livingEntity.getMobType() == MobType.UNDEAD);
}
