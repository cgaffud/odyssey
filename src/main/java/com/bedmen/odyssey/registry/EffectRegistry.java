package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;

import com.bedmen.odyssey.potions.OdysseyEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectRegistry {

    public static DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS , Odyssey.MOD_ID);
    public static DeferredRegister<Effect> EFFECTS_VANILLA = DeferredRegister.create(ForgeRegistries.POTIONS , "minecraft");

    public static void init() {
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EFFECTS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Effect> GLOWING = EFFECTS_VANILLA.register("glowing", () -> new OdysseyEffect(EffectType.NEUTRAL, 0xFFBC5E));
    public static final RegistryObject<Effect> LEVITATION = EFFECTS_VANILLA.register("levitation", () -> new OdysseyEffect(EffectType.HARMFUL, 0x976997));
    public static final RegistryObject<Effect> CONSTRUCTION = EFFECTS.register("construction", () -> new OdysseyEffect(EffectType.BENEFICIAL, 0xb75a40));
    public static final RegistryObject<Effect> BUILDING_FATIGUE = EFFECTS.register("building_fatigue", () -> new OdysseyEffect(EffectType.HARMFUL, 0x7f3e2c));
    public static final RegistryObject<Effect> BLEEDING = EFFECTS.register("bleeding", () -> new OdysseyEffect(EffectType.HARMFUL, 0x7f3e2c));
    public static final RegistryObject<Effect> LIFE_INCREASE = EFFECTS.register("life_increase", () -> new OdysseyEffect(EffectType.BENEFICIAL, 0xff0000));
    public static final RegistryObject<Effect> LAVA_VISION = EFFECTS.register("lava_vision", () -> new OdysseyEffect(EffectType.BENEFICIAL, 0xa01e80));

}