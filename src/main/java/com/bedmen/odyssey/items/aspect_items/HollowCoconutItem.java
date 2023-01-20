package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.combat.OdysseyArmorMaterial;
import com.bedmen.odyssey.items.aspect_items.AspectArmorItem;
import com.bedmen.odyssey.registry.BlockRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;

import javax.annotation.Nullable;
import java.util.List;

public class HollowCoconutItem extends AspectArmorItem {
    private final Block block = BlockRegistry.HOLLOW_COCONUT.get();

    public HollowCoconutItem(Properties properties, OdysseyArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList) {
        super(properties, armorMaterial, equipmentSlot, abilityList, innateModifierList);
    }

    public InteractionResult useOn(UseOnContext p_195939_1_) {
        InteractionResult actionresulttype = this.place(new BlockPlaceContext(p_195939_1_));
        return !actionresulttype.consumesAction() && this.isEdible() ? this.use(p_195939_1_.getLevel(), p_195939_1_.getPlayer(), p_195939_1_.getHand()).getResult() : actionresulttype;
    }

    public InteractionResult place(BlockPlaceContext p_40577_) {
        if (!p_40577_.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext blockplacecontext = this.updatePlacementContext(p_40577_);
            if (blockplacecontext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState blockstate = this.getPlacementState(blockplacecontext);
                if (blockstate == null) {
                    return InteractionResult.FAIL;
                } else if (!this.placeBlock(blockplacecontext, blockstate)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos blockpos = blockplacecontext.getClickedPos();
                    Level level = blockplacecontext.getLevel();
                    Player player = blockplacecontext.getPlayer();
                    ItemStack itemstack = blockplacecontext.getItemInHand();
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    if (blockstate1.is(blockstate.getBlock())) {
                        blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
                        this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
                        blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
                        }
                    }

                    level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
                    SoundType soundtype = blockstate1.getSoundType(level, blockpos, p_40577_.getPlayer());
                    level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, p_40577_.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    if (player == null || !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
    }

    @Nullable
    public static CompoundTag getBlockEntityData(ItemStack p_186337_) {
        return p_186337_.getTagElement("BlockEntityTag");
    }

    protected boolean updateCustomBlockEntityTag(BlockPos p_40597_, Level p_40598_, @Nullable Player p_40599_, ItemStack p_40600_, BlockState p_40601_) {
        return updateCustomBlockEntityTag(p_40598_, p_40599_, p_40597_, p_40600_);
    }

    public static boolean updateCustomBlockEntityTag(Level p_40583_, @Nullable Player p_40584_, BlockPos p_40585_, ItemStack p_40586_) {
        MinecraftServer minecraftserver = p_40583_.getServer();
        if (minecraftserver == null) {
            return false;
        } else {
            CompoundTag compoundtag = getBlockEntityData(p_40586_);
            if (compoundtag != null) {
                BlockEntity blockentity = p_40583_.getBlockEntity(p_40585_);
                if (blockentity != null) {
                    if (!p_40583_.isClientSide && blockentity.onlyOpCanSetNbt() && (p_40584_ == null || !p_40584_.canUseGameMasterBlocks())) {
                        return false;
                    }

                    CompoundTag compoundtag1 = blockentity.saveWithoutMetadata();
                    CompoundTag compoundtag2 = compoundtag1.copy();
                    compoundtag1.merge(compoundtag);
                    if (!compoundtag1.equals(compoundtag2)) {
                        blockentity.load(compoundtag1);
                        blockentity.setChanged();
                        return true;
                    }
                }
            }

            return false;
        }
    }

    @Deprecated //Forge: Use more sensitive version {@link BlockItem#getPlaceSound(BlockState, IBlockReader, BlockPos, Entity) }
    protected SoundEvent getPlaceSound(BlockState p_219983_1_) {
        return p_219983_1_.getSoundType().getPlaceSound();
    }

    //Forge: Sensitive version of BlockItem#getPlaceSound
    protected SoundEvent getPlaceSound(BlockState state, Level world, BlockPos pos, Player entity) {
        return state.getSoundType(world, pos, entity).getPlaceSound();
    }

    @Nullable
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext p_219984_1_) {
        return p_219984_1_;
    }

    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext p_195945_1_) {
        BlockState blockstate = this.getBlock().getStateForPlacement(p_195945_1_);
        return blockstate != null && this.canPlace(p_195945_1_, blockstate) ? blockstate : null;
    }

    private BlockState updateBlockStateFromTag(BlockPos p_40603_, Level p_40604_, ItemStack p_40605_, BlockState p_40606_) {
        BlockState blockstate = p_40606_;
        CompoundTag compoundtag = p_40605_.getTag();
        if (compoundtag != null) {
            CompoundTag compoundtag1 = compoundtag.getCompound("BlockStateTag");
            StateDefinition<Block, BlockState> statedefinition = p_40606_.getBlock().getStateDefinition();

            for(String s : compoundtag1.getAllKeys()) {
                Property<?> property = statedefinition.getProperty(s);
                if (property != null) {
                    String s1 = compoundtag1.get(s).getAsString();
                    blockstate = updateState(blockstate, property, s1);
                }
            }
        }

        if (blockstate != p_40606_) {
            p_40604_.setBlock(p_40603_, blockstate, 2);
        }

        return blockstate;
    }

    private static <T extends Comparable<T>> BlockState updateState(BlockState p_219988_0_, Property<T> p_219988_1_, String p_219988_2_) {
        return p_219988_1_.getValue(p_219988_2_).map((p_219986_2_) -> {
            return p_219988_0_.setValue(p_219988_1_, p_219986_2_);
        }).orElse(p_219988_0_);
    }

    protected boolean canPlace(BlockPlaceContext p_195944_1_, BlockState p_195944_2_) {
        Player playerentity = p_195944_1_.getPlayer();
        CollisionContext iselectioncontext = playerentity == null ? CollisionContext.empty() : CollisionContext.of(playerentity);
        return (!this.mustSurvive() || p_195944_2_.canSurvive(p_195944_1_.getLevel(), p_195944_1_.getClickedPos())) && p_195944_1_.getLevel().isUnobstructed(p_195944_2_, p_195944_1_.getClickedPos(), iselectioncontext);
    }

    protected boolean mustSurvive() {
        return true;
    }

    protected boolean placeBlock(BlockPlaceContext p_195941_1_, BlockState p_195941_2_) {
        return p_195941_1_.getLevel().setBlock(p_195941_1_.getClickedPos(), p_195941_2_, 11);
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

    public boolean isEnderMask(ItemStack stack, Player player, EnderMan endermanEntity)
    {
        return true;
    }
}
