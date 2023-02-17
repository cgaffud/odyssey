package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

public class OdysseyConfiguredStructureTags {

    public static TagKey<ConfiguredStructureFeature<?, ?>> ON_COVEN_HUT_MAPS = create("on_coven_hut_maps");

    private static TagKey<ConfiguredStructureFeature<?, ?>> create(String key) {
        return TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, key));
    }
}
