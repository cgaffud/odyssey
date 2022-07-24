package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.StructurePieceTypeRegistry;
import com.bedmen.odyssey.util.WorldGenUtil;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.*;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UndergroundRuinPieces {
    private static final List<ResourceLocation> HOUSES = Arrays.asList(new ResourceLocation(Odyssey.MOD_ID, "underground_ruins/abandoned_underground_house"));
    static final Map<ResourceLocation, BlockPos> PIVOTS = ImmutableMap.of(HOUSES.get(0), new BlockPos(3, 5, 0));

    public static void addPiece(StructureManager manager, BlockPos blockPos, Rotation rotation, StructurePieceAccessor accessor) {
        ResourceLocation structureLoc = HOUSES.get(0);
    public static void addPiece(StructureManager manager, BlockPos blockPos, Rotation rotation, StructurePieceAccessor accessor, Random random) {
        ResourceLocation structureLoc = HOUSES.get(random.nextInt(HOUSES.size()));
        accessor.addPiece(new UndergroundRuinPiece(manager, structureLoc, blockPos, rotation));
    }

    public static class UndergroundRuinPiece extends TemplateStructurePiece {
        public UndergroundRuinPiece(StructureManager structureManager, ResourceLocation resourceLocation, BlockPos blockPos, Rotation rotation) {
            super(StructurePieceTypeRegistry.UNDERGROUND_RUIN.get(), 0, structureManager, resourceLocation, resourceLocation.toString(), makeSettings(rotation, resourceLocation), blockPos);
        }

        public UndergroundRuinPiece(StructureManager structureManager, CompoundTag tag) {
            super(StructurePieceTypeRegistry.UNDERGROUND_RUIN.get(), tag, structureManager, (resourceLocation) -> {
                return makeSettings(Rotation.valueOf(tag.getString("Rot")), resourceLocation);
            });
        }

        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
            super.addAdditionalSaveData(context, tag);
            tag.putString("Rot", this.placeSettings.getRotation().name());
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation, ResourceLocation resourceLocation) {
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).setRotationPivot(UndergroundRuinPieces.PIVOTS.get(resourceLocation)).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
            ResourceLocation resourceLocation = new ResourceLocation(this.templateName);
            BlockPos opening = getTwoAirBlocks(level, random, chunkPos, resourceLocation);
            this.templatePosition = opening.offset(PIVOTS.get(resourceLocation).multiply(-1));
            super.postProcess(level, manager, chunkGenerator, random, boundingBox, chunkPos, pos);
        }

        /**
         * Searches for an air space two blocks tall on a solid block in a chunk and returns the bottom air space blockpos
         */
        private BlockPos getTwoAirBlocks(WorldGenLevel level, Random random, ChunkPos chunkPos, ResourceLocation resourceLocation) {
            List<Integer> xList = IntStream.rangeClosed(chunkPos.getMinBlockX(), chunkPos.getMaxBlockX()).boxed().collect(Collectors.toList());
            Collections.shuffle(xList, random);
            List<Integer> zList = IntStream.rangeClosed(chunkPos.getMinBlockZ(), chunkPos.getMaxBlockZ()).boxed().collect(Collectors.toList());
            Collections.shuffle(zList, random);
            List<Integer> yList = IntStream.rangeClosed(-28, 28).map(i -> 2*i).boxed().collect(Collectors.toList());
            Collections.shuffle(yList, random);
            List<Rotation> rList = Arrays.stream(Rotation.values()).collect(Collectors.toList());
            Collections.shuffle(rList, random);
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for(Integer x : xList) {
                mutable.setX(x);
                for(Integer z : zList) {
                    mutable.setZ(z);
                    for(Rotation r : rList){
                        this.placeSettings.setRotation(r);
                        this.templatePosition = mutable.offset(PIVOTS.get(resourceLocation).multiply(-1));
                        BoundingBox boundingBox =  this.template.getBoundingBox(this.placeSettings, this.templatePosition);
                        if(d16(boundingBox.minX()) != d16(boundingBox.maxX()) || d16(boundingBox.minZ()) != d16(boundingBox.maxZ())){
                            continue;
                        }
                        for(Integer y : yList){
                            mutable.setY(y);
                            if(WorldGenUtil.isEmpty(level, mutable)){
                                if(WorldGenUtil.isEmpty(level, mutable.above()) && WorldGenUtil.isSolid(level, mutable.below())){
                                    return mutable;
                                } else if(WorldGenUtil.isEmpty(level, mutable.below()) && WorldGenUtil.isSolid(level, mutable.below(2))){
                                    return mutable.below();
                                }
                            }
                        }
                    }
                }
            }
            this.placeSettings.setRotation(Rotation.NONE);
            return new BlockPos(chunkPos.getMinBlockX() + 8, random.nextInt(112) - 56, chunkPos.getMinBlockZ() + 1);
        }

        private int d16(int i){
            return (i < 0 ? i-15 : i) / 16;
        }

        @Override
        protected void handleDataMarker(String dataMarker, BlockPos blockPos, ServerLevelAccessor accessor, Random random, BoundingBox boundingBox) {
            if ("sterling_silver_chest".equals(dataMarker)) {
                accessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                BlockEntity blockentity = accessor.getBlockEntity(blockPos.below());
                if (blockentity instanceof ChestBlockEntity) {
                    ((ChestBlockEntity)blockentity).setLootTable(OdysseyLootTables.STERLING_SILVER_TREASURE_CHEST, random.nextLong());
                }
            }
        }
    }
}
