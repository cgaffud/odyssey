package com.bedmen.odyssey.world.gen.biome;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class BiomeResourceKeys {

    public static final ResourceKey<Biome> TROPICS_RESOURCE_KEY = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, "tropics"));
    public static final ResourceKey<Biome> PRAIRIE_RESOURCE_KEY = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, "prairie"));
    public static final ResourceKey<Biome> ARCTIC_RESOURCE_KEY = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, "arctic"));

}