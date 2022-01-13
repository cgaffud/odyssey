package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class OdysseyBlockTags {

    public static final Tag.Named<Block> SILVER_ORES = BlockTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "silver_ores"));
    public static final Tag.Named<Block> STERLING_SILVER_TAG = BlockTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "needs_sterling_silver_tool"));
    public static final Tag.Named<Block> PALM_LOGS = BlockTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "palm_logs"));
    public static final Tag.Named<Block> GREATWOOD_LOGS = BlockTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "greatwood_logs"));
}
