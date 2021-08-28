package com.bedmen.odyssey.enchantment;

import net.minecraft.enchantment.DepthStriderEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class VulcanStriderEnchantment extends DepthStriderEnchantment {
    public VulcanStriderEnchantment(Rarity p_i46720_1_, EquipmentSlotType... p_i46720_2_) {
        super(p_i46720_1_, p_i46720_2_);
    }

    public Enchantment getDowngrade(){
        return Enchantments.DEPTH_STRIDER;
    }
}
