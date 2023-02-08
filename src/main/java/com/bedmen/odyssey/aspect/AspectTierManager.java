package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.encapsulator.RandomAspectList;
import com.bedmen.odyssey.aspect.object.Aspects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class AspectTierManager {

    public static final int HIGHEST_TIER = 1;

    public static ItemStack itemBuffedByTier(Item item, Random random, int tier) {
        return itemBuffedByTier(item, random, tier, 0.5f);
    }

    public static ItemStack itemBuffedByTier(Item item, Random random, int tier, float chance) {
        return itemStackBuffedByTier(item.getDefaultInstance(), random, tier, chance);
    }

    public static ItemStack itemStackBuffedByTier(ItemStack itemStack, Random random, int tier, float chance) {
        List<AspectInstance> aspectInstanceList = BONUS_MODIFIERS_BY_TIER.get(tier-1).generateAspectInstances(itemStack, random, chance);
        aspectInstanceList.forEach(aspectInstance -> AspectUtil.replaceModifier(itemStack, aspectInstance));
        return itemStack;
    }

    private static List<RandomAspectList> BONUS_MODIFIERS_BY_TIER;
    private static List<List<AspectInstance>> CURSES_BY_TIER;


    static {
        RandomAspectList bonusModifierPool1 = RandomAspectList.builder()
                .add(Aspects.DURABILITY, 1.0f, 10)
                .add(Aspects.DAMAGE_ON_ARTHROPOD, 0.75f, 2)
                .add(Aspects.SMITE_DAMAGE, 0.75f, 2)
                .add(Aspects.KNOCKBACK, 0.5f,  2)
                .add(Aspects.FATAL_HIT, 7.5f, 2)
                .add(Aspects.ADDITIONAL_SWEEP_DAMAGE, 1.0f, 2)
                .add(Aspects.POISON_DAMAGE, 2.0f, 1)
                .add(Aspects.COBWEB_CHANCE, 0.2f, 1)
                .add(Aspects.ACCURACY, 2.0f, 2)
                .add(Aspects.LOYALTY, 1.0f, 2)
                .add(Aspects.VELOCITY, 0.25f, 2)
                .add(Aspects.PIERCING, 1.0f, 2)
                .add(Aspects.PROJECTILE_POISON_DAMAGE, 2.0f, 1)
                .add(Aspects.PROJECTILE_COBWEB_CHANCE, 0.2f, 1)
                .add(Aspects.PROJECTILE_KNOCKBACK, 0.5f, 2)
                .add(Aspects.FEATHER_FALLING, 2.0f, 2)
                .add(Aspects.ICE_PROTECTION, 1.0f, 2)
                .add(Aspects.BLAST_PROTECTION, 1.0f, 2)
                .add(Aspects.RESPIRATION, 1.0f, 2)
                .add(Aspects.SNOWSHOE, 1.0f, 1)
                .add(Aspects.FREEZE_IMMUNITY, 1.0f, 1)
                .add(Aspects.THORNS, 1.0f, 2)
                .add(Aspects.EXPLOSION_DAMAGE_BLOCK, 5.0f, 2)
                .add(Aspects.IMPENETRABILITY, 1.0f, 2)
                .add(Aspects.RECOVERY_SPEED, 1.0f, 2)
                .add(Aspects.EFFICIENCY, 0.5f, 2)
                .build();

//        List<AspectInstance> curseTier1 = List.of(
//                new AspectInstance(Aspects.BLOOD_LOSS, 1).withObfuscation(),
//                new AspectInstance(Aspects.WEIGHT, 1).withObfuscation(),
//                new AspectInstance(Aspects.OXYGEN_DEPRIVATION, 1).withObfuscation(),
//                new AspectInstance(Aspects.BLANK).withObfuscation(),
//                new AspectInstance(Aspects.BINDING).withObfuscation(),
//                new AspectInstance(Aspects.VANISHING).withObfuscation(),
//                new AspectInstance(Aspects.VOLATILITY, 1.0f).withObfuscation()
//        );
        BONUS_MODIFIERS_BY_TIER = List.of(bonusModifierPool1);
        //CURSES_BY_TIER = List.of(curseTier1);
    }

}
