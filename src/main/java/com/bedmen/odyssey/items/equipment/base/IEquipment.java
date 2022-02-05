package com.bedmen.odyssey.items.equipment.base;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IEquipment {

    LevEnchSup UNENCHANTABLE = new LevEnchSup(EnchantmentRegistry.UNENCHANTABLE);

    //Call this in appendHoverText
    default void appendInnateEnchantments(List<Component> tooltip, TooltipFlag flagIn){
        for(Enchantment e : this.getInnateEnchantmentMap().keySet()){
            if(EnchantmentRegistry.UNENCHANTABLE.get() == e && flagIn.isAdvanced())
                tooltip.add(1, EnchantmentUtil.getUnenchantableName());
            else if (EnchantmentRegistry.UNENCHANTABLE.get() != e ){
                Component component = e.getFullname(this.getInnateEnchantmentMap().get(e));
                if(component instanceof MutableComponent mutableComponent){
                    component = mutableComponent.withStyle(OdysseyChatFormatting.LAVENDER);
                }
                tooltip.add(component);
            }
        }
    }

    default int getInnateEnchantmentLevel(Enchantment e) {
        return 0;
    }

    default Map<Enchantment, Integer> getInnateEnchantmentMap(){
        return new HashMap<>();
    }

    boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment);
}
