package com.bedmen.odyssey.world.gen.processor;

import com.bedmen.odyssey.registry.structure.StructureProcessorRegistry;
import com.bedmen.odyssey.tags.OdysseyBiomeTags;
import com.bedmen.odyssey.world.BiomeUtil;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Optional;

public class VineProcessor extends StructureProcessor {

    public static final Codec<VineProcessor> CODEC = Codec.FLOAT.fieldOf("vinePercent").xmap(VineProcessor::new, (vineProcessor) -> vineProcessor.vinePercent).codec();

    private final float vinePercent;

    public VineProcessor(float vinePercent) {
        this.vinePercent = vinePercent;
    }

    @Nullable
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos1, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo1, StructurePlaceSettings structurePlaceSettings) {
        RandomSource randomSource = structurePlaceSettings.getRandom(structureBlockInfo1.pos);
        BlockState blockState = structureBlockInfo1.state;
        BlockPos blockpos = structureBlockInfo1.pos;

        Biome biome = levelReader.getBiome(blockpos).value();
        Optional<Holder<Biome>> optionalBiomeHolder = ForgeRegistries.BIOMES.getHolder(biome);
        boolean isGreen = optionalBiomeHolder.isPresent() && optionalBiomeHolder.get().is(OdysseyBiomeTags.IS_GREEN);

        if(blockState.is(Blocks.VINE) && (!isGreen || randomSource.nextFloat() >= this.vinePercent)){
            // Returning null means nothing gets placed in the world at this blockPos for this structure, whatever was in the world already stays there
            return null;
        }

        return structureBlockInfo1;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorRegistry.VINE.get();
    }

}
