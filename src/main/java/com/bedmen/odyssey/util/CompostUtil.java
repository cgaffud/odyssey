package com.bedmen.odyssey.util;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

public class CompostUtil {

    private static void add(float f, ItemLike iItemProvider) {
        ComposterBlock.COMPOSTABLES.put(iItemProvider, f);
    }

    public static void addCompostingRecipes() {
//        add(0.3f, ItemRegistry.AUTUMN_LEAVES_RED.get());
//        add(0.3f, ItemRegistry.AUTUMN_LEAVES_ORANGE.get());
//        add(0.3f, ItemRegistry.AUTUMN_LEAVES_YELLOW.get());
        add(0.3f, ItemRegistry.PALM_LEAVES.get());
    }
}