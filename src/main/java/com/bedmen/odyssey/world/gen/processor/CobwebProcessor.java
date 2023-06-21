package com.bedmen.odyssey.world.gen.processor;

import com.bedmen.odyssey.registry.structure.StructureProcessorRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;

public class CobwebProcessor extends StructureProcessor {

    public static final Codec<CobwebProcessor> CODEC = Codec.FLOAT.fieldOf("cobwebPercent").xmap(CobwebProcessor::new, (cobwebProcessor) -> cobwebProcessor.cobwebPercent).codec();

    private final float cobwebPercent;

    public CobwebProcessor(float cobwebPercent) {
        this.cobwebPercent = cobwebPercent;
    }

    @Nullable
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos1, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo1, StructurePlaceSettings structurePlaceSettings) {
        RandomSource randomSource = structurePlaceSettings.getRandom(structureBlockInfo1.pos);
        BlockState blockState = structureBlockInfo1.state;
        BlockPos blockpos = structureBlockInfo1.pos;
        BlockState newBlockState = null;

        if(blockState.is(Blocks.COBWEB) && randomSource.nextFloat() >= this.cobwebPercent){
            newBlockState = Blocks.AIR.defaultBlockState();
        }

        return newBlockState != null ? new StructureTemplate.StructureBlockInfo(blockpos, newBlockState, structureBlockInfo1.nbt) : structureBlockInfo1;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorRegistry.COBWEB.get();
    }

}
