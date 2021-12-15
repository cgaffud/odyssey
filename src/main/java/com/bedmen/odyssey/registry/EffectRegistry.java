package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.potions.OdysseyEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

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
    public static final RegistryObject<MobEffect> BLEEDING = EFFECTS.register("bleeding", () -> new OdysseyEffect(MobEffectCategory.HARMFUL, 0x7f3e2c));
//    public static final RegistryObject<MobEffect> LIFE_INCREASE = EFFECTS.register("life_increase", () -> new OdysseyEffect(MobEffectCategory.BENEFICIAL, 0xff0000));
//    public static final RegistryObject<MobEffect> LAVA_VISION = EFFECTS.register("lava_vision", () -> new OdysseyEffect(MobEffectCategory.BENEFICIAL, 0xa01e80));
    public static final RegistryObject<MobEffect> SHATTERED = EFFECTS.register("shattered", () -> (new OdysseyEffect(MobEffectCategory.HARMFUL, 0x1E2434)).addAttributeModifier(Attributes.ARMOR, "54ae752d-771a-4c38-b482-a514f2bf0177", -2.0D, AttributeModifier.Operation.ADDITION));
}