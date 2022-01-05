package com.bedmen.odyssey.world.gen.feature;

import com.bedmen.odyssey.block.TreasureChestBlock;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
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

public class TreasureChestFeature extends Feature<NoneFeatureConfiguration> {
    public TreasureChestFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        Random random = context.random();
        if(random.nextInt(30) != 0){
            return false;
        }
        WorldGenLevel worldgenlevel = context.level();
        ChunkPos chunkpos = new ChunkPos(context.origin());
        List<Integer> list = IntStream.rangeClosed(chunkpos.getMinBlockX(), chunkpos.getMaxBlockX()).boxed().collect(Collectors.toList());
        Collections.shuffle(list, random);
        List<Integer> list1 = IntStream.rangeClosed(chunkpos.getMinBlockZ(), chunkpos.getMaxBlockZ()).boxed().collect(Collectors.toList());
        Collections.shuffle(list1, random);
        List<Integer> yList = IntStream.rangeClosed(-7, 7).boxed().map((i) -> i*8) .collect(Collectors.toList());
        Collections.shuffle(yList, random);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(Integer integer : list) {
            for(Integer integer1 : list1) {
                for(Integer y : yList){
                    blockpos$mutableblockpos.set(integer, y, integer1);
                    for(int i = 0; i < 8; i++){
                        blockpos$mutableblockpos.offset(0, -1, 0);
                        BlockPos blockPosBelow = blockpos$mutableblockpos.below();
                        if (isSolid(worldgenlevel, blockpos$mutableblockpos) && !isSolid(worldgenlevel, blockPosBelow)) {
                            if(blockpos$mutableblockpos.getY() >= 0){
                                BlockState blockState = Blocks.CHEST.defaultBlockState().setValue(TreasureChestBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
                                worldgenlevel.setBlock(blockpos$mutableblockpos, blockState, 2);
                                RandomizableContainerBlockEntity.setLootTable(worldgenlevel, random, blockpos$mutableblockpos, OdysseyLootTables.WOODEN_TREASURE_CHEST);
                                return true;
                            } else {
                                BlockState blockState = BlockRegistry.STERLING_SILVER_CHEST.get().defaultBlockState().setValue(TreasureChestBlock.LOCKED, true).setValue(TreasureChestBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
                                worldgenlevel.setBlock(blockpos$mutableblockpos, blockState, 2);
                                RandomizableContainerBlockEntity.setLootTable(worldgenlevel, random, blockpos$mutableblockpos, OdysseyLootTables.STERLING_SILVER_TREASURE_CHEST);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean isSolid(WorldGenLevel worldgenlevel, BlockPos blockPos){
        return (worldgenlevel.isEmptyBlock(blockPos) || worldgenlevel.getBlockState(blockPos).getCollisionShape(worldgenlevel, blockPos).isEmpty()) && worldgenlevel.getBlockState(blockPos).getFluidState().isEmpty();
    }
}
