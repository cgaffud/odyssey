package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.util.BlockRegistry;
import com.bedmen.odyssey.util.FogUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class OdysseyBlockTags {

    public static Set<Block> FOG_TAG;
    public static Set<Block> AUTUMN_TAG;

    public static void init(){
        FOG_TAG = new HashSet<>();
        for(int i = 1; i <= 8; i++)
            FOG_TAG.add(FogUtil.intToFog(i));

        AUTUMN_TAG = new HashSet<>();
        AUTUMN_TAG.add(BlockRegistry.AUTUMN_LEAVES_RED.get());
        AUTUMN_TAG.add(BlockRegistry.AUTUMN_LEAVES_ORANGE.get());
        AUTUMN_TAG.add(BlockRegistry.AUTUMN_LEAVES_YELLOW.get());
        AUTUMN_TAG.add(Blocks.OAK_LEAVES);
        AUTUMN_TAG.add(Blocks.BIRCH_LEAVES);
        AUTUMN_TAG.add(Blocks.SPRUCE_LEAVES);
        AUTUMN_TAG.add(Blocks.DARK_OAK_LEAVES);
        AUTUMN_TAG.add(Blocks.ACACIA_LEAVES);
        AUTUMN_TAG.add(Blocks.JUNGLE_LEAVES);
        AUTUMN_TAG.add(Blocks.OAK_LOG);
        AUTUMN_TAG.add(Blocks.BIRCH_LOG);
        AUTUMN_TAG.add(Blocks.SPRUCE_LOG);
        AUTUMN_TAG.add(Blocks.ACACIA_LOG);
        AUTUMN_TAG.add(Blocks.JUNGLE_LOG);
        AUTUMN_TAG.add(Blocks.DARK_OAK_LOG);
        AUTUMN_TAG.add(Blocks.AIR);
    }
}
