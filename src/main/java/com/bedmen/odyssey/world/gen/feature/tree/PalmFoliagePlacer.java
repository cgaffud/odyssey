package com.bedmen.odyssey.world.gen.feature.tree;

import com.bedmen.odyssey.block.CoconutBlock;
import com.bedmen.odyssey.block.HollowCoconutBlock;
import com.bedmen.odyssey.block.wood.CornerLeavesBlock;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.FoliagePlacerTypeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
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
        return FoliagePlacerTypeRegistry.PALM_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, TreeConfiguration treeConfiguration, int p_161350_, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int foliageRadius, int offset) {
        BlockPos blockpos = foliageAttachment.pos();
        for (int h = -1; h < foliageHeight; h++)
            this.placeLeavesRow(levelSimulatedReader, biConsumer, random, treeConfiguration, blockpos, foliageRadius, h, false);
    }

    protected void placeLeavesRow(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, TreeConfiguration treeConfiguration, BlockPos blockPos, int foliageRadius, int height, boolean isDoubleTrunk) {
        int i = isDoubleTrunk ? 1 : 0;
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();

        for(int j = -foliageRadius; j <= foliageRadius + i; ++j) {
            for(int k = -foliageRadius; k <= foliageRadius + i; ++k) {
                if(height <= 0){
                    boolean flip = Math.abs(j) == 2-height && Math.abs(k) == 1-height;
                    if(flip || Math.abs(j) == 1-height && Math.abs(k) == 2-height){
                        blockpos$mutable.setWithOffset(blockPos, j, height, k);
                        tryPlaceCornerLeaf(levelSimulatedReader, biConsumer, blockpos$mutable, flip, j > 0, k > 0);
                    }
                }
                if(height == -1){
                    int absj = Math.abs(j);
                    int absk = Math.abs(k);
                    if((absj == 0 && absk == 1) || (absj == 1 && absk == 0)){
                        blockpos$mutable.setWithOffset(blockPos, j, height, k);
                        BlockState blockState = null;
                        switch(random.nextInt(20)){
                            case 0:
                                blockState = BlockRegistry.COCONUT.get().defaultBlockState().setValue(CoconutBlock.AGE, 0);
                                break;
                            case 1:
                                blockState = BlockRegistry.COCONUT.get().defaultBlockState().setValue(CoconutBlock.AGE, 1);
                                break;
                            case 2:
                                blockState = BlockRegistry.COCONUT.get().defaultBlockState().setValue(CoconutBlock.AGE, 2);
                                break;
                            case 3:
                            case 4:
                            case 5:
                                blockState = BlockRegistry.HOLLOW_COCONUT.get().defaultBlockState().setValue(HollowCoconutBlock.HANGING, Boolean.TRUE);
                                break;

                        }
                        if(blockState != null){
                            biConsumer.accept(blockpos$mutable, blockState);
                        }
                    }
                }

                if (!this.shouldSkipLocationSigned(random, j, height, k, foliageRadius, isDoubleTrunk)) {
                    blockpos$mutable.setWithOffset(blockPos, j, height, k);
                    tryPlaceLeaf(levelSimulatedReader, biConsumer, random, treeConfiguration, blockpos$mutable);
                }
            }
        }
    }

    @Override
    public int foliageHeight(Random random, int treeHeight, TreeConfiguration config) {
        return treeHeight-4;
    }

    protected static void tryPlaceCornerLeaf(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, BlockPos blockPos, boolean flip, boolean xPositive, boolean zPositive) {
        if (TreeFeature.validTreePos(levelSimulatedReader, blockPos)) {
            Direction direction = xPositive ? (zPositive ? Direction.NORTH : Direction.EAST) : (zPositive ? Direction.WEST : Direction.SOUTH);
            if(flip){
                direction = direction.getOpposite();
            }
            biConsumer.accept(blockPos, BlockRegistry.PALM_CORNER_LEAVES.get().defaultBlockState().setValue(CornerLeavesBlock.FACING, direction));
        }
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
