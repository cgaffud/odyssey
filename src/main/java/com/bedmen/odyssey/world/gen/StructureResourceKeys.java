package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

public class StructureResourceKeys {
    public static ResourceKey<ConfiguredStructureFeature<?, ?>> COVEN_HUT = createKey("coven_hut");

    private static ResourceKey<ConfiguredStructureFeature<?, ?>> createKey(String key) {
        return ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, key));
    }
}
