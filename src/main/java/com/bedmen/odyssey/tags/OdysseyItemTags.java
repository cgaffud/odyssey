package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class OdysseyItemTags {

    public static final TagKey<Item> PALM_LOGS = ItemTags.create(new ResourceLocation(Odyssey.MOD_ID, "palm_logs"));
    public static final TagKey<Item> POWDER_SNOW_WALKABLES = ItemTags.create(new ResourceLocation(Odyssey.MOD_ID, "powder_snow_walkables"));
    public static final TagKey<Item> RAW_COPPERS = ItemTags.create(new ResourceLocation(Odyssey.MOD_ID, "raw_coppers"));
    public static final TagKey<Item> RAW_GOLDS = ItemTags.create(new ResourceLocation(Odyssey.MOD_ID, "raw_golds"));
    public static final TagKey<Item> RAW_SILVERS = ItemTags.create(new ResourceLocation(Odyssey.MOD_ID, "raw_silvers"));
    public static final TagKey<Item> SHIELDS = ItemTags.create(new ResourceLocation(Odyssey.MOD_ID, "shields"));
    public static final TagKey<Item> SILVER_ORES = ItemTags.create(new ResourceLocation(Odyssey.MOD_ID, "silver_ores"));
}
