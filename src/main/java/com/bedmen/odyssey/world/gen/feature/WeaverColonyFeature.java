package com.bedmen.odyssey.world.gen.feature;

import com.bedmen.odyssey.world.gen.structure.WeaverColonySpherePiece;
import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Random;

public class WeaverColonyFeature extends StructureFeature<NoneFeatureConfiguration> {

    public WeaverColonyFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), WeaverColonyFeature::generatePieces));
    }

    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, PieceGenerator.Context<NoneFeatureConfiguration> context) {
        Random random = context.random();
        ChunkPos chunkPos = context.chunkPos();
        double r0 = random.nextDouble()*4+6d;
        int x = chunkPos.getMinBlockX();
        int z = chunkPos.getMinBlockZ();
        int boundingLength = Mth.ceil(2d*r0);
        WeaverColonySpherePiece weaverColonySpherePiece = new WeaverColonySpherePiece(random, x, z, r0, boundingLength);
        structurePiecesBuilder.addPiece(weaverColonySpherePiece);
    }
}