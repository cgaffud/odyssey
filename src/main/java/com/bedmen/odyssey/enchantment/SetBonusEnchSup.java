package com.bedmen.odyssey.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Supplier;

public class SetBonusEnchSup extends LevEnchSup{
    public final String key;

    public SetBonusEnchSup(Supplier<Enchantment> enchantmentSupplier, String key){
        super(enchantmentSupplier);
        this.key = key;
    }
}
