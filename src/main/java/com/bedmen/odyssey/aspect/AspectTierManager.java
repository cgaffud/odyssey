package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.encapsulator.RandomAspectList;
import com.bedmen.odyssey.aspect.object.Aspects;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AspectTierManager {

    public static final int HIGHEST_TIER = 2;

    public static ItemStack itemModifyByTier(Item item, RandomSource randomSource, int tier, float chance, float cursePercentage) {
        return itemStackModifyByTier(item.getDefaultInstance(), randomSource, tier, chance, cursePercentage);
    }

    public static ItemStack itemStackModifyByTier(ItemStack itemStack, RandomSource randomSource, int tier, float chance, float cursePercentage) {
        float modifiability = AspectUtil.getModifiabilityRemaining(itemStack);
        List<AspectInstance<?>> aspectInstanceList = new ArrayList<>();
        while(modifiability > 0f){
            boolean curse = randomSource.nextFloat() < cursePercentage;
            AspectInstance<?> aspectInstance = (curse ? CURSES_BY_TIER : BUFFS_BY_TIER).get(tier-1).getRandomAspectInstance(randomSource);
            if(curse){
                aspectInstance = aspectInstance.withObfuscation();
            }
            modifiability -= aspectInstance.getModifiability();
            if(modifiability < 0f){
                break;
            } else {
                aspectInstanceList.add(aspectInstance);
            }
        }
        aspectInstanceList = aspectInstanceList.stream().filter(aspectInstance -> randomSource.nextFloat() < chance).collect(Collectors.toList());
        for(AspectInstance<?> aspectInstance : aspectInstanceList){
            AspectUtil.addModifier(itemStack, aspectInstance);
        }
        return itemStack;
    }

    private static final List<RandomAspectList> BUFFS_BY_TIER;
    private static final List<RandomAspectList> CURSES_BY_TIER;


    static {
        RandomAspectList buffPool1 = new RandomAspectList.Builder()
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
                .add(Aspects.SNOWSHOE, null, 1)
                .add(Aspects.THORNS, 1.0f, 2)
                .add(Aspects.EXPLOSION_DAMAGE_BLOCK, 5.0f, 2)
                .add(Aspects.RECOVERY_SPEED, 0.5f, 2)
                .add(Aspects.EFFICIENCY, 0.25f, 2)
                .build();

        RandomAspectList buffPool2 = new RandomAspectList.Builder()
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
                .add(Aspects.SNOWSHOE, null, 1)
                .add(Aspects.THORNS, 1.5f, 2)
                .add(Aspects.EXPLOSION_DAMAGE_BLOCK, 7.5f, 2)
                .add(Aspects.RECOVERY_SPEED, 1.0f, 2)
                .add(Aspects.EFFICIENCY, 0.5f, 2)
                .add(Aspects.FORTUNE, 1, 1)
                .build();

        RandomAspectList cursePool1 = new RandomAspectList.Builder()
                .add(Aspects.BLOOD_LOSS, 1, 1)
                .add(Aspects.WEIGHT, 1, 1)
                .add(Aspects.OXYGEN_DEPRIVATION, 1, 1)
                .add(Aspects.BLANK, null,  1)
                .add(Aspects.BINDING, null, 1)
                .add(Aspects.VANISHING, null, 1)
                .add(Aspects.VOLATILITY, 3.0f, 1)
                .build();

        RandomAspectList cursePool2 = new RandomAspectList.Builder()
                .add(Aspects.BLOOD_LOSS, 1, 1)
                .add(Aspects.WEIGHT, 1, 1)
                .add(Aspects.OXYGEN_DEPRIVATION, 1, 1)
                .add(Aspects.BLANK, null,  1)
                .add(Aspects.BINDING, null, 1)
                .add(Aspects.VANISHING, null, 1)
                .add(Aspects.VOLATILITY, 3.0f, 1)
                .build();

        BUFFS_BY_TIER = List.of(buffPool1, buffPool2);
        CURSES_BY_TIER = List.of(cursePool1, cursePool2);
    }

}
