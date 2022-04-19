package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.CamoCreeperModel;
import com.bedmen.odyssey.client.model.OdysseyCreeperModel;
import com.bedmen.odyssey.client.renderer.entity.layer.OdysseyCreeperPowerLayer;
import com.bedmen.odyssey.entity.monster.CamoCreeper;
import com.bedmen.odyssey.entity.monster.OdysseyCreeper;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class CamoCreeperRenderer extends AbstractCreeperRenderer<CamoCreeper, CamoCreeperModel<CamoCreeper>> {
    public static final ResourceLocation GRAY_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/camo_creeper/gray_creeper.png");
    protected static final ResourceLocation DESERT_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/camo_creeper/desert_creeper.png");
    protected static final ResourceLocation SNOW_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/camo_creeper/snow_creeper.png");
    protected static final ResourceLocation STONE_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/camo_creeper/stone_creeper.png");
    protected static final ResourceLocation DEEPSLATE_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/camo_creeper/deepslate_creeper.png");

    public CamoCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, () -> new CamoCreeperModel<>(context.bakeLayer(ModelLayers.CREEPER)));
    }

    public ResourceLocation getTextureLocation(OdysseyCreeper odysseyCreeper) {
        return getTexture(odysseyCreeper);
    }

    public static ResourceLocation getTexture(OdysseyCreeper odysseyCreeper) {
        Biome biome = ((CamoCreeper)odysseyCreeper).getBiome();

        if(biome == null || Objects.equals(biome.getRegistryName(), Biomes.LUSH_CAVES.location())){
            return GRAY_CREEPER_LOCATION;
        }

        if (odysseyCreeper.position().y < 0) {
            return DEEPSLATE_CREEPER_LOCATION;
        }

        if (odysseyCreeper.position().y < 56
                || Objects.equals(biome.getRegistryName(), Biomes.STONY_PEAKS.location())
                || Objects.equals(biome.getRegistryName(), Biomes.STONY_SHORE.location())) {
            return STONE_CREEPER_LOCATION;
        }

        if(biome.getBiomeCategory() == Biome.BiomeCategory.DESERT){
            return DESERT_CREEPER_LOCATION;
        }

        if(biome.coldEnoughToSnow(odysseyCreeper.blockPosition())){
            return SNOW_CREEPER_LOCATION;
        }

        return GRAY_CREEPER_LOCATION;
    }
}
