package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.util.WorldGenUtil;
import com.bedmen.odyssey.world.gen.structure.OdysseyStructurePieceType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
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
    private static final ResourceLocation STRUCTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"clover_stone_sanctuary/clover_stone_sanctuary");
    private static final List<Block> STONE_BRICKS = List.of(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS);
    private static final List<Block> STONE_BRICK_STAIRS = List.of(Blocks.STONE_BRICK_STAIRS, Blocks.MOSSY_STONE_BRICK_STAIRS);
    private static final List<Block> STONE_BRICK_SLABS = List.of(Blocks.STONE_BRICK_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB);
    private static final List<Block> STONE_BRICK_WALLS = List.of(Blocks.STONE_BRICK_WALL, Blocks.MOSSY_STONE_BRICK_WALL);
    private static final List<Block> SPECIAL_STONE = List.of(Blocks.CHISELED_STONE_BRICKS, BlockRegistry.CLOVER_STONE.get());
    private static final Map<Block, List<Block>> BLOCK_MAP = Map.of(Blocks.STONE_BRICKS, STONE_BRICKS, Blocks.STONE_BRICK_STAIRS, STONE_BRICK_STAIRS, Blocks.STONE_BRICK_SLAB, STONE_BRICK_SLABS, Blocks.STONE_BRICK_WALL, STONE_BRICK_WALLS, Blocks.CHISELED_STONE_BRICKS, SPECIAL_STONE);
    public CloverStoneSanctuaryPiece(StructureManager structureManager, BlockPos blockPos) {
        super(OdysseyStructurePieceType.CLOVER_STONE_SANCTUARY, 0, structureManager, STRUCTURE_LOCATION, STRUCTURE_LOCATION.toString(), makeSettings(), blockPos);
    }

    public CloverStoneSanctuaryPiece(StructureManager structureManager, CompoundTag compoundTag) {
        super(OdysseyStructurePieceType.CLOVER_STONE_SANCTUARY, compoundTag, structureManager, (p_162451_) -> {
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
        for(int x = 0; x < boundingBox.getXSpan(); x++){
            for(int y = 0; y < boundingBox.getYSpan(); y++){
                for(int z = 0; z < boundingBox.getZSpan(); z++){
                    BlockPos blockPos1 = this.templatePosition.offset(x,y,z);
                    BlockState blockState = worldGenLevel.getBlockState(blockPos1);
                    Block block = blockState.getBlock();
                    if(BLOCK_MAP.containsKey(block)){
                        List<Block> list = BLOCK_MAP.get(block);
                        Block block1 = list.get(random.nextInt(list.size()));
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
                    worldGenLevel.setBlock(mutableBlockPos, STONE_BRICKS.get(random.nextInt(STONE_BRICKS.size())).defaultBlockState(), 3);
                    mutableBlockPos.move(Direction.DOWN);
                }
                mutableBlockPos = center.offset(j,0,k*5).mutable();
                while(worldGenLevel.ensureCanWrite(mutableBlockPos) && !WorldGenUtil.isSolid(worldGenLevel, mutableBlockPos)){
                    worldGenLevel.setBlock(mutableBlockPos, STONE_BRICKS.get(random.nextInt(STONE_BRICKS.size())).defaultBlockState(), 3);
                    mutableBlockPos.move(Direction.DOWN);
                }
            }
        }
        this.templatePosition = blockpos2;
    }
}
