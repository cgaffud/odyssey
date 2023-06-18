package com.bedmen.odyssey.block;

import com.bedmen.odyssey.effect.FireEffect;
import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.Map;

public class HexFireBlock extends BaseFireBlock implements INeedsToRegisterRenderType {
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((p_53467_) -> {
        return p_53467_.getKey() != Direction.DOWN;
    }).collect(Util.toMap());

    public HexFireBlock(BlockBehaviour.Properties p_56653_) {
        super(p_56653_, 0F);
    }

    public boolean canCatchFire(BlockGetter world, BlockPos pos, Direction face) {
        return world.getBlockState(pos).isFlammable(world, pos, face);
    }

    public BlockState updateShape(BlockState p_56659_, Direction p_56660_, BlockState p_56661_, LevelAccessor p_56662_, BlockPos p_56663_, BlockPos p_56664_) {
        return this.canSurvive(p_56659_, p_56662_, p_56663_) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
    }

    private boolean isValidFireLocation(BlockGetter p_53486_, BlockPos p_53487_) {
        for(Direction direction : Direction.values()) {
            if (this.canCatchFire(p_53486_, p_53487_.relative(direction), direction.getOpposite())) {
                return true;
            }
        }
        return false;
    }

    public boolean canSurvive(BlockState p_53454_, LevelReader p_53455_, BlockPos p_53456_) {
        BlockPos blockpos = p_53456_.below();
        return p_53455_.getBlockState(blockpos).isFaceSturdy(p_53455_, blockpos, Direction.UP) || this.isValidFireLocation(p_53455_, p_53456_);
    }

    // Not sure if we should keep this here, but this would have handled hexflame spawning on other surfaces
    public BlockState getStateForPlacement(BlockGetter p_53471_, BlockPos p_53472_) {
//        BlockPos blockpos = p_53472_.below();
//        BlockState blockstate = p_53471_.getBlockState(blockpos);
//        if (!this.canCatchFire(p_53471_, p_53472_, Direction.UP) && !blockstate.isFaceSturdy(p_53471_, blockpos, Direction.UP)) {
//            BlockState blockstate1 = this.defaultBlockState();
//
//            for(Direction direction : Direction.values()) {
//                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(direction);
//                if (booleanproperty != null) {
//                    blockstate1 = blockstate1.setValue(booleanproperty, Boolean.valueOf(this.canCatchFire(p_53471_, p_53472_.relative(direction), direction.getOpposite())));
//                }
//            }
//
//            return blockstate1;
//        } else {
        return this.defaultBlockState();
//        }
    }

    @Override
    protected boolean canBurn(BlockState p_49284_) {
        return false;
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.cutout();
    }

    public void entityInside(BlockState blockState, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity){
            livingEntity.addEffect(FireEffect.getFireEffectInstance(EffectRegistry.HEXFLAME.get(), 16, 0));
        }
    }

}
