package com.bedmen.odyssey.enchantment.vanilla;

import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyDepthStriderEnchantment extends Enchantment {
    public OdysseyDepthStriderEnchantment(Enchantment.Rarity p_i46720_1_, EquipmentSlotType... p_i46720_2_) {
        super(p_i46720_1_, EnchantmentType.ARMOR_FEET, p_i46720_2_);
    }

    public int getMinCost(int p_77321_1_) {
        return p_77321_1_ * 10;
    }

    public int getMaxCost(int p_223551_1_) {
        return this.getMinCost(p_223551_1_) + 15;
    }

    public int getMaxLevel() {
        return 3;
    }

    public boolean checkCompatibility(Enchantment p_77326_1_) {
        return super.checkCompatibility(p_77326_1_) && p_77326_1_ != Enchantments.FROST_WALKER;
    }

    public Enchantment getUpgrade(){
        return EnchantmentRegistry.VULCAN_STRIDER.get();
    }
}