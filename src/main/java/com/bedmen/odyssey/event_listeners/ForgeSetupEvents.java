package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.commands.ModifyCommand;
import com.bedmen.odyssey.items.odyssey_versions.OdysseySpawnEggItem;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeSetupEvents {

    @SubscribeEvent
    public static void onRegisterCommandsEvent(final RegisterCommandsEvent event){
        ModifyCommand.register(event.getDispatcher());
    }
}