package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.tileentity.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundEventRegistry {

    public static DeferredRegister<SoundEvent> SOUND_EVENTS_VANILLA = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS , "minecraft");
    public static DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS , Odyssey.MOD_ID);

    public static void init() {
        SOUND_EVENTS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SoundEvent> KEY_UNLOCK = SOUND_EVENTS.register("key_unlock", () -> new SoundEvent(new ResourceLocation(Odyssey.MOD_ID, "block.key_unlock")));
}
