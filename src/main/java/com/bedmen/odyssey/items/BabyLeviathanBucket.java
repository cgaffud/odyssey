package com.bedmen.odyssey.items;

import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class BabyLeviathanBucket extends BucketItem {

    public BabyLeviathanBucket(Item.Properties properties) {
        super(Fluids.EMPTY, properties);
    }

    public void checkExtraContent(World p_203792_1_, ItemStack p_203792_2_, BlockPos p_203792_3_) {
        if (p_203792_1_ instanceof ServerWorld) {
            this.spawn((ServerWorld)p_203792_1_, p_203792_2_, p_203792_3_);
        }
    }

    protected void playEmptySound(@Nullable PlayerEntity pPlayer, IWorld pLevel, BlockPos pPos) {
        pLevel.playSound(pPlayer, pPos, SoundEvents.BUCKET_EMPTY_FISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

    private void spawn(ServerWorld p_205357_1_, ItemStack p_205357_2_, BlockPos p_205357_3_) {
        EntityTypeRegistry.BABY_LEVIATHAN.get().spawn(p_205357_1_, p_205357_2_, (PlayerEntity)null, p_205357_3_, SpawnReason.BUCKET, true, false);
    }

    public ActionResult<ItemStack> use(World pLevel, PlayerEntity pPlayer, Hand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        RayTraceResult raytraceresult = getPlayerPOVHitResult(pLevel, pPlayer, RayTraceContext.FluidMode.NONE);
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(pPlayer, pLevel, itemstack, raytraceresult);
        if (ret != null) return ret;
        if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
            return ActionResult.pass(itemstack);
        } else if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
            return ActionResult.pass(itemstack);
        } else {
            BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)raytraceresult;
            BlockPos blockpos = blockraytraceresult.getBlockPos();
            Direction direction = blockraytraceresult.getDirection();
            BlockPos blockpos1 = blockpos.relative(direction);
            if (pLevel.mayInteract(pPlayer, blockpos) && pPlayer.mayUseItemAt(blockpos1, direction, itemstack)) {
                BlockPos blockpos2 = blockpos1;
                this.checkExtraContent(pLevel, itemstack, blockpos2);
                pPlayer.awardStat(Stats.ITEM_USED.get(this));
                return ActionResult.sidedSuccess(this.getEmptySuccessItem(itemstack, pPlayer), pLevel.isClientSide());
            } else {
                return ActionResult.fail(itemstack);
            }
        }
    }
}
