package com.bedmen.odyssey.block.light_emitters;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Random;

public class LightEmitterWallBlock extends WallBlock {
    private IsLitProvider provider;
    private static final BooleanProperty LIT = BlockStateProperties.LIT;

    // SET IsRandomlyTicking to true in properties before using
    public LightEmitterWallBlock(Properties p_49795_, IsLitProvider provider) {
        super(p_49795_);
        this.provider = provider;
        this.registerDefaultState(this.defaultBlockState().setValue(LIT,false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(LIT);
        super.createBlockStateDefinition(stateBuilder);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, @NotNull ServerLevel serverLevel, BlockPos blockPos, Random random) {
        super.randomTick(state, serverLevel, blockPos, random);
        if (!serverLevel.isAreaLoaded(blockPos, 1)) return;
        // equiv to (lit && !lit_cond) || (!lit && lit_cond)
        if (state.getValue(LIT) ^ this.provider.isLit(blockPos, serverLevel)) {
            serverLevel.setBlock(blockPos, state.cycle(LIT), 2);
        }
    }

    @Override
    public Map<BlockState, VoxelShape> makeShapes(float p_57966_, float p_57967_, float p_57968_, float p_57969_, float p_57970_, float p_57971_) {
        float f = 8.0F - p_57966_;
        float f1 = 8.0F + p_57966_;
        float f2 = 8.0F - p_57967_;
        float f3 = 8.0F + p_57967_;
        VoxelShape voxelshape = Block.box((double)f, 0.0D, (double)f, (double)f1, (double)p_57968_, (double)f1);
        VoxelShape voxelshape1 = Block.box((double)f2, (double)p_57969_, 0.0D, (double)f3, (double)p_57970_, (double)f3);
        VoxelShape voxelshape2 = Block.box((double)f2, (double)p_57969_, (double)f2, (double)f3, (double)p_57970_, 16.0D);
        VoxelShape voxelshape3 = Block.box(0.0D, (double)p_57969_, (double)f2, (double)f3, (double)p_57970_, (double)f3);
        VoxelShape voxelshape4 = Block.box((double)f2, (double)p_57969_, (double)f2, 16.0D, (double)p_57970_, (double)f3);
        VoxelShape voxelshape5 = Block.box((double)f2, (double)p_57969_, 0.0D, (double)f3, (double)p_57971_, (double)f3);
        VoxelShape voxelshape6 = Block.box((double)f2, (double)p_57969_, (double)f2, (double)f3, (double)p_57971_, 16.0D);
        VoxelShape voxelshape7 = Block.box(0.0D, (double)p_57969_, (double)f2, (double)f3, (double)p_57971_, (double)f3);
        VoxelShape voxelshape8 = Block.box((double)f2, (double)p_57969_, (double)f2, 16.0D, (double)p_57971_, (double)f3);
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        for(Boolean obool : UP.getPossibleValues()) {
            for(WallSide wallside : EAST_WALL.getPossibleValues()) {
                for(WallSide wallside1 : NORTH_WALL.getPossibleValues()) {
                    for(WallSide wallside2 : WEST_WALL.getPossibleValues()) {
                        for(WallSide wallside3 : SOUTH_WALL.getPossibleValues()) {
                            VoxelShape voxelshape9 = Shapes.empty();
                            voxelshape9 = applyWallShape(voxelshape9, wallside, voxelshape4, voxelshape8);
                            voxelshape9 = applyWallShape(voxelshape9, wallside2, voxelshape3, voxelshape7);
                            voxelshape9 = applyWallShape(voxelshape9, wallside1, voxelshape1, voxelshape5);
                            voxelshape9 = applyWallShape(voxelshape9, wallside3, voxelshape2, voxelshape6);
                            if (obool) {
                                voxelshape9 = Shapes.or(voxelshape9, voxelshape);
                            }

                            BlockState blockstate = this.defaultBlockState().setValue(UP, obool).setValue(EAST_WALL, wallside).setValue(WEST_WALL, wallside2).setValue(NORTH_WALL, wallside1).setValue(SOUTH_WALL, wallside3);

                            for(Boolean waterlogged : WATERLOGGED.getPossibleValues()) {
                                for(Boolean lit : LIT.getPossibleValues()) {
                                    builder.put(blockstate.setValue(WATERLOGGED, waterlogged).setValue(LIT, lit), voxelshape9);
                                }
                            }
                        }
                    }
                }
            }
        }

        return builder.build();
    }
}
