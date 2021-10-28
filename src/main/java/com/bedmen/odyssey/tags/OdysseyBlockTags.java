package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

public class OdysseyBlockTags {
    public static final ITag.INamedTag<Block> FOG_TAG = BlockTags.bind(Odyssey.MOD_ID+":fog");
}
