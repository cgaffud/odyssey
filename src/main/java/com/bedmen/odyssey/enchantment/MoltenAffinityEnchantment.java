package com.bedmen.odyssey.enchantment;

import com.bedmen.odyssey.enchantment.vanilla.OdysseyAquaAffinityEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class MoltenAffinityEnchantment extends OdysseyAquaAffinityEnchantment {
    public MoltenAffinityEnchantment(Rarity p_i46719_1_, EquipmentSlotType... p_i46719_2_) {
        super(p_i46719_1_, p_i46719_2_);
    }

    public Enchantment getDowngrade(){
        return EnchantmentRegistry.AQUA_AFFINITY.get();
    }
}
