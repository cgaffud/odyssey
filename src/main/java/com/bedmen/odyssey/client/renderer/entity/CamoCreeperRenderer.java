package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.CamoCreeperModel;
import com.bedmen.odyssey.client.model.OdysseyCreeperModel;
import com.bedmen.odyssey.entity.monster.CamoCreeper;
import com.bedmen.odyssey.world.gen.biome.BiomeResourceKeys;
import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public class CamoCreeperRenderer extends OdysseyCreeperRenderer<CamoCreeper> {
    public static final ResourceLocation GRAY_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/creeper/gray_creeper.png");
    protected static final ResourceLocation DESERT_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/creeper/desert_creeper.png");
    protected static final ResourceLocation SNOW_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/creeper/snow_creeper.png");
    protected static final ResourceLocation STONE_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/creeper/stone_creeper.png");
    protected static final ResourceLocation DEEPSLATE_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/creeper/deepslate_creeper.png");
    protected static final ResourceLocation RED_DESERT_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/creeper/red_desert_creeper.png");
    protected static final ResourceLocation DRIPSTONE_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/creeper/dripstone_creeper.png");


    public CamoCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, () -> new CamoCreeperModel<>(context.bakeLayer(OdysseyCreeperModel.LAYER_LOCATION)));
    }

    public ResourceLocation getTextureLocation(CamoCreeper camoCreeper){
        return getTexture(camoCreeper);
    }

   
    public static ResourceLocation getTexture(CamoCreeper camoCreeper) {
        Holder<Biome> biomeHolder = camoCreeper.level.getBiome(camoCreeper.blockPosition());

        if(biomeHolder.is(Biomes.LUSH_CAVES)){
            return GRAY_CREEPER_LOCATION;
        }
        if (biomeHolder.is(Biomes.DRIPSTONE_CAVES)) {
            return DRIPSTONE_CREEPER_LOCATION;
        }
        if (camoCreeper.position().y < 0) {
            return DEEPSLATE_CREEPER_LOCATION;
        }
        if (camoCreeper.position().y < GeneralUtil.START_OF_UNDERGROUND
                || biomeHolder.is(Biomes.STONY_PEAKS)
                || biomeHolder.is(Biomes.STONY_SHORE)) {
            return STONE_CREEPER_LOCATION;
        }

        if(biomeHolder.get().coldEnoughToSnow(camoCreeper.blockPosition())){
            return SNOW_CREEPER_LOCATION;
        }

        if(biomeHolder.is(Biomes.DESERT) || biomeHolder.is(Biomes.BEACH) || biomeHolder.is(BiomeResourceKeys.TROPICS_RESOURCE_KEY)){
            return DESERT_CREEPER_LOCATION;
        }

        if (biomeHolder.is(BiomeTags.IS_BADLANDS)) {
            return RED_DESERT_CREEPER_LOCATION;
        }

        return GRAY_CREEPER_LOCATION;
    }
}
