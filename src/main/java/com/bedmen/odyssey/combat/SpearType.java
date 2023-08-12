package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpearType extends ThrowableType {

    public static final Set<SpearType> NEED_MODEL_REGISTERED_SET = new HashSet<>();
    public static final String SPEAR_TYPE_TAG_PREFIX = "SpearType";
    public final ResourceLocation entityTexture;
    public final ModelResourceLocation itemModelResourceLocation;
    public final boolean isTrident;

    SpearType(String id, double damage, float velocity, SoundProfile soundProfile, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList){
        super(id, damage, velocity, soundProfile, abilityList, innateModifierList);
        this.entityTexture = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/"+id+".png");
        this.itemModelResourceLocation = new ModelResourceLocation(Odyssey.MOD_ID, id, "inventory");
        this.isTrident = id.endsWith("trident");
        NEED_MODEL_REGISTERED_SET.add(this);
    }

    protected String getPrefix(){
        return SPEAR_TYPE_TAG_PREFIX;
    }
}
