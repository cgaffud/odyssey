package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectHolder;
import com.bedmen.odyssey.aspect.AspectInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public enum SpearType implements ThrowableType {
    STONE("stone", 4.0d, 1.0f, List.of(), List.of());

    public static final String SPEAR_TYPE_TAG_PREFIX = "SpearType:";
    public final double damage;
    public final float velocity;
    public final AspectHolder aspectHolder;
    public final ResourceLocation entityTexture;

    SpearType(String id, double damage, float velocity, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList){
        this.damage = damage;
        this.velocity = velocity;
        this.aspectHolder = new AspectHolder(abilityList, innateModifierList);
        this.entityTexture = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/"+id+"_spear.png");
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
