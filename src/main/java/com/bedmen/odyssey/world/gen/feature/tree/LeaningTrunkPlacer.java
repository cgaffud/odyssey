package com.bedmen.odyssey.world.gen.feature.tree;

import com.bedmen.odyssey.registry.tree.TrunkPlacerTypeRegistry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class LeaningTrunkPlacer extends TrunkPlacer {
    public static final Codec<LeaningTrunkPlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return trunkPlacerParts(instance).apply(instance, LeaningTrunkPlacer::new);
    });

    public LeaningTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    protected TrunkPlacerType<?> type() {
        return TrunkPlacerTypeRegistry.LEANING_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, int maxTreeHeight, BlockPos pos, TreeConfiguration config) {
        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();

        Direction mainDir = Direction.Plane.HORIZONTAL.getRandomDirection(randomSource);
        Direction offDir = (randomSource.nextInt(2) == 1) ? mainDir.getClockWise(): mainDir.getCounterClockWise();
        BlockPos.MutableBlockPos pos$mutable = new BlockPos.MutableBlockPos();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int[] h = new int[3];
        h[0] = maxTreeHeight/3;
        h[1] = randomSource.nextInt(maxTreeHeight/3)+maxTreeHeight/3+h[0];
        h[2] = maxTreeHeight;
        int i = 0;

        int l = 0;

        placeLog(levelSimulatedReader, biConsumer, randomSource, pos$mutable.set(x-mainDir.getStepX(),y,z-mainDir.getStepZ()), config);
        placeLog(levelSimulatedReader, biConsumer, randomSource, pos$mutable.set(x-offDir.getStepX(),y,z-offDir.getStepZ()), config);

        for (int yOff = 0; yOff < maxTreeHeight; yOff++) {
            if (yOff == h[i]) {
                x += mainDir.getStepX();
                z += mainDir.getStepZ();
                i++;
            }

            if (placeLog(levelSimulatedReader, biConsumer, randomSource, pos$mutable.set(x,yOff+y,z), config))
                l = y+yOff+1;
        }

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(new BlockPos(x,l,z), 0, false));
    }


}
