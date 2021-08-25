package com.bedmen.odyssey.world.gen.feature.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.ForkyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class LeaningTrunkPlacer extends AbstractTrunkPlacer {
    public static final Codec<LeaningTrunkPlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return trunkPlacerParts(instance).apply(instance, LeaningTrunkPlacer::new);
    });

    public LeaningTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.FORKING_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.Foliage> placeTrunk(IWorldGenerationReader worldGenerationReader, Random random, int maxTreeHeight, BlockPos pos, Set<BlockPos> set, MutableBoundingBox mutableBoundingBox, BaseTreeFeatureConfig config) {
        List<FoliagePlacer.Foliage> list = Lists.newArrayList();

        Direction mainDir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        Direction offDir = (random.nextInt(2) == 1) ? mainDir.getClockWise(): mainDir.getCounterClockWise();
        BlockPos.Mutable pos$mutable = new BlockPos.Mutable();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int[] h = new int[3];
        h[0] = maxTreeHeight/3;
        h[1] = random.nextInt(maxTreeHeight/3)+maxTreeHeight/3+h[0];
        h[2] = maxTreeHeight;
        int i = 0;

        int l = 0;

        placeLog(worldGenerationReader, random, pos$mutable.set(x-mainDir.getStepX(),y,z-mainDir.getStepZ()), set, mutableBoundingBox, config);
        placeLog(worldGenerationReader, random, pos$mutable.set(x-offDir.getStepX(),y,z-offDir.getStepZ()), set, mutableBoundingBox, config);

        for (int yOff = 0; yOff < maxTreeHeight; yOff++) {
            if (yOff == h[i]) {
                x += mainDir.getStepX();
                z += mainDir.getStepZ();
                i++;
            }

            if (placeLog(worldGenerationReader, random, pos$mutable.set(x,yOff+y,z),set,mutableBoundingBox,config))
                l = y+yOff+1;
        }

        return ImmutableList.of(new FoliagePlacer.Foliage(new BlockPos(x,l,z), 0, false));
    }


}
