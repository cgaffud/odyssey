package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.AspectHolder;
import com.bedmen.odyssey.aspect.AspectInstance;

import java.util.List;

public enum SpearType implements ThrowableType {
    STONE(4.0d, 0.8f, List.of(), List.of());

    public static final String SPEAR_TYPE_TAG_PREFIX = "SpearType:";
    public final double damage;
    public final float velocity;
    public final AspectHolder aspectHolder;

    SpearType(double damage, float velocity, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList){
        this.damage = damage;
        this.velocity = velocity;
        this.aspectHolder = new AspectHolder(abilityList, innateModifierList);
    }

    public AspectHolder getAspectHolder() {
        return this.aspectHolder;
    }

    public float getVelocity() {
        return this.velocity;
    }

    public double getThrownDamage() {
        return this.damage;
    }

    public String getName(){
        return SPEAR_TYPE_TAG_PREFIX+this.name();
    }
}
