package com.bedmen.odyssey.items;

import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;

import net.minecraft.world.item.Item.Properties;

public class BabyLeviathanBucket extends BucketItem {

    public BabyLeviathanBucket(Properties properties) {
        super(Fluids.EMPTY, properties);
    }

    public void checkExtraContent(Level p_203792_1_, ItemStack p_203792_2_, BlockPos p_203792_3_) {
        if (p_203792_1_ instanceof ServerLevel) {
            this.spawn((ServerLevel)p_203792_1_, p_203792_2_, p_203792_3_);
        }
    }

    protected void playEmptySound(@Nullable Player pPlayer, LevelAccessor pLevel, BlockPos pPos) {
        pLevel.playSound(pPlayer, pPos, SoundEvents.BUCKET_EMPTY_FISH, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    private void spawn(ServerLevel p_205357_1_, ItemStack p_205357_2_, BlockPos p_205357_3_) {
        EntityTypeRegistry.BABY_LEVIATHAN.get().spawn(p_205357_1_, p_205357_2_, (Player)null, p_205357_3_, MobSpawnType.BUCKET, true, false);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        HitResult raytraceresult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.NONE);
        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(pPlayer, pLevel, itemstack, raytraceresult);
        if (ret != null) return ret;
        if (raytraceresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else if (raytraceresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            BlockHitResult blockraytraceresult = (BlockHitResult)raytraceresult;
            BlockPos blockpos = blockraytraceresult.getBlockPos();
            Direction direction = blockraytraceresult.getDirection();
            BlockPos blockpos1 = blockpos.relative(direction);
            if (pLevel.mayInteract(pPlayer, blockpos) && pPlayer.mayUseItemAt(blockpos1, direction, itemstack)) {
                BlockPos blockpos2 = blockpos1;
                this.checkExtraContent(pLevel, itemstack, blockpos2);
                pPlayer.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.sidedSuccess(this.getEmptySuccessItem(itemstack, pPlayer), pLevel.isClientSide());
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }
}
