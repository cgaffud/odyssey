package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.registry.structure.StructureTypeRegistry;
import com.bedmen.odyssey.world.gen.structure.pieces.CaveBlacksmithPiece;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class CaveBlacksmithStructure extends Structure {
    public static final Codec<CaveBlacksmithStructure> CODEC = simpleCodec(CaveBlacksmithStructure::new);

    public CaveBlacksmithStructure(StructureSettings structureSettings) {
        super(structureSettings);
    }

    public Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext) {
        return onTopOfChunkCenter(generationContext, Heightmap.Types.WORLD_SURFACE_WG, (structurePiecesBuilder) -> {
            this.generatePieces(structurePiecesBuilder, generationContext);
        });
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder, GenerationContext generationContext) {
        BlockPos blockpos = new BlockPos(generationContext.chunkPos().getMinBlockX(), 90, generationContext.chunkPos().getMinBlockZ());
        WorldgenRandom worldgenRandom = generationContext.random();
        Rotation rotation = Rotation.getRandom(worldgenRandom);
        CaveBlacksmithPiece caveBlacksmithPiece = new CaveBlacksmithPiece(generationContext.structureTemplateManager(), blockpos, rotation, CaveBlacksmithPiece.STRUCTURE_LOCATIONS.getRandomValue(worldgenRandom).get());
        structurePiecesBuilder.addPiece(caveBlacksmithPiece);
    }

    public StructureType<?> type() {
        return StructureTypeRegistry.CAVE_BLACKSMITH.get();
    }
}
