package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectHolder;
import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public enum SpearType implements ThrowableType {
    FLINT("flint", 4.0d, 1.0f, List.of(), List.of(new AspectInstance(Aspects.VELOCITY, 0.5f))),
    AMETHYST("amethyst", 6.0d, 1.0f, List.of(), List.of(new AspectInstance(Aspects.VELOCITY, 1.0f)));

    public static final String SPEAR_TYPE_TAG_PREFIX = "SpearType:";
    public final double damage;
    public final float velocity;
    public final AspectHolder aspectHolder;
    public final ResourceLocation entityTexture;
    public final ModelResourceLocation itemModelResourceLocation;
    public final ModelResourceLocation entityModelResourceLocation;

    SpearType(String id, double damage, float velocity, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList){
        this.damage = damage;
        this.velocity = velocity;
        this.aspectHolder = new AspectHolder(abilityList, innateModifierList);
        String itemName = id+"_spear";
        this.entityTexture = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/"+itemName+".png");
        this.itemModelResourceLocation = new ModelResourceLocation(Odyssey.MOD_ID, itemName, "inventory");
        this.entityModelResourceLocation = new ModelResourceLocation(Odyssey.MOD_ID, itemName+"_in_hand", "inventory");
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
