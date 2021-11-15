package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.CamoCreeperEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class CamoCreeperRenderer<T extends CamoCreeperEntity> extends OdysseyCreeperRenderer<T> {
    protected static final ResourceLocation DESERT_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/camo_creeper/desert_creeper.png");
    protected static final ResourceLocation SNOW_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/camo_creeper/snow_creeper.png");
    public CamoCreeperRenderer(EntityRendererManager p_i46186_1_) {
        super(p_i46186_1_);
    }

    public ResourceLocation getTextureLocation(T pEntity) {
        Biome biome = pEntity.getBiome();

        if(biome == null){
            return CREEPER_LOCATION;
        }

        if(biome.getBiomeCategory() == Biome.Category.DESERT){
            return DESERT_CREEPER_LOCATION;
        }

        if(biome.getBaseTemperature() < 0.1f){
            return SNOW_CREEPER_LOCATION;
        }

        return CREEPER_LOCATION;
    }
}
