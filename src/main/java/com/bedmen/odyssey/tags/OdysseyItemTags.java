package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class OdysseyItemTags {

    public static final Tag.Named<Block> SILVER_ORES = BlockTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "silver_ores"));
    public static final Tag.Named<Block> RAW_SILVERS = BlockTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "raw_silvers"));

}
