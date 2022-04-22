package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.world.gen.structure.pieces.UndergroundRuinPieces;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class UndergroundRuin extends StructureFeature<NoneFeatureConfiguration> {

    public UndergroundRuin(Codec<NoneFeatureConfiguration> codec) {
        super(codec, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), UndergroundRuin::generatePieces));
    }

    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_DECORATION;
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, PieceGenerator.Context<NoneFeatureConfiguration> context) {
        BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), 0, context.chunkPos().getMinBlockZ());
        Rotation rotation = Rotation.NONE;
        UndergroundRuinPieces.addPiece(context.structureManager(), blockpos, rotation, structurePiecesBuilder, context.random());
    }




}
