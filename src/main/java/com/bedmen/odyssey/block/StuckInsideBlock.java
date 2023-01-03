package com.bedmen.odyssey.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class StuckInsideBlock extends Block {

    protected Vec3 stuckSpeedMultiplier;

    public StuckInsideBlock(BlockBehaviour.Properties properties, Vec3 stuckSpeedMultiplier) {
        super(properties);
        this.stuckSpeedMultiplier = stuckSpeedMultiplier;
    }

    public StuckInsideBlock(BlockBehaviour.Properties properties) {
        super(properties);
        // If speedMultiplier is Vec3.Zero, you can just pass through the block. So we need the
        // multiplier not to be zero but close to the defined epsilon (1E-7)
        this.stuckSpeedMultiplier = new Vec3(2E-4D, 2E-4D, 2E-4D);
    }

    public void entityInside(BlockState state, Level p_58181_, BlockPos p_58182_, Entity p_58183_) {
        p_58183_.makeStuckInBlock(state, this.stuckSpeedMultiplier);
    }
}
