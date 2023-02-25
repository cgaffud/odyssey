package com.bedmen.odyssey.world.gen.processor;

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

    public static final Codec<MossyBlockProcessor> CODEC = Codec.FLOAT.fieldOf("mossyPercent").xmap(MossyBlockProcessor::new, (mossyBlockProcessor) -> mossyBlockProcessor.mossyPercent).codec();

    private final float mossyPercent;

    public MossyBlockProcessor(float mossyPercent) {
        this.mossyPercent = mossyPercent;
    }

    private static final Map<Block, Block> MOSSY_MAP = Map.of(
            Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE,
            Blocks.COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB,
            Blocks.COBBLESTONE_STAIRS, Blocks.MOSSY_COBBLESTONE_STAIRS,
            Blocks.COBBLESTONE_WALL, Blocks.MOSSY_COBBLESTONE_WALL,
            Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS,
            Blocks.STONE_BRICK_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB,
            Blocks.STONE_BRICK_STAIRS, Blocks.MOSSY_STONE_BRICK_STAIRS,
            Blocks.STONE_BRICK_WALL, Blocks.MOSSY_STONE_BRICK_WALL
            );

    @Nullable
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos1, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo1, StructurePlaceSettings structurePlaceSettings) {
        Random random = structurePlaceSettings.getRandom(structureBlockInfo1.pos);
        BlockState blockState = structureBlockInfo1.state;
        BlockPos blockpos = structureBlockInfo1.pos;
        BlockState newBlockState = null;

        Block block = blockState.getBlock();
        if(MOSSY_MAP.containsKey(block) && random.nextFloat() < this.mossyPercent){
            newBlockState = MOSSY_MAP.get(block).withPropertiesOf(blockState);
        }

        return newBlockState != null ? new StructureTemplate.StructureBlockInfo(blockpos, newBlockState, structureBlockInfo1.nbt) : structureBlockInfo1;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorRegistry.MOSSY.get();
    }
}