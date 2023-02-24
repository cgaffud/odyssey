package com.bedmen.odyssey.loot;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class OdysseyLootTables {

    public static final ResourceLocation COPPER_TREASURE_CHEST = BuiltInLootTables.register(new ResourceLocation(Odyssey.MOD_ID, "chests/copper_treasure"));
    public static final ResourceLocation STERLING_SILVER_TREASURE_CHEST = BuiltInLootTables.register(new ResourceLocation(Odyssey.MOD_ID, "chests/sterling_silver_treasure"));
    public static final ResourceLocation HIDDEN_JUNGLE_TEMPLE_CHEST = BuiltInLootTables.register(new ResourceLocation(Odyssey.MOD_ID, "chests/hidden_jungle_temple"));
    public static final ResourceLocation MOON_TOWER_CHEST = BuiltInLootTables.register(new ResourceLocation(Odyssey.MOD_ID, "chests/moon_tower"));

    // Barn
    public static final ResourceLocation BARN_BARREL = BuiltInLootTables.register(new ResourceLocation(Odyssey.MOD_ID, "chests/barn/barrel"));
    public static final ResourceLocation BARN_TREASURE = BuiltInLootTables.register(new ResourceLocation(Odyssey.MOD_ID, "chests/barn/treasure"));

}
