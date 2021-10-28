package com.bedmen.odyssey.items.equipment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.SwordItem;

import java.util.HashMap;
import java.util.Map;

public interface IEquipment {

    default int getInnateEnchantmentLevel(Enchantment e) {
        return 0;
    }

    default Map<Enchantment, Integer> getInnateEnchantmentMap(){
        return new HashMap<>();
    }

    default int getSetBonusLevel(Enchantment e) {
        return 0;
    }

    default boolean isSwordLike(){
        return this instanceof SwordItem;
    }
}
