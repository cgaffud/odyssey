package com.bedmen.odyssey.items;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;

public class TomeItem extends Item {
    public static final String TAG_LEARNED_ENCHANTMENTS = "LearnedEnchantments";
    public final Map<Enchantment, Integer> learnableEnchantmentsMap = new HashMap<>();
    protected final Set<LevEnchSup> levEnchSupSet = new HashSet<>();
    public static final List<TomeItem> TOMES = new ArrayList<>();
    private final int color;

    /**
     * @param color for identifying the tome item's color
     * @param levEnchSups for identifying what enchantments can be stored in this tome
     */
    public TomeItem(Item.Properties properties, int color, LevEnchSup... levEnchSups) {
        super(properties);
        Collections.addAll(this.levEnchSupSet, levEnchSups);
        TOMES.add(this);
        this.color = color;
    }

    public static void initTomes(){
        for(final TomeItem tomeItem : TOMES){
            tomeItem.init();
        }
    }

    public void init(){
        for(LevEnchSup levEnchSup : this.levEnchSupSet){
            this.learnableEnchantmentsMap.put(levEnchSup.enchantmentSupplier.get(), levEnchSup.level);
        }
    }

    public int getColor(){
        return this.color;
    }

    public boolean isEnchantable(ItemStack itemStack) {
        return false;
    }

    public static ListTag getEnchantmentsListTag(ItemStack itemStack) {
        CompoundTag compoundtag = itemStack.getTag();
        return compoundtag != null ? compoundtag.getList(TAG_LEARNED_ENCHANTMENTS, 10) : new ListTag();
    }

    public static Map<Enchantment, Integer> getEnchantmentsMap(ItemStack itemStack) {
        ListTag listTag = getEnchantmentsListTag(itemStack);
        return EnchantmentHelper.deserializeEnchantments(listTag);
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> componentList, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, componentList, tooltipFlag);
        ItemStack.appendEnchantmentNames(componentList, getEnchantmentsListTag(itemStack));
    }
}