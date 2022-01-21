package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.structure.OdysseyStructurePieceType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.Random;

public class UndergroundRuinPieces {
    private static final ResourceLocation[] HOUSES = new ResourceLocation[]{new ResourceLocation(Odyssey.MOD_ID,"underground_ruins/abandoned_underground_house")};

    public static void addPiece(StructureManager manager, BlockPos blockPos, Rotation rotation, StructurePieceAccessor accessor, Random random) {
        ResourceLocation structureLoc = HOUSES[0];
        accessor.addPiece(new UndergroundRuinPiece(manager, structureLoc, blockPos, rotation));
    }

    public static class UndergroundRuinPiece extends TemplateStructurePiece {
        public UndergroundRuinPiece(StructureManager structureManager, ResourceLocation resourceLocation, BlockPos blockPos, Rotation rotation) {
            super(OdysseyStructurePieceType.UNDERGROUND_RUIN, 0, structureManager, resourceLocation, resourceLocation.toString(), makeSettings(rotation), blockPos);
        }

        public UndergroundRuinPiece(StructureManager structureManager, CompoundTag tag) {
            super(OdysseyStructurePieceType.UNDERGROUND_RUIN, tag, structureManager, (loc) -> {
                return makeSettings(Rotation.valueOf(tag.getString("Rot")));
            });
        }

        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
            super.addAdditionalSaveData(context, tag);
            tag.putString("Rot", this.placeSettings.getRotation().name());
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation) {
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE);
        }

        @Override
        protected void handleDataMarker(String dataMarker, BlockPos blockPos, ServerLevelAccessor accessor, Random random, BoundingBox boundingBox) {
        }
//            if ("sterling_silver_chest".equals(dataMarker)) {
//                accessor.setBlock(blockPos, BlockRegistry.STERLING_SILVER_CHEST.get().defaultBlockState(), 2);
//                BlockEntity blockentity = accessor.getBlockEntity(blockPos);
//                if (blockentity instanceof ChestBlockEntity) {
//                    ((ChestBlockEntity) blockentity).setLootTable(BuiltInLootTables.UNDERWATER_RUIN_SMALL, random.nextLong());
//                }
//            }
//        }
    }
}
