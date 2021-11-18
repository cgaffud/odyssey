package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.HashMap;
import java.util.Map;

public interface IEquipment {

    LevEnchSup UNENCHANTABLE = new LevEnchSup(EnchantmentRegistry.UNENCHANTABLE);

    default int getInnateEnchantmentLevel(Enchantment e) {
        return 0;
    }

    default Map<Enchantment, Integer> getInnateEnchantmentMap(){
        return new HashMap<>();
    }

    default int getSetBonusLevel(Enchantment e) {
        return 0;
    }

    default boolean canSweep(){
        return this instanceof SwordItem;
    }
}
