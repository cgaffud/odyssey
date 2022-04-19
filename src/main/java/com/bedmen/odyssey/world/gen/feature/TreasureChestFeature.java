package com.bedmen.odyssey.world.gen.feature;

import com.bedmen.odyssey.block.TreasureChestBlock;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.util.WorldGenUtil;
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
    private static final int RARITY = 1;
    public final TreasureChestMaterial treasureChestMaterial;
    private final int minY;
    private final int maxY;

    public TreasureChestFeature(Codec<NoneFeatureConfiguration> codec, TreasureChestMaterial treasureChestMaterial, int minY, int maxY) {
        super(codec);
        this.treasureChestMaterial = treasureChestMaterial;
        this.minY = minY + 8;
        this.maxY = maxY;
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        Random random = context.random();
        if(random.nextInt(RARITY) != 0){
            return false;
        }
        WorldGenLevel worldgenlevel = context.level();
        ChunkPos chunkpos = new ChunkPos(context.origin());
        List<Integer> list = IntStream.rangeClosed(chunkpos.getMinBlockX(), chunkpos.getMaxBlockX()).boxed().collect(Collectors.toList());
        Collections.shuffle(list, random);
        List<Integer> list1 = IntStream.rangeClosed(chunkpos.getMinBlockZ(), chunkpos.getMaxBlockZ()).boxed().collect(Collectors.toList());
        Collections.shuffle(list1, random);
        List<Integer> yList = IntStream.rangeClosed(this.minY / 8, this.maxY / 8).boxed().map((i) -> i*8) .collect(Collectors.toList());
        Collections.shuffle(yList, random);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(Integer integer : list) {
            for(Integer integer1 : list1) {
                for(Integer y : yList){
                    blockpos$mutableblockpos.set(integer, y, integer1);
                    for(int i = 0; i < 8; i++){
                        blockpos$mutableblockpos.move(0, -1, 0);
                        BlockPos blockPosBelow = blockpos$mutableblockpos.below();
                        if (WorldGenUtil.isEmpty(worldgenlevel, blockpos$mutableblockpos) && WorldGenUtil.isSolid(worldgenlevel, blockPosBelow)) {
                            BlockState blockState = this.treasureChestMaterial.getBlockState().setValue(TreasureChestBlock.LOCKED, true).setValue(TreasureChestBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
                            worldgenlevel.setBlock(blockpos$mutableblockpos, blockState, 2);
                            RandomizableContainerBlockEntity.setLootTable(worldgenlevel, random, blockpos$mutableblockpos, this.treasureChestMaterial.lootTable);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


}
