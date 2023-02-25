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

public class CrackedBlockProcessor extends StructureProcessor {

    public static final Codec<CrackedBlockProcessor> CODEC = Codec.FLOAT.fieldOf("crackPercent").xmap(CrackedBlockProcessor::new, (mossyBlockProcessor) -> mossyBlockProcessor.crackPercent).codec();

    private final float crackPercent;

    public CrackedBlockProcessor(float crackPercent) {
        this.crackPercent = crackPercent;
    }

    private static final Map<Block, Block> CRACKED_MAP = Map.of(
            Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS,
            Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS,
            Blocks.DEEPSLATE_TILES, Blocks.CRACKED_DEEPSLATE_TILES,
            Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS,
            Blocks.NETHER_BRICKS, Blocks.CRACKED_NETHER_BRICKS
    );

    @Nullable
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos1, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo1, StructurePlaceSettings structurePlaceSettings) {
        Random random = structurePlaceSettings.getRandom(structureBlockInfo1.pos);
        BlockState blockState = structureBlockInfo1.state;
        BlockPos blockpos = structureBlockInfo1.pos;
        BlockState newBlockState = null;

        Block block = blockState.getBlock();
        if(CRACKED_MAP.containsKey(block) && random.nextFloat() < this.crackPercent){
            newBlockState = CRACKED_MAP.get(block).withPropertiesOf(blockState);
        }

        return newBlockState != null ? new StructureTemplate.StructureBlockInfo(blockpos, newBlockState, structureBlockInfo1.nbt) : structureBlockInfo1;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorRegistry.MOSSY.get();
    }
}