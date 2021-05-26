package com.bedmen.odyssey.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BigGlassBottleItem extends Item {
    public BigGlassBottleItem(Item.Properties builder) {
        super(builder);
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     * {@link #onItemUse}.
     */
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        RayTraceResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
        if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
            return ActionResult.pass(itemstack);
        } else {
            if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = ((BlockRayTraceResult)raytraceresult).getBlockPos();
                if (!worldIn.mayInteract(playerIn, blockpos)) {
                    return ActionResult.pass(itemstack);
                }
            }

            return ActionResult.pass(itemstack);
        }
    }

    protected ItemStack turnBottleIntoItem(ItemStack bottleStack, PlayerEntity player, ItemStack stack) {
        player.awardStat(Stats.ITEM_USED.get(this));
        return DrinkHelper.createFilledResult(bottleStack, player, stack);
    }
}
