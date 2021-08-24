package com.bedmen.odyssey.world.gen.carver;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

public class FogCarver extends WorldCarver<ProbabilityConfig> {

    public FogCarver(Codec<ProbabilityConfig> codec) {
        super(codec, 256);
    }

    public boolean isStartChunk(Random random, int i1, int i2, ProbabilityConfig pConfig) {
        return random.nextFloat() <= pConfig.probability ;
    }

    public boolean carve(IChunk chunk, Function<BlockPos, Biome> getBiome, Random random, int sealvl, int x, int z, int chunkX, int chunkZ, BitSet bitSet, ProbabilityConfig pConfig) {
        return false;
    }



    @Override
    protected boolean skip(double p_222708_1_, double p_222708_3_, double p_222708_5_, int p_222708_7_) {
        return false;
    }
}
