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
    public static final RegistryObject<MobEffect> FREEZING = EFFECTS.register("freezing",() -> (new TemperatureEffect(MobEffectCategory.HARMFUL, 0xd0f1f0, false)));
    public static final RegistryObject<MobEffect> ROASTING = EFFECTS.register("roasting",() -> (new TemperatureEffect(MobEffectCategory.HARMFUL, 0xff9f5a, true)));
    public static final RegistryObject<MobEffect> COOLING = EFFECTS.register("cooling",() -> (new TemperatureEffect(MobEffectCategory.BENEFICIAL, 0x565fff, false)));
    public static final RegistryObject<MobEffect> WARMING = EFFECTS.register("warming",() -> (new TemperatureEffect(MobEffectCategory.BENEFICIAL, 0xcf7200, true)));

    // Buff Sources
    public static final RegistryObject<MobEffect> STRENGTH_GAMBIT_BUFF = EFFECTS.register("strength_gambit_buff", () -> new AspectEffect(MobEffectCategory.BENEFICIAL, List.of(new AspectInstance(Aspects.ATTACK_DAMAGE, 2), new AspectInstance(Aspects.BONUS_EXPERIENCE, -0.2f / 20f))));
    public static final RegistryObject<MobEffect> STRENGTH_GAMBIT_NERF = EFFECTS.register("strength_gambit_nerf", () -> new AspectEffect(MobEffectCategory.HARMFUL, List.of(new AspectInstance(Aspects.ATTACK_DAMAGE, -2))));
    public static final RegistryObject<MobEffect> RESISTANCE_GAMBIT_BUFF = EFFECTS.register("resistance_gambit_buff", () -> new AspectEffect(MobEffectCategory.BENEFICIAL, List.of(new AspectInstance(Aspects.PROTECTION, 2), new AspectInstance(Aspects.BONUS_EXPERIENCE, -0.2f / 20f))));
    public static final RegistryObject<MobEffect> RESISTANCE_GAMBIT_NERF = EFFECTS.register("resistance_gambit_nerf", () -> new AspectEffect(MobEffectCategory.HARMFUL, List.of(new AspectInstance(Aspects.PROTECTION, -2))));
    public static final RegistryObject<MobEffect> SPEED_GAMBIT_BUFF = EFFECTS.register("speed_gambit_buff", () -> new AspectEffect(MobEffectCategory.BENEFICIAL, List.of(new AspectInstance(Aspects.MOVEMENT_SPEED, 0.2f), new AspectInstance(Aspects.BONUS_EXPERIENCE, -0.2f / 20f))));
    public static final RegistryObject<MobEffect> SPEED_GAMBIT_NERF = EFFECTS.register("speed_gambit_nerf", () -> new AspectEffect(MobEffectCategory.HARMFUL, List.of(new AspectInstance(Aspects.MOVEMENT_SPEED, -0.2f))));


}