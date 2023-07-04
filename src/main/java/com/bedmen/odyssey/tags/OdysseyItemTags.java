package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class OdysseyItemTags {

    public static final TagKey<Item> PALM_LOGS = create("palm_logs");
    public static final TagKey<Item> RAW_COPPERS = create("raw_coppers");
    public static final TagKey<Item> RAW_GOLDS = create("raw_golds");
    public static final TagKey<Item> RAW_SILVERS = create("raw_silvers");
    public static final TagKey<Item> SHIELDS = create("shields");
    public static final TagKey<Item> PARRYABLES = create("parryables");
    public static final TagKey<Item> SILVER_ORES = create("silver_ores");

    public static final TagKey<Item> BOWS = create("bows");

    public static final TagKey<Item> CROSSBOWS = create("crossbows");
    
    private static TagKey<Item> create(String key) {
        return ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation(Odyssey.MOD_ID, key));
    }
}
