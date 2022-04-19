package com.bedmen.odyssey.block.wood;

import com.bedmen.odyssey.world.gen.TreeGen;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Random;

public class PalmTreeGrower extends AbstractTreeGrower {
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean b) {
        return TreeGen.PALM;
    }
}