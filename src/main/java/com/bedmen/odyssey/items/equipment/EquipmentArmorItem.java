package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.armor.OdysseyArmorMaterial;
import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.enchantment.SetBonusEnchSup;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.OdysseyRarity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
    private static final LevEnchSup UNENCHANTABLE = new LevEnchSup(EnchantmentRegistry.UNENCHANTABLE);

    public EquipmentArmorItem(OdysseyArmorMaterial armorMaterial, EquipmentSlotType slotType, Properties properties, LevEnchSup... levEnchSups) {
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

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(this instanceof HollowCoconutItem){
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
        } else {
            for(Enchantment e : this.enchantmentMap.keySet()){
                if(EnchantmentRegistry.UNENCHANTABLE.get() == e && flagIn.isAdvanced())
                    tooltip.add(1, EnchantmentUtil.getUnenchantableName());
                else if (EnchantmentRegistry.UNENCHANTABLE.get() != e )
                    tooltip.add(e.getFullname(this.enchantmentMap.get(e)));
            }
            if(this.setBonusMap.size() > 0)
                tooltip.add(new TranslationTextComponent("item.oddc.equipment.setbonus").withStyle(TextFormatting.GRAY));
            for(Enchantment e : this.setBonusMap.keySet()){
                IFormattableTextComponent iformattabletextcomponent = (IFormattableTextComponent)(e.getFullname(this.setBonusMap.get(e).getA()));
                String key = this.setBonusMap.get(e).getB();
                if(key.equals("passive"))
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
}
