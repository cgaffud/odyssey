package com.bedmen.odyssey.potions;

import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.PotionRegistry;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.Potions;

public class OdysseyPotions {
    public static void addBrewingRecipes() {
        PotionBrewing.POTION_MIXES.clear();

        //Awkward Potion
        PotionBrewing.addMix(Potions.WATER, Items.NETHER_WART, Potions.AWKWARD);

        //Speed
        PotionBrewing.addMix(Potions.AWKWARD, Items.SUGAR, Potions.SWIFTNESS);
        PotionBrewing.addMix(Potions.SWIFTNESS, Items.REDSTONE, Potions.LONG_SWIFTNESS);
        PotionBrewing.addMix(Potions.SWIFTNESS, Items.QUARTZ, Potions.STRONG_SWIFTNESS);
        //Slowness
        PotionBrewing.addMix(Potions.AWKWARD, Items.IRON_INGOT, Potions.SLOWNESS);
        PotionBrewing.addMix(Potions.SLOWNESS, Items.REDSTONE, Potions.LONG_SLOWNESS);
        PotionBrewing.addMix(Potions.SLOWNESS, Items.QUARTZ, Potions.STRONG_SLOWNESS);
        //Haste
        PotionBrewing.addMix(Potions.AWKWARD, ItemRegistry.ELECTRUM_INGOT.get(), PotionRegistry.HASTE.get());
        PotionBrewing.addMix(PotionRegistry.HASTE.get(), Items.REDSTONE, PotionRegistry.LONG_HASTE.get());
        PotionBrewing.addMix(PotionRegistry.HASTE.get(), Items.QUARTZ, PotionRegistry.STRONG_HASTE.get());
        //Mining Fatigue
        PotionBrewing.addMix(Potions.AWKWARD, Items.PRISMARINE_SHARD, PotionRegistry.MINING_FATIGUE.get());
        PotionBrewing.addMix(PotionRegistry.MINING_FATIGUE.get(), Items.REDSTONE, PotionRegistry.LONG_MINING_FATIGUE.get());
        PotionBrewing.addMix(PotionRegistry.MINING_FATIGUE.get(), Items.QUARTZ, PotionRegistry.STRONG_MINING_FATIGUE.get());
        //Construction
        PotionBrewing.addMix(Potions.AWKWARD, Items.BRICK, PotionRegistry.CONSTRUCTION.get());
        PotionBrewing.addMix(PotionRegistry.CONSTRUCTION.get(), Items.REDSTONE, PotionRegistry.LONG_CONSTRUCTION.get());
        PotionBrewing.addMix(PotionRegistry.CONSTRUCTION.get(), Items.QUARTZ, PotionRegistry.STRONG_CONSTRUCTION.get());
        //Building Fatigue
        PotionBrewing.addMix(Potions.AWKWARD, Items.NETHER_BRICK, PotionRegistry.BUILDING_FATIGUE.get());
        PotionBrewing.addMix(PotionRegistry.BUILDING_FATIGUE.get(), Items.REDSTONE, PotionRegistry.LONG_BUILDING_FATIGUE.get());
        //Strength
        PotionBrewing.addMix(Potions.AWKWARD, Items.RED_MUSHROOM, Potions.STRENGTH);
        PotionBrewing.addMix(Potions.STRENGTH, Items.REDSTONE, Potions.LONG_STRENGTH);
        PotionBrewing.addMix(Potions.STRENGTH, Items.QUARTZ, Potions.STRONG_STRENGTH);
        //Weakness
        PotionBrewing.addMix(Potions.AWKWARD, Items.BROWN_MUSHROOM, Potions.WEAKNESS);
        PotionBrewing.addMix(Potions.WEAKNESS, Items.REDSTONE, Potions.LONG_WEAKNESS);
        //Instant Health
        PotionBrewing.addMix(Potions.AWKWARD, Items.GLISTERING_MELON_SLICE, Potions.HEALING);
        PotionBrewing.addMix(Potions.HEALING, Items.QUARTZ, Potions.STRONG_HEALING);
        //Instant Damage
        PotionBrewing.addMix(Potions.AWKWARD, Items.SPIDER_EYE, Potions.HARMING);
        PotionBrewing.addMix(Potions.HARMING, Items.QUARTZ, Potions.STRONG_HARMING);
        //Regeneration
        PotionBrewing.addMix(Potions.AWKWARD, Items.GHAST_TEAR, Potions.REGENERATION);
        PotionBrewing.addMix(Potions.REGENERATION, Items.REDSTONE, Potions.LONG_REGENERATION);
        PotionBrewing.addMix(Potions.REGENERATION, Items.QUARTZ, Potions.STRONG_REGENERATION);
        //Poison
        PotionBrewing.addMix(Potions.AWKWARD, Items.POISONOUS_POTATO, Potions.POISON);
        PotionBrewing.addMix(Potions.POISON, Items.REDSTONE, Potions.LONG_POISON);
        PotionBrewing.addMix(Potions.POISON, Items.QUARTZ, Potions.STRONG_POISON);
        //Glowing
        PotionBrewing.addMix(Potions.AWKWARD, Items.GLOWSTONE_DUST, PotionRegistry.GLOW.get());
        PotionBrewing.addMix(PotionRegistry.GLOW.get(), Items.REDSTONE, PotionRegistry.LONG_GLOW.get());
        //Invisibility
        PotionBrewing.addMix(Potions.AWKWARD, Items.GLASS, Potions.INVISIBILITY);
        PotionBrewing.addMix(Potions.INVISIBILITY, Items.REDSTONE, Potions.LONG_INVISIBILITY);
        //Night Vision
        PotionBrewing.addMix(Potions.AWKWARD, Items.GOLDEN_CARROT, Potions.NIGHT_VISION);
        PotionBrewing.addMix(Potions.NIGHT_VISION, Items.REDSTONE, Potions.LONG_NIGHT_VISION);
        //Lava Vision
        PotionBrewing.addMix(Potions.NIGHT_VISION, Items.MAGMA_CREAM, PotionRegistry.LAVA_VISION.get());
        PotionBrewing.addMix(PotionRegistry.LAVA_VISION.get(), Items.REDSTONE, PotionRegistry.LONG_LAVA_VISION.get());
        //Blindness
        PotionBrewing.addMix(Potions.AWKWARD, Items.FERMENTED_SPIDER_EYE, PotionRegistry.BLINDNESS.get());
        PotionBrewing.addMix(PotionRegistry.BLINDNESS.get(), Items.REDSTONE, PotionRegistry.LONG_BLINDNESS.get());
        //Slow Falling
        PotionBrewing.addMix(Potions.AWKWARD, Items.PHANTOM_MEMBRANE, Potions.SLOW_FALLING);
        PotionBrewing.addMix(Potions.SLOW_FALLING, Items.REDSTONE, Potions.LONG_SLOW_FALLING);
        //Levitation
        PotionBrewing.addMix(Potions.AWKWARD, Items.SHULKER_SHELL, PotionRegistry.LEVITATION.get());
        PotionBrewing.addMix(PotionRegistry.LEVITATION.get(), Items.REDSTONE, PotionRegistry.LONG_LEVITATION.get());
        PotionBrewing.addMix(PotionRegistry.LEVITATION.get(), Items.QUARTZ, PotionRegistry.STRONG_LEVITATION.get());
        //Fire Resistance
        PotionBrewing.addMix(Potions.AWKWARD, Items.MAGMA_CREAM, Potions.FIRE_RESISTANCE);
        PotionBrewing.addMix(Potions.FIRE_RESISTANCE, Items.REDSTONE, Potions.LONG_FIRE_RESISTANCE);
        //Water Breathing
        PotionBrewing.addMix(Potions.AWKWARD, Items.PUFFERFISH, Potions.WATER_BREATHING);
        PotionBrewing.addMix(Potions.WATER_BREATHING, Items.REDSTONE, Potions.LONG_WATER_BREATHING);
        //Turtle Master
        PotionBrewing.addMix(Potions.AWKWARD, Items.TURTLE_HELMET, Potions.TURTLE_MASTER);
        PotionBrewing.addMix(Potions.TURTLE_MASTER, Items.REDSTONE, Potions.LONG_TURTLE_MASTER);
        PotionBrewing.addMix(Potions.TURTLE_MASTER, Items.QUARTZ, Potions.STRONG_TURTLE_MASTER);
        //Jump
        PotionBrewing.addMix(Potions.AWKWARD, Items.RABBIT_FOOT, Potions.LEAPING);
        PotionBrewing.addMix(Potions.LEAPING, Items.REDSTONE, Potions.LONG_LEAPING);
        PotionBrewing.addMix(Potions.LEAPING, Items.QUARTZ, Potions.STRONG_LEAPING);
        //Hunger
        PotionBrewing.addMix(Potions.AWKWARD, Items.ROTTEN_FLESH, PotionRegistry.HUNGER.get());
        PotionBrewing.addMix(PotionRegistry.HUNGER.get(), Items.REDSTONE, PotionRegistry.LONG_HUNGER.get());
        PotionBrewing.addMix(PotionRegistry.HUNGER.get(), Items.QUARTZ, PotionRegistry.STRONG_HUNGER.get());
        //Luck
        PotionBrewing.addMix(Potions.AWKWARD, ItemRegistry.CLOVER.get(), Potions.LUCK);
        PotionBrewing.addMix(Potions.LUCK, Items.REDSTONE, PotionRegistry.LONG_LUCK.get());
    }
}