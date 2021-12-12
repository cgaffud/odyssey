package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.block.HollowCoconutBlock;
import com.bedmen.odyssey.entity.item.FallingHollowCoconut;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class HollowCoconutBlockEntity extends BlockEntity {

    public HollowCoconutBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.HOLLOW_COCONUT.get(), blockPos, blockState);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, HollowCoconutBlockEntity hollowCoconutBlockEntity) {
        if(blockState.getValue(HollowCoconutBlock.HANGING)){
            AABB axisAlignedBB = HollowCoconutBlock.SHAPE_HANGING.bounds().move(blockPos).move(0.0d,-3.0d, 0.0d).inflate(0.0d, 2.0d, 0.0d);
            List<Player> playerEntityList = level.getEntitiesOfClass(Player.class, axisAlignedBB);
            if(!playerEntityList.isEmpty()){
                FallingBlockEntity fallingblockentity = new FallingHollowCoconut(level, (double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, level.getBlockState(blockPos).setValue(HollowCoconutBlock.HANGING, Boolean.FALSE));
                level.addFreshEntity(fallingblockentity);
            }
        }
    }
}
