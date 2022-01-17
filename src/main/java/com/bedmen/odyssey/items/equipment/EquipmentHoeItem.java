package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.OdysseyRarity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;

public class EquipmentHoeItem extends HoeItem implements IEquipment {
    protected final Set<LevEnchSup> levEnchSupSet = new HashSet<>();
    private final Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
    protected static final List<EquipmentHoeItem> UNFINISHED_EQUIPMENT = new ArrayList<>();

    public EquipmentHoeItem(Tier tier, float damage, float attackSpeed, Properties properties, LevEnchSup... levEnchSups) {
        super(tier, (int)damage, attackSpeed, properties.rarity(OdysseyRarity.EQUIPMENT));
        this.levEnchSupSet.add(UNENCHANTABLE);
        Collections.addAll(this.levEnchSupSet, levEnchSups);
        UNFINISHED_EQUIPMENT.add(this);
    }

    public static void initEquipment(){
        for(final EquipmentHoeItem equipmentHoeItem : UNFINISHED_EQUIPMENT){
            equipmentHoeItem.init();
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
        for(Enchantment e : this.enchantmentMap.keySet()){
            if(EnchantmentRegistry.UNENCHANTABLE.get() == e && flagIn.isAdvanced())
                tooltip.add(1, EnchantmentUtil.getUnenchantableName());
            else if (EnchantmentRegistry.UNENCHANTABLE.get() != e )
                tooltip.add(e.getFullname(this.enchantmentMap.get(e)));
        }
    }
}
