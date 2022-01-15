package com.bedmen.odyssey.loot;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class OdysseyLootItemFunctions {
    public static LootItemFunctionType ENCHANT_WITH_TIER;

    public static LootItemFunctionType register(String name, Serializer<? extends LootItemFunction> serializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(Odyssey.MOD_ID, name), new LootItemFunctionType(serializer));
    }
}