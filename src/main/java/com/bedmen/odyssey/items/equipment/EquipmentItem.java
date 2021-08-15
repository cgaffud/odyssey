package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EquipmentItem extends TieredItem {
    protected final Map<Lazy<Enchantment>, Integer> enchantmentLazyMap = new HashMap<>();
    private final Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
    protected static final List<EquipmentItem> UNFINISHED_EQUIPMENT = new ArrayList<>();

    public EquipmentItem(IItemTier tier, Item.Properties builderIn) {
        super(tier, builderIn);
        this.enchantmentLazyMap.put(Lazy.of(EnchantmentRegistry.UNENCHANTABLE::get),1);
        UNFINISHED_EQUIPMENT.add(this);
    }

    public static void initEquipment(){
        for(final EquipmentItem equipmentItem : UNFINISHED_EQUIPMENT){
            equipmentItem.init();
        }
        UNFINISHED_EQUIPMENT.clear();
    }

    public void init(){
        for(Lazy<Enchantment> lazy : this.enchantmentLazyMap.keySet()){
            this.enchantmentMap.put(lazy.get(), this.enchantmentLazyMap.get(lazy));
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
