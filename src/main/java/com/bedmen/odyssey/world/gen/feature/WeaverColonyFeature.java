package com.bedmen.odyssey.world.gen.feature;

import com.bedmen.odyssey.world.gen.structure.WeaverColonySpherePiece;
import com.mojang.serialization.Codec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class WeaverColonyFeature extends StructureFeature<NoneFeatureConfiguration> {

    public WeaverColonyFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return WeaverColonyFeature.FeatureStart::new;
    }

    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, WorldgenRandom worldgenRandom, ChunkPos chunkPos, Biome biome, ChunkPos chunkPos1, NoneFeatureConfiguration noneFeatureConfiguration, LevelHeightAccessor levelHeightAccessor) {
        if(biome.getBiomeCategory() == Biome.BiomeCategory.OCEAN){
            return false;
        }
        return true;
    }

    public static class FeatureStart extends StructureStart<NoneFeatureConfiguration> {
        public FeatureStart(StructureFeature<NoneFeatureConfiguration> structureFeature, ChunkPos chunkPos, int p_160491_, long p_160492_) {
            super(structureFeature, chunkPos, p_160491_, p_160492_);
        }

        public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, NoneFeatureConfiguration noneFeatureConfiguration, LevelHeightAccessor levelHeightAccessor) {
            double r0 = random.nextDouble()*4+6d;
            int x = chunkPos.getMinBlockX();
            int z = chunkPos.getMinBlockZ();
            int boundingLength = Mth.ceil(2d*r0);
            WeaverColonySpherePiece weaverColonySpherePiece = new WeaverColonySpherePiece(this.random, x, z, r0, boundingLength);
            this.addPiece(weaverColonySpherePiece);

//            double angle = this.random.nextDouble()*2*Math.PI;
//            x += (this.random.nextInt(16)+16)*Math.cos(angle);
//            z += (this.random.nextInt(16)+16)*Math.sin(angle);
//            r0 = random.nextDouble()*4+8d;
//            boundingLength = Mth.ceil(2d*r0);
//            weaverColonySpherePiece = new WeaverColonySpherePiece(this.random, x, z, r0, boundingLength);
//            this.addPiece(weaverColonySpherePiece);
        }
    }
}