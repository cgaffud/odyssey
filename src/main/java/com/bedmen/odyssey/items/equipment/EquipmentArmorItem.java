package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.util.EnchantmentRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.*;

public class EquipmentArmorItem extends ArmorItem {
    protected final Map<Lazy<Enchantment>, Integer> enchantmentLazyMap = new HashMap<>();
    private final Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
    protected static final List<EquipmentArmorItem> UNFINISHED_EQUIPMENT = new ArrayList<EquipmentArmorItem>();

    public EquipmentArmorItem(IArmorMaterial armorMaterial, EquipmentSlotType slotType, Properties p_i48534_3_) {
        super(armorMaterial, slotType, p_i48534_3_);
        this.enchantmentLazyMap.put(Lazy.of(EnchantmentRegistry.UNENCHANTABLE::get),1);
        UNFINISHED_EQUIPMENT.add(this);
    }

    public static void initEquipment(){
        for(final EquipmentArmorItem equipmentArmorItem : UNFINISHED_EQUIPMENT){
            for(Lazy<Enchantment> lazy : equipmentArmorItem.enchantmentLazyMap.keySet()){
                equipmentArmorItem.enchantmentMap.put(lazy.get(), equipmentArmorItem.enchantmentLazyMap.get(lazy));
            }
        }
        UNFINISHED_EQUIPMENT.clear();
    }

    public static int getInnateEnchantmentLevel(Enchantment e, Item item) {
        if(item instanceof EquipmentArmorItem){
            EquipmentArmorItem equipmentArmorItem = (EquipmentArmorItem)item;
            return equipmentArmorItem.getInnateEnchantmentLevelHelper(e);
        }
        return 0;
    }

    public int getInnateEnchantmentLevelHelper(Enchantment e) {
        if(e == EnchantmentRegistry.UNENCHANTABLE.get())
            return 1;
        Integer i = this.enchantmentMap.get(e);
        if(i == null)
            return 0;
        return i;
    }

    public Map<Enchantment, Integer> getInnateEnchantmentMapHelper(){
        return this.enchantmentMap;
    }

    public static Map<Enchantment, Integer> getInnateEnchantmentMap(Item item) {
        if(item instanceof EquipmentArmorItem){
            EquipmentArmorItem equipmentArmorItem = (EquipmentArmorItem)item;
            return equipmentArmorItem.getInnateEnchantmentMapHelper();
        }
        return new HashMap<>();
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        for(Enchantment e : this.enchantmentMap.keySet()){
            if(EnchantmentRegistry.UNENCHANTABLE.get() == e)
                tooltip.add(1, e.getFullname(this.enchantmentMap.get(e)));
            else
                tooltip.add(e.getFullname(this.enchantmentMap.get(e)));
        }
    }
}
