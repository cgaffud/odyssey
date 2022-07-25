package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class OdysseyBlockTags {

    public static final TagKey<Block> SILVER_ORES = create("silver_ores");
    public static final TagKey<Block> STERLING_SILVER_TAG = create("needs_sterling_silver_tool");
    public static final TagKey<Block> PALM_LOGS = create("palm_logs");
    public static final TagKey<Block> GREATWOOD_LOGS = create("greatwood_logs");

    private static TagKey<Block> create(String name) {
        return ForgeRegistries.BLOCKS.tags().createTagKey(new ResourceLocation(Odyssey.MOD_ID, name));
    }
}
