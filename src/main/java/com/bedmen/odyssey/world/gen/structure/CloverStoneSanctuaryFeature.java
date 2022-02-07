package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.world.gen.structure.pieces.CloverStoneSanctuaryPiece;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class CloverStoneSanctuaryFeature extends StructureFeature<NoneFeatureConfiguration> {

    public CloverStoneSanctuaryFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), CloverStoneSanctuaryFeature::generatePieces));
    }

    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, PieceGenerator.Context<NoneFeatureConfiguration> context) {
        BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), 90, context.chunkPos().getMinBlockZ());
        CloverStoneSanctuaryPiece cloverStoneSanctuaryPiece = new CloverStoneSanctuaryPiece(context.structureManager(), blockpos);
        structurePiecesBuilder.addPiece(cloverStoneSanctuaryPiece);
    }
}
