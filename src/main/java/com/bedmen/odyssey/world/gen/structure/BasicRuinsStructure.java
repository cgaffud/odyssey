package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.registry.structure.StructureTypeRegistry;
import com.bedmen.odyssey.world.gen.structure.pieces.BasicRuinsPiece;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class BasicRuinsStructure extends Structure {
    public static final Codec<BasicRuinsStructure> CODEC = simpleCodec(BasicRuinsStructure::new);

    public BasicRuinsStructure(Structure.StructureSettings structureSettings) {
        super(structureSettings);
    }

    public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext generationContext) {
        return onTopOfChunkCenter(generationContext, Heightmap.Types.WORLD_SURFACE_WG, (structurePiecesBuilder) -> {
            this.generatePieces(structurePiecesBuilder, generationContext);
        });
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder, Structure.GenerationContext generationContext) {
        BlockPos blockpos = new BlockPos(generationContext.chunkPos().getMinBlockX(), 90, generationContext.chunkPos().getMinBlockZ());
        WorldgenRandom worldgenRandom = generationContext.random();
        Rotation rotation = Rotation.getRandom(worldgenRandom);
        BasicRuinsPiece basicRuinsPiece = new BasicRuinsPiece(generationContext.structureTemplateManager(), blockpos, rotation, worldgenRandom.nextInt(BasicRuinsPiece.STRUCTURE_LOCATIONS.length));
        structurePiecesBuilder.addPiece(basicRuinsPiece);
    }

    public StructureType<?> type() {
        return StructureTypeRegistry.BASIC_RUINS.get();
    }
}
