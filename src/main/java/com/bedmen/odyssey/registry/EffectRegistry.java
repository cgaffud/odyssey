package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.effect.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class EffectRegistry {

    public static DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS , Odyssey.MOD_ID);
    public static DeferredRegister<MobEffect> EFFECTS_VANILLA = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS , "minecraft");

    public static void init() {
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EFFECTS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

//    public static final RegistryObject<MobEffect> GLOWING = EFFECTS_VANILLA.register("glowing", () -> new OdysseyEffect(MobEffectCategory.NEUTRAL, 0xFFBC5E));
//    public static final RegistryObject<MobEffect> LEVITATION = EFFECTS_VANILLA.register("levitation", () -> new OdysseyEffect(MobEffectCategory.HARMFUL, 0x976997));
//    public static final RegistryObject<MobEffect> CONSTRUCTION = EFFECTS.register("construction", () -> new OdysseyEffect(MobEffectCategory.BENEFICIAL, 0xb75a40));
//    public static final RegistryObject<MobEffect> BUILDING_FATIGUE = EFFECTS.register("building_fatigue", () -> new OdysseyEffect(MobEffectCategory.HARMFUL, 0x7f3e2c));
    public static final RegistryObject<MobEffect> BLEEDING = EFFECTS.register("bleeding", () -> new OdysseyEffect(MobEffectCategory.HARMFUL, 0x7f3e2c, true));
//    public static final RegistryObject<MobEffect> LIFE_INCREASE = EFFECTS.register("life_increase", () -> new OdysseyEffect(MobEffectCategory.BENEFICIAL, 0xff0000));
//    public static final RegistryObject<MobEffect> LAVA_VISION = EFFECTS.register("lava_vision", () -> new OdysseyEffect(MobEffectCategory.BENEFICIAL, 0xa01e80));
    public static final RegistryObject<MobEffect> SHATTERED = EFFECTS.register("shattered", () -> (new OdysseyEffect(MobEffectCategory.HARMFUL, 0x1E2434, true)).addAttributeModifier(Attributes.ARMOR, "54ae752d-771a-4c38-b482-a514f2bf0177", -2.0D, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<MobEffect> DROWNING = EFFECTS.register("drowning",() -> (new OdysseyEffect(MobEffectCategory.HARMFUL, 0x3D57D6, true)));
    public static final RegistryObject<MobEffect> VULNERABLE = EFFECTS.register("vulnerable", () -> (new OdysseyEffect(MobEffectCategory.HARMFUL, 0x00000, true)));
    public static final RegistryObject<MobEffect> HEXFLAME = EFFECTS.register("hexflame", () -> new FireEffect(MobEffectCategory.HARMFUL, 0x6b2494, false, FireType.HEX));
    // TODO make soulfire blocks apply soulflame
    public static final RegistryObject<MobEffect> SOULFLAME = EFFECTS.register("soulflame", () -> new FireEffect(MobEffectCategory.HARMFUL, 0x4ff1f6, false, FireType.SOUL));
    public static final RegistryObject<MobEffect> HUSK_DRYING = EFFECTS.register("husk_drying", () -> new AspectEffect(MobEffectCategory.HARMFUL, List.of(new AspectInstance<>(Aspects.TEMPERATURE_PER_SECOND, 5f))));

    public static final RegistryObject<MobEffect> STRAY_FREEZING = EFFECTS.register("stray_freezing", () -> new AspectEffect(MobEffectCategory.HARMFUL, List.of(new AspectInstance<>(Aspects.TEMPERATURE_PER_SECOND, -5f))));

    // Buff Sources
    public static final RegistryObject<MobEffect> STRENGTH_GAMBIT_BUFF = EFFECTS.register("strength_gambit_buff", () -> new AspectEffect(MobEffectCategory.BENEFICIAL, List.of(new AspectInstance<>(Aspects.ATTACK_DAMAGE, 2f), new AspectInstance<>(Aspects.EXPERIENCE_PER_SECOND, -0.2f / 20f))));
    public static final RegistryObject<MobEffect> STRENGTH_GAMBIT_NERF = EFFECTS.register("strength_gambit_nerf", () -> new AspectEffect(MobEffectCategory.HARMFUL, List.of(new AspectInstance<>(Aspects.ATTACK_DAMAGE, -2f))));
    public static final RegistryObject<MobEffect> RESISTANCE_GAMBIT_BUFF = EFFECTS.register("resistance_gambit_buff", () -> new AspectEffect(MobEffectCategory.BENEFICIAL, List.of(new AspectInstance<>(Aspects.PROTECTION, 2f), new AspectInstance<>(Aspects.EXPERIENCE_PER_SECOND, -0.2f / 20f))));
    public static final RegistryObject<MobEffect> RESISTANCE_GAMBIT_NERF = EFFECTS.register("resistance_gambit_nerf", () -> new AspectEffect(MobEffectCategory.HARMFUL, List.of(new AspectInstance<>(Aspects.PROTECTION, -2f))));
    public static final RegistryObject<MobEffect> SPEED_GAMBIT_BUFF = EFFECTS.register("speed_gambit_buff", () -> new AspectEffect(MobEffectCategory.BENEFICIAL, List.of(new AspectInstance<>(Aspects.MOVEMENT_SPEED, 0.2f), new AspectInstance<>(Aspects.EXPERIENCE_PER_SECOND, -0.2f / 20f))));
    public static final RegistryObject<MobEffect> SPEED_GAMBIT_NERF = EFFECTS.register("speed_gambit_nerf", () -> new AspectEffect(MobEffectCategory.HARMFUL, List.of(new AspectInstance<>(Aspects.MOVEMENT_SPEED, -0.2f))));
    public static final RegistryObject<MobEffect> PARRY_STRENGTH = EFFECTS.register("parry_strength", () -> new AspectEffect(MobEffectCategory.BENEFICIAL, List.of(new AspectInstance<>(Aspects.ATTACK_DAMAGE, 2f))));
    public static final RegistryObject<MobEffect> ASSISTED_STRIKE_STRENGTH = EFFECTS.register("assisted_strike_strength", () -> new AspectEffect(MobEffectCategory.BENEFICIAL, List.of(new AspectInstance<>(Aspects.ATTACK_DAMAGE, 1f))));
    public static final RegistryObject<MobEffect> BLUDGEONING_SLOWNESS = EFFECTS.register("bludgeoning_slowness", () -> new AspectEffect(MobEffectCategory.HARMFUL, List.of(new AspectInstance<>(Aspects.MOVEMENT_SPEED, -0.2f))));
    public static final RegistryObject<MobEffect> VAMPIRIC_SPEED = EFFECTS.register("vampiric_speed", () -> new AspectEffect(MobEffectCategory.BENEFICIAL, List.of(new AspectInstance<>(Aspects.MOVEMENT_SPEED, 0.05f))));

    public static final RegistryObject<MobEffect> CAMPFIRE_WARMTH = EFFECTS.register("campfire_warmth", () -> new AspectEffect(MobEffectCategory.BENEFICIAL, List.of(new AspectInstance<>(Aspects.WARMTH, 1f))));

    public static final RegistryObject<MobEffect> WARM_FOOD = EFFECTS.register("warm_food", () -> new AspectEffect(MobEffectCategory.BENEFICIAL, List.of(new AspectInstance<>(Aspects.WARMTH, 1f))));

    public static final RegistryObject<MobEffect> COLD_FOOD = EFFECTS.register("cold_food", () -> new AspectEffect(MobEffectCategory.BENEFICIAL, List.of(new AspectInstance<>(Aspects.COOLING, 1f))));

}