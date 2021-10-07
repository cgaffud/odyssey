package com.bedmen.odyssey.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;

public class StripableLogBlock extends FlammableLogBlock {

    private final RegistryObject<Block> strippedBlock;
    public StripableLogBlock(Properties p_i48339_1_, RegistryObject<Block> strippedBlock) {
        super(p_i48339_1_);
        this.strippedBlock = strippedBlock;
    }

    @Nullable
    public BlockState getToolModifiedState(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack stack, ToolType toolType)
    {
        if (toolType == ToolType.AXE){
            return strippedBlock.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
        }
        return null;
    }
}
