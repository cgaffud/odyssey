package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.registry.structure.StructureTypeRegistry;
import com.bedmen.odyssey.world.gen.structure.pieces.BarnPiece;
import com.bedmen.odyssey.world.gen.structure.pieces.BasicRuinsPiece;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class BarnStructure extends Structure {
    public static final Codec<BarnStructure> CODEC = simpleCodec(BarnStructure::new);

    public BarnStructure(Structure.StructureSettings structureSettings) {
        super(structureSettings);
    }

    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext generationContext) {
        return onTopOfChunkCenter(generationContext, Heightmap.Types.WORLD_SURFACE_WG, (structurePiecesBuilder) -> {
            this.generatePieces(structurePiecesBuilder, generationContext);
        });
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder, Structure.GenerationContext generationContext) {
        BlockPos blockpos = new BlockPos(generationContext.chunkPos().getMinBlockX(), 90, generationContext.chunkPos().getMinBlockZ());
        WorldgenRandom worldgenRandom = generationContext.random();
        Rotation rotation = Rotation.getRandom(generationContext.random());
        BarnPiece barnPiece = new BarnPiece(generationContext.structureTemplateManager(), blockpos, rotation, worldgenRandom);
        structurePiecesBuilder.addPiece(barnPiece);
    }

    public StructureType<?> type() {
        return StructureTypeRegistry.BARN.get();
    }
}
