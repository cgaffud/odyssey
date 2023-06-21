package com.bedmen.odyssey.items;

import com.bedmen.odyssey.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.item.Item.Properties;

public class WeaverEggItem extends Item {
    public WeaverEggItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos blockpos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockpos);
        if(blockState.is(Blocks.COBWEB)){
            level.setBlock(blockpos, BlockRegistry.WEAVER_EGG_COBWEB.get().defaultBlockState(), 2);
            useOnContext.getItemInHand().shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
