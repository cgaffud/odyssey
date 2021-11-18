package com.bedmen.odyssey.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Supplier;

public class LevEnchSup {
    public final Supplier<Enchantment> enchantmentSupplier;
    public final int level;

    public LevEnchSup(Supplier<Enchantment> enchantmentSupplier){
        this(enchantmentSupplier, 1);
    }
    public LevEnchSup(Supplier<Enchantment> enchantmentSupplier, int level){
        this.enchantmentSupplier = enchantmentSupplier;
        this.level = level;
    }
}
