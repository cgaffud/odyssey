package com.bedmen.odyssey.aspect;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class AspectTierManager {
    public static ItemStack itemBuffedByTier(Item item, Random random, int tier, double expectedValue) {
        ItemStack itemStack = item.getDefaultInstance();
        return itemStack;
    }

    /*
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
    */
}
