package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;

import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionRegistry {

    public static DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES , Odyssey.MOD_ID);
    public static DeferredRegister<Potion> POTIONS_VANILLA = DeferredRegister.create(ForgeRegistries.POTION_TYPES , "minecraft");

    public static void init() {
        POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        POTIONS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Potion> HASTE = POTIONS.register("haste", () -> new Potion(new EffectInstance(Effects.DIG_SPEED, 3600)));
    public static final RegistryObject<Potion> LONG_HASTE = POTIONS.register("long_haste", () -> new Potion(new EffectInstance(Effects.DIG_SPEED, 7200)));
    public static final RegistryObject<Potion> STRONG_HASTE = POTIONS.register("strong_haste", () -> new Potion(new EffectInstance(Effects.DIG_SPEED, 1800,1)));

    public static final RegistryObject<Potion> MINING_FATIGUE = POTIONS.register("mining_fatigue", () -> new Potion(new EffectInstance(Effects.DIG_SLOWDOWN, 1800)));
    public static final RegistryObject<Potion> LONG_MINING_FATIGUE = POTIONS.register("long_mining_fatigue", () -> new Potion(new EffectInstance(Effects.DIG_SLOWDOWN, 3600)));
    public static final RegistryObject<Potion> STRONG_MINING_FATIGUE = POTIONS.register("strong_mining_fatigue", () -> new Potion(new EffectInstance(Effects.DIG_SLOWDOWN, 900,1)));

    public static final RegistryObject<Potion> GLOW = POTIONS.register("glow", () -> new Potion(new EffectInstance(Effects.GLOWING, 1800)));
    public static final RegistryObject<Potion> LONG_GLOW = POTIONS.register("long_glow", () -> new Potion(new EffectInstance(Effects.GLOWING, 3600)));

    public static final RegistryObject<Potion> BLINDNESS = POTIONS.register("blindness", () -> new Potion(new EffectInstance(Effects.BLINDNESS, 1800)));
    public static final RegistryObject<Potion> LONG_BLINDNESS = POTIONS.register("long_blindness", () -> new Potion(new EffectInstance(Effects.BLINDNESS, 3600)));

    public static final RegistryObject<Potion> LEVITATION = POTIONS.register("levitation", () -> new Potion(new EffectInstance(Effects.LEVITATION, 900)));
    public static final RegistryObject<Potion> LONG_LEVITATION = POTIONS.register("long_levitation", () -> new Potion(new EffectInstance(Effects.LEVITATION, 1800)));
    public static final RegistryObject<Potion> STRONG_LEVITATION = POTIONS.register("strong_levitation", () -> new Potion(new EffectInstance(Effects.LEVITATION, 300,2)));

    public static final RegistryObject<Potion> HUNGER = POTIONS.register("hunger", () -> new Potion(new EffectInstance(Effects.HUNGER, 1800)));
    public static final RegistryObject<Potion> LONG_HUNGER = POTIONS.register("long_hunger", () -> new Potion(new EffectInstance(Effects.HUNGER, 3600)));
    public static final RegistryObject<Potion> STRONG_HUNGER = POTIONS.register("strong_hunger", () -> new Potion(new EffectInstance(Effects.HUNGER, 900,1)));

    public static final RegistryObject<Potion> CONSTRUCTION = POTIONS.register("construction", () -> new Potion(new EffectInstance(EffectRegistry.CONSTRUCTION.get(), 3600)));
    public static final RegistryObject<Potion> LONG_CONSTRUCTION = POTIONS.register("long_construction", () -> new Potion(new EffectInstance(EffectRegistry.CONSTRUCTION.get(), 7200)));
    public static final RegistryObject<Potion> STRONG_CONSTRUCTION = POTIONS.register("strong_construction", () -> new Potion(new EffectInstance(EffectRegistry.CONSTRUCTION.get(), 1800,1)));

    public static final RegistryObject<Potion> BUILDING_FATIGUE = POTIONS.register("building_fatigue", () -> new Potion(new EffectInstance(EffectRegistry.BUILDING_FATIGUE.get(), 1800)));
    public static final RegistryObject<Potion> LONG_BUILDING_FATIGUE = POTIONS.register("long_building_fatigue", () -> new Potion(new EffectInstance(EffectRegistry.BUILDING_FATIGUE.get(), 3600)));

    public static final RegistryObject<Potion> LUCK = POTIONS_VANILLA.register("luck", () -> new Potion(new EffectInstance(Effects.LUCK, 3600)));
    public static final RegistryObject<Potion> LONG_LUCK = POTIONS.register("long_luck", () -> new Potion(new EffectInstance(Effects.LUCK, 7200)));

    public static final RegistryObject<Potion> LONG_NIGHT_VISION = POTIONS_VANILLA.register("long_night_vision", () -> new Potion("night_vision", new EffectInstance(Effects.NIGHT_VISION, 7200)));
    public static final RegistryObject<Potion> LONG_INVISIBILITY = POTIONS_VANILLA.register("long_invisibility", () -> new Potion("invisibility", new EffectInstance(Effects.INVISIBILITY, 7200)));
    public static final RegistryObject<Potion> LONG_LEAPING = POTIONS_VANILLA.register("long_leaping", () -> new Potion("leaping", new EffectInstance(Effects.JUMP, 7200)));
    public static final RegistryObject<Potion> LONG_FIRE_RESISTANCE = POTIONS_VANILLA.register("long_fire_resistance", () -> new Potion("fire_resistance", new EffectInstance(Effects.FIRE_RESISTANCE, 7200)));
    public static final RegistryObject<Potion> LONG_SWIFTNESS = POTIONS_VANILLA.register("long_swiftness", () -> new Potion("swiftness", new EffectInstance(Effects.MOVEMENT_SPEED, 7200)));
    public static final RegistryObject<Potion> LONG_SLOWNESS = POTIONS_VANILLA.register("long_slowness", () -> new Potion("slowness", new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 3600)));
    public static final RegistryObject<Potion> LONG_WATER_BREATHING = POTIONS_VANILLA.register("long_water_breathing", () -> new Potion("water_breathing", new EffectInstance(Effects.WATER_BREATHING, 7200)));
    public static final RegistryObject<Potion> LONG_STRENGTH = POTIONS_VANILLA.register("long_strength", () -> new Potion("strength", new EffectInstance(Effects.DAMAGE_BOOST, 7200)));
    public static final RegistryObject<Potion> LONG_WEAKNESS = POTIONS_VANILLA.register("long_weakness", () -> new Potion("weakness", new EffectInstance(Effects.WEAKNESS, 3600)));
    public static final RegistryObject<Potion> LONG_SLOW_FALLING = POTIONS_VANILLA.register("long_slow_falling", () -> new Potion("slow_falling", new EffectInstance(Effects.SLOW_FALLING, 3600)));
}