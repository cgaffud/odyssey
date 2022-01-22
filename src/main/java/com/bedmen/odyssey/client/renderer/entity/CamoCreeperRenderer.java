package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.CamoCreeper;
import com.bedmen.odyssey.entity.monster.OdysseyCreeper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class CamoCreeperRenderer extends OdysseyCreeperRenderer {
    protected static final ResourceLocation DESERT_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/camo_creeper/desert_creeper.png");
    protected static final ResourceLocation SNOW_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/camo_creeper/snow_creeper.png");
    protected static final ResourceLocation STONE_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/camo_creeper/stone_creeper.png");
    protected static final ResourceLocation DEEPSLATE_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/camo_creeper/deepslate_creeper.png");

    public CamoCreeperRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(OdysseyCreeper odysseyCreeper) {
        Biome biome = ((CamoCreeper)odysseyCreeper).getBiome();

        if (odysseyCreeper.position().y < 0) {
            return DEEPSLATE_CREEPER_LOCATION;
        }

        if (odysseyCreeper.position().y < 56) {
            return STONE_CREEPER_LOCATION;
        }

        if(biome == null){
            return CREEPER_LOCATION;
        }

        if(biome.getBiomeCategory() == Biome.BiomeCategory.DESERT){
            return DESERT_CREEPER_LOCATION;
        }

        if(biome.getBaseTemperature() < 0.1f){
            return SNOW_CREEPER_LOCATION;
        }

        return CREEPER_LOCATION;
    }
}
