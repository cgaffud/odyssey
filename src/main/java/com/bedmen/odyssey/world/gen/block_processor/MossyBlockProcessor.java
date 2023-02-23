package com.bedmen.odyssey.world.gen.block_processor;

import com.bedmen.odyssey.registry.StructureProcessorRegistry;

import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

public class MossyBlockProcessor extends StructureProcessor {

    public static final Codec<MossyBlockProcessor> CODEC = Codec.unit(() -> MossyBlockProcessor.INSTANCE);
    public static final MossyBlockProcessor INSTANCE = new MossyBlockProcessor();

    private static final Block[] COBBLESTONE = {Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE};
    private static final Block[] COBBLESTONE_SLAB = {Blocks.COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB};
    private static final Block[] COBBLESTONE_STAIRS = {Blocks.COBBLESTONE_STAIRS, Blocks.MOSSY_COBBLESTONE_STAIRS};
    private static final Block[] COBBLESTONE_WALL = {Blocks.COBBLESTONE_WALL, Blocks.MOSSY_COBBLESTONE_WALL};
    private static final Block[] STONE_BRICKS = {Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS};
    private static final Block[] STONE_BRICK_SLAB = {Blocks.STONE_BRICK_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB};
    private static final Block[] STONE_BRICK_STAIRS = {Blocks.STONE_BRICK_STAIRS, Blocks.MOSSY_STONE_BRICK_STAIRS};
    private static final Block[] STONE_BRICK_WALL = {Blocks.STONE_BRICK_WALL, Blocks.MOSSY_STONE_BRICK_WALL};

    private static final Map<Block, Block[]> MOSSY_MAP = Map.of(
            Blocks.COBBLESTONE, COBBLESTONE,
            Blocks.COBBLESTONE_SLAB, COBBLESTONE_SLAB,
            Blocks.COBBLESTONE_STAIRS, COBBLESTONE_STAIRS,
            Blocks.COBBLESTONE_WALL, COBBLESTONE_WALL,
            Blocks.STONE_BRICKS, STONE_BRICKS,
            Blocks.STONE_BRICK_SLAB, STONE_BRICK_SLAB,
            Blocks.STONE_BRICK_STAIRS, STONE_BRICK_STAIRS,
            Blocks.STONE_BRICK_WALL, STONE_BRICK_WALL
            );

    public MossyBlockProcessor() {
    }

    @Nullable
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos1, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo1, StructurePlaceSettings structurePlaceSettings) {
        Random random = structurePlaceSettings.getRandom(structureBlockInfo1.pos);
        BlockState blockState = structureBlockInfo1.state;
        BlockPos blockpos = structureBlockInfo1.pos;
        BlockState newBlockState = null;

        Block block = blockState.getBlock();
        if(MOSSY_MAP.containsKey(block)){
            Block[] blockArray = MOSSY_MAP.get(block);
            newBlockState = blockArray[random.nextInt(blockArray.length)].withPropertiesOf(blockState);
        }

        return newBlockState != null ? new StructureTemplate.StructureBlockInfo(blockpos, newBlockState, structureBlockInfo1.nbt) : structureBlockInfo1;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorRegistry.MOSSY.get();
    }
}