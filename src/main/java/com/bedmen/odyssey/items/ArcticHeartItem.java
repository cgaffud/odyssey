package com.bedmen.odyssey.items;

import com.bedmen.odyssey.entity.IPlayerPermanentBuffs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ArcticHeartItem extends Item {

    public ArcticHeartItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        IPlayerPermanentBuffs player = (IPlayerPermanentBuffs) playerIn;
        if(player.getNetherImmune()){
            return ActionResult.fail(itemstack);
        }
        player.setNetherImmune(true);
        worldIn.playSound((PlayerEntity)null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.BEACON_ACTIVATE, SoundCategory.PLAYERS, 2.0F, 0.3F);
        itemstack.shrink(1);
        return ActionResult.sidedSuccess(itemstack, worldIn.isClientSide());
    }

}
