package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.armor.OdysseyArmorMaterial;
import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.util.OdysseyRarity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HollowCoconutItem extends EquipmentArmorItem {
    private final Block block = BlockRegistry.HOLLOW_COCONUT.get();

    public HollowCoconutItem(OdysseyArmorMaterial armorMaterial, EquipmentSlotType slotType, Properties properties, LevEnchSup... levEnchSups) {
        super(armorMaterial, slotType, properties.rarity(OdysseyRarity.EQUIPMENT), levEnchSups);
    }

    public ActionResultType useOn(ItemUseContext p_195939_1_) {
        ActionResultType actionresulttype = this.place(new BlockItemUseContext(p_195939_1_));
        return !actionresulttype.consumesAction() && this.isEdible() ? this.use(p_195939_1_.getLevel(), p_195939_1_.getPlayer(), p_195939_1_.getHand()).getResult() : actionresulttype;
    }

    public ActionResultType place(BlockItemUseContext p_195942_1_) {
        if (!p_195942_1_.canPlace()) {
            return ActionResultType.FAIL;
        } else {
            BlockItemUseContext blockitemusecontext = this.updatePlacementContext(p_195942_1_);
            if (blockitemusecontext == null) {
                return ActionResultType.FAIL;
            } else {
                BlockState blockstate = this.getPlacementState(blockitemusecontext);
                if (blockstate == null) {
                    return ActionResultType.FAIL;
                } else if (!this.placeBlock(blockitemusecontext, blockstate)) {
                    return ActionResultType.FAIL;
                } else {
                    BlockPos blockpos = blockitemusecontext.getClickedPos();
                    World world = blockitemusecontext.getLevel();
                    PlayerEntity playerentity = blockitemusecontext.getPlayer();
                    ItemStack itemstack = blockitemusecontext.getItemInHand();
                    BlockState blockstate1 = world.getBlockState(blockpos);
                    Block block = blockstate1.getBlock();
                    if (block == blockstate.getBlock()) {
                        blockstate1 = this.updateBlockStateFromTag(blockpos, world, itemstack, blockstate1);
                        this.updateCustomBlockEntityTag(blockpos, world, playerentity, itemstack, blockstate1);
                        block.setPlacedBy(world, blockpos, blockstate1, playerentity, itemstack);
                        if (playerentity instanceof ServerPlayerEntity) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerentity, blockpos, itemstack);
                        }
                    }

                    SoundType soundtype = blockstate1.getSoundType(world, blockpos, p_195942_1_.getPlayer());
                    world.playSound(playerentity, blockpos, this.getPlaceSound(blockstate1, world, blockpos, p_195942_1_.getPlayer()), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    if (playerentity == null || !playerentity.abilities.instabuild) {
                        itemstack.shrink(1);
                    }

                    return ActionResultType.sidedSuccess(world.isClientSide);
                }
            }
        }
    }

    @Deprecated //Forge: Use more sensitive version {@link BlockItem#getPlaceSound(BlockState, IBlockReader, BlockPos, Entity) }
    protected SoundEvent getPlaceSound(BlockState p_219983_1_) {
        return p_219983_1_.getSoundType().getPlaceSound();
    }

    //Forge: Sensitive version of BlockItem#getPlaceSound
    protected SoundEvent getPlaceSound(BlockState state, World world, BlockPos pos, PlayerEntity entity) {
        return state.getSoundType(world, pos, entity).getPlaceSound();
    }

    @Nullable
    public BlockItemUseContext updatePlacementContext(BlockItemUseContext p_219984_1_) {
        return p_219984_1_;
    }

    protected boolean updateCustomBlockEntityTag(BlockPos p_195943_1_, World p_195943_2_, @Nullable PlayerEntity p_195943_3_, ItemStack p_195943_4_, BlockState p_195943_5_) {
        return updateCustomBlockEntityTag(p_195943_2_, p_195943_3_, p_195943_1_, p_195943_4_);
    }

    @Nullable
    protected BlockState getPlacementState(BlockItemUseContext p_195945_1_) {
        BlockState blockstate = this.getBlock().getStateForPlacement(p_195945_1_);
        return blockstate != null && this.canPlace(p_195945_1_, blockstate) ? blockstate : null;
    }

    private BlockState updateBlockStateFromTag(BlockPos p_219985_1_, World p_219985_2_, ItemStack p_219985_3_, BlockState p_219985_4_) {
        BlockState blockstate = p_219985_4_;
        CompoundNBT compoundnbt = p_219985_3_.getTag();
        if (compoundnbt != null) {
            CompoundNBT compoundnbt1 = compoundnbt.getCompound("BlockStateTag");
            StateContainer<Block, BlockState> statecontainer = p_219985_4_.getBlock().getStateDefinition();

            for(String s : compoundnbt1.getAllKeys()) {
                Property<?> property = statecontainer.getProperty(s);
                if (property != null) {
                    String s1 = compoundnbt1.get(s).getAsString();
                    blockstate = updateState(blockstate, property, s1);
                }
            }
        }

        if (blockstate != p_219985_4_) {
            p_219985_2_.setBlock(p_219985_1_, blockstate, 2);
        }

        return blockstate;
    }

    private static <T extends Comparable<T>> BlockState updateState(BlockState p_219988_0_, Property<T> p_219988_1_, String p_219988_2_) {
        return p_219988_1_.getValue(p_219988_2_).map((p_219986_2_) -> {
            return p_219988_0_.setValue(p_219988_1_, p_219986_2_);
        }).orElse(p_219988_0_);
    }

    protected boolean canPlace(BlockItemUseContext p_195944_1_, BlockState p_195944_2_) {
        PlayerEntity playerentity = p_195944_1_.getPlayer();
        ISelectionContext iselectioncontext = playerentity == null ? ISelectionContext.empty() : ISelectionContext.of(playerentity);
        return (!this.mustSurvive() || p_195944_2_.canSurvive(p_195944_1_.getLevel(), p_195944_1_.getClickedPos())) && p_195944_1_.getLevel().isUnobstructed(p_195944_2_, p_195944_1_.getClickedPos(), iselectioncontext);
    }

    protected boolean mustSurvive() {
        return true;
    }

    protected boolean placeBlock(BlockItemUseContext p_195941_1_, BlockState p_195941_2_) {
        return p_195941_1_.getLevel().setBlock(p_195941_1_.getClickedPos(), p_195941_2_, 11);
    }

    public static boolean updateCustomBlockEntityTag(World p_179224_0_, @Nullable PlayerEntity p_179224_1_, BlockPos p_179224_2_, ItemStack p_179224_3_) {
        MinecraftServer minecraftserver = p_179224_0_.getServer();
        if (minecraftserver == null) {
            return false;
        } else {
            CompoundNBT compoundnbt = p_179224_3_.getTagElement("BlockEntityTag");
            if (compoundnbt != null) {
                TileEntity tileentity = p_179224_0_.getBlockEntity(p_179224_2_);
                if (tileentity != null) {
                    if (!p_179224_0_.isClientSide && tileentity.onlyOpCanSetNbt() && (p_179224_1_ == null || !p_179224_1_.canUseGameMasterBlocks())) {
                        return false;
                    }

                    CompoundNBT compoundnbt1 = tileentity.save(new CompoundNBT());
                    CompoundNBT compoundnbt2 = compoundnbt1.copy();
                    compoundnbt1.merge(compoundnbt);
                    compoundnbt1.putInt("x", p_179224_2_.getX());
                    compoundnbt1.putInt("y", p_179224_2_.getY());
                    compoundnbt1.putInt("z", p_179224_2_.getZ());
                    if (!compoundnbt1.equals(compoundnbt2)) {
                        tileentity.load(p_179224_0_.getBlockState(p_179224_2_), compoundnbt1);
                        tileentity.setChanged();
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public String getDescriptionId() {
        return this.getBlock().getDescriptionId();
    }

    public Block getBlock() {
        return this.getBlockRaw() == null ? null : this.getBlockRaw().delegate.get();
    }

    private Block getBlockRaw() {
        return this.block;
    }

    public boolean isEnderMask(ItemStack stack, PlayerEntity player, EndermanEntity endermanEntity)
    {
        return true;
    }
}
