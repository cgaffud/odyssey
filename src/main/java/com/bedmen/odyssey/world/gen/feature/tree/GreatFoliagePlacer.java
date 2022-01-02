package com.bedmen.odyssey.world.gen.feature.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaJungleFoliagePlacer;

import java.util.Random;
import java.util.function.BiConsumer;

public class GreatFoliagePlacer extends MegaJungleFoliagePlacer {

    //This might be a bad way to do this
    public GreatFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
        super(radius, offset, height);
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, TreeConfiguration config, int p_161462_, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int foliageRadius, int offset) {
        int i = foliageAttachment.doubleTrunk() ? foliageHeight : 1;

        for(int j = offset; j >= offset - i; --j) {
            int k = foliageRadius + foliageAttachment.radiusOffset() + 1 - j;
            this.placeLeavesRow(levelSimulatedReader, biConsumer, random, config, foliageAttachment.pos(), k, j, foliageAttachment.doubleTrunk());
        }
    }

    @Override
    protected boolean shouldSkipLocation(Random random, int xOff, int yOff, int zOff, int radius, boolean isDoubleTrunk) {
        if (isDoubleTrunk) {
            if (xOff + zOff >= 7)
                return true;
            else
                return xOff * xOff + zOff * zOff > radius * radius;
        }
        else {
            return xOff * xOff + zOff * zOff > (radius-2) * (radius-2);
        }
    }
}
