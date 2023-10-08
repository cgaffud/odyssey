package com.bedmen.odyssey.world.gen.structure;

import com.bedmen.odyssey.registry.structure.StructureTypeRegistry;
import com.bedmen.odyssey.world.gen.structure.pieces.DungeonlessMoonTowerPiece;
import com.bedmen.odyssey.world.gen.structure.pieces.MoonTowerPiece;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class MoonTowerRuinsStructure extends Structure {
    public static final Codec<MoonTowerRuinsStructure> CODEC = simpleCodec(MoonTowerRuinsStructure::new);

    public MoonTowerRuinsStructure(StructureSettings structureSettings) {
        super(structureSettings);
    }

    public Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext) {
        return onTopOfChunkCenter(generationContext, Heightmap.Types.WORLD_SURFACE_WG, (structurePiecesBuilder) -> {
            this.generatePieces(structurePiecesBuilder, generationContext);
        });
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder, GenerationContext generationContext) {
        BlockPos blockpos = new BlockPos(generationContext.chunkPos().getMinBlockX(), 200, generationContext.chunkPos().getMinBlockZ());
        Rotation rotation = Rotation.getRandom(generationContext.random());
        RandomSource worldgenRandom = generationContext.random();
        DungeonlessMoonTowerPiece moonTowerPiece = new DungeonlessMoonTowerPiece(generationContext.structureTemplateManager(), DungeonlessMoonTowerPiece.RUINS_LOCATIONS.getRandomValue(worldgenRandom).get(), blockpos, rotation);
        structurePiecesBuilder.addPiece(moonTowerPiece);
    }

    public StructureType<?> type() {
        return StructureTypeRegistry.MOON_TOWER_RUINS.get();
    }
}
