package com.bedmen.odyssey.items;

import com.bedmen.odyssey.entity.projectile.AmethystArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AmethystArrowItem extends ArrowItem {
    public AmethystArrowItem(Item.Properties p_i48464_1_) {
        super(p_i48464_1_);
    }

    public AbstractArrowEntity createArrow(World p_200887_1_, ItemStack p_200887_2_, LivingEntity p_200887_3_) {
        return new AmethystArrowEntity(p_200887_1_, p_200887_3_);
    }
}