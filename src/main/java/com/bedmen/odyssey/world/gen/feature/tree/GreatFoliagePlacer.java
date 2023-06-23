package com.bedmen.odyssey.world.gen.feature.tree;

import com.bedmen.odyssey.registry.tree.FoliagePlacerTypeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaJungleFoliagePlacer;

import java.util.Random;
import java.util.function.BiConsumer;

//This might be a bad way to do this
public class GreatFoliagePlacer extends MegaJungleFoliagePlacer {
    public static final Codec<GreatFoliagePlacer> CODEC = RecordCodecBuilder.create((greatFoliagePlacerInstance) -> {
        return foliagePlacerParts(greatFoliagePlacerInstance).and(Codec.intRange(0, 16).fieldOf("height").forGetter((greatFoliagePlacer) -> {
            return greatFoliagePlacer.height;
        })).apply(greatFoliagePlacerInstance, GreatFoliagePlacer::new);
    });

    public GreatFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
        super(radius, offset, height);
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, TreeConfiguration config, int p_161462_, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int foliageRadius, int offset) {
        int i = foliageAttachment.doubleTrunk() ? foliageHeight : 1;
        // new edits
        i++;

        for(int j = offset; j >= offset - i; --j) {
            //foliageRadius + foliageAttachment.radiusOffset() -3 - j
            int k = foliageRadius + foliageAttachment.radiusOffset() - j  + ((j == offset-i) ? -2 : 1);
            this.placeLeavesRow(levelSimulatedReader, biConsumer, randomSource, config, foliageAttachment.pos(), k, j, foliageAttachment.doubleTrunk());
        }
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource, int xOff, int yOff, int zOff, int radius, boolean isDoubleTrunk) {
        if (isDoubleTrunk) {
            if (xOff + zOff >= 7)
                return true;
            else
                return xOff * xOff + zOff * zOff > radius * radius;
        }
        else {
            return xOff * xOff + zOff * zOff > (radius-2) * (radius-2);
        }
    }

    protected FoliagePlacerType<?> type() {
        return FoliagePlacerTypeRegistry.GREAT_FOLIAGE_PLACER.get();
    }
}