package com.bedmen.odyssey.util;

import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.*;

public class EnchantmentUtil {
//    private static final Map<Enchantment, Integer> ENCHANTMENT_TO_INTEGER_MAP = new HashMap<>();
//    private static final Map<Integer, Enchantment> INTEGER_TO_ENCHANTMENT_MAP = new HashMap<>();

    private static List<List<AspectInstance>> BONUS_MODIFIERS_BY_TIER;
    private static List<List<AspectInstance>> CURSES_BY_TIER;


    public static void init(){
        List<AspectInstance> bonusModifierTier1 = List.of(
                new AspectInstance(Aspects.BLAST_PROTECTION, 1.0f),
                new AspectInstance(Aspects.FEATHER_FALLING, 1.0f),
                new AspectInstance(Aspects.ICE_PROTECTION, 1.0f),
                new AspectInstance(Aspects.RESPIRATION, 1.0f),
                new AspectInstance(Aspects.SWIM_SPEED, 1.0f),
                new AspectInstance(Aspects.DAMAGE_ON_ARTHROPOD, 1.0f),
                new AspectInstance(Aspects.SMITE_DAMAGE, 1.0f),
                new AspectInstance(Aspects.ADDITIONAL_SWEEP_DAMAGE, 1.0f),
                new AspectInstance(Aspects.FATAL_HIT, 1.0f),
                new AspectInstance(Aspects.KNOCKBACK, 1.0f),
                new AspectInstance(Aspects.PIERCING, 1.0f),
                new AspectInstance(Aspects.PROJECTILE_KNOCKBACK, 1.0f)
        );
        List<AspectInstance> curseTier1 = List.of(
                new AspectInstance(Aspects.BLOOD_LOSS, 1).withObfuscation(),
                new AspectInstance(Aspects.WEIGHT, 1).withObfuscation(),
                new AspectInstance(Aspects.OXYGEN_DEPRIVATION, 1).withObfuscation(),
                new AspectInstance(Aspects.BLANK).withObfuscation(),
                new AspectInstance(Aspects.BINDING).withObfuscation(),
                new AspectInstance(Aspects.VANISHING).withObfuscation(),
                new AspectInstance(Aspects.VOLATILITY, 1.0f).withObfuscation()
        );
        BONUS_MODIFIERS_BY_TIER = List.of(bonusModifierTier1);
        CURSES_BY_TIER = List.of(curseTier1);
    }

    public static List<AspectInstance> getBonusModifiersByTier(int i){
        if(i > 0 && i <= BONUS_MODIFIERS_BY_TIER.size()){
            return BONUS_MODIFIERS_BY_TIER.get(i-1);
        }
        return new ArrayList<>();
    }

    public static List<AspectInstance> getCursesByTier(int i){
        if(i > 0 && i <= CURSES_BY_TIER.size()){
            return CURSES_BY_TIER.get(i-1);
        }
        return new ArrayList<>();
    }

    public static boolean canEnchantOntoItemStack(Enchantment enchantment, ItemStack itemStack){
        if(!enchantment.canEnchant(itemStack)){
            return false;
        }
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);
        for(Enchantment enchantment1 : map.keySet()){
            if(!enchantment1.isCompatibleWith(enchantment)){
                return false;
            }
        }
        return true;
    }

    // todo fix this
//    public static List<AspectInstance> filterModifiers(List<AspectInstance> aspectInstanceList, ItemStack itemStack){
//        return aspectInstanceList.stream().filter((pair) -> canEnchantOntoItemStack(pair.getFirst(), itemStack)).collect(Collectors.toList());
//    }
//
//    public static ItemStack itemWithEnchantmentByTier(Item item, Random random, int tier, double probability) {
//        ItemStack itemStack = new ItemStack(item);
//        if (random.nextDouble() < probability) {
//            List<AspectInstance> enchantmentList = EnchantmentUtil.getBonusModifiersByTier(tier);
//            enchantmentList = EnchantmentUtil.filterModifiers(enchantmentList, itemStack);
//            AspectInstance enchantmentIntegerPair = enchantmentList.get(random.nextInt(enchantmentList.size()));
//            itemStack.enchant(enchantmentIntegerPair.getFirst(), enchantmentIntegerPair.getSecond());
//        }
//        return itemStack;
//    }

    public static ItemStack itemWithEnchantmentByTier(Item item, Random random, int tier, double probability) {
        return item.getDefaultInstance();
    }
}
