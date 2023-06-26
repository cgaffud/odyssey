package com.bedmen.odyssey.block;

import com.bedmen.odyssey.entity.animal.PassiveWeaver;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class WeaverEggWebBlock extends WebBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;

    public WeaverEggWebBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    public boolean isRandomlyTicking(BlockState blockState) {
        return true;
    }

    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        int i = blockState.getValue(AGE);
        if (i < 2 && serverLevel.random.nextInt(5) == 0) {
            serverLevel.setBlock(blockPos, blockState.setValue(AGE, i + 1), 2);
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(serverLevel, blockPos, blockState);
        } else if (i == 2){
            serverLevel.setBlock(blockPos, Blocks.COBWEB.defaultBlockState(), 2);
            serverLevel.levelEvent(2001, blockPos, Block.getId(blockState));
            PassiveWeaver passiveWeaver = EntityTypeRegistry.PASSIVE_WEAVER.get().create(serverLevel);
            passiveWeaver.setAge(-24000);
            passiveWeaver.moveTo((double)blockPos.getX() + 0.5D, blockPos.getY(), (double)blockPos.getZ() + 0.5D, 0.0F, 0.0F);
            serverLevel.addFreshEntity(passiveWeaver);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
