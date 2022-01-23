package com.bedmen.odyssey.world.gen.feature;

import com.bedmen.odyssey.block.TriplePlantBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

public class TriplePlantBlockFeature extends SimpleBlockFeature {
    public TriplePlantBlockFeature(Codec<SimpleBlockConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> featurePlaceContext) {
        SimpleBlockConfiguration simpleblockconfiguration = featurePlaceContext.config();
        WorldGenLevel worldgenlevel = featurePlaceContext.level();
        BlockPos blockpos = featurePlaceContext.origin();
        BlockState blockstate = simpleblockconfiguration.toPlace().getState(featurePlaceContext.random(), blockpos);
        if (blockstate.getBlock() instanceof TriplePlantBlock && blockstate.canSurvive(worldgenlevel, blockpos) && worldgenlevel.isEmptyBlock(blockpos.above()) && worldgenlevel.isEmptyBlock(blockpos.above(2))) {
            TriplePlantBlock.placeAt(worldgenlevel, blockstate, blockpos, 2);
            return true;
        }
        return false;
    }
}
