package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.StructurePieceTypeRegistry;
import com.bedmen.odyssey.util.WorldGenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class CloverStoneSanctuaryPiece extends TemplateStructurePiece {
    private static final ResourceLocation STRUCTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"clover_stone_sanctuary");
    private static final SimpleWeightedRandomList<Block> STONE_BRICKS = new SimpleWeightedRandomList.Builder<Block>()
            .add(Blocks.STONE_BRICKS, 1)
            .add(Blocks.MOSSY_STONE_BRICKS, 2)
            .add(Blocks.CRACKED_STONE_BRICKS, 1).build();
    private static final SimpleWeightedRandomList<Block> STONE_BRICK_STAIRS = new SimpleWeightedRandomList.Builder<Block>()
            .add(Blocks.STONE_BRICK_STAIRS, 1)
            .add(Blocks.MOSSY_STONE_BRICK_STAIRS, 1).build();
    private static final SimpleWeightedRandomList<Block> STONE_BRICK_SLABS = new SimpleWeightedRandomList.Builder<Block>()
            .add(Blocks.STONE_BRICK_SLAB, 1)
            .add(Blocks.MOSSY_STONE_BRICK_SLAB, 1).build();
    private static final SimpleWeightedRandomList<Block> STONE_BRICK_WALLS = new SimpleWeightedRandomList.Builder<Block>()
            .add(Blocks.STONE_BRICK_WALL, 1)
            .add(Blocks.MOSSY_STONE_BRICK_WALL, 1).build();
    private static final SimpleWeightedRandomList<Block> SPECIAL_STONE = new SimpleWeightedRandomList.Builder<Block>()
            .add(Blocks.CHISELED_STONE_BRICKS, 1)
            .add(BlockRegistry.CLOVER_STONE.get(), 1).build();
    private static final Map<Block, SimpleWeightedRandomList<Block>> BLOCK_MAP = Map.of(Blocks.STONE_BRICKS, STONE_BRICKS, Blocks.STONE_BRICK_STAIRS, STONE_BRICK_STAIRS, Blocks.STONE_BRICK_SLAB, STONE_BRICK_SLABS, Blocks.STONE_BRICK_WALL, STONE_BRICK_WALLS, Blocks.CHISELED_STONE_BRICKS, SPECIAL_STONE);
    public CloverStoneSanctuaryPiece(StructureManager structureManager, BlockPos blockPos) {
        super(StructurePieceTypeRegistry.CLOVER_STONE_SANCTUARY.get(), 0, structureManager, STRUCTURE_LOCATION, STRUCTURE_LOCATION.toString(), makeSettings(), blockPos);
    }

    public CloverStoneSanctuaryPiece(StructureManager structureManager, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.CLOVER_STONE_SANCTUARY.get(), compoundTag, structureManager, (p_162451_) -> {
            return makeSettings();
        });
    }

    private static StructurePlaceSettings makeSettings() {
        return (new StructurePlaceSettings()).setRotation(Rotation.NONE).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        super.addAdditionalSaveData(structurePieceSerializationContext, compoundTag);
    }

    protected void handleDataMarker(String s, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, Random random, BoundingBox boundingBox) {

    }

    public void postProcess(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        StructurePlaceSettings structureplacesettings = makeSettings();
        int i = Integer.MIN_VALUE;
        for(int j = 0; j < 2; j++){
            for(int k = 0; k < 2; k++){
                BlockPos blockpos1 = this.templatePosition.offset(StructureTemplate.calculateRelativePosition(structureplacesettings, new BlockPos(10*j, 0, 10*k)));
                i = Integer.max(i, worldGenLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, blockpos1.getX(), blockpos1.getZ()));
            }
        }
        BlockPos blockpos2 = this.templatePosition;
        this.templatePosition = this.templatePosition.offset(0, i - 90 - 1, 0);
        super.postProcess(worldGenLevel, structureFeatureManager, chunkGenerator, random, boundingBox, chunkPos, blockPos);
        for(int x1 = this.boundingBox.minX(); x1 <= this.boundingBox.maxX(); x1++){
            for(int y1 = this.boundingBox.minY(); y1 <= this.boundingBox.maxY(); y1++){
                for(int z1 = this.boundingBox.minZ(); z1 <= this.boundingBox.maxZ(); z1++){
                    BlockPos blockPos1 = new BlockPos(x1, y1, z1);
                    BlockState blockState = worldGenLevel.getBlockState(blockPos1);
                    Block block = blockState.getBlock();
                    if(BLOCK_MAP.containsKey(block)){
                        SimpleWeightedRandomList<Block> simpleWeightedRandomList = BLOCK_MAP.get(block);
                        Block block1 = simpleWeightedRandomList.getRandomValue(random).get();
                        BlockState blockState1 = block1.withPropertiesOf(blockState);
                        worldGenLevel.setBlock(blockPos1, blockState1, 3);
                    }
                }
            }
        }
        BlockPos center = this.templatePosition.offset(5,-1,5);
        for(int j = -1; j <= 1; j += 2){
            for(int k = -1; k <= 1; k += 2){
                BlockPos.MutableBlockPos mutableBlockPos = center.offset(j*5,0,k).mutable();
                while(worldGenLevel.ensureCanWrite(mutableBlockPos) && !WorldGenUtil.isSolid(worldGenLevel, mutableBlockPos)){
                    worldGenLevel.setBlock(mutableBlockPos, STONE_BRICKS.getRandomValue(random).get().defaultBlockState(), 3);
                    mutableBlockPos.move(Direction.DOWN);
                }
                mutableBlockPos = center.offset(j,0,k*5).mutable();
                while(worldGenLevel.ensureCanWrite(mutableBlockPos) && !WorldGenUtil.isSolid(worldGenLevel, mutableBlockPos)){
                    worldGenLevel.setBlock(mutableBlockPos, STONE_BRICKS.getRandomValue(random).get().defaultBlockState(), 3);
                    mutableBlockPos.move(Direction.DOWN);
                }
            }
        }
        this.templatePosition = blockpos2;
    }
}
