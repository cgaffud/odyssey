package com.bedmen.odyssey.block.wood;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.common.util.Lazy;

public class GreatwoodTreeGrower extends AbstractTreeGrower {
    private static final ResourceKey<ConfiguredFeature<?,?>> resourceKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, "greatwood_tree"));
    private static final Lazy<Holder<ConfiguredFeature<?,?>>> holder = Lazy.of(() -> BuiltinRegistries.CONFIGURED_FEATURE.getHolderOrThrow(resourceKey));
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean b) {
        return holder.get();
    }
}

