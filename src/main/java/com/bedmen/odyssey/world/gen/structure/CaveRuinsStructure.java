package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.registry.structure.StructureTypeRegistry;
import com.bedmen.odyssey.world.gen.structure.pieces.CaveRuinsPiece;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class CaveRuinsStructure extends Structure{
    public static final Codec<CaveRuinsStructure> CODEC = simpleCodec(CaveRuinsStructure::new);

    public CaveRuinsStructure(Structure.StructureSettings structureSettings) {
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
        Rotation rotation = Rotation.getRandom(worldgenRandom);
        int id = CaveRuinsPiece.ID_LIST.getRandomValue(worldgenRandom).get();
        CaveRuinsPiece caveRuinsPiece = new CaveRuinsPiece(generationContext.structureTemplateManager(), blockpos, rotation, id);
        structurePiecesBuilder.addPiece(caveRuinsPiece);
    }

    public StructureType<?> type() {
        return StructureTypeRegistry.CAVE_RUINS.get();
    }
}
