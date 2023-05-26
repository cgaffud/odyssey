package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundEventRegistry {

    public static DeferredRegister<SoundEvent> SOUND_EVENTS_VANILLA = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS , "minecraft");
    public static DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS , Odyssey.MOD_ID);

    public static void init() {
        SOUND_EVENTS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Locked Chests
    public static final RegistryObject<SoundEvent> KEY_UNLOCK = SOUND_EVENTS.register("key_unlock", () -> new SoundEvent(new ResourceLocation(Odyssey.MOD_ID, "block.key_unlock")));
    public static final RegistryObject<SoundEvent> LOCKED_CHEST = SOUND_EVENTS.register("locked_chest", () -> new SoundEvent(new ResourceLocation(Odyssey.MOD_ID, "block.locked_chest")));

    // Mineral Leviathan
    public static final RegistryObject<SoundEvent> MINERAL_LEVIATHAN_ROAR = SOUND_EVENTS.register("mineral_leviathan_roar", () -> new SoundEvent(new ResourceLocation(Odyssey.MOD_ID, "entity.mineral_leviathan.roar")));
    public static final RegistryObject<SoundEvent> MINERAL_LEVIATHAN_HURT = SOUND_EVENTS.register("mineral_leviathan_hurt", () -> new SoundEvent(new ResourceLocation(Odyssey.MOD_ID, "entity.mineral_leviathan.hurt")));
    public static final RegistryObject<SoundEvent> MINERAL_LEVIATHAN_DEATH = SOUND_EVENTS.register("mineral_leviathan_death", () -> new SoundEvent(new ResourceLocation(Odyssey.MOD_ID, "entity.mineral_leviathan.death")));

    // Wraith
    public static final RegistryObject<SoundEvent> WRAITH_HURT = SOUND_EVENTS.register("wraith_hurt", () -> new SoundEvent(new ResourceLocation(Odyssey.MOD_ID, "entity.wraith.hurt")));
    public static final RegistryObject<SoundEvent> WRAITH_DEATH = SOUND_EVENTS.register("wraith_death", () -> new SoundEvent(new ResourceLocation(Odyssey.MOD_ID, "entity.wraith.death")));
    public static final RegistryObject<SoundEvent> WRAITH_LAUGH = SOUND_EVENTS.register("wraith_laugh", () -> new SoundEvent(new ResourceLocation(Odyssey.MOD_ID, "entity.wraith.laugh")));
    public static final RegistryObject<SoundEvent> WRAITH_SCREAM = SOUND_EVENTS.register("wraith_scream", () -> new SoundEvent(new ResourceLocation(Odyssey.MOD_ID, "entity.wraith.scream")));

    // Music Discs
    public static final RegistryObject<SoundEvent> MUSIC_DISC_MESA = SOUND_EVENTS.register("music_disc_mesa", () -> new SoundEvent(new ResourceLocation(Odyssey.MOD_ID, "music_disc.mesa")));
}
