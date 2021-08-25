package com.bedmen.odyssey.enchantment.vanilla;

import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.enchantment.EnchantmentType;

public class OdysseyRespirationEnchantment extends Enchantment {
    public OdysseyRespirationEnchantment(Enchantment.Rarity p_i46724_1_, EquipmentSlotType... p_i46724_2_) {
        super(p_i46724_1_, EnchantmentType.ARMOR_HEAD, p_i46724_2_);
    }

    public int getMinCost(int p_77321_1_) {
        return 10 * p_77321_1_;
    }

    public int getMaxCost(int p_223551_1_) {
        return this.getMinCost(p_223551_1_) + 30;
    }

    public int getMaxLevel() {
        return 3;
    }

    public Enchantment getUpgrade(){
        return EnchantmentRegistry.PYROPNEUMATIC.get();
    }
}