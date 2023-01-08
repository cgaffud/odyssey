package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.tags.OdysseyEntityTags;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.MobType;

public class Aspects {
    public static final Aspect DAMAGE_AGAINST_ARTHROPOD = new ConditionalMeleeAspect("damage_against_arthropod", livingEntity -> livingEntity.getMobType() == MobType.ARTHROPOD);
    public static final Aspect SMITE_DAMAGE = new ConditionalMeleeAspect("smite_damage", livingEntity -> livingEntity.getMobType() == MobType.UNDEAD);
    public static final Aspect KNOCKBACK = new Aspect("knockback");
    public static final Aspect FATAL_HIT = new Aspect("fatal_hit");
    public static final Aspect SWEEP_DAMAGE = new Aspect("sweep_damage");
    public static final Aspect POISON_DAMAGE = new Aspect("poison_damage");
    public static final Aspect COBWEB_CHANCE = new Aspect("cobweb_chance", aspectInstance -> new TranslatableComponent("aspect.oddc."+aspectInstance.aspect.id, StringUtil.percentFormat(aspectInstance.strength)));
    public static final Aspect HYDRO_DAMAGE = new ConditionalMeleeAspect("hydro_damage", livingEntity -> livingEntity.getType().is(OdysseyEntityTags.HYDROPHOBIC));
}
