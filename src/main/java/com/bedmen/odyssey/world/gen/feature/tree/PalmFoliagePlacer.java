package com.bedmen.odyssey.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;

import java.util.Random;
import java.util.Set;

public class PalmFoliagePlacer extends FoliagePlacer {
    public static final Codec<PalmFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return foliagePlacerParts(instance).apply(instance, PalmFoliagePlacer::new);
    });

    public PalmFoliagePlacer(FeatureSpread radius, FeatureSpread offset) {
        super(radius, offset);
    }

    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.FANCY_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(IWorldGenerationReader worldGenerationReader, Random random, BaseTreeFeatureConfig config, int maxTreeHeight, Foliage foliage, int foliageHeight, int foliageRadius, Set<BlockPos> set, int offset, MutableBoundingBox mutableBoundingBox) {
        BlockPos blockpos = foliage.foliagePos();
        for (int h = -1; h < foliageHeight; h++)
            this.placeLeavesRow(worldGenerationReader, random, config, blockpos, foliageRadius, set, h, false, mutableBoundingBox);
    }

    @Override
    public int foliageHeight(Random random, int treeHeight, BaseTreeFeatureConfig config) {
        return treeHeight-4;
    }

    @Override
    protected boolean shouldSkipLocation(Random random, int xOff, int yOff, int zOff, int radius, boolean isDoubleTrunk) {
        int smallRadius = radius/2;
        int horizontalSum = xOff + zOff;
        if ((yOff >= 0) && (yOff+horizontalSum) <= smallRadius)
            return false;
        else if ((yOff == 0) && ((xOff+zOff) <= radius-1)) {
            if (xOff == zOff) return false;
            if (((xOff == 0) || (zOff == 0)) && (horizontalSum <= radius-2)) return false;
            return true;
        }
        else if (yOff == -1){
            if ((xOff == zOff) && (horizontalSum >= radius-1) && (horizontalSum <= radius+1)) return false;
            if (((xOff == 0) || (zOff == 0)) && ((horizontalSum == radius-2) || (horizontalSum == radius-1))) return false;
        }
        return true;
    }
}
