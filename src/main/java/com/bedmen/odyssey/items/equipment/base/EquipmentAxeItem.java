package com.bedmen.odyssey.items.equipment.base;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.util.OdysseyRarity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;

public class EquipmentAxeItem extends AxeItem implements IEquipment {
    protected final Set<LevEnchSup> levEnchSupSet = new HashSet<>();
    private final Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
    protected static final List<EquipmentAxeItem> UNFINISHED_EQUIPMENT = new ArrayList<>();

    public EquipmentAxeItem(Tier tier, float damage, float attackSpeed, Properties properties, LevEnchSup... levEnchSups) {
        super(tier, damage, attackSpeed, properties);
        this.levEnchSupSet.add(UNENCHANTABLE);
        Collections.addAll(this.levEnchSupSet, levEnchSups);
        UNFINISHED_EQUIPMENT.add(this);
    }

    public static void initEquipment(){
        for(final EquipmentAxeItem equipmentAxeItem : UNFINISHED_EQUIPMENT){
            equipmentAxeItem.init();
        }
        UNFINISHED_EQUIPMENT.clear();
    }

    public void init(){
        for(LevEnchSup levEnchSup : this.levEnchSupSet){
            this.enchantmentMap.put(levEnchSup.enchantmentSupplier.get(), levEnchSup.level);
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

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return this.getInnateEnchantmentLevel(enchantment) == 0 && enchantment.category.canEnchant(stack.getItem());
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        this.appendInnateEnchantments(tooltip, flagIn);
    }
}
