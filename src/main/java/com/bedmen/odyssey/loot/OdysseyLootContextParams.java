package com.bedmen.odyssey.loot;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class OdysseyLootContextParams {
    public static final LootContextParam<Boolean> IS_EXPLOSION = create("is_explosion");

    private static <T> LootContextParam<T> create(String s) {
        return new LootContextParam<>(new ResourceLocation(Odyssey.MOD_ID, s));
    }
}
