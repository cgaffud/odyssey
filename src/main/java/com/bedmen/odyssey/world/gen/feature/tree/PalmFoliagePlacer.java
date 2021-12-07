package com.bedmen.odyssey.world.gen.feature.tree;

import com.bedmen.odyssey.registry.BlockRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;

public class PalmFoliagePlacer extends FoliagePlacer {
    public static final Codec<PalmFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return foliagePlacerParts(instance).apply(instance, PalmFoliagePlacer::new);
    });

    public PalmFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.FANCY_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, TreeConfiguration treeConfiguration, int p_161350_, FoliagePlacer.FoliageAttachment foliageAttachment, int p_161352_, int p_161353_, int p_161354_) {
//        BlockPos blockpos = foliage.foliagePos();
//        for (int h = -1; h < foliageHeight; h++)
//            this.placeLeavesRow(worldGenerationReader, random, config, blockpos, foliageRadius, set, h, false, mutableBoundingBox);
    }

    protected void placeLeavesRow(LevelSimulatedRW worldGenerationReader, Random random, TreeConfiguration baseTreeFeatureConfig, BlockPos blockPos, int foliageRadius, Set<BlockPos> blockPosSet, int height, boolean p_236753_8_, BoundingBox p_236753_9_) {
//        int i = p_236753_8_ ? 1 : 0;
//        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
//
//        for(int j = -foliageRadius; j <= foliageRadius + i; ++j) {
//            for(int k = -foliageRadius; k <= foliageRadius + i; ++k) {
//                if (!this.shouldSkipLocationSigned(random, j, height, k, foliageRadius, p_236753_8_)) {
//                    blockpos$mutable.setWithOffset(blockPos, j, height, k);
//                    if (TreeFeature.validTreePos(worldGenerationReader, blockpos$mutable)) {
//                        worldGenerationReader.setBlock(blockpos$mutable, baseTreeFeatureConfig.leavesProvider.getState(random, blockpos$mutable), 19);
//                        p_236753_9_.expand(new BoundingBox(blockpos$mutable, blockpos$mutable));
//                        blockPosSet.add(blockpos$mutable.immutable());
//                    }
//                }
//                int absj = Math.abs(j);
//                int absk = Math.abs(k);
//                if(height == -1 && ((absj == 0 && absk == 1) || (absj == 1 && absk == 0))){
//                    blockpos$mutable.setWithOffset(blockPos, j, height, k);
//                    int r = random.nextInt(16);
//                    if(r < 9){
//                        BlockState blockState = BlockRegistry.COCONUT.get().defaultBlockState();
//                        if(r >= 6){
//                            blockState = blockState.setValue(CoconutBlock.AGE, 2);
//                        } else if(r >= 3) {
//                            blockState = blockState.setValue(CoconutBlock.AGE, 1);
//                        } else {
//                            blockState = blockState.setValue(CoconutBlock.AGE, 0);
//                        }
//                        worldGenerationReader.setBlock(blockpos$mutable, blockState, 19);
//                    } else if (r < 12){
//                        BlockState blockState = BlockRegistry.HOLLOW_COCONUT.get().defaultBlockState().setValue(HollowCoconutBlock.HANGING, Boolean.TRUE);
//                        worldGenerationReader.setBlock(blockpos$mutable, blockState, 19);
//                    }
//                }
//            }
//        }
    }

    @Override
    public int foliageHeight(Random random, int treeHeight, TreeConfiguration config) {
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
