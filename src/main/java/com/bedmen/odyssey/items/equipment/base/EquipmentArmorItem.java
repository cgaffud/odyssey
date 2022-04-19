package com.bedmen.odyssey.items.equipment.base;

import com.bedmen.odyssey.armor.OdysseyArmorMaterials;
import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.enchantment.SetBonusEnchSup;
import com.bedmen.odyssey.items.equipment.HollowCoconutItem;
import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class EquipmentArmorItem extends ArmorItem implements IEquipment {
    protected final Set<LevEnchSup> levEnchSupSet = new HashSet<>();
    protected final Set<SetBonusEnchSup> setBonusEnchSupSet = new HashSet<>();
    protected final Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
    protected final Map<Enchantment, Tuple<Integer, String>> setBonusMap = new HashMap<>();
    protected Map<Enchantment, Integer> cachedSetBonusMap = null;

    protected static final List<EquipmentArmorItem> UNFINISHED_EQUIPMENT = new ArrayList<>();

    public EquipmentArmorItem(OdysseyArmorMaterials armorMaterial, EquipmentSlot slotType, Properties properties, LevEnchSup... levEnchSups) {
        super(armorMaterial, slotType, properties);
        this.levEnchSupSet.add(UNENCHANTABLE);
        Collections.addAll(this.levEnchSupSet, levEnchSups);
        Collections.addAll(this.setBonusEnchSupSet, armorMaterial.getSetBonusEnchSups());
        UNFINISHED_EQUIPMENT.add(this);
    }

    public static void initEquipment(){
        for(final EquipmentArmorItem equipmentArmorItem : UNFINISHED_EQUIPMENT){
            equipmentArmorItem.init();
        }
        UNFINISHED_EQUIPMENT.clear();
    }

    public void init(){
        for(LevEnchSup levEnchSup : this.levEnchSupSet){
            this.enchantmentMap.put(levEnchSup.enchantmentSupplier.get(), levEnchSup.level);
        }
        for(SetBonusEnchSup setBonusEnchSup : this.setBonusEnchSupSet){
            this.setBonusMap.put(setBonusEnchSup.enchantmentSupplier.get(), new Tuple<>(setBonusEnchSup.level, setBonusEnchSup.key));
        }
    }

    public int getInnateEnchantmentLevel(Enchantment e) {
        Integer i = this.enchantmentMap.get(e);
        if(i == null)
            return 0;
        return i;
    }

    public Map<Enchantment, Integer> getInnateEnchantmentMap(){
        return this.enchantmentMap;
    }

    public int getSetBonusLevel(Enchantment e) {
        Tuple<Integer, String> tuple = this.setBonusMap.get(e);
        if(tuple == null)
            return 0;
        return tuple.getA();
    }

    public Map<Enchantment, Integer> getSetBonusMap() {
        if (this.cachedSetBonusMap == null) {
            this.cachedSetBonusMap = this.setBonusMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, (entry) -> entry.getValue().getA()));
        }
        return this.cachedSetBonusMap;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return this.getInnateEnchantmentLevel(enchantment) == 0 && enchantment.category.canEnchant(stack.getItem());
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        if(!(this instanceof HollowCoconutItem)){
            this.appendInnateEnchantments(tooltip, flagIn);
            if(this.setBonusMap.size() > 0)
                tooltip.add(new TranslatableComponent("item.oddc.equipment.setbonus").withStyle(OdysseyChatFormatting.LAVENDER));
            for(Enchantment e : this.setBonusMap.keySet()){
                MutableComponent mutableComponent = ((MutableComponent)e.getFullname(this.setBonusMap.get(e).getA())).withStyle(OdysseyChatFormatting.LAVENDER);
                String key = this.setBonusMap.get(e).getB();
                if(key.equals("passive"))
                    mutableComponent.append(new TranslatableComponent("item.oddc.equipment.passive").withStyle(OdysseyChatFormatting.LAVENDER));
                else{
                    mutableComponent.append(" [").withStyle(OdysseyChatFormatting.LAVENDER);
                    mutableComponent.append(new TranslatableComponent(key).withStyle(OdysseyChatFormatting.LAVENDER));
                    mutableComponent.append(new TranslatableComponent("item.oddc.equipment.key").withStyle(OdysseyChatFormatting.LAVENDER));
                }
                tooltip.add(mutableComponent);
            }
        }
    }
}
