package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.aspect.encapsulator.RandomAspectList;
import com.bedmen.odyssey.aspect.encapsulator.RandomBuffList;
import com.bedmen.odyssey.aspect.encapsulator.RandomCurseList;
import com.bedmen.odyssey.aspect.object.Aspects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class AspectTierManager {

    public static final int HIGHEST_TIER = 2;

    public static ItemStack itemModifyByTier(Item item, Random random, int tier, float chance, boolean curse) {
        return itemStackModifyByTier(item.getDefaultInstance(), random, tier, chance, curse);
    }

    public static ItemStack itemStackModifyByTier(ItemStack itemStack, Random random, int tier, float chance, boolean curse) {
        RandomAspectList randomAspectList = curse ? CURSES_BY_TIER.get(tier-1) :  BUFFS_BY_TIER.get(tier-1);
        randomAspectList.addAspectInstances(itemStack, random, chance);
        return itemStack;
    }

    private static final List<RandomBuffList> BUFFS_BY_TIER;
    private static final List<RandomCurseList> CURSES_BY_TIER;


    static {
        RandomBuffList buffPool1 = RandomBuffList.builder()
                .add(Aspects.DURABILITY, 1.0f, 5)
                .add(Aspects.DAMAGE_ON_ARTHROPOD, 0.75f, 2)
                .add(Aspects.SMITE_DAMAGE, 0.75f, 2)
                .add(Aspects.KNOCKBACK, 0.5f,  2)
                .add(Aspects.FATAL_HIT, 7.5f, 2)
                .add(Aspects.ADDITIONAL_SWEEP_DAMAGE, 1.0f, 2)
                .add(Aspects.POISON_DAMAGE, 2, 1)
                .add(Aspects.COBWEB_CHANCE, 0.2f, 1)
                .add(Aspects.ACCURACY, 2.0f, 2)
                .add(Aspects.LOYALTY, 1.0f, 2)
                .add(Aspects.VELOCITY, 0.25f, 2)
                .add(Aspects.PIERCING, 1.0f, 2)
                .add(Aspects.PROJECTILE_POISON_DAMAGE, 2, 1)
                .add(Aspects.PROJECTILE_COBWEB_CHANCE, 0.2f, 1)
                .add(Aspects.PROJECTILE_KNOCKBACK, 0.5f, 2)
                .add(Aspects.FEATHER_FALLING, 2.0f, 2)
                .add(Aspects.ICE_PROTECTION, 1.0f, 2)
                .add(Aspects.BLAST_PROTECTION, 0.5f, 2)
                .add(Aspects.RESPIRATION, 1.0f, 2)
                .add(Aspects.SWIM_SPEED, 0.25f, 2)
                .add(Aspects.MOVEMENT_SPEED, 0.05f, 2)
                .add(Aspects.SNOWSHOE, 0, 1)
                .add(Aspects.THORNS, 1.0f, 2)
                .add(Aspects.EXPLOSION_DAMAGE_BLOCK, 5.0f, 2)
                .add(Aspects.IMPENETRABILITY, 0.5f, 2)
                .add(Aspects.RECOVERY_SPEED, 0.5f, 2)
                .add(Aspects.EFFICIENCY, 0.25f, 2)
                .build();

        RandomBuffList buffPool2 = RandomBuffList.builder()
                .add(Aspects.DURABILITY, 1.5f, 5)
                .add(Aspects.DAMAGE_ON_ARTHROPOD, 1.0f, 2)
                .add(Aspects.SMITE_DAMAGE, 1.0f, 2)
                .add(Aspects.HYDRO_DAMAGE, 0.5f, 1)
                .add(Aspects.KNOCKBACK, 1.0f,  2)
                .add(Aspects.FATAL_HIT, 10.0f, 2)
                .add(Aspects.ADDITIONAL_SWEEP_DAMAGE, 2.0f, 2)
                .add(Aspects.POISON_DAMAGE, 2, 1)
                .add(Aspects.COBWEB_CHANCE, 0.2f, 1)
                .add(Aspects.LARCENY_CHANCE, 0.1f, 1)
                .add(Aspects.LOOTING_LUCK, 1, 1)
                .add(Aspects.SOLAR_STRENGTH, 0.5f, 1)
                .add(Aspects.LUNAR_STRENGTH, 0.5f, 1)
                .add(Aspects.BOTANICAL_STRENGTH, 0.5f, 1)
                .add(Aspects.SCORCHED_STRENGTH, 0.5f, 1)
                .add(Aspects.WINTERY_STRENGTH, 0.5f, 1)
                .add(Aspects.VOID_STRENGTH, 0.5f, 1)
                .add(Aspects.ACCURACY, 4.0f, 2)
                .add(Aspects.MULTISHOT, 2.0f, 1)
                .add(Aspects.LOYALTY, 1.0f, 2)
                .add(Aspects.VELOCITY, 0.5f, 2)
                .add(Aspects.PIERCING, 2.0f, 2)
                .add(Aspects.PROJECTILE_LOOTING_LUCK, 1, 1)
                .add(Aspects.PROJECTILE_POISON_DAMAGE, 2, 1)
                .add(Aspects.PROJECTILE_COBWEB_CHANCE, 0.2f, 1)
                .add(Aspects.PROJECTILE_LARCENY_CHANCE, 0.1f, 1)
                .add(Aspects.PROJECTILE_KNOCKBACK, 1.0f, 2)
                .add(Aspects.FEATHER_FALLING, 4.0f, 2)
                .add(Aspects.ICE_PROTECTION, 1.5f, 2)
                .add(Aspects.BLAST_PROTECTION, 1.0f, 2)
                .add(Aspects.RESPIRATION, 1.0f, 2)
                .add(Aspects.SWIM_SPEED, 0.25f, 2)
                .add(Aspects.MOVEMENT_SPEED, 0.1f, 2)
                .add(Aspects.SNOWSHOE, 0, 1)
                .add(Aspects.THORNS, 1.5f, 2)
                .add(Aspects.EXPLOSION_DAMAGE_BLOCK, 7.5f, 2)
                .add(Aspects.IMPENETRABILITY, 1.0f, 2)
                .add(Aspects.RECOVERY_SPEED, 1.0f, 2)
                .add(Aspects.EFFICIENCY, 0.5f, 2)
                .add(Aspects.FORTUNE, 1, 1)
                .build();

        RandomCurseList cursePool1 = RandomCurseList.builder()
                .add(Aspects.BLOOD_LOSS, 1.0f, 1)
                .add(Aspects.WEIGHT, 1.0f, 1)
                .add(Aspects.OXYGEN_DEPRIVATION, 1.0f, 1)
                .add(Aspects.BLANK, 1.0f,  1)
                .add(Aspects.BINDING, 1.0f, 1)
                .add(Aspects.VANISHING, 1.0f, 1)
                .add(Aspects.VOLATILITY, 3.0f, 1)
                .build();

        RandomCurseList cursePool2 = RandomCurseList.builder()
                .add(Aspects.BLOOD_LOSS, 1.0f, 1)
                .add(Aspects.WEIGHT, 1.0f, 1)
                .add(Aspects.OXYGEN_DEPRIVATION, 1.0f, 1)
                .add(Aspects.BLANK, 1.0f,  1)
                .add(Aspects.BINDING, 1.0f, 1)
                .add(Aspects.VANISHING, 1.0f, 1)
                .add(Aspects.VOLATILITY, 3.0f, 1)
                .build();

        BUFFS_BY_TIER = List.of(buffPool1, buffPool2);
        CURSES_BY_TIER = List.of(cursePool1, cursePool2);
    }

}
