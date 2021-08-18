package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.util.FogUtil;
import net.minecraft.block.Block;

import java.util.HashSet;
import java.util.Set;

public class OdysseyBlockTags {

    public static Set<Block> FOG_TAG;

    public static void init(){
        Set<Block> set = new HashSet<>();
        for(int i = 1; i <= 8; i++)
            set.add(FogUtil.intToFog(i));
        FOG_TAG = set;
    }
}
