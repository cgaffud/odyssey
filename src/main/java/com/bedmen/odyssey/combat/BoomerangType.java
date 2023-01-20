package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.AspectHolder;
import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;

import java.util.List;

public enum BoomerangType implements ThrowableType {
    WOODEN(4.0d, 0.8f, 200, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.0f))),
    SHARP_BONE(5.0d, 1.0f, 0, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.2f), new AspectInstance(Aspects.PIERCING, 1.0f))),
    HEAVY_BONE(5.0d, 1.0f, 0, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.2f), new AspectInstance(Aspects.PROJECTILE_KNOCKBACK, 1.0f))),
    SPEEDY_BONE(5.0d, 1.2f, 0, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.5f))),
    BONERANG(5.0d, 1.2f, 0, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.5f), new AspectInstance(Aspects.PIERCING, 1.0f), new AspectInstance(Aspects.PROJECTILE_KNOCKBACK, 1.0f))),
    CLOVER_STONE(6.0d, 1.0f, 0, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.2f), new AspectInstance(Aspects.PROJECTILE_LOOTING_LUCK, 1))),
    SHARP_GREATROOT(8.0d, 1.2f, 0, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.5f), new AspectInstance(Aspects.PIERCING, 2.0f))),
    HEAVY_GREATROOT(8.0d, 1.2f, 0, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.5f), new AspectInstance(Aspects.PROJECTILE_KNOCKBACK, 2.0f))),
    MULTISHOT_GREATROOT(8.0d, 1.2f, 0, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.5f), new AspectInstance(Aspects.MULTISHOT, 2.0f))),
    SPEEDY_GREATROOT(8.0d, 1.5f, 0, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.8f))),
    SUPER_GREATROOT(12.0d, 1.25f, 0, List.of(), List.of(new AspectInstance(Aspects.LOYALTY, 1.5f), new AspectInstance(Aspects.MAX_CHARGE_TIME, 0.5f)));

    public final double damage;
    public final float velocity;
    public final int burnTime;
    public final AspectHolder aspectHolder;

    BoomerangType(double damage, float velocity, int burnTime, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList){
        this.damage = damage;
        this.velocity = velocity;
        this.burnTime = burnTime;
        this.aspectHolder = new AspectHolder(abilityList, innateModifierList);
    }

    public AspectHolder getAspectHolder() {
        return this.aspectHolder;
    }

    public float getVelocity() {
        return this.velocity;
    }
}