package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class OdysseyItemTags {

    public static final Tag.Named<Item> PALM_LOGS = ItemTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "palm_logs"));
    public static final Tag.Named<Item> POWDER_SNOW_WALKABLES = ItemTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "powder_snow_walkables"));
    public static final Tag.Named<Item> RAW_COPPERS = ItemTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "raw_coppers"));
    public static final Tag.Named<Item> RAW_GOLDS = ItemTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "raw_golds"));
    public static final Tag.Named<Item> RAW_SILVERS = ItemTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "raw_silvers"));
    public static final Tag.Named<Item> SHIELDS = ItemTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "shields"));
    public static final Tag.Named<Item> SILVER_ORES = ItemTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "silver_ores"));
}
