package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.StructurePieceTypeRegistry;
import com.bedmen.odyssey.util.WorldGenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UndergroundRuinPieces {
    private static final int PLACEMENT_RADIUS = 0;
    private static final List<ResourceLocation> HOUSES = Arrays.asList(new ResourceLocation(Odyssey.MOD_ID, "underground_ruins/abandoned_underground_house"));

    public static void addPiece(StructureManager manager, BlockPos blockPos, Rotation rotation, StructurePieceAccessor accessor) {
        ResourceLocation structureLoc = HOUSES.get(0);
        accessor.addPiece(new UndergroundRuinPiece(manager, structureLoc, blockPos, rotation));
    }

    public static class UndergroundRuinPiece extends TemplateStructurePiece {
        public UndergroundRuinPiece(StructureManager structureManager, ResourceLocation resourceLocation, BlockPos blockPos, Rotation rotation) {
            super(StructurePieceTypeRegistry.UNDERGROUND_RUIN.get(), 0, structureManager, resourceLocation, resourceLocation.toString(), makeSettings(rotation), blockPos);
        }

        public UndergroundRuinPiece(StructureManager structureManager, CompoundTag tag) {
            super(StructurePieceTypeRegistry.UNDERGROUND_RUIN.get(), tag, structureManager, (loc) -> {
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

        public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
            StructurePlaceSettings structureplacesettings = makeSettings(this.placeSettings.getRotation());
            BlockPos blockpos1 = this.templatePosition.offset(StructureTemplate.calculateRelativePosition(structureplacesettings, new BlockPos(0, 0, 3)));
            BlockPos housePosition = getHousePosition(level, random, blockpos1);
            BlockPos blockpos2 = this.templatePosition;
            this.templatePosition = this.templatePosition.offset(housePosition.getX() - blockpos2.getX(), housePosition.getY() - 90 - 5, housePosition.getZ() - blockpos2.getZ());
            super.postProcess(level, manager, chunkGenerator, random, boundingBox, chunkPos, pos);
            this.templatePosition = blockpos2;
        }

        private BlockPos getHousePosition(WorldGenLevel level, Random random, BlockPos blockPos) {
            List<Integer> xList = IntStream.rangeClosed(blockPos.getX()-PLACEMENT_RADIUS, blockPos.getX()+PLACEMENT_RADIUS).boxed().collect(Collectors.toList());
            Collections.shuffle(xList, random);
            List<Integer> zList = IntStream.rangeClosed(blockPos.getZ()-PLACEMENT_RADIUS, blockPos.getZ()+PLACEMENT_RADIUS).boxed().collect(Collectors.toList());
            Collections.shuffle(zList, random);
            List<Integer> yList = IntStream.rangeClosed(-7, 7).boxed().map((i) -> i*8) .collect(Collectors.toList());
            Collections.shuffle(yList, random);
            BlockPos.MutableBlockPos mutable = blockPos.mutable();
//
            for(Integer x : xList) {
                for(Integer z : zList) {
                    for(Integer y : yList){
                        mutable.set(x,y,z);
                        for(int i = 0; i < 8; i++){
                            mutable.move(0,-1,0);
                            if (WorldGenUtil.isEmpty(level, mutable.above()) && WorldGenUtil.isEmpty(level, mutable) && WorldGenUtil.isSolid(level, mutable.below())) {
                               return mutable;
                            }
                        }
                    }
                }
            }
            return blockPos.atY(random.nextInt(96)-48);
        }

        @Override
        protected void handleDataMarker(String dataMarker, BlockPos blockPos, ServerLevelAccessor accessor, Random random, BoundingBox boundingBox) {
            if ("sterling_silver_chest".equals(dataMarker)) {
                accessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                BlockEntity blockentity = accessor.getBlockEntity(blockPos.below());
                if (blockentity instanceof ChestBlockEntity) {
                    ((ChestBlockEntity)blockentity).setLootTable(OdysseyLootTables.STERLING_SILVER_TREASURE_CHEST, random.nextLong());
                    System.out.println("Lootable Added");
                }
            }
        }
    }
}
