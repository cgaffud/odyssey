package com.bedmen.odyssey.potions;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Comparator;

public enum FireType {

    // List from strongest to weakest for sorting by enum ordinal

    SOUL("minecraft", "soul", true),
    HEX(Odyssey.MOD_ID, "hex", false),
    NONE();

    public final Material material0;
    public final Material material1;

    FireType(String modid, String id, boolean isSwitched){
        this.material0 = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(modid,"block/"+id+"_fire_" + (isSwitched ? 1 : 0)));
        this.material1 = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(modid,"block/"+id+"_fire_" + (isSwitched ? 0 : 1)));
    }

    FireType(){
        this.material0 = null;
        this.material1 = null;
    }

    public boolean isNone(){
        return this == NONE;
    }

    public boolean isNotNone(){
        return this != NONE;
    }

    public static FireType getStrongestFireEffectType(LivingEntity livingEntity){
        return livingEntity.getActiveEffects().stream()
                .filter(mobEffectInstance -> mobEffectInstance.getEffect() instanceof FireEffect)
                .map(mobEffectInstance -> ((FireEffect) mobEffectInstance.getEffect()).fireType)
                .min(Comparator.comparingInt(Enum::ordinal))
                .orElse(NONE);
    }

}
