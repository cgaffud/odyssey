package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.armor.OdysseyArmorMaterials;
import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.enchantment.SetBonusEnchSup;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.OdysseyRarity;
import net.minecraft.ChatFormatting;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;

public class EquipmentArmorItem extends ArmorItem implements IEquipment {
    protected final Set<LevEnchSup> levEnchSupSet = new HashSet<>();
    protected final Set<SetBonusEnchSup> setBonusEnchSupSet = new HashSet<>();
    protected final Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
    protected final Map<Enchantment, Tuple<Integer, String>> setBonusMap = new HashMap<>();
    protected static final List<EquipmentArmorItem> UNFINISHED_EQUIPMENT = new ArrayList<>();

    public EquipmentArmorItem(OdysseyArmorMaterials armorMaterial, EquipmentSlot slotType, Properties properties, LevEnchSup... levEnchSups) {
        super(armorMaterial, slotType, properties.rarity(OdysseyRarity.EQUIPMENT));
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

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
//        if(this instanceof HollowCoconutItem){
//            super.appendHoverText(stack, worldIn, tooltip, flagIn);
//        } else {
            for(Enchantment e : this.enchantmentMap.keySet()){
                if(EnchantmentRegistry.UNENCHANTABLE.get() == e && flagIn.isAdvanced())
                    tooltip.add(1, EnchantmentUtil.getUnenchantableName());
                else if (EnchantmentRegistry.UNENCHANTABLE.get() != e )
                    tooltip.add(e.getFullname(this.enchantmentMap.get(e)));
            }
            if(this.setBonusMap.size() > 0)
                tooltip.add(new TranslatableComponent("item.oddc.equipment.setbonus").withStyle(ChatFormatting.GRAY));
            for(Enchantment e : this.setBonusMap.keySet()){
                MutableComponent mutableComponent = (MutableComponent)(e.getFullname(this.setBonusMap.get(e).getA()));
                String key = this.setBonusMap.get(e).getB();
                if(key.equals("passive"))
                    mutableComponent.append(new TranslatableComponent("item.oddc.equipment.passive").withStyle(ChatFormatting.GRAY));
                else{
                    mutableComponent.append(" [");
                    mutableComponent.append(new TranslatableComponent(key).withStyle(ChatFormatting.GRAY));
                    mutableComponent.append(new TranslatableComponent("item.oddc.equipment.key").withStyle(ChatFormatting.GRAY));
                }
                tooltip.add(mutableComponent);
            }
//        }
    }
}
