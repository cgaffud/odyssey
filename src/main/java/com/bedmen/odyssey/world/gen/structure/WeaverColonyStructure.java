package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.registry.structure.StructureTypeRegistry;
import com.bedmen.odyssey.world.gen.structure.pieces.WeaverColonySpherePiece;
import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class WeaverColonyStructure extends Structure {
    public static final Codec<WeaverColonyStructure> CODEC = simpleCodec(WeaverColonyStructure::new);

    public WeaverColonyStructure(Structure.StructureSettings structureSettings) {
        super(structureSettings);
    }

    public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext generationContext) {
        return onTopOfChunkCenter(generationContext, Heightmap.Types.WORLD_SURFACE_WG, (structurePiecesBuilder) -> {
            this.generatePieces(structurePiecesBuilder, generationContext);
        });
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder, Structure.GenerationContext generationContext) {
        WorldgenRandom worldgenRandom = generationContext.random();
        ChunkPos chunkPos = generationContext.chunkPos();
        double radius = worldgenRandom.nextDouble() * 4.0d + 6.0d;
        int x = chunkPos.getMinBlockX();
        int y = -32 + worldgenRandom.nextInt(64);
        int z = chunkPos.getMinBlockZ();
        int width = Mth.ceil(radius) * 2 + 1;
        WeaverColonySpherePiece weaverColonySpherePiece = new WeaverColonySpherePiece(x, y, z, radius, width);
        structurePiecesBuilder.addPiece(weaverColonySpherePiece);
    }

    public StructureType<?> type() {
        return StructureTypeRegistry.WEAVER_COLONY.get();
    }
}