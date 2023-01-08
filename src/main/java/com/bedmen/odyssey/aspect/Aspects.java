package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.MobType;

public class Aspects {
    public static final Aspect DAMAGE_AGAINST_ARTHROPOD = new ConditionalMeleeAspect("damage_against_arthropod", Aspects::additionFunction, livingEntity -> livingEntity.getMobType() == MobType.ARTHROPOD);
    public static final Aspect DAMAGE_AGAINST_UNDEAD = new ConditionalMeleeAspect("damage_against_undead", Aspects::additionFunction, livingEntity -> livingEntity.getMobType() == MobType.UNDEAD);
    public static final Aspect KNOCKBACK = new Aspect("knockback", Aspects::multiplicationFunction);
    public static final Aspect FATAL_HIT = new Aspect("fatal_hit", Aspects::hpFunction);
    public static final Aspect SWEEP_DAMAGE = new Aspect("sweep_damage", Aspects::numberFunction);
    public static final Aspect POISON_DAMAGE = new Aspect("poison_damage", Aspects::numberFunction);
    public static final Aspect COBWEB_CHANCE = new Aspect("cobweb_chance", Aspects::percentileFunction);

    public static MutableComponent numberFunction(AspectInstance aspectInstance){
        return new TextComponent(StringUtil.floatFormat(aspectInstance.strength)+" ").append(new TranslatableComponent("aspect.oddc."+aspectInstance.aspect.id));
    }

    public static MutableComponent hpFunction(AspectInstance aspectInstance){
        return new TextComponent(StringUtil.floatFormat(aspectInstance.strength)+"HP ").append(new TranslatableComponent("aspect.oddc."+aspectInstance.aspect.id));
    }

    public static MutableComponent additionFunction(AspectInstance aspectInstance){
        return new TextComponent("+"+StringUtil.floatFormat(aspectInstance.strength)+" ").append(new TranslatableComponent("aspect.oddc."+aspectInstance.aspect.id));
    }

    public static MutableComponent multiplicationFunction(AspectInstance aspectInstance){
        return new TextComponent("x"+StringUtil.floatFormat(aspectInstance.strength)+" ").append(new TranslatableComponent("aspect.oddc."+aspectInstance.aspect.id));
    }

    public static MutableComponent percentileFunction(AspectInstance aspectInstance){
        return new TextComponent(StringUtil.percentFormat(aspectInstance.strength)+" ").append(new TranslatableComponent("aspect.oddc."+aspectInstance.aspect.id));
    }
}
