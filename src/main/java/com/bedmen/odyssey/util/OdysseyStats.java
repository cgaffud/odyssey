package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;

public class OdysseyStats {

    public static final ResourceLocation INTERACT_WITH_RECYCLING_FURNACE = makeCustomStat("interact_with_recycling_furnace", StatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_STITCHING_TABLE = makeCustomStat("interact_with_stitching_table", StatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_ALLOY_FURNACE = makeCustomStat("interact_with_alloy_furnace", StatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_ODYSSEY_GRINDSTONE = makeCustomStat("interact_with_odyssey_grindstone", StatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_ODYSSEY_ANVIL = makeCustomStat("interact_with_odyssey_anvil", StatFormatter.DEFAULT);

    public static final ResourceLocation INTERACT_WITH_ABSTRACT_CRAFTER = makeCustomStat("interact_with_abstract_crafter", StatFormatter.DEFAULT);

    public static ResourceLocation makeCustomStat(String s, StatFormatter statFormatter) {
        ResourceLocation resourcelocation = new ResourceLocation(Odyssey.MOD_ID, s);
        Registry.register(Registry.CUSTOM_STAT, s, resourcelocation);
        Stats.CUSTOM.get(resourcelocation, statFormatter);
        return resourcelocation;
    }

    private static <T> StatType<T> makeRegistryStatType(String s, Registry<T> registry) {
        return Registry.register(Registry.STAT_TYPE, s, new StatType<>(registry));
    }
}
