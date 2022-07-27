package com.bedmen.odyssey.items;

import com.bedmen.odyssey.block.wood.GreatSaplingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;

public class GreatSaplingHelperItem extends Item {
    protected final Lazy<Block> saplingBlock;
    protected final boolean isBottle;

    public GreatSaplingHelperItem(Properties properties, Lazy<Block> saplingBlock, boolean isBottle) {
        super(properties);
        this.saplingBlock = saplingBlock;
        this.isBottle = isBottle;
    }

    private GreatSaplingBlock.Status getStatus(){
        return this.isBottle ? GreatSaplingBlock.Status.THIRSTY : GreatSaplingBlock.Status.HUNGRY;
    }

    private SoundEvent getSound(){
        return this.isBottle ? SoundEvents.GENERIC_SPLASH : SoundEvents.BONE_MEAL_USE;
    }

    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.is(this.saplingBlock.get()) && blockstate.getValue(GreatSaplingBlock.STATUS) == this.getStatus()) {
            level.playSound(player, blockpos, getSound(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            if (!level.isClientSide) {
                GreatSaplingBlock greatSaplingBlock = (GreatSaplingBlock) blockstate.getBlock();
                greatSaplingBlock.advanceAge((ServerLevel) level, blockpos, level.getBlockState(blockpos));
            }
            GreatSaplingBlock.greenParticles(level, blockpos);
            if (player != null) {
                player.setItemInHand(context.getHand(), emptyOrUseItem(context.getItemInHand(), player));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.FAIL;
        }
    }

    public ItemStack emptyOrUseItem(ItemStack itemStack, Player player) {
        if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
            ItemStack emptyStack = this.isBottle ? Items.GLASS_BOTTLE.getDefaultInstance() : ItemStack.EMPTY;
            if (itemStack.isEmpty()) {
                return emptyStack;
            } else {
                if (!player.getInventory().add(emptyStack)) {
                    player.drop(emptyStack, false);
                }
                return itemStack;
            }
        }
        return itemStack;
    }
}
