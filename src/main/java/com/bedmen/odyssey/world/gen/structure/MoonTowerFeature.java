package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.world.gen.structure.pieces.MoonTowerPiece;
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

public class MoonTowerFeature extends StructureFeature<NoneFeatureConfiguration> {
    public MoonTowerFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), MoonTowerFeature::generatePieces));
    }

    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

//    private static boolean checkLocation(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {
//
//        int minX = context.chunkPos().getBlockX(5);
//        int minZ = context.chunkPos().getBlockZ(5);
//        int[] corners = context.getCornerHeights(minX, minX+6, minZ, minZ+6);
//        int minCorner = Math.min(Math.min(corners[0], corners[1]), Math.min(corners[2], corners[3]));
//
//        return context.validBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG) && (minCorner > 90);
//    }


    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, PieceGenerator.Context<NoneFeatureConfiguration> context) {
        BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), 200, context.chunkPos().getMinBlockZ());
        Rotation rotation = Rotation.getRandom(context.random());
        MoonTowerPiece moonTowerPiece = new MoonTowerPiece(context.structureManager(), blockpos, rotation);
        structurePiecesBuilder.addPiece(moonTowerPiece);
    }
}
