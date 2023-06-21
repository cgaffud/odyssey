package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.object.Aspect;
import com.bedmen.odyssey.commands.arguments.ItemModifierArgument;
import com.bedmen.odyssey.world.gen.OdysseyGeneration;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.ItemEnchantmentArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.checkerframework.checker.units.qual.A;

import java.util.function.Supplier;

public class ArgumentTypeRegistry {

    public static DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, Odyssey.MOD_ID);
    public static void init() {
        ARGUMENT_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<ArgumentTypeInfo<?, ?>> ITEM_MODIFIER = register("item_modifier", () -> SingletonArgumentInfo.contextFree(ItemModifierArgument::modifier), ItemModifierArgument.class);

    private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>> RegistryObject<ArgumentTypeInfo<?, ?>> register(String key, Supplier<I> supplier, Class<A> infoClass){
        ArgumentTypeInfos.registerByClass(ItemModifierArgument.class, SingletonArgumentInfo.contextFree(ItemModifierArgument::modifier));
        ArgumentTypeInfos.registerByClass(infoClass, supplier.get());
        return ARGUMENT_TYPES.register(key, supplier);
    }
}