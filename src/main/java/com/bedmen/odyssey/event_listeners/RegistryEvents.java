package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.items.odyssey_versions.OdysseySpawnEggItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    /**
     * Initiates spawn eggs
     */
    @SubscribeEvent
    public static void onRegisterEvent(final RegisterEvent event){
        if(event.getRegistryKey().equals(ForgeRegistries.Keys.ENTITY_TYPES)){
            OdysseySpawnEggItem.initSpawnEggs();
        }
    }
}