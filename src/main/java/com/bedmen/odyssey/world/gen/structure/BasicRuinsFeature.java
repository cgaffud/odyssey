package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.world.gen.structure.pieces.BasicRuinsPiece;
import com.bedmen.odyssey.world.gen.structure.pieces.CovenHutPiece;
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

import java.util.Random;

public class BasicRuinsFeature extends StructureFeature<NoneFeatureConfiguration> {

    public BasicRuinsFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), BasicRuinsFeature::generatePieces));
    }

    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, PieceGenerator.Context<NoneFeatureConfiguration> context) {
        BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), 90, context.chunkPos().getMinBlockZ());
        Random random = context.random();
        Rotation rotation = Rotation.getRandom(random);
        BasicRuinsPiece basicRuinsPiece = new BasicRuinsPiece(context.structureManager(), blockpos, rotation, random.nextInt(BasicRuinsPiece.STRUCTURE_LOCATIONS.length));
        structurePiecesBuilder.addPiece(basicRuinsPiece);
    }
}
