package com.bedmen.odyssey.util;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

public class CompostUtil {

    private static void add(float f, ItemLike itemLike) {
        if(!ComposterBlock.COMPOSTABLES.containsKey(itemLike)){
            ComposterBlock.COMPOSTABLES.put(itemLike, f);
        }
    }

    public static void addCompostingRecipes() {
        add(0.3f, ItemRegistry.PALM_LEAVES.get());
        add(0.3f, ItemRegistry.PALM_CORNER_LEAVES.get());
        add(0.3f, ItemRegistry.GREATWOOD_LEAVES.get());
        add(0.3f, ItemRegistry.PALM_SAPLING.get());
        add(0.3f, ItemRegistry.GREATWOOD_SAPLING.get());

        add(0.65f, ItemRegistry.PRAIRIE_GRASS.get());
        add(0.65f, ItemRegistry.COCONUT_FLOWER.get());
        add(0.65f, ItemRegistry.HOLLOW_COCONUT.get());

        add(0.85f, ItemRegistry.COCONUT_COOKIE.get());

        add(1f, ItemRegistry.CACTUS_HELMET.get());
        add(1f, ItemRegistry.CACTUS_CHESTPLATE.get());
        add(1f, ItemRegistry.CACTUS_LEGGINGS.get());
        add(1f, ItemRegistry.CACTUS_BOOTS.get());
    }
}