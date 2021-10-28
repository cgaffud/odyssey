package com.bedmen.odyssey.util;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.block.ComposterBlock;
import net.minecraft.util.IItemProvider;

public class CompostUtil {

    private static void add(float f, IItemProvider iItemProvider) {
        ComposterBlock.COMPOSTABLES.put(iItemProvider, f);
    }

    public static void addCompostingRecipes() {
        add(0.3f, ItemRegistry.AUTUMN_LEAVES_RED.get());
        add(0.3f, ItemRegistry.AUTUMN_LEAVES_ORANGE.get());
        add(0.3f, ItemRegistry.AUTUMN_LEAVES_YELLOW.get());
        add(0.3f, ItemRegistry.PALM_LEAVES.get());
    }
}