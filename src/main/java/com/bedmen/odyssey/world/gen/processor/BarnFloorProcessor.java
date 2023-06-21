package com.bedmen.odyssey.world.gen.processor;

import com.bedmen.odyssey.registry.structure.StructureProcessorRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;

public class BarnFloorProcessor extends StructureProcessor {

    public static final Codec<BarnFloorProcessor> CODEC = Codec.unit(() -> BarnFloorProcessor.INSTANCE);
    public static final BarnFloorProcessor INSTANCE = new BarnFloorProcessor();

    private static final Block[] REPLACEMENTS = {Blocks.COARSE_DIRT, Blocks.DIRT_PATH, Blocks.PODZOL};
    private static final float REPLACEMENT_CHANCE = 0.3f;

    public BarnFloorProcessor() {
    }

    @Nullable
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos1, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo1, StructurePlaceSettings structurePlaceSettings) {
        RandomSource randomSource = structurePlaceSettings.getRandom(structureBlockInfo1.pos);
        BlockState blockState = structureBlockInfo1.state;
        BlockPos blockpos = structureBlockInfo1.pos;
        BlockState newBlockState = null;

        if(blockState.is(Blocks.SPRUCE_PLANKS) && randomSource.nextFloat() < REPLACEMENT_CHANCE){
            newBlockState = REPLACEMENTS[randomSource.nextInt(REPLACEMENTS.length)].defaultBlockState();
        }

        return newBlockState != null ? new StructureTemplate.StructureBlockInfo(blockpos, newBlockState, structureBlockInfo1.nbt) : structureBlockInfo1;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorRegistry.BARN_FLOOR.get();
    }

}
