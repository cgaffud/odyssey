package com.bedmen.odyssey.world.gen.feature.tree;

import com.bedmen.odyssey.block.wood.CornerLeavesBlock;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

import java.util.*;
import java.util.function.BiConsumer;

public class CornerLeafTreeFeature extends Feature<TreeConfiguration> {

    public CornerLeafTreeFeature(Codec<TreeConfiguration> p_67201_) {
        super(p_67201_);
    }

    public static boolean isFree(LevelSimulatedReader p_67263_, BlockPos p_67264_) {
        return validTreePos(p_67263_, p_67264_) || p_67263_.isStateAtPosition(p_67264_, (p_67281_) -> {
            return p_67281_.is(BlockTags.LOGS);
        });
    }

    private static boolean isVine(LevelSimulatedReader p_67278_, BlockPos p_67279_) {
        return p_67278_.isStateAtPosition(p_67279_, (p_67276_) -> {
            return p_67276_.is(Blocks.VINE);
        });
    }

    private static boolean isBlockWater(LevelSimulatedReader p_67283_, BlockPos p_67284_) {
        return p_67283_.isStateAtPosition(p_67284_, (p_67271_) -> {
            return p_67271_.is(Blocks.WATER);
        });
    }

    public static boolean isAirOrLeaves(LevelSimulatedReader p_67268_, BlockPos p_67269_) {
        return p_67268_.isStateAtPosition(p_67269_, (p_67266_) -> {
            return p_67266_.isAir() || p_67266_.is(BlockTags.LEAVES);
        });
    }

    private static boolean isReplaceablePlant(LevelSimulatedReader p_67289_, BlockPos p_67290_) {
        return p_67289_.isStateAtPosition(p_67290_, (p_160551_) -> {
            Material material = p_160551_.getMaterial();
            return material == Material.REPLACEABLE_PLANT;
        });
    }

    private static void setBlockKnownShape(LevelWriter p_67257_, BlockPos p_67258_, BlockState p_67259_) {
        p_67257_.setBlock(p_67258_, p_67259_, 19);
    }

    public static boolean validTreePos(LevelSimulatedReader p_67273_, BlockPos p_67274_) {
        return isAirOrLeaves(p_67273_, p_67274_) || isReplaceablePlant(p_67273_, p_67274_) || isBlockWater(p_67273_, p_67274_);
    }

