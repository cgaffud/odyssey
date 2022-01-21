package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.TreasureChestBlock;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.world.gen.structure.OdysseyStructurePieceType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.IglooPieces;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UndergroundRuinPieces {
    private static final List<ResourceLocation> HOUSES = Arrays.asList(new ResourceLocation(Odyssey.MOD_ID, "underground_ruins/abandoned_underground_house"));

    public static void addPiece(StructureManager manager, BlockPos blockPos, Rotation rotation, StructurePieceAccessor accessor, Random random) {
        ResourceLocation structureLoc = HOUSES.get(0);
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

//        public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
//            ResourceLocation resourceLocation = new ResourceLocation(this.templateName);
//
//            super.postProcess(level, manager, chunkGenerator, random, boundingBox, chunkPos, pos);
//            if (HOUSES.contains(resourceLocation)) {
//                BlockPos newpos = getHousePosition(level, random, chunkPos, true);
//                // I also tried setting pos to newpos, but that had little effect either
//                if (newpos != null)
//                    this.templatePosition = newpos;
//            }
//        }

        // IGLOO CODE WITH MAPPED VARIABLES
            //            ResourceLocation resourcelocation = new ResourceLocation(this.templateName);
//            StructurePlaceSettings structureplacesettings = makeSettings(this.placeSettings.getRotation());
//            BlockPos blockpos = IglooPieces.OFFSETS.get(resourcelocation);
//            BlockPos blockpos1 = this.templatePosition.offset(StructureTemplate.calculateRelativePosition(structureplacesettings, new BlockPos(3 - blockpos.getX(), 0, -blockpos.getZ())));
//            int i = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, blockpos1.getX(), blockpos1.getZ());
//            BlockPos blockpos2 = this.templatePosition;
//            this.templatePosition = this.templatePosition.offset(0, i - 90 - 1, 0);
//            super.postProcess(level, manager, chunkGenerator, random, boundingBox, chunkPos, pos);
//            if (resourcelocation.equals(IglooPieces.STRUCTURE_LOCATION_IGLOO)) {
//                BlockPos blockpos3 = this.templatePosition.offset(StructureTemplate.calculateRelativePosition(structureplacesettings, new BlockPos(3, 0, 5)));
//                BlockState blockstate = level.getBlockState(blockpos3.below());
//                if (!blockstate.isAir() && !blockstate.is(Blocks.LADDER)) {
//                    level.setBlock(blockpos3, Blocks.SNOW_BLOCK.defaultBlockState(), 3);
//                }
//            }
//
//            this.templatePosition = blockpos2;
//        }

        private BlockPos getHousePosition(WorldGenLevel level, Random random, ChunkPos chunkPos, boolean hasBunker) {
            Rotation rotation = this.placeSettings.getRotation();
            StructurePlaceSettings structureplacesettings = makeSettings(this.placeSettings.getRotation());
//            List<Integer> xList = IntStream.rangeClosed(chunkPos.getMinBlockX(), chunkPos.getMaxBlockX()).boxed().collect(Collectors.toList());
//            Collections.shuffle(xList, random);
//            List<Integer> zList = IntStream.rangeClosed(chunkPos.getMinBlockZ(), chunkPos.getMaxBlockZ()).boxed().collect(Collectors.toList());
//            Collections.shuffle(zList, random);
            List<Integer> yList = IntStream.rangeClosed(-7, 7).boxed().map((i) -> i*8) .collect(Collectors.toList());
            Collections.shuffle(yList, random);
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
//
//            for(Integer x : xList) {
//                for(Integer z : zList) {
                    for(Integer y : yList){
                        mutable.set(chunkPos.getMinBlockX(), y, chunkPos.getMinBlockZ());
                        for(int i = 0; i < 8; i++){
                            mutable.offset(0,-1,0);
                            BlockPos.MutableBlockPos doorChecker = new BlockPos.MutableBlockPos();
                            doorChecker.set(mutable.getX(), mutable.getY(), mutable.getZ());
                            /*I think this is how this is supposed to be used*/
                            doorChecker.offset(StructureTemplate.calculateRelativePosition(structureplacesettings, new BlockPos(0,hasBunker ? 7 : 3, 3)));
                            /*But this is what I intially tried. Both don't seem to work*/
//                            switch(rotation) {
//                                case NONE -> doorChecker.offset(0, hasBunker ? 7 : 3, 3);
//                                case CLOCKWISE_90 -> doorChecker.offset(-3,hasBunker ? 7 : 3,0);
//                                case CLOCKWISE_180 -> doorChecker.offset(0,hasBunker ? 7 : 3,-3);
//                                case COUNTERCLOCKWISE_90 -> doorChecker.offset(3,hasBunker ? 7 : 3,0);
//                            }
                            if (isSolid(level, doorChecker.above()) && isSolid(level, doorChecker) && !isSolid(level,doorChecker.below(2))) {
                               System.out.println("Passed");
                               System.out.println(mutable.toString());
                               return mutable;
                            }

                        }
            //        }
          //      }
            }
            System.out.println("Failed");
            return null;
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

        private boolean isSolid(WorldGenLevel worldgenlevel, BlockPos blockPos){
            return (worldgenlevel.isEmptyBlock(blockPos) || worldgenlevel.getBlockState(blockPos).getCollisionShape(worldgenlevel, blockPos).isEmpty()) && worldgenlevel.getBlockState(blockPos).getFluidState().isEmpty();
        }
    }
}
