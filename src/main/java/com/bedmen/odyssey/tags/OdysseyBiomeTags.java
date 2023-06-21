package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class OdysseyBiomeTags {

    public static final TagKey<Biome> IS_GREEN = create("is_green");

    private static TagKey<Biome> create(String key) {
        return ForgeRegistries.BIOMES.tags().createTagKey(new ResourceLocation(Odyssey.MOD_ID, key));
    }
}
