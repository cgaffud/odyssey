package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.world.gen.structure.pieces.WeaverColonySpherePiece;
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
        double radius = random.nextDouble() * 4.0d + 6.0d;
        int x = chunkPos.getMinBlockX();
        int y = -32 + random.nextInt(64);
        int z = chunkPos.getMinBlockZ();
        int width = Mth.ceil(radius) * 2 + 1;
        WeaverColonySpherePiece weaverColonySpherePiece = new WeaverColonySpherePiece(x, y, z, radius, width);
        structurePiecesBuilder.addPiece(weaverColonySpherePiece);
    }
}