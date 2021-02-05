package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;

import com.bedmen.odyssey.potions.ModEffect;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AttributeRegistry {

    public static DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES , Odyssey.MOD_ID);
    public static DeferredRegister<Attribute> ATTRIBUTES_VANILLA = DeferredRegister.create(ForgeRegistries.ATTRIBUTES , "minecraft");

    public static void init() {
        ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ATTRIBUTES_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //public static final RegistryObject<Attribute> ARMOR = ATTRIBUTES_VANILLA.register("generic.armor", () -> (new RangedAttribute("attribute.name.generic.armor", 0.0D, 0.0D, 120.0D)).setShouldWatch(true));

}