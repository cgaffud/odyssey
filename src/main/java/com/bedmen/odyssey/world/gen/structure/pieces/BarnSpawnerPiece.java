package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.structure.StructurePieceTypeRegistry;
import com.bedmen.odyssey.world.gen.processor.CobwebProcessor;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class BarnSpawnerPiece extends RotatedStructurePiece {

    protected final int id;
    private static final String ID_TAG = "id";

    public static final Pair<ResourceLocation, BlockPos>[] PIECE_INFOS = new Pair[]{
            Pair.of(new ResourceLocation(Odyssey.MOD_ID,"barn/spawner_hay_0"), new BlockPos(9, 1, 2)),
            Pair.of(new ResourceLocation(Odyssey.MOD_ID,"barn/spawner_hay_1"), new BlockPos(10, 1, 17)),
            Pair.of(new ResourceLocation(Odyssey.MOD_ID,"barn/spawner_hay_2"), new BlockPos(2, 7, 18)),
            Pair.of(new ResourceLocation(Odyssey.MOD_ID,"barn/spawner_column_0"), new BlockPos(4, 5, 7)),
            Pair.of(new ResourceLocation(Odyssey.MOD_ID,"barn/spawner_column_1"), new BlockPos(4, 5, 14)),
            Pair.of(new ResourceLocation(Odyssey.MOD_ID,"barn/spawner_column_2"), new BlockPos(9, 5, 14))
    };

    public BarnSpawnerPiece(StructureTemplateManager structureTemplateManager, BlockPos blockPos, Rotation rotation, int id) {
        super(StructurePieceTypeRegistry.BARN_SPAWNER.get(), 1, structureTemplateManager, PIECE_INFOS[id].getFirst(), PIECE_INFOS[id].getFirst().toString(), makeSettings(), blockPos.offset(PIECE_INFOS[id].getSecond().rotate(rotation)), rotation);
        this.id = id;
    }

    public BarnSpawnerPiece(StructureTemplateManager structureTemplateManager, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.BARN.get(), compoundTag, structureTemplateManager, (resourceLocation) -> makeSettings());
        this.id = compoundTag.getInt(ID_TAG);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putInt(ID_TAG, this.id);
    }

    private static StructurePlaceSettings makeSettings() {
        return (new StructurePlaceSettings()).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(new CobwebProcessor(0.5f));
    }
}
