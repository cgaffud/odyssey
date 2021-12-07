package com.bedmen.odyssey.block.wood;


import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.fmllegacy.RegistryObject;

import javax.annotation.Nullable;

public class StripableLogBlock extends FlammableLogBlock {

    private final RegistryObject<Block> strippedBlock;
    public StripableLogBlock(Properties p_i48339_1_, RegistryObject<Block> strippedBlock) {
        super(p_i48339_1_);
        this.strippedBlock = strippedBlock;
    }

    @Nullable
    public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction toolAction)
    {
        if (toolAction == ToolActions.AXE_STRIP){
            return strippedBlock.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
        }
        return null;
    }
}
