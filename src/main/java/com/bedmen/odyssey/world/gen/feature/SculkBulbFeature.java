package com.bedmen.odyssey.world.gen.feature;

import com.bedmen.odyssey.block.TreasureChestBlock;
import com.bedmen.odyssey.lock.TreasureChestType;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.util.GeneralUtil;
import com.bedmen.odyssey.world.WorldGenUtil;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SculkBulbFeature extends Feature<NoneFeatureConfiguration> {

    public SculkBulbFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource randomSource = context.random();
        Random random = new Random(randomSource.nextInt());
        WorldGenLevel worldgenlevel = context.level();
        ChunkPos chunkpos = new ChunkPos(context.origin());
        List<Integer> list = IntStream.rangeClosed(chunkpos.getMinBlockX(), chunkpos.getMaxBlockX()).boxed().collect(Collectors.toList());
        Collections.shuffle(list, random);
        List<Integer> list1 = IntStream.rangeClosed(chunkpos.getMinBlockZ(), chunkpos.getMaxBlockZ()).boxed().collect(Collectors.toList());
        Collections.shuffle(list1, random);
        List<Integer> yList = IntStream.rangeClosed(worldgenlevel.getMinBuildHeight() / 8, 0).boxed().map((i) -> i*8) .collect(Collectors.toList());
        Collections.shuffle(yList, random);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(Integer integer : list) {
            for(Integer integer1 : list1) {
                for(Integer y : yList){
                    blockpos$mutableblockpos.set(integer, y, integer1);
                    for(int i = 0; i < 8; i++){
                        blockpos$mutableblockpos.move(0, -1, 0);
                        if (validPlacement(worldgenlevel, blockpos$mutableblockpos)) {
                            worldgenlevel.setBlock(blockpos$mutableblockpos, BlockRegistry.SCULK_BULB.get().defaultBlockState(), 2);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private static boolean validPlacement(LevelAccessor levelAccessor, BlockPos.MutableBlockPos mutableBlockPos) {
        return (WorldGenUtil.isEmpty(levelAccessor, mutableBlockPos) && levelAccessor.getBlockState(mutableBlockPos.below()).is(Blocks.SCULK))
                || (!WorldGenUtil.isEmpty(levelAccessor, mutableBlockPos.below()) && levelAccessor.getBlockState(mutableBlockPos).is(Blocks.SCULK_VEIN));
    }
}
