package com.bedmen.odyssey.blocks;

import com.bedmen.odyssey.container.OdysseyGrindstoneContainer;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class OdysseyGrindstoneBlock extends HorizontalFaceBlock {
    public static final VoxelShape FLOOR_NORTH_SOUTH_LEFT_POST = Block.box(2.0D, 0.0D, 6.0D, 4.0D, 7.0D, 10.0D);
    public static final VoxelShape FLOOR_NORTH_SOUTH_RIGHT_POST = Block.box(12.0D, 0.0D, 6.0D, 14.0D, 7.0D, 10.0D);
    public static final VoxelShape FLOOR_NORTH_SOUTH_LEFT_PIVOT = Block.box(2.0D, 7.0D, 5.0D, 4.0D, 13.0D, 11.0D);
    public static final VoxelShape FLOOR_NORTH_SOUTH_RIGHT_PIVOT = Block.box(12.0D, 7.0D, 5.0D, 14.0D, 13.0D, 11.0D);
    public static final VoxelShape FLOOR_NORTH_SOUTH_LEFT_LEG;
    public static final VoxelShape FLOOR_NORTH_SOUTH_RIGHT_LEG;
    public static final VoxelShape FLOOR_NORTH_SOUTH_ALL_LEGS;
    public static final VoxelShape FLOOR_NORTH_SOUTH_GRINDSTONE;
    public static final VoxelShape FLOOR_EAST_WEST_LEFT_POST;
    public static final VoxelShape FLOOR_EAST_WEST_RIGHT_POST;
    public static final VoxelShape FLOOR_EAST_WEST_LEFT_PIVOT;
    public static final VoxelShape FLOOR_EAST_WEST_RIGHT_PIVOT;
    public static final VoxelShape FLOOR_EAST_WEST_LEFT_LEG;
    public static final VoxelShape FLOOR_EAST_WEST_RIGHT_LEG;
    public static final VoxelShape FLOOR_EAST_WEST_ALL_LEGS;
    public static final VoxelShape FLOOR_EAST_WEST_GRINDSTONE;
    public static final VoxelShape WALL_SOUTH_LEFT_POST;
    public static final VoxelShape WALL_SOUTH_RIGHT_POST;
    public static final VoxelShape WALL_SOUTH_LEFT_PIVOT;
    public static final VoxelShape WALL_SOUTH_RIGHT_PIVOT;
    public static final VoxelShape WALL_SOUTH_LEFT_LEG;
    public static final VoxelShape WALL_SOUTH_RIGHT_LEG;
    public static final VoxelShape WALL_SOUTH_ALL_LEGS;
    public static final VoxelShape WALL_SOUTH_GRINDSTONE;
    public static final VoxelShape WALL_NORTH_LEFT_POST;
    public static final VoxelShape WALL_NORTH_RIGHT_POST;
    public static final VoxelShape WALL_NORTH_LEFT_PIVOT;
    public static final VoxelShape WALL_NORTH_RIGHT_PIVOT;
    public static final VoxelShape WALL_NORTH_LEFT_LEG;
    public static final VoxelShape WALL_NORTH_RIGHT_LEG;
    public static final VoxelShape WALL_NORTH_ALL_LEGS;
    public static final VoxelShape WALL_NORTH_GRINDSTONE;
    public static final VoxelShape WALL_WEST_LEFT_POST;
    public static final VoxelShape WALL_WEST_RIGHT_POST;
    public static final VoxelShape WALL_WEST_LEFT_PIVOT;
    public static final VoxelShape WALL_WEST_RIGHT_PIVOT;
    public static final VoxelShape WALL_WEST_LEFT_LEG;
    public static final VoxelShape WALL_WEST_RIGHT_LEG;
    public static final VoxelShape WALL_WEST_ALL_LEGS;
    public static final VoxelShape WALL_WEST_GRINDSTONE;
    public static final VoxelShape WALL_EAST_LEFT_POST;
    public static final VoxelShape WALL_EAST_RIGHT_POST;
    public static final VoxelShape WALL_EAST_LEFT_PIVOT;
    public static final VoxelShape WALL_EAST_RIGHT_PIVOT;
    public static final VoxelShape WALL_EAST_LEFT_LEG;
    public static final VoxelShape WALL_EAST_RIGHT_LEG;
    public static final VoxelShape WALL_EAST_ALL_LEGS;
    public static final VoxelShape WALL_EAST_GRINDSTONE;
    public static final VoxelShape CEILING_NORTH_SOUTH_LEFT_POST;
    public static final VoxelShape CEILING_NORTH_SOUTH_RIGHT_POST;
    public static final VoxelShape CEILING_NORTH_SOUTH_LEFT_PIVOT;
    public static final VoxelShape CEILING_NORTH_SOUTH_RIGHT_PIVOT;
    public static final VoxelShape CEILING_NORTH_SOUTH_LEFT_LEG;
    public static final VoxelShape CEILING_NORTH_SOUTH_RIGHT_LEG;
    public static final VoxelShape CEILING_NORTH_SOUTH_ALL_LEGS;
    public static final VoxelShape CEILING_NORTH_SOUTH_GRINDSTONE;
    public static final VoxelShape CEILING_EAST_WEST_LEFT_POST;
    public static final VoxelShape CEILING_EAST_WEST_RIGHT_POST;
    public static final VoxelShape CEILING_EAST_WEST_LEFT_PIVOT;
    public static final VoxelShape CEILING_EAST_WEST_RIGHT_PIVOT;
    public static final VoxelShape CEILING_EAST_WEST_LEFT_LEG;
    public static final VoxelShape CEILING_EAST_WEST_RIGHT_LEG;
    public static final VoxelShape CEILING_EAST_WEST_ALL_LEGS;
    public static final VoxelShape CEILING_EAST_WEST_GRINDSTONE;
    private static final ITextComponent CONTAINER_TITLE;

    public OdysseyGrindstoneBlock() {
        super(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.STONE));
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(FACE, AttachFace.WALL));
    }

    public BlockRenderType getRenderShape(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }

    private VoxelShape getVoxelShape(BlockState p_220186_1_) {
        Direction direction = (Direction)p_220186_1_.getValue(FACING);
        switch((AttachFace)p_220186_1_.getValue(FACE)) {
            case FLOOR:
                if (direction != Direction.NORTH && direction != Direction.SOUTH) {
                    return FLOOR_EAST_WEST_GRINDSTONE;
                }

                return FLOOR_NORTH_SOUTH_GRINDSTONE;
            case WALL:
                if (direction == Direction.NORTH) {
                    return WALL_NORTH_GRINDSTONE;
                } else if (direction == Direction.SOUTH) {
                    return WALL_SOUTH_GRINDSTONE;
                } else {
                    if (direction == Direction.EAST) {
                        return WALL_EAST_GRINDSTONE;
                    }

                    return WALL_WEST_GRINDSTONE;
                }
            case CEILING:
                if (direction != Direction.NORTH && direction != Direction.SOUTH) {
                    return CEILING_EAST_WEST_GRINDSTONE;
                }

                return CEILING_NORTH_SOUTH_GRINDSTONE;
            default:
                return FLOOR_EAST_WEST_GRINDSTONE;
        }
    }

    public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        return this.getVoxelShape(p_220071_1_);
    }

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return this.getVoxelShape(p_220053_1_);
    }

    public boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
        return true;
    }

    public ActionResultType use(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        if (p_225533_2_.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            p_225533_4_.openMenu(p_225533_1_.getMenuProvider(p_225533_2_, p_225533_3_));
            p_225533_4_.awardStat(Stats.INTERACT_WITH_GRINDSTONE);
            return ActionResultType.CONSUME;
        }
    }

    public INamedContainerProvider getMenuProvider(BlockState p_220052_1_, World p_220052_2_, BlockPos p_220052_3_) {
        return new SimpleNamedContainerProvider((p_220187_2_, p_220187_3_, p_220187_4_) -> {
            return new OdysseyGrindstoneContainer(p_220187_2_, p_220187_3_, IWorldPosCallable.create(p_220052_2_, p_220052_3_));
        }, CONTAINER_TITLE);
    }

    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
        return (BlockState)p_185499_1_.setValue(FACING, p_185499_2_.rotate((Direction)p_185499_1_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
        return p_185471_1_.rotate(p_185471_2_.getRotation((Direction)p_185471_1_.getValue(FACING)));
    }

    protected void createBlockStateDefinition(Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(new Property[]{FACING, FACE});
    }

    public boolean isPathfindable(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_, PathType p_196266_4_) {
        return false;
    }

    static {
        FLOOR_NORTH_SOUTH_LEFT_LEG = VoxelShapes.or(FLOOR_NORTH_SOUTH_LEFT_POST, FLOOR_NORTH_SOUTH_LEFT_PIVOT);
        FLOOR_NORTH_SOUTH_RIGHT_LEG = VoxelShapes.or(FLOOR_NORTH_SOUTH_RIGHT_POST, FLOOR_NORTH_SOUTH_RIGHT_PIVOT);
        FLOOR_NORTH_SOUTH_ALL_LEGS = VoxelShapes.or(FLOOR_NORTH_SOUTH_LEFT_LEG, FLOOR_NORTH_SOUTH_RIGHT_LEG);
        FLOOR_NORTH_SOUTH_GRINDSTONE = VoxelShapes.or(FLOOR_NORTH_SOUTH_ALL_LEGS, Block.box(4.0D, 4.0D, 2.0D, 12.0D, 16.0D, 14.0D));
        FLOOR_EAST_WEST_LEFT_POST = Block.box(6.0D, 0.0D, 2.0D, 10.0D, 7.0D, 4.0D);
        FLOOR_EAST_WEST_RIGHT_POST = Block.box(6.0D, 0.0D, 12.0D, 10.0D, 7.0D, 14.0D);
        FLOOR_EAST_WEST_LEFT_PIVOT = Block.box(5.0D, 7.0D, 2.0D, 11.0D, 13.0D, 4.0D);
        FLOOR_EAST_WEST_RIGHT_PIVOT = Block.box(5.0D, 7.0D, 12.0D, 11.0D, 13.0D, 14.0D);
        FLOOR_EAST_WEST_LEFT_LEG = VoxelShapes.or(FLOOR_EAST_WEST_LEFT_POST, FLOOR_EAST_WEST_LEFT_PIVOT);
        FLOOR_EAST_WEST_RIGHT_LEG = VoxelShapes.or(FLOOR_EAST_WEST_RIGHT_POST, FLOOR_EAST_WEST_RIGHT_PIVOT);
        FLOOR_EAST_WEST_ALL_LEGS = VoxelShapes.or(FLOOR_EAST_WEST_LEFT_LEG, FLOOR_EAST_WEST_RIGHT_LEG);
        FLOOR_EAST_WEST_GRINDSTONE = VoxelShapes.or(FLOOR_EAST_WEST_ALL_LEGS, Block.box(2.0D, 4.0D, 4.0D, 14.0D, 16.0D, 12.0D));
        WALL_SOUTH_LEFT_POST = Block.box(2.0D, 6.0D, 0.0D, 4.0D, 10.0D, 7.0D);
        WALL_SOUTH_RIGHT_POST = Block.box(12.0D, 6.0D, 0.0D, 14.0D, 10.0D, 7.0D);
        WALL_SOUTH_LEFT_PIVOT = Block.box(2.0D, 5.0D, 7.0D, 4.0D, 11.0D, 13.0D);
        WALL_SOUTH_RIGHT_PIVOT = Block.box(12.0D, 5.0D, 7.0D, 14.0D, 11.0D, 13.0D);
        WALL_SOUTH_LEFT_LEG = VoxelShapes.or(WALL_SOUTH_LEFT_POST, WALL_SOUTH_LEFT_PIVOT);
        WALL_SOUTH_RIGHT_LEG = VoxelShapes.or(WALL_SOUTH_RIGHT_POST, WALL_SOUTH_RIGHT_PIVOT);
        WALL_SOUTH_ALL_LEGS = VoxelShapes.or(WALL_SOUTH_LEFT_LEG, WALL_SOUTH_RIGHT_LEG);
        WALL_SOUTH_GRINDSTONE = VoxelShapes.or(WALL_SOUTH_ALL_LEGS, Block.box(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 16.0D));
        WALL_NORTH_LEFT_POST = Block.box(2.0D, 6.0D, 7.0D, 4.0D, 10.0D, 16.0D);
        WALL_NORTH_RIGHT_POST = Block.box(12.0D, 6.0D, 7.0D, 14.0D, 10.0D, 16.0D);
        WALL_NORTH_LEFT_PIVOT = Block.box(2.0D, 5.0D, 3.0D, 4.0D, 11.0D, 9.0D);
        WALL_NORTH_RIGHT_PIVOT = Block.box(12.0D, 5.0D, 3.0D, 14.0D, 11.0D, 9.0D);
        WALL_NORTH_LEFT_LEG = VoxelShapes.or(WALL_NORTH_LEFT_POST, WALL_NORTH_LEFT_PIVOT);
        WALL_NORTH_RIGHT_LEG = VoxelShapes.or(WALL_NORTH_RIGHT_POST, WALL_NORTH_RIGHT_PIVOT);
        WALL_NORTH_ALL_LEGS = VoxelShapes.or(WALL_NORTH_LEFT_LEG, WALL_NORTH_RIGHT_LEG);
        WALL_NORTH_GRINDSTONE = VoxelShapes.or(WALL_NORTH_ALL_LEGS, Block.box(4.0D, 2.0D, 0.0D, 12.0D, 14.0D, 12.0D));
        WALL_WEST_LEFT_POST = Block.box(7.0D, 6.0D, 2.0D, 16.0D, 10.0D, 4.0D);
        WALL_WEST_RIGHT_POST = Block.box(7.0D, 6.0D, 12.0D, 16.0D, 10.0D, 14.0D);
        WALL_WEST_LEFT_PIVOT = Block.box(3.0D, 5.0D, 2.0D, 9.0D, 11.0D, 4.0D);
        WALL_WEST_RIGHT_PIVOT = Block.box(3.0D, 5.0D, 12.0D, 9.0D, 11.0D, 14.0D);
        WALL_WEST_LEFT_LEG = VoxelShapes.or(WALL_WEST_LEFT_POST, WALL_WEST_LEFT_PIVOT);
        WALL_WEST_RIGHT_LEG = VoxelShapes.or(WALL_WEST_RIGHT_POST, WALL_WEST_RIGHT_PIVOT);
        WALL_WEST_ALL_LEGS = VoxelShapes.or(WALL_WEST_LEFT_LEG, WALL_WEST_RIGHT_LEG);
        WALL_WEST_GRINDSTONE = VoxelShapes.or(WALL_WEST_ALL_LEGS, Block.box(0.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D));
        WALL_EAST_LEFT_POST = Block.box(0.0D, 6.0D, 2.0D, 9.0D, 10.0D, 4.0D);
        WALL_EAST_RIGHT_POST = Block.box(0.0D, 6.0D, 12.0D, 9.0D, 10.0D, 14.0D);
        WALL_EAST_LEFT_PIVOT = Block.box(7.0D, 5.0D, 2.0D, 13.0D, 11.0D, 4.0D);
        WALL_EAST_RIGHT_PIVOT = Block.box(7.0D, 5.0D, 12.0D, 13.0D, 11.0D, 14.0D);
        WALL_EAST_LEFT_LEG = VoxelShapes.or(WALL_EAST_LEFT_POST, WALL_EAST_LEFT_PIVOT);
        WALL_EAST_RIGHT_LEG = VoxelShapes.or(WALL_EAST_RIGHT_POST, WALL_EAST_RIGHT_PIVOT);
        WALL_EAST_ALL_LEGS = VoxelShapes.or(WALL_EAST_LEFT_LEG, WALL_EAST_RIGHT_LEG);
        WALL_EAST_GRINDSTONE = VoxelShapes.or(WALL_EAST_ALL_LEGS, Block.box(4.0D, 2.0D, 4.0D, 16.0D, 14.0D, 12.0D));
        CEILING_NORTH_SOUTH_LEFT_POST = Block.box(2.0D, 9.0D, 6.0D, 4.0D, 16.0D, 10.0D);
        CEILING_NORTH_SOUTH_RIGHT_POST = Block.box(12.0D, 9.0D, 6.0D, 14.0D, 16.0D, 10.0D);
        CEILING_NORTH_SOUTH_LEFT_PIVOT = Block.box(2.0D, 3.0D, 5.0D, 4.0D, 9.0D, 11.0D);
        CEILING_NORTH_SOUTH_RIGHT_PIVOT = Block.box(12.0D, 3.0D, 5.0D, 14.0D, 9.0D, 11.0D);
        CEILING_NORTH_SOUTH_LEFT_LEG = VoxelShapes.or(CEILING_NORTH_SOUTH_LEFT_POST, CEILING_NORTH_SOUTH_LEFT_PIVOT);
        CEILING_NORTH_SOUTH_RIGHT_LEG = VoxelShapes.or(CEILING_NORTH_SOUTH_RIGHT_POST, CEILING_NORTH_SOUTH_RIGHT_PIVOT);
        CEILING_NORTH_SOUTH_ALL_LEGS = VoxelShapes.or(CEILING_NORTH_SOUTH_LEFT_LEG, CEILING_NORTH_SOUTH_RIGHT_LEG);
        CEILING_NORTH_SOUTH_GRINDSTONE = VoxelShapes.or(CEILING_NORTH_SOUTH_ALL_LEGS, Block.box(4.0D, 0.0D, 2.0D, 12.0D, 12.0D, 14.0D));
        CEILING_EAST_WEST_LEFT_POST = Block.box(6.0D, 9.0D, 2.0D, 10.0D, 16.0D, 4.0D);
        CEILING_EAST_WEST_RIGHT_POST = Block.box(6.0D, 9.0D, 12.0D, 10.0D, 16.0D, 14.0D);
        CEILING_EAST_WEST_LEFT_PIVOT = Block.box(5.0D, 3.0D, 2.0D, 11.0D, 9.0D, 4.0D);
        CEILING_EAST_WEST_RIGHT_PIVOT = Block.box(5.0D, 3.0D, 12.0D, 11.0D, 9.0D, 14.0D);
        CEILING_EAST_WEST_LEFT_LEG = VoxelShapes.or(CEILING_EAST_WEST_LEFT_POST, CEILING_EAST_WEST_LEFT_PIVOT);
        CEILING_EAST_WEST_RIGHT_LEG = VoxelShapes.or(CEILING_EAST_WEST_RIGHT_POST, CEILING_EAST_WEST_RIGHT_PIVOT);
        CEILING_EAST_WEST_ALL_LEGS = VoxelShapes.or(CEILING_EAST_WEST_LEFT_LEG, CEILING_EAST_WEST_RIGHT_LEG);
        CEILING_EAST_WEST_GRINDSTONE = VoxelShapes.or(CEILING_EAST_WEST_ALL_LEGS, Block.box(2.0D, 0.0D, 4.0D, 14.0D, 12.0D, 12.0D));
        CONTAINER_TITLE = new TranslationTextComponent("container.grindstone_title");
    }
}
