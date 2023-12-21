package com.bedmen.odyssey.tags;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class OdysseyStructureTags {
    public static final TagKey<Structure> ON_COVEN_HUT_MAPS = create("on_coven_hut_maps");
    public static final TagKey<Structure> ON_BANDIT_HIDEOUT_MAPS = create("on_bandit_hideout_maps");

    private static TagKey<Structure> create(String key) {
        return TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, key));
    }
}
