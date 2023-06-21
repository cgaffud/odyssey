package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class OdysseyEntityTags {
    public static final TagKey<EntityType<?>> HYDROPHOBIC = create("hydrophobic");
    public static final TagKey<EntityType<?>> OVERHEATING_IMMUNE = create("overheating_immune");

    private static TagKey<EntityType<?>> create(String key) {
        return ForgeRegistries.ENTITY_TYPES.tags().createTagKey(new ResourceLocation(Odyssey.MOD_ID, key));
    }
}