    private boolean doPlace(WorldGenLevel worldGenLevel, RandomSource randomSource, BlockPos blockPos, BiConsumer<BlockPos, BlockState> biConsumer, BiConsumer<BlockPos, BlockState> biConsumer1, TreeConfiguration treeConfiguration) {
        int i = treeConfiguration.trunkPlacer.getTreeHeight(randomSource);
        int j = treeConfiguration.foliagePlacer.foliageHeight(randomSource, i, treeConfiguration);
        int k = i - j;
        int l = treeConfiguration.foliagePlacer.foliageRadius(randomSource, k);
        if (blockPos.getY() >= worldGenLevel.getMinBuildHeight() + 1 && blockPos.getY() + i + 1 <= worldGenLevel.getMaxBuildHeight()) {
            OptionalInt optionalint = treeConfiguration.minimumSize.minClippedHeight();
            int i1 = this.getMaxFreeTreeHeight(worldGenLevel, i, blockPos, treeConfiguration);
            if (i1 >= i || optionalint.isPresent() && i1 >= optionalint.getAsInt()) {
                List<FoliagePlacer.FoliageAttachment> list = treeConfiguration.trunkPlacer.placeTrunk(worldGenLevel, biConsumer, randomSource, i1, blockPos, treeConfiguration);
                list.forEach((p_160539_) -> {
                    treeConfiguration.foliagePlacer.createFoliage(worldGenLevel, biConsumer1, randomSource, treeConfiguration, i1, p_160539_, j, l);
                });
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getMaxFreeTreeHeight(LevelSimulatedReader p_67216_, int p_67217_, BlockPos p_67218_, TreeConfiguration p_67219_) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int i = 0; i <= p_67217_ + 1; ++i) {
            int j = p_67219_.minimumSize.getSizeAtHeight(p_67217_, i);

            for(int k = -j; k <= j; ++k) {
                for(int l = -j; l <= j; ++l) {
                    blockpos$mutableblockpos.setWithOffset(p_67218_, k, i, l);
                    if (!isFree(p_67216_, blockpos$mutableblockpos) || !p_67219_.ignoreVines && isVine(p_67216_, blockpos$mutableblockpos)) {
                        return i - 2;
                    }
                }
            }
        }

        return p_67217_;
    }

    protected void setBlock(LevelWriter p_67221_, BlockPos p_67222_, BlockState p_67223_) {
        setBlockKnownShape(p_67221_, p_67222_, p_67223_);
    }

    public final boolean place(FeaturePlaceContext<TreeConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomSource = context.random();
        BlockPos blockpos = context.origin();
        TreeConfiguration treeconfiguration = context.config();
        Set<BlockPos> set = Sets.newHashSet();
        Set<BlockPos> set1 = Sets.newHashSet();
        Set<BlockPos> set2 = Sets.newHashSet();
        Set<BlockPos> set3 = Sets.newHashSet();
        BiConsumer<BlockPos, BlockState> biconsumer = (p_160555_, p_160556_) -> {
            set.add(p_160555_.immutable());
            worldgenlevel.setBlock(p_160555_, p_160556_, 19);
        };
        BiConsumer<BlockPos, BlockState> biconsumer1 = (p_160548_, p_160549_) -> {
            set1.add(p_160548_.immutable());
            worldgenlevel.setBlock(p_160548_, p_160549_, 19);
        };
        BiConsumer<BlockPos, BlockState> biconsumer2 = (p_160543_, p_160544_) -> {
            set2.add(p_160543_.immutable());
            worldgenlevel.setBlock(p_160543_, p_160544_, 19);
        };
        BiConsumer<BlockPos, BlockState> biconsumer3 = (p_225290_, p_225291_) -> {
            set3.add(p_225290_.immutable());
            worldgenlevel.setBlock(p_225290_, p_225291_, 19);
        };
        boolean flag = this.doPlace(worldgenlevel, randomSource, blockpos, biconsumer, biconsumer1, treeconfiguration);
        if (flag && (!set.isEmpty() || !set1.isEmpty())) {
            if (!treeconfiguration.decorators.isEmpty()) {
                TreeDecorator.Context treedecorator$context = new TreeDecorator.Context(worldgenlevel, biconsumer3, randomSource, set1, set2, set);
                treeconfiguration.decorators.forEach((p_225282_) -> {
                    p_225282_.place(treedecorator$context);
                });
            }

            return BoundingBox.encapsulatingPositions(Iterables.concat(set, set1, set2)).map((p_160521_) -> {
                DiscreteVoxelShape discretevoxelshape = updateLeaves(worldgenlevel, p_160521_, set, set2);
                StructureTemplate.updateShapeAtEdge(worldgenlevel, 3, discretevoxelshape, p_160521_.minX(), p_160521_.minY(), p_160521_.minZ());
                return true;
            }).orElse(false);
        } else {
            return false;
        }
    }

    private static DiscreteVoxelShape updateLeaves(LevelAccessor p_67203_, BoundingBox p_67204_, Set<BlockPos> p_67205_, Set<BlockPos> p_67206_) {
        List<Set<BlockPos>> list = Lists.newArrayList();
        DiscreteVoxelShape discretevoxelshape = new BitSetDiscreteVoxelShape(p_67204_.getXSpan(), p_67204_.getYSpan(), p_67204_.getZSpan());
        int i = 6;

        for(int j = 0; j < 6; ++j) {
            list.add(Sets.newHashSet());
        }

        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(BlockPos blockpos : Lists.newArrayList(p_67206_)) {
            if (p_67204_.isInside(blockpos)) {
                discretevoxelshape.fill(blockpos.getX() - p_67204_.minX(), blockpos.getY() - p_67204_.minY(), blockpos.getZ() - p_67204_.minZ());
            }
        }

        for(BlockPos blockpos1 : Lists.newArrayList(p_67205_)) {
            if (p_67204_.isInside(blockpos1)) {
                discretevoxelshape.fill(blockpos1.getX() - p_67204_.minX(), blockpos1.getY() - p_67204_.minY(), blockpos1.getZ() - p_67204_.minZ());
            }

            for(Direction direction : Direction.values()) {
                blockpos$mutableblockpos.setWithOffset(blockpos1, direction);
                if (!p_67205_.contains(blockpos$mutableblockpos)) {
                    BlockState blockstate = p_67203_.getBlockState(blockpos$mutableblockpos);
                    if (blockstate.hasProperty(BlockStateProperties.DISTANCE)) {
                        list.get(0).add(blockpos$mutableblockpos.immutable());
                        setBlockKnownShape(p_67203_, blockpos$mutableblockpos, blockstate.setValue(BlockStateProperties.DISTANCE, Integer.valueOf(1)));
                        if (p_67204_.isInside(blockpos$mutableblockpos)) {
                            discretevoxelshape.fill(blockpos$mutableblockpos.getX() - p_67204_.minX(), blockpos$mutableblockpos.getY() - p_67204_.minY(), blockpos$mutableblockpos.getZ() - p_67204_.minZ());
                        }
                    }
                }
            }
        }

        for(int l = 1; l < 6; ++l) {
            Set<BlockPos> set = list.get(l - 1);
            Set<BlockPos> set1 = list.get(l);

            for(BlockPos blockpos2 : set) {
                if (p_67204_.isInside(blockpos2)) {
                    discretevoxelshape.fill(blockpos2.getX() - p_67204_.minX(), blockpos2.getY() - p_67204_.minY(), blockpos2.getZ() - p_67204_.minZ());
                }

                for(Direction direction1 : Direction.values()) {
                    blockpos$mutableblockpos.setWithOffset(blockpos2, direction1);
                    if (!set.contains(blockpos$mutableblockpos) && !set1.contains(blockpos$mutableblockpos)) {
                        BlockState blockstate1 = p_67203_.getBlockState(blockpos$mutableblockpos);
                        if (blockstate1.hasProperty(BlockStateProperties.DISTANCE)) {
                            int m = blockstate1.getBlock() instanceof CornerLeavesBlock ? 0 : 1;
                            int k = blockstate1.getValue(BlockStateProperties.DISTANCE);
                            if (k > l + m) {
                                BlockState blockstate2 = blockstate1.setValue(BlockStateProperties.DISTANCE, l + m);
                                setBlockKnownShape(p_67203_, blockpos$mutableblockpos, blockstate2);
                                if (p_67204_.isInside(blockpos$mutableblockpos)) {
                                    discretevoxelshape.fill(blockpos$mutableblockpos.getX() - p_67204_.minX(), blockpos$mutableblockpos.getY() - p_67204_.minY(), blockpos$mutableblockpos.getZ() - p_67204_.minZ());
                                }

                                set1.add(blockpos$mutableblockpos.immutable());
                            }
                        }
                    }
                }
            }
        }

        return discretevoxelshape;
    }
}
