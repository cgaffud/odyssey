package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class OdysseyBlockTags {

    public static final TagKey<Block> SILVER_ORES = BlockTags.create(new ResourceLocation(Odyssey.MOD_ID, "silver_ores"));
    public static final TagKey<Block> STERLING_SILVER_TAG = BlockTags.create(new ResourceLocation(Odyssey.MOD_ID, "needs_sterling_silver_tool"));
    public static final TagKey<Block> PALM_LOGS = BlockTags.create(new ResourceLocation(Odyssey.MOD_ID, "palm_logs"));
    public static final TagKey<Block> GREATWOOD_LOGS = BlockTags.create(new ResourceLocation(Odyssey.MOD_ID, "greatwood_logs"));
}
