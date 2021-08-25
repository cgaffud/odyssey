package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.util.Lazy;

public class UpperLeviathanArmorItem extends EquipmentArmorItem {
    public UpperLeviathanArmorItem(IArmorMaterial armorMaterial, EquipmentSlotType slotType, Properties p_i48534_3_) {
        super(armorMaterial, slotType, p_i48534_3_);
        this.enchantmentLazyMap.put(Lazy.of(EnchantmentRegistry.PYROPNEUMATIC), 1);
        this.setBonusLazyMap.put(Lazy.of(EnchantmentRegistry.FIREPROOF), new Tuple<>(1,"key.sneak"));
    }
}
