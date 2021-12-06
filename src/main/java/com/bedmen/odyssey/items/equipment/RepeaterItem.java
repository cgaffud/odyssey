package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RepeaterItem extends EquipmentBowItem {

    public RepeaterItem(Properties builder, float velocity, int chargeTime, LevEnchSup... levEnchSups) {
        super(builder, velocity, chargeTime, levEnchSups);
    }

    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pCount) {
        if(this.getUseDuration(pStack) - pCount > this.getChargeTime(pStack)){
            pLivingEntity.stopUsingItem();
            this.releaseUsing(pStack, pLevel, pLivingEntity, pCount);
        }
    }
}
