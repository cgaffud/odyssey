package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;

public interface ParryableWeaponItem {

    int getRecoveryTime(ItemStack itemStack);

    float getDamageBlock(ItemStack itemStack, DamageSource damageSource);

    float getBlockingAngleWidth(ItemStack itemStack);
}
