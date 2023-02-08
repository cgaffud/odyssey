package com.bedmen.odyssey.loot;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.loot.functions.ApplyChanceDecayFunction;
import com.bedmen.odyssey.loot.functions.ModifyWithTierFunction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class OdysseyLootItemFunctions {
    public static LootItemFunctionType MODIFY_WITH_TIER;
    public static LootItemFunctionType CHANCE_DECAY;

    public static void registerFunctions() {
        MODIFY_WITH_TIER = OdysseyLootItemFunctions.register("modify_with_tier", new ModifyWithTierFunction.Serializer());
        CHANCE_DECAY = OdysseyLootItemFunctions.register("chance_decay", new ApplyChanceDecayFunction.Serializer());
    }

    public static LootItemFunctionType register(String name, Serializer<? extends LootItemFunction> serializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(Odyssey.MOD_ID, name), new LootItemFunctionType(serializer));
    }
}