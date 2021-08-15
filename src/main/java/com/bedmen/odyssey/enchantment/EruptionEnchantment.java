package com.bedmen.odyssey.enchantment;

import net.minecraft.inventory.EquipmentSlotType;

public class EruptionEnchantment extends OdysseyRiptideEnchantment {
    public EruptionEnchantment(Rarity p_i46719_1_, EquipmentSlotType... p_i46719_2_) {
        super(p_i46719_1_, p_i46719_2_);
    }

    public boolean isVolcanic(){
        return true;
    }
}
