package com.bedmen.odyssey.items;

import com.bedmen.odyssey.blocks.AbstractWarpingFireBlock;
import com.bedmen.odyssey.util.EnchantmentRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NewFlintAndSteelItem extends Item {
    public NewFlintAndSteelItem(Item.Properties builder) {
        super(builder);
    }

    public int getItemEnchantability() {
        return 10;
    }

    /**
     * Called when this item is used when targetting a Block
     */
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity playerentity = context.getPlayer();
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (CampfireBlock.canBeLit(blockstate)) {
            world.playSound(playerentity, blockpos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
            world.setBlockState(blockpos, blockstate.with(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
            if (playerentity != null) {
                context.getItem().damageItem(1, playerentity, (player) -> {
                    player.sendBreakAnimation(context.getHand());
                });
            }

            return ActionResultType.func_233537_a_(world.isRemote());
        } else {
            BlockPos blockpos1 = blockpos.offset(context.getFace());
            ItemStack itemstack = context.getItem();
            int i = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.WARPING.get(), itemstack);
            if(i == 1){
                if (AbstractWarpingFireBlock.canLightBlock(world, blockpos1, context.getPlacementHorizontalFacing())) {
                    world.playSound(playerentity, blockpos1, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
                    BlockState blockstate1 = AbstractWarpingFireBlock.getFireForPlacement(world, blockpos1);
                    world.setBlockState(blockpos1, blockstate1, 11);
                    if (playerentity instanceof ServerPlayerEntity) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerentity, blockpos1, itemstack);
                        itemstack.damageItem(1, playerentity, (player) -> {
                            player.sendBreakAnimation(context.getHand());
                        });
                    }

                    return ActionResultType.func_233537_a_(world.isRemote());
                } else {
                    return ActionResultType.FAIL;
                }
            }
            else{
                if (AbstractFireBlock.canLightBlock(world, blockpos1, context.getPlacementHorizontalFacing())) {
                    world.playSound(playerentity, blockpos1, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
                    BlockState blockstate1 = AbstractFireBlock.getFireForPlacement(world, blockpos1);
                    world.setBlockState(blockpos1, blockstate1, 11);
                    if (playerentity instanceof ServerPlayerEntity) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerentity, blockpos1, itemstack);
                        itemstack.damageItem(1, playerentity, (player) -> {
                            player.sendBreakAnimation(context.getHand());
                        });
                    }

                    return ActionResultType.func_233537_a_(world.isRemote());
                } else {
                    return ActionResultType.FAIL;
                }
            }
        }
    }
}
