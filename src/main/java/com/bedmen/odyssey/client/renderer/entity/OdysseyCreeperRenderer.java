package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.OdysseyCreeperModel;
import com.bedmen.odyssey.client.renderer.entity.layer.CreeperDripstoneLayer;
import com.bedmen.odyssey.entity.monster.OdysseyCreeper;
import com.bedmen.odyssey.util.GeneralUtil;
import com.bedmen.odyssey.world.gen.biome.BiomeResourceKeys;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.function.Supplier;

public class OdysseyCreeperRenderer<T extends OdysseyCreeper> extends AbstractCreeperRenderer<T, OdysseyCreeperModel<T>> {

    public static final ResourceLocation GRAY_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/creeper/gray_creeper.png");
    protected static final ResourceLocation DESERT_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/creeper/desert_creeper.png");
    protected static final ResourceLocation SNOW_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/creeper/snow_creeper.png");
    protected static final ResourceLocation STONE_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/creeper/stone_creeper.png");
    protected static final ResourceLocation DEEPSLATE_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/creeper/deepslate_creeper.png");
    protected static final ResourceLocation RED_DESERT_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/creeper/red_desert_creeper.png");
    protected static final ResourceLocation DRIPSTONE_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/creeper/dripstone_creeper.png");

    protected static final ResourceLocation OVERGROWN_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/creeper/overgrown.png");

    public OdysseyCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, () -> new OdysseyCreeperModel<>(context.bakeLayer(OdysseyCreeperModel.LAYER_LOCATION)));
        this.addLayer(new CreeperDripstoneLayer<>(this, context.getModelSet()));
    }

    public ResourceLocation getTextureLocation(OdysseyCreeper odysseyCreeper) {
        switch (odysseyCreeper.getCreeperType()){
            case CAMO -> {
                return getCamoTexture(odysseyCreeper);
            }
            case OVERGROWN -> {
                return OVERGROWN_LOCATION;
            }
        }
        return CREEPER_LOCATION;
    }

    public static ResourceLocation getCamoTexture(OdysseyCreeper odysseyCreeper){
        Holder<Biome> biomeHolder = odysseyCreeper.level.getBiome(odysseyCreeper.blockPosition());

        if(biomeHolder.is(Biomes.LUSH_CAVES)){
            return GRAY_CREEPER_LOCATION;
        }
        if (biomeHolder.is(Biomes.DRIPSTONE_CAVES)) {
            return DRIPSTONE_CREEPER_LOCATION;
        }
        if (odysseyCreeper.position().y < 0) {
            return DEEPSLATE_CREEPER_LOCATION;
        }
        if (odysseyCreeper.position().y < GeneralUtil.START_OF_UNDERGROUND
                || biomeHolder.is(Biomes.STONY_PEAKS)
                || biomeHolder.is(Biomes.STONY_SHORE)) {
            return STONE_CREEPER_LOCATION;
        }

        if(biomeHolder.get().coldEnoughToSnow(odysseyCreeper.blockPosition())){
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