package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.registry.structure.StructureTypeRegistry;
import com.bedmen.odyssey.world.gen.structure.pieces.CovenHutPiece;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class CovenHutStructure extends Structure {
    public static final int INITIAL_HEIGHT = 90;
    public static final Codec<CovenHutStructure> CODEC = simpleCodec(CovenHutStructure::new);

    public CovenHutStructure(Structure.StructureSettings structureSettings) {
        super(structureSettings);
    }

    public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext generationContext) {
        return onTopOfChunkCenter(generationContext, Heightmap.Types.WORLD_SURFACE_WG, (structurePiecesBuilder) -> {
            this.generatePieces(structurePiecesBuilder, generationContext);
        });
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder, Structure.GenerationContext generationContext) {
        BlockPos blockpos = new BlockPos(generationContext.chunkPos().getMinBlockX(), INITIAL_HEIGHT, generationContext.chunkPos().getMinBlockZ());
        Rotation rotation = Rotation.getRandom(generationContext.random());
        CovenHutPiece covenHutPiece = new CovenHutPiece(generationContext.structureTemplateManager(), blockpos, rotation);
        structurePiecesBuilder.addPiece(covenHutPiece);
    }

    public StructureType<?> type() {
        return StructureTypeRegistry.COVEN_HUT.get();
    }
}
