package com.bedmen.odyssey.enchantment;

import net.minecraft.enchantment.AquaAffinityEnchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class MoltenAffinityEnchantment extends AquaAffinityEnchantment {
    public MoltenAffinityEnchantment(Rarity p_i46719_1_, EquipmentSlotType... p_i46719_2_) {
        super(p_i46719_1_, p_i46719_2_);
    }

    public boolean isVolcanic(){
        return true;
    }
}
