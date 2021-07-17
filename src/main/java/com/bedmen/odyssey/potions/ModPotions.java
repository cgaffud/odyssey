package com.bedmen.odyssey.potions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.bedmen.odyssey.util.ItemRegistry;
import com.bedmen.odyssey.util.PotionRegistry;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.renderer.color.ItemColors;

public class ModPotions {

    private static Method addMixMethod;
    private static Method addContainerMethod;
    private static Method addContainerRecipeMethod;
    private static java.util.Map<net.minecraftforge.registries.IRegistryDelegate<Item>, IItemColor> color;
    private static List brewing_list;

    private static void addMix(Potion start, Item ingredient, Potion result) {
        if(addMixMethod == null) {
            addMixMethod = ObfuscationReflectionHelper.findMethod(PotionBrewing.class, "addMix", Potion.class, Item.class, Potion.class);
            addMixMethod.setAccessible(true);
        }

        try {
            addMixMethod.invoke(null, start, ingredient, result);
        }
        catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
            e.printStackTrace();
        }
    }

    private static void addContainer(Item start) {
        if(addContainerMethod == null) {
            addContainerMethod = ObfuscationReflectionHelper.findMethod(PotionBrewing.class, "addContainer", Item.class);
            addContainerMethod.setAccessible(true);
        }

        try {
            addContainerMethod.invoke(null, start);
        }
        catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
            e.printStackTrace();
        }
    }

    private static void addContainerRecipe(Item start, Item ingredient, Item result) {
        if(addContainerRecipeMethod == null) {
            addContainerRecipeMethod = ObfuscationReflectionHelper.findMethod(PotionBrewing.class, "addContainerRecipe", Item.class, Item.class, Item.class);
            addContainerRecipeMethod.setAccessible(true);
        }

        try {
            addContainerRecipeMethod.invoke(null, start, ingredient, result);
        }
        catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
            e.printStackTrace();
        }
    }

    private static void removeMix(){
        if(brewing_list == null) {
            brewing_list = ObfuscationReflectionHelper.getPrivateValue(PotionBrewing.class, null, "POTION_MIXES");
        }

        while(!brewing_list.isEmpty()){
            brewing_list.remove(0);
        }
    }

    public static void addBrewingRecipes() {
        removeMix();
        //addColorRegister();

        //Awkward Potion
        addMix(Potions.WATER, Items.NETHER_WART, Potions.AWKWARD);

        //Speed
        addMix(Potions.AWKWARD, Items.SUGAR, Potions.SWIFTNESS);
        addMix(Potions.SWIFTNESS, Items.REDSTONE, Potions.LONG_SWIFTNESS);
        addMix(Potions.SWIFTNESS, Items.QUARTZ, Potions.STRONG_SWIFTNESS);
        //Slowness
        addMix(Potions.AWKWARD, Items.IRON_INGOT, Potions.SLOWNESS);
        addMix(Potions.SLOWNESS, Items.REDSTONE, Potions.LONG_SLOWNESS);
        addMix(Potions.SLOWNESS, Items.QUARTZ, Potions.STRONG_SLOWNESS);
        //Haste
        addMix(Potions.AWKWARD, Items.PRISMARINE_CRYSTALS, PotionRegistry.HASTE.get());
        addMix(PotionRegistry.HASTE.get(), Items.REDSTONE, PotionRegistry.LONG_HASTE.get());
        addMix(PotionRegistry.HASTE.get(), Items.QUARTZ, PotionRegistry.STRONG_HASTE.get());
        //Mining Fatigue
        addMix(Potions.AWKWARD, Items.PRISMARINE_SHARD, PotionRegistry.MINING_FATIGUE.get());
        addMix(PotionRegistry.MINING_FATIGUE.get(), Items.REDSTONE, PotionRegistry.LONG_MINING_FATIGUE.get());
        addMix(PotionRegistry.MINING_FATIGUE.get(), Items.QUARTZ, PotionRegistry.STRONG_MINING_FATIGUE.get());
        //Construction
        addMix(Potions.AWKWARD, Items.BRICK, PotionRegistry.CONSTRUCTION.get());
        addMix(PotionRegistry.CONSTRUCTION.get(), Items.REDSTONE, PotionRegistry.LONG_CONSTRUCTION.get());
        addMix(PotionRegistry.CONSTRUCTION.get(), Items.QUARTZ, PotionRegistry.STRONG_CONSTRUCTION.get());
        //Building Fatigue
        addMix(Potions.AWKWARD, Items.NETHER_BRICK, PotionRegistry.BUILDING_FATIGUE.get());
        addMix(PotionRegistry.BUILDING_FATIGUE.get(), Items.REDSTONE, PotionRegistry.LONG_BUILDING_FATIGUE.get());
        //Strength
        addMix(Potions.AWKWARD, Items.RED_MUSHROOM, Potions.STRENGTH);
        addMix(Potions.STRENGTH, Items.REDSTONE, Potions.LONG_STRENGTH);
        addMix(Potions.STRENGTH, Items.QUARTZ, Potions.STRONG_STRENGTH);
        //Weakness
        addMix(Potions.AWKWARD, Items.BROWN_MUSHROOM, Potions.WEAKNESS);
        addMix(Potions.WEAKNESS, Items.REDSTONE, Potions.LONG_WEAKNESS);
        //Instant Health
        addMix(Potions.AWKWARD, Items.GLISTERING_MELON_SLICE, Potions.HEALING);
        addMix(Potions.HEALING, Items.QUARTZ, Potions.STRONG_HEALING);
        //Instant Damage
        addMix(Potions.AWKWARD, Items.SPIDER_EYE, Potions.HARMING);
        addMix(Potions.HARMING, Items.QUARTZ, Potions.STRONG_HARMING);
        //Regeneration
        addMix(Potions.AWKWARD, Items.GHAST_TEAR, Potions.REGENERATION);
        addMix(Potions.REGENERATION, Items.REDSTONE, Potions.LONG_REGENERATION);
        addMix(Potions.REGENERATION, Items.QUARTZ, Potions.STRONG_REGENERATION);
        //Poison
        addMix(Potions.AWKWARD, Items.POISONOUS_POTATO, Potions.POISON);
        addMix(Potions.POISON, Items.REDSTONE, Potions.LONG_POISON);
        addMix(Potions.POISON, Items.QUARTZ, Potions.STRONG_POISON);
        //Glowing
        addMix(Potions.AWKWARD, Items.GLOWSTONE_DUST, PotionRegistry.GLOW.get());
        addMix(PotionRegistry.GLOW.get(), Items.REDSTONE, PotionRegistry.LONG_GLOW.get());
        //Invisibility
        addMix(Potions.AWKWARD, Items.GLASS, Potions.INVISIBILITY);
        addMix(Potions.INVISIBILITY, Items.REDSTONE, Potions.LONG_INVISIBILITY);
        //Night Vision
        addMix(Potions.AWKWARD, Items.GOLDEN_CARROT, Potions.NIGHT_VISION);
        addMix(Potions.NIGHT_VISION, Items.REDSTONE, Potions.LONG_NIGHT_VISION);
        //Blindness
        addMix(Potions.AWKWARD, Items.FERMENTED_SPIDER_EYE, PotionRegistry.BLINDNESS.get());
        addMix(PotionRegistry.BLINDNESS.get(), Items.REDSTONE, PotionRegistry.LONG_BLINDNESS.get());
        //Slow Falling
        addMix(Potions.AWKWARD, Items.PHANTOM_MEMBRANE, Potions.SLOW_FALLING);
        addMix(Potions.SLOW_FALLING, Items.REDSTONE, Potions.LONG_SLOW_FALLING);
        //Levitation
        addMix(Potions.AWKWARD, Items.SHULKER_SHELL, PotionRegistry.LEVITATION.get());
        addMix(PotionRegistry.LEVITATION.get(), Items.REDSTONE, PotionRegistry.LONG_LEVITATION.get());
        addMix(PotionRegistry.LEVITATION.get(), Items.QUARTZ, PotionRegistry.STRONG_LEVITATION.get());
        //Fire Resistance
        addMix(Potions.AWKWARD, Items.MAGMA_CREAM, Potions.FIRE_RESISTANCE);
        addMix(Potions.FIRE_RESISTANCE, Items.REDSTONE, Potions.LONG_FIRE_RESISTANCE);
        //Water Breathing
        addMix(Potions.AWKWARD, Items.PUFFERFISH, Potions.WATER_BREATHING);
        addMix(Potions.WATER_BREATHING, Items.REDSTONE, Potions.LONG_WATER_BREATHING);
        //Turtle Master
        addMix(Potions.AWKWARD, Items.TURTLE_HELMET, Potions.TURTLE_MASTER);
        addMix(Potions.TURTLE_MASTER, Items.REDSTONE, Potions.LONG_TURTLE_MASTER);
        addMix(Potions.TURTLE_MASTER, Items.QUARTZ, Potions.STRONG_TURTLE_MASTER);
        //Jump
        addMix(Potions.AWKWARD, Items.RABBIT_FOOT, Potions.LEAPING);
        addMix(Potions.LEAPING, Items.REDSTONE, Potions.LONG_LEAPING);
        addMix(Potions.LEAPING, Items.QUARTZ, Potions.STRONG_LEAPING);
        //Hunger
        addMix(Potions.AWKWARD, Items.ROTTEN_FLESH, PotionRegistry.HUNGER.get());
        addMix(PotionRegistry.HUNGER.get(), Items.REDSTONE, PotionRegistry.LONG_HUNGER.get());
        addMix(PotionRegistry.HUNGER.get(), Items.QUARTZ, PotionRegistry.STRONG_HUNGER.get());
        //Luck
        addMix(Potions.AWKWARD, ItemRegistry.CLOVER.get(), Potions.LUCK);
    }

}