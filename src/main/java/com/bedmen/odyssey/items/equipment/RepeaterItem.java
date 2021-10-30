package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RepeaterItem extends EquipmentBowItem {

    public RepeaterItem(Properties builder, float velocity, int chargeTime, LevEnchSup... levEnchSups) {
        super(builder, velocity, chargeTime, levEnchSups);
    }

    public void onUseTick(World pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pCount) {
//        System.out.println(pCount);
//        System.out.println(this.getChargeTime(pStack));
        if(this.getUseDuration(pStack) - pCount > this.getChargeTime(pStack)){
            pLivingEntity.stopUsingItem();
            this.releaseUsing(pStack, pLevel, pLivingEntity, pCount);
        }
    }
}
