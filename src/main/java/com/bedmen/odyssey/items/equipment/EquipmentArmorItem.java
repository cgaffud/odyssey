package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.util.EnchantmentRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.*;

public class EquipmentArmorItem extends ArmorItem {
    protected final Map<Lazy<Enchantment>, Integer> enchantmentLazyMap = new HashMap<>();
    protected final Map<Lazy<Enchantment>, Tuple<Integer, String>> setBonusLazyMap = new HashMap<>();
    private final Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
    private final Map<Enchantment, Tuple<Integer, String>> setBonusMap = new HashMap<>();
    protected static final List<EquipmentArmorItem> UNFINISHED_EQUIPMENT = new ArrayList<EquipmentArmorItem>();

    public EquipmentArmorItem(IArmorMaterial armorMaterial, EquipmentSlotType slotType, Properties properties) {
        super(armorMaterial, slotType, properties);
        this.enchantmentLazyMap.put(Lazy.of(EnchantmentRegistry.UNENCHANTABLE::get),1);
        UNFINISHED_EQUIPMENT.add(this);
    }

    public static void initEquipment(){
        for(final EquipmentArmorItem equipmentArmorItem : UNFINISHED_EQUIPMENT){
            for(Lazy<Enchantment> lazy : equipmentArmorItem.enchantmentLazyMap.keySet()){
                equipmentArmorItem.enchantmentMap.put(lazy.get(), equipmentArmorItem.enchantmentLazyMap.get(lazy));
            }
            for(Lazy<Enchantment> lazy : equipmentArmorItem.setBonusLazyMap.keySet()){
                equipmentArmorItem.setBonusMap.put(lazy.get(), equipmentArmorItem.setBonusLazyMap.get(lazy));
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
        Integer i = this.enchantmentMap.get(e);
        if(i == null)
            return 0;
        return i;
    }

    public static Map<Enchantment, Integer> getInnateEnchantmentMap(Item item) {
        if(item instanceof EquipmentArmorItem){
            EquipmentArmorItem equipmentArmorItem = (EquipmentArmorItem)item;
            return equipmentArmorItem.getInnateEnchantmentMapHelper();
        }
        return new HashMap<>();
    }

    public Map<Enchantment, Integer> getInnateEnchantmentMapHelper(){
        return this.enchantmentMap;
    }

    public static int getSetBonusLevel(Enchantment e, Item item) {
        if(item instanceof EquipmentArmorItem){
            EquipmentArmorItem equipmentArmorItem = (EquipmentArmorItem)item;
            return equipmentArmorItem.getSetBonusLevelHelper(e);
        }
        return 0;
    }

    public int getSetBonusLevelHelper(Enchantment e) {
        Tuple<Integer, String> tuple = this.setBonusMap.get(e);
        if(tuple == null)
            return 0;
        return tuple.getA();
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
        if(this.setBonusMap.size() > 0)
            tooltip.add(new TranslationTextComponent("item.oddc.equipment.setbonus").withStyle(TextFormatting.GRAY));
        for(Enchantment e : this.setBonusMap.keySet()){
            IFormattableTextComponent iformattabletextcomponent = (IFormattableTextComponent)(e.getFullname(this.setBonusMap.get(e).getA()));
            String key = this.setBonusMap.get(e).getB();
            if(key.equals(""))
                iformattabletextcomponent.append(new TranslationTextComponent("item.oddc.equipment.passive").withStyle(TextFormatting.GRAY));
            else{
                iformattabletextcomponent.append(" [");
                iformattabletextcomponent.append(new TranslationTextComponent(key).withStyle(TextFormatting.GRAY));
                iformattabletextcomponent.append(new TranslationTextComponent("item.oddc.equipment.key").withStyle(TextFormatting.GRAY));
            }
            tooltip.add(iformattabletextcomponent);
        }
    }
}
