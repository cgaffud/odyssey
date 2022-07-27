package com.bedmen.odyssey.world.gen.feature;

import com.bedmen.odyssey.block.AbandonedIronGolemBlock;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class AbandonedIronGolemFeature extends Feature<NoneFeatureConfiguration> {
    private static final Vec3i CENTER = new Vec3i(0,64,0);
    private static final double RARITY_RADIUS = 480;
    private static final int FAR_RARITY = 400;
    private static final int NEAR_RARITY = 100;
    public AbandonedIronGolemFeature(Codec<NoneFeatureConfiguration> p_i231962_1_) {
        super(p_i231962_1_);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        Random random = context.random();
        WorldGenLevel worldGenLevel = context.level();
        BlockPos origin = context.origin();
        double distance = Math.sqrt(origin.distSqr(CENTER));
        int rarity = distance > RARITY_RADIUS ? FAR_RARITY : (int) Mth.lerp(distance / RARITY_RADIUS, NEAR_RARITY, FAR_RARITY);
        if(random.nextInt(rarity) != 0){
            return false;
        }
        origin = worldGenLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, origin);
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockPos.MutableBlockPos mutable = origin.mutable();
        if(mayPlaceOn(worldGenLevel, mutable)){
            this.setBlock(worldGenLevel, mutable, BlockRegistry.ABANDONED_IRON_GOLEM.get().defaultBlockState().setValue(AbandonedIronGolemBlock.FACING, direction).setValue(AbandonedIronGolemBlock.HALF, DoubleBlockHalf.LOWER));
            mutable = mutable.move(Direction.UP);
            this.setBlock(worldGenLevel, mutable, BlockRegistry.ABANDONED_IRON_GOLEM.get().defaultBlockState().setValue(AbandonedIronGolemBlock.FACING, direction).setValue(AbandonedIronGolemBlock.HALF, DoubleBlockHalf.UPPER));
            return true;
        }
        return false;
    }

    private boolean mayPlaceOn(LevelAccessor levelAccessor, BlockPos blockPos) {
        BlockPos blockpos = blockPos.below();
        BlockState blockstate = levelAccessor.getBlockState(blockpos);
        return blockstate.isFaceSturdy(levelAccessor, blockpos, Direction.UP);
    }
}