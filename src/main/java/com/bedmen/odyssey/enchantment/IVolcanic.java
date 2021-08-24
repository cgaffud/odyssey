package com.bedmen.odyssey.enchantment;

import net.minecraft.enchantment.Enchantment;

public interface IVolcanic {
    default Enchantment getPredecessor(){
        return null;
    }
}
