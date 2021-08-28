package com.bedmen.odyssey.items.equipment;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
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

    default void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
    }
}
