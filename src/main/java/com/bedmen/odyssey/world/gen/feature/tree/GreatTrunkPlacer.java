package com.bedmen.odyssey.world.gen.feature.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class GreatTrunkPlacer extends TrunkPlacer {
    public static final Codec<GreatTrunkPlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return trunkPlacerParts(instance).apply(instance, GreatTrunkPlacer::new);
    });

    public GreatTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.GIANT_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, int maxTreeHeight, BlockPos pos, TreeConfiguration config) {
        BlockPos dirt = pos.below();
        /* base of the trunk */
        setDirtAt(levelSimulatedReader, biConsumer, random, dirt, config);
        setDirtAt(levelSimulatedReader, biConsumer, random, dirt.east(), config);
        setDirtAt(levelSimulatedReader, biConsumer, random, dirt.south(), config);
        setDirtAt(levelSimulatedReader, biConsumer, random, dirt.south().east(), config);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        int quarterHeight = (maxTreeHeight-maxTreeHeight/2)/2;
        //designed to be 4
        for (int i = 0; i < quarterHeight; i++) {
            // places 2x2 thick log
            for (int j = 0; j < 4; j++)
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, pos, j/2, i, j%2);
        }

        /* diagonal lean of the trunk */
        int randLean = random.nextInt(4);
        BlockPos lean = pos.offset(randLean/2, quarterHeight,randLean%2);
        int dirx = (randLean/2 == 0) ? 1 : -1;
        int dirz = (randLean%2 == 0) ? 1 : -1;

        for (int i = 0; i < quarterHeight; i++){
            if (i % 2 == 0) {
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, dirx, 0, 0);
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, 0, 0, dirz);
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, dirx, 0, dirz);
                if (i != 0 ) {
                    boolean lower = random.nextBoolean();
                    placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, lower ? dirx : 0, -1, lower ? 0 : dirz);
                }
            }
            else {
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, 2*dirx, 1, 0);
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, 0, 1, 2*dirz);
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, dirx, 1, dirz);
                lean = lean.offset(dirx, 2, dirz);
            }
        }
        // adjust in case quarterHeight is odd, as lean doesn't get moved to new location
        if (quarterHeight % 2 == 1) lean = lean.offset(0,1,0);

        List<FoliagePlacer.FoliageAttachment> foliage = Lists.newArrayList();

        /* remainder of trunk */
        for (int i = 0; i < maxTreeHeight/2; i++) {
            for (int j = 0; j < 4; j++) {
                if (((i == 0)||(i == maxTreeHeight/2-1)) && (j == 0))
                    continue;
                placeLogIfFreeWithOffset(levelSimulatedReader, biConsumer, random, blockpos$mutableblockpos, config, lean, (j/2==0) ? 0 : dirx, i, (j%2==0) ? 0 : dirz);
            }
        }

        // correct lean to minimal x/z on 2x2 trunk to input to foliage place (otherwise it might be all wonky)
        BlockPos topFoliagePosition = lean.offset((dirx == 1) ? 0 : -1, maxTreeHeight/2, (dirz == 1) ? 0 : -1);
        foliage.add(new FoliagePlacer.FoliageAttachment(topFoliagePosition,0,true));
        return foliage;
    }



    private static void placeLogIfFreeWithOffset(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, Random random, BlockPos.MutableBlockPos mutable, TreeConfiguration config, BlockPos pos, int x, int y, int z) {
        mutable.setWithOffset(pos, x, y, z);
        placeLogIfFree(levelSimulatedReader, biConsumer, random, mutable, config);
    }

}
