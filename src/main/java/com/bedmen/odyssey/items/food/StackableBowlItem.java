package com.bedmen.odyssey.items.food;

import com.bedmen.odyssey.food.OdysseyFood;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class StackableBowlItem extends Item {
    public StackableBowlItem(Item.Properties properties) {
        super(properties);
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        return OdysseyFood.finishingUsingBowlItem(() -> super.finishUsingItem(itemStack, level, livingEntity), livingEntity);
    }
}
