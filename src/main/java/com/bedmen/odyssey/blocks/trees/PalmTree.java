package com.bedmen.odyssey.blocks.trees;

import java.util.Random;
import javax.annotation.Nullable;

import com.bedmen.odyssey.world.gen.OdysseyFeatureGen;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class PalmTree extends Tree {
    @Nullable
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random p_225546_1_, boolean p_225546_2_) {
        return OdysseyFeatureGen.PALM_TREE;
    }
}