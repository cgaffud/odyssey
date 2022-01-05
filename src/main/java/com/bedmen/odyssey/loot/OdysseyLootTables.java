package com.bedmen.odyssey.loot;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class OdysseyLootTables {
    public static final ResourceLocation WOODEN_TREASURE_CHEST = BuiltInLootTables.register(new ResourceLocation(Odyssey.MOD_ID, "chests/wooden_treasure_chest"));
    public static final ResourceLocation STERLING_SILVER_TREASURE_CHEST = BuiltInLootTables.register(new ResourceLocation(Odyssey.MOD_ID, "chests/sterling_silver_treasure_chest"));
}
