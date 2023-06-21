package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

public class StructureResourceKeys {
    public static ResourceKey<Structure> COVEN_HUT = createKey("coven_hut");

    private static ResourceKey<Structure> createKey(String key) {
        return ResourceKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, key));
    }
}
