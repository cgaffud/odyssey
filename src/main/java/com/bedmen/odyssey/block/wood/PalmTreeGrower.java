package com.bedmen.odyssey.block.wood;

import com.bedmen.odyssey.world.gen.FeatureGen;
import com.bedmen.odyssey.world.gen.TreeGen;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

import javax.annotation.Nullable;
import java.util.Random;

public class PalmTreeGrower extends AbstractTreeGrower {
    @Nullable
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random p_225546_1_, boolean p_225546_2_) {
        return TreeGen.PALM_TREE;
    }
}