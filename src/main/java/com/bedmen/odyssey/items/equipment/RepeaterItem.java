package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RepeaterItem extends EquipmentBowItem {

    public RepeaterItem(Properties builder, float velocity, int chargeTime, LevEnchSup... levEnchSups) {
        super(builder, velocity, chargeTime, levEnchSups);
    }

    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int count) {
        if(this.getUseDuration(itemStack) - count > this.getChargeTime(itemStack)){
            livingEntity.stopUsingItem();
            this.releaseUsing(itemStack, level, livingEntity, count);
        }
    }
}
