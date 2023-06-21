package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.registry.structure.StructureTypeRegistry;
import com.bedmen.odyssey.world.gen.structure.pieces.MoonTowerPiece;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class MoonTowerStructure extends Structure {
    public static final Codec<MoonTowerStructure> CODEC = simpleCodec(MoonTowerStructure::new);

    public MoonTowerStructure(Structure.StructureSettings structureSettings) {
        super(structureSettings);
    }

    public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext generationContext) {
        return onTopOfChunkCenter(generationContext, Heightmap.Types.WORLD_SURFACE_WG, (structurePiecesBuilder) -> {
            this.generatePieces(structurePiecesBuilder, generationContext);
        });
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder, Structure.GenerationContext generationContext) {
        BlockPos blockpos = new BlockPos(generationContext.chunkPos().getMinBlockX(), 200, generationContext.chunkPos().getMinBlockZ());
        Rotation rotation = Rotation.getRandom(generationContext.random());
        MoonTowerPiece moonTowerPiece = new MoonTowerPiece(generationContext.structureTemplateManager(), blockpos, rotation);
        structurePiecesBuilder.addPiece(moonTowerPiece);
    }

    public StructureType<?> type() {
        return StructureTypeRegistry.MOON_TOWER.get();
    }
}
