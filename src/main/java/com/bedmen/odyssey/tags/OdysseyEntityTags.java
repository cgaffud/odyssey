package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class OdysseyEntityTags {
    public static final TagKey<EntityType<?>> HYDROPHOBIC = create(new ResourceLocation(Odyssey.MOD_ID, "hydrophobic"));

    private static TagKey<EntityType<?>> create(ResourceLocation resourceLocation) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, resourceLocation);
    }
}

