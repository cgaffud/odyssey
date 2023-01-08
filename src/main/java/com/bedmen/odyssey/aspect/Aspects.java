package com.bedmen.odyssey.aspect;

import net.minecraft.world.entity.MobType;

public class Aspects {
    public static final Aspect DAMAGE_AGAINST_ARTHROPOD = new AdditiveConditionalMeleeAspect("damage_against_arthropod", livingEntity -> livingEntity.getMobType() == MobType.ARTHROPOD);
    public static final Aspect DAMAGE_AGAINST_UNDEAD = new AdditiveConditionalMeleeAspect("damage_against_undead", livingEntity -> livingEntity.getMobType() == MobType.UNDEAD);
    public static final Aspect KNOCKBACK = new MultiplicativeAspect("knockback");
    public static final Aspect FATAL_HIT = new AssertAspect("fatal_hit");
    public static final Aspect SWEEP_DAMAGE = new AssertAspect("sweep_damage");
    public static final Aspect POISON_DAMAGE = new AssertAspect("poison_damage");
}
