package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.block.TreasureChestBlock;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.world.gen.structure.pieces.UndergroundRuinPieces;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OceanRuinConfiguration;
import net.minecraft.world.level.levelgen.structure.OceanRuinPieces;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UndergroundRuin extends StructureFeature<NoneFeatureConfiguration> {

    public UndergroundRuin(Codec<NoneFeatureConfiguration> codec) {
        super(codec, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), UndergroundRuin::generatePieces));
    }

    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, PieceGenerator.Context<NoneFeatureConfiguration> context) {
        BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), 50, context.chunkPos().getMinBlockZ());
        Rotation rotation = Rotation.getRandom(context.random());
        UndergroundRuinPieces.addPiece(context.structureManager(), blockpos, rotation, structurePiecesBuilder, context.random());
    }




}
