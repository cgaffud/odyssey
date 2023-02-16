package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.block.wood.CovenHutDoorBlock;
import com.bedmen.odyssey.potions.FireEffect;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.stream.Collectors;

public class CovenHutDoorBlockEntity extends BlockEntity {
    public CovenHutDoorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.COVEN_HUT_DOOR.get(), blockPos, blockState);
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, CovenHutDoorBlockEntity covenHutDoorBlockEntity) {
//        if(blockState.getValue(CovenHutDoorBlock.LOCKED)){
//        }
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, CovenHutDoorBlockEntity covenHutDoorBlockEntity) {
        if(blockState.getValue(CovenHutDoorBlock.LOCKED)){
            level.getEntitiesOfClass(ServerPlayer.class, new AABB(blockPos).inflate(20.0d, 15.0d, 20.0d))
                    .stream()
                    .filter(serverPlayer -> !serverPlayer.isCreative() && !serverPlayer.isSpectator())
                    .collect(Collectors.toList())
                    .forEach(serverPlayer -> serverPlayer.addEffect(FireEffect.getFireEffectInstance(EffectRegistry.HEXFLAME.get(), 2, 3)));
        }
    }
}
