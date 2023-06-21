package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.world.BiomeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

import net.minecraft.world.item.Item.Properties;

public class OdysseyPowderSnowBucketItem extends SolidBucketItem {
    public OdysseyPowderSnowBucketItem(Properties properties) {
        super(Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, properties);
    }

    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        Player player = useOnContext.getPlayer();
        if(BiomeUtil.isTooHotForPowderSnow(level, blockPos) && player != null){
            InteractionHand interactionHand = useOnContext.getHand();
            InteractionResult interactionResult = Items.WATER_BUCKET.use(level, player, interactionHand).getResult();
            if (interactionResult.consumesAction() && !player.isCreative()) {
                player.setItemInHand(interactionHand, Items.BUCKET.getDefaultInstance());
            }
            return interactionResult;
        }
        return super.useOn(useOnContext);
    }

    public boolean emptyContents(@Nullable Player player, Level level, BlockPos blockPos, @Nullable BlockHitResult blockHitResult) {
        if (level.isInWorldBounds(blockPos) && level.isEmptyBlock(blockPos)) {
            if(BiomeUtil.isTooHotForPowderSnow(level, blockPos)){
                return ((BucketItem)Items.WATER_BUCKET).emptyContents(player, level, blockPos, blockHitResult);
            } else {
                if (!level.isClientSide) {
                    level.setBlock(blockPos, this.getBlock().defaultBlockState(), 3);
                }

                level.playSound(player, blockPos, this.getPlaceSound(Blocks.POWDER_SNOW.defaultBlockState()), SoundSource.BLOCKS, 1.0F, 1.0F);
                return true;
            }
        } else {
            return false;
        }
    }

    public static void registerDispenseBehavior(){
        // todo fix bucket override bug
        DispenseItemBehavior bucketDispenseBehavior = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            public ItemStack execute(BlockSource p_123561_, ItemStack p_123562_) {
                DispensibleContainerItem dispensiblecontaineritem = (DispensibleContainerItem)p_123562_.getItem();
                BlockPos blockpos = p_123561_.getPos().relative(p_123561_.getBlockState().getValue(DispenserBlock.FACING));
                Level level = p_123561_.getLevel();
                if (dispensiblecontaineritem.emptyContents((Player)null, level, blockpos, (BlockHitResult)null)) {
                    dispensiblecontaineritem.checkExtraContent((Player)null, level, p_123562_, blockpos);
                    return new ItemStack(Items.BUCKET);
                } else {
                    return this.defaultDispenseItemBehavior.dispense(p_123561_, p_123562_);
                }
            }
        };

        DispenserBlock.registerBehavior(ItemRegistry.POWDER_SNOW_BUCKET.get(), bucketDispenseBehavior);
    }
}
