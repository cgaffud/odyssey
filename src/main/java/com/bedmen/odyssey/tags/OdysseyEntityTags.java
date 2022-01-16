package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;

public class OdysseyEntityTags {
    public static final Tag.Named<EntityType<?>> HYDROPHOBIC = EntityTypeTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "hydrophobic"));
}

