package com.bedmen.odyssey.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nullable;
import java.util.Locale;

public class TriplePlantBlock extends TransparentBushBlock {
    public static final EnumProperty<TripleBlockThird> THIRD = OdysseyBlockStateProperties.TRIPLE_BLOCK_THIRD;

    public TriplePlantBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(THIRD, TripleBlockThird.LOWER));
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        TripleBlockThird tripleBlockThird = blockState.getValue(THIRD);
        if (direction.getAxis() != Direction.Axis.Y || tripleBlockThird == TripleBlockThird.LOWER != (direction == Direction.UP) || (blockState1.is(this) && blockState1.getValue(THIRD) != tripleBlockThird)) {
            return tripleBlockThird == TripleBlockThird.LOWER && direction == Direction.DOWN && !blockState.canSurvive(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, blockState1, levelAccessor, blockPos, blockPos1);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockPos blockpos = blockPlaceContext.getClickedPos();
        Level level = blockPlaceContext.getLevel();
        return blockpos.getY() < level.getMaxBuildHeight() - 2 && level.getBlockState(blockpos.above()).canBeReplaced(blockPlaceContext) && level.getBlockState(blockpos.above(2)).canBeReplaced(blockPlaceContext) ? super.getStateForPlacement(blockPlaceContext) : null;
    }

    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        blockPos = blockPos.above();
        level.setBlock(blockPos, copyWaterloggedFrom(level, blockPos, this.defaultBlockState().setValue(THIRD, TripleBlockThird.MIDDLE)), 3);
        blockPos = blockPos.above();
        level.setBlock(blockPos, copyWaterloggedFrom(level, blockPos, this.defaultBlockState().setValue(THIRD, TripleBlockThird.UPPER)), 3);
    }

    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        TripleBlockThird tripleBlockThird = blockState.getValue(THIRD);
        if (tripleBlockThird == TripleBlockThird.LOWER) {
            return super.canSurvive(blockState, levelReader, blockPos);
        } else {
            BlockState blockstate = levelReader.getBlockState(blockPos.below());
            if (blockState.getBlock() != this) return super.canSurvive(blockState, levelReader, blockPos); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return blockstate.is(this) && blockstate.getValue(THIRD) == tripleBlockThird.getBelow();
        }
    }

    public static void placeAt(LevelAccessor levelAccessor, BlockState blockState, BlockPos blockPos, int i) {
        levelAccessor.setBlock(blockPos, copyWaterloggedFrom(levelAccessor, blockPos, blockState.setValue(THIRD, TripleBlockThird.LOWER)), i);
        blockPos = blockPos.above();
        levelAccessor.setBlock(blockPos, copyWaterloggedFrom(levelAccessor, blockPos, blockState.setValue(THIRD, TripleBlockThird.MIDDLE)), i);
        blockPos = blockPos.above();
        levelAccessor.setBlock(blockPos, copyWaterloggedFrom(levelAccessor, blockPos, blockState.setValue(THIRD, TripleBlockThird.UPPER)), i);
    }

    public static BlockState copyWaterloggedFrom(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? blockState.setValue(BlockStateProperties.WATERLOGGED, levelReader.isWaterAt(blockPos)) : blockState;
    }

    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (!level.isClientSide) {
            if (player.isCreative()) {
                preventCreativeDropFromBottomPart(level, blockPos, blockState, player);
            } else {
                dropResources(blockState, level, blockPos, (BlockEntity)null, player, player.getMainHandItem());
            }
        }

        super.playerWillDestroy(level, blockPos, blockState, player);
    }

    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        super.playerDestroy(level, player, blockPos, Blocks.AIR.defaultBlockState(), blockEntity, itemStack);
    }

    protected static void preventCreativeDropFromBottomPart(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        TripleBlockThird tripleBlockThird = blockState.getValue(THIRD);
        if (tripleBlockThird.isUpperHalf()) {
            BlockPos blockpos = blockPos.below();
            BlockState blockstate = level.getBlockState(blockpos);
            if (blockstate.is(blockState.getBlock()) && blockstate.getValue(THIRD) == tripleBlockThird.getBelow()) {
                BlockState blockstate1 = blockstate.hasProperty(BlockStateProperties.WATERLOGGED) && blockstate.getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level.setBlock(blockpos, blockstate1, 35);
                level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
            }
            if(tripleBlockThird == TripleBlockThird.UPPER){
                preventCreativeDropFromBottomPart(level, blockpos, blockstate, player);
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(THIRD);
    }

    public BlockBehaviour.OffsetType getOffsetType() {
        return BlockBehaviour.OffsetType.XZ;
    }

    public long getSeed(BlockState blockState, BlockPos blockPos) {
        TripleBlockThird tripleBlockThird = blockState.getValue(THIRD);
        return Mth.getSeed(blockPos.getX(), blockPos.below(tripleBlockThird == TripleBlockThird.LOWER ? 0 : (tripleBlockThird == TripleBlockThird.MIDDLE ? 1 : 2)).getY(), blockPos.getZ());
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face)
    {
        return 60;
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face)
    {
        return 100;
    }

    @Override
    public net.minecraftforge.common.PlantType getPlantType(BlockGetter world, BlockPos pos) {
        return PlantType.PLAINS;
    }

    public enum TripleBlockThird implements StringRepresentable {
        UPPER,
        MIDDLE,
        LOWER;

        public String toString() {
            return this.getSerializedName();
        }

        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public TripleBlockThird getBelow(){
            return this == UPPER ? MIDDLE : LOWER;
        }

        public TripleBlockThird getAbove(){
            return this == LOWER ? MIDDLE : UPPER;
        }

        public boolean isUpperHalf(){
            return this != LOWER;
        }

        public boolean isLowerHalf(){
            return this != UPPER;
        }
    }
}
