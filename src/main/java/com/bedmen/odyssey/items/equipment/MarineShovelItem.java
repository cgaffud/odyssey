package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.item.IItemTier;
import net.minecraftforge.common.util.Lazy;

public class MarineShovelItem extends EquipmentShovelItem{
    public MarineShovelItem(IItemTier p_i48478_1_, float p_i48478_2_, float p_i48478_3_, Properties p_i48478_4_) {
        super(p_i48478_1_, p_i48478_2_, p_i48478_3_, p_i48478_4_);
        this.enchantmentLazyMap.put(Lazy.of(()-> EnchantmentRegistry.AQUA_AFFINITY.get()), 1);
    }
}
