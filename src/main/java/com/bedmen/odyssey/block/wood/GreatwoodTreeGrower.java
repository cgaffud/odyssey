package com.bedmen.odyssey.block.wood;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class GreatwoodTreeGrower extends AbstractTreeGrower {
    private static final ResourceKey<ConfiguredFeature<?, ?>> GREATWOOD_TREE_CONFIGURED_FEATURE =
            ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, "greatwood_tree"));

    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(ServerLevel level, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, RandomSource random, boolean hasFlowers) {

        return level.registryAccess().registry(Registry.CONFIGURED_FEATURE_REGISTRY).get().getHolderOrThrow(GREATWOOD_TREE_CONFIGURED_FEATURE);
    }

    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean hasFlowers) {
        return null;
    }
}

