package com.bedmen.odyssey.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CovenRootsBlock extends Block {

    public CovenRootsBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if ((pos.distToCenterSqr(entity.position()) <= 0.5f) && (entity instanceof Player)) {

            if (entity.position().y > pos.getY())
                entity.makeStuckInBlock(state, new Vec3(2E-4D, 1.0, 2E-4D));
            else
                entity.makeStuckInBlock(state, new Vec3(2E-4D, 2E-4D, 2E-4D));
        }
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return Block.box(2,0,2,14,14,14);
    }
}
