package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class OdysseyThornsEnchantment extends Enchantment {
    private static final float CHANCE_PER_LEVEL = 0.15F;

    public OdysseyThornsEnchantment(Enchantment.Rarity rarity, EquipmentSlot... equipmentSlots) {
        super(rarity, EnchantmentCategory.ARMOR_CHEST, equipmentSlots);
    }

    public int getMaxLevel() {
        return 5;
    }

    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof ArmorItem || super.canEnchant(itemStack);
    }

    public void doPostHurt(LivingEntity livingEntity, Entity entity, int enchantmentLevel) {
        Random random = livingEntity.getRandom();
        Entry<EquipmentSlot, ItemStack> entry = getRandomArmorPiece(livingEntity);
        if (shouldHit(enchantmentLevel, random)) {
            entity.hurt(DamageSource.thorns(livingEntity), (float)getDamage(enchantmentLevel, random));
            if (entry != null) {
                entry.getValue().hurtAndBreak(2, livingEntity, (p_45208_) -> {
                    p_45208_.broadcastBreakEvent(entry.getKey());
                });
            }
        }
    }

    public Entry<EquipmentSlot, ItemStack> getRandomArmorPiece(LivingEntity livingEntity) {
        Map<EquipmentSlot, ItemStack> map = this.getSlotItems(livingEntity);
        int setBonusLevel = EnchantmentUtil.getSetBonusLevel(map.values(), this);
        if(setBonusLevel > 0){
            List<Entry<EquipmentSlot, ItemStack>> list1 = new ArrayList<>(map.entrySet());
            return list1.get(livingEntity.getRandom().nextInt(list1.size()));
        }
        List<Entry<EquipmentSlot, ItemStack>> list = new ArrayList<>();
        for(Entry<EquipmentSlot, ItemStack> entry : map.entrySet()){
            ItemStack itemStack = entry.getValue();
            if(EnchantmentUtil.getThorns(itemStack) > 0){
                list.add(entry);
            }
        }
        if(list.size() > 0){
            return list.get(livingEntity.getRandom().nextInt(list.size()));
        }
        return null;
    }

    public static boolean shouldHit(int enchantmentLevel, Random random) {
        if (enchantmentLevel <= 0) {
            return false;
        } else {
            return random.nextFloat() < 0.15F * (float)enchantmentLevel;
        }
    }

    public static int getDamage(int enchantmentLevel, Random random) {
        return enchantmentLevel > 10 ? enchantmentLevel - 10 : 1 + random.nextInt(4);
    }
}