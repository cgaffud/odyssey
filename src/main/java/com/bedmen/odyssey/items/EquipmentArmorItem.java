package com.bedmen.odyssey.items;

import com.bedmen.odyssey.armor.ModArmorMaterial;
import com.bedmen.odyssey.util.AttributeRegistry;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;

public class EquipmentArmorItem extends ArmorItem {
    private final Map<Enchantment, Integer> enchantmentMap = new HashMap<>();

    public EquipmentArmorItem(IArmorMaterial armorMaterial, EquipmentSlotType slotType, Enchantment[] enchantments, int[] levels, Properties p_i48534_3_) {
        super(armorMaterial, slotType, p_i48534_3_);
        assert (enchantments.length == levels.length);
        for(int i = 0; i < enchantments.length; i++){
            this.enchantmentMap.put(enchantments[i], levels[i]);
        }
    }

    public int getInnateEnchantmentLevel(Enchantment e) {
        Integer i = this.enchantmentMap.get(e);
        if(i == null)
            return 0;
        return i;
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        for(Enchantment e : this.enchantmentMap.keySet()){
            tooltip.add(e.getFullname(this.enchantmentMap.get(e)));
        }
    }
}
