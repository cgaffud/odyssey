package com.bedmen.odyssey.block.wood;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class PalmTreeGrower extends AbstractTreeGrower {
    private static final ResourceKey<ConfiguredFeature<?, ?>> PALM_TREE_CONFIGURED_FEATURE =
            ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, "palm_tree"));

    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(ServerLevel level, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, RandomSource random, boolean hasFlowers) {
        return level.registryAccess().registry(Registry.CONFIGURED_FEATURE_REGISTRY).get().getHolderOrThrow(PALM_TREE_CONFIGURED_FEATURE);
    }

    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean hasFlowers) {
        return null;
    }
}