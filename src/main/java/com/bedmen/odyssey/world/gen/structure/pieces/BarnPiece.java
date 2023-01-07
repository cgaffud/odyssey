package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.StructurePieceTypeRegistry;
import com.bedmen.odyssey.util.WorldGenUtil;
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
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.List;
import java.util.Random;

public class BarnPiece extends TemplateStructurePiece {
    private static final ResourceLocation STRUCTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"barn");
    public BarnPiece(StructureManager structureManager, BlockPos blockPos, Rotation rotation) {
        super(StructurePieceTypeRegistry.BARN.get(), 0, structureManager, STRUCTURE_LOCATION, STRUCTURE_LOCATION.toString(), makeSettings(rotation), blockPos);
    }

    public BarnPiece(StructureManager structureManager, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.BARN.get(), compoundTag, structureManager, (resourceLocation) -> {
            return makeSettings(Rotation.valueOf(compoundTag.getString("Rot")));
        });
    }

    private static StructurePlaceSettings makeSettings(Rotation rotation) {
        return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putString("Rot", this.placeSettings.getRotation().name());
    }

    @Override
    protected void handleDataMarker(String dataMarker, BlockPos blockPos, ServerLevelAccessor accessor, Random random, BoundingBox boundingBox) {
        if ("sterling_silver_chest".equals(dataMarker)) {
            accessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
            BlockEntity blockentity = accessor.getBlockEntity(blockPos.below());
            if (blockentity instanceof ChestBlockEntity) {
                ((ChestBlockEntity)blockentity).setLootTable(OdysseyLootTables.BARN_CHEST, random.nextLong());
            }
        }
    }

    public void postProcess(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        BlockPos center = this.boundingBox.getCenter();
        BlockPos offset = switch (this.placeSettings.getRotation()) {
            case NONE -> new BlockPos(0, 0, -12);
            case CLOCKWISE_90 -> new BlockPos(11, 0, 0);
            case CLOCKWISE_180 -> new BlockPos(0, 0, 11);
            case COUNTERCLOCKWISE_90 -> new BlockPos(-12, 0, 0);
        };
        BlockPos opening = center.offset(offset);
        int i = worldGenLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, opening.getX(), opening.getZ());
        BlockPos blockpos2 = this.templatePosition;
        this.templatePosition = this.templatePosition.offset(0, i - 90 - 1, 0);
        super.postProcess(worldGenLevel, structureFeatureManager, chunkGenerator, random, boundingBox, chunkPos, blockPos);
        int y = this.boundingBox.minY()-1;
        List<BlockPos.MutableBlockPos> corners = List.of(
                new BlockPos.MutableBlockPos(this.boundingBox.minX()+1, y, this.boundingBox.minZ()+1),
                new BlockPos.MutableBlockPos(this.boundingBox.minX()+1, y, this.boundingBox.maxZ()-1),
                new BlockPos.MutableBlockPos(this.boundingBox.maxX()-1, y, this.boundingBox.minZ()+1),
                new BlockPos.MutableBlockPos(this.boundingBox.maxX()-1, y, this.boundingBox.maxZ()-1));
        for(BlockPos.MutableBlockPos corner : corners){
            while(worldGenLevel.ensureCanWrite(corner) && !WorldGenUtil.isSolid(worldGenLevel, corner)){
                worldGenLevel.setBlock(corner, Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState(), 3);
                corner.move(Direction.DOWN);
            }
        }
        this.templatePosition = blockpos2;
    }
}
