package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class OdysseyEntityTags {
    public static final TagKey<EntityType<?>> HYDROPHOBIC = create("hydrophobic");

    private static TagKey<EntityType<?>> create(String name) {
        return ForgeRegistries.ENTITIES.tags().createTagKey(new ResourceLocation(Odyssey.MOD_ID, name));
    }
}

