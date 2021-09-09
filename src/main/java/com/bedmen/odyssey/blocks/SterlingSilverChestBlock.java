package com.bedmen.odyssey.blocks;

import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.SoundEventRegistry;
import com.bedmen.odyssey.registry.TileEntityTypeRegistry;
import com.bedmen.odyssey.tileentity.SterlingSilverChestTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.tileentity.TrappedChestTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class SterlingSilverChestBlock extends ChestBlock {

    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;

    public SterlingSilverChestBlock(AbstractBlock.Properties p_i48306_1_) {
        super(p_i48306_1_, TileEntityTypeRegistry.STERLING_SILVER_CHEST::get);
        this.registerDefaultState(this.stateDefinition.any().setValue(LOCKED, Boolean.FALSE));
    }

    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new SterlingSilverChestTileEntity();
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(LOCKED);
        super.createBlockStateDefinition(stateBuilder);
    }

    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult p_225533_6_) {

        if(blockState.getValue(LOCKED)){
            ItemStack itemStack = playerEntity.getItemInHand(hand);
            if(itemStack.getItem() == ItemRegistry.STERLING_SILVER_KEY.get()){
                world.setBlock(blockPos, blockState.setValue(LOCKED, Boolean.FALSE), 3);
                if (blockState.getValue(TYPE) != ChestType.SINGLE) {
                    switch(getConnectedDirection(blockState)) {
                        case NORTH:
                        default:
                            world.setBlock(blockPos.north(), world.getBlockState(blockPos.north()).setValue(LOCKED, Boolean.FALSE), 3);
                            break;
                        case SOUTH:
                            world.setBlock(blockPos.south(), world.getBlockState(blockPos.south()).setValue(LOCKED, Boolean.FALSE), 3);
                            break;
                        case WEST:
                            world.setBlock(blockPos.west(), world.getBlockState(blockPos.west()).setValue(LOCKED, Boolean.FALSE), 3);
                            break;
                        case EAST:
                            world.setBlock(blockPos.east(), world.getBlockState(blockPos.east()).setValue(LOCKED, Boolean.FALSE), 3);
                            break;
                    }
                }
                world.playSound(playerEntity, blockPos, SoundEventRegistry.KEY_UNLOCK.get(), SoundCategory.BLOCKS, 1.0f, 1.0f);
                itemStack.shrink(1);
            }
        }
        if(!blockState.getValue(LOCKED)){
            if (world.isClientSide) {
                return ActionResultType.SUCCESS;
            } else {
                INamedContainerProvider inamedcontainerprovider = this.getMenuProvider(blockState, world, blockPos);
                if (inamedcontainerprovider != null) {
                    playerEntity.openMenu(inamedcontainerprovider);
                    playerEntity.awardStat(this.getOpenChestStat());
                    PiglinTasks.angerNearbyPiglins(playerEntity, true);
                }

                return ActionResultType.CONSUME;
            }
        } else {
            world.playSound(playerEntity, blockPos, SoundEventRegistry.LOCKED_CHEST.get(), SoundCategory.BLOCKS, 1.0f, 1.0f);
            return ActionResultType.FAIL;
        }
    }

    public void onRemove(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean p_196243_5_) {
        if (!blockState.is(blockState2.getBlock())) {
            TileEntity tileentity = world.getBlockEntity(blockPos);
            if (tileentity instanceof IInventory && !blockState.getValue(LOCKED)) {
                InventoryHelper.dropContents(world, blockPos, (IInventory)tileentity);
                world.updateNeighbourForOutputSignal(blockPos, this);
            }

            if (blockState.hasTileEntity() && (!blockState.is(blockState2.getBlock()) || !blockState2.hasTileEntity())) {
                world.removeBlockEntity(blockPos);
            }
        }
    }
}