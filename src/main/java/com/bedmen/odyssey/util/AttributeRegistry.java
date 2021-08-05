package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AttributeRegistry {

    public static DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES , Odyssey.MOD_ID);

    public static void init() {
        ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //public static final RegistryObject<Attribute> ARMOR = ATTRIBUTES.register("generic.armor", () -> (new RangedAttribute("attribute.name.generic.armor", 0.0D, 0.0D, 1024.0D)).setSyncable(true));

}