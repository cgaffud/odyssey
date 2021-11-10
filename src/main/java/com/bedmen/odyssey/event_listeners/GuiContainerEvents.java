package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.gui.OdysseyCreativeScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GuiContainerEvents {

    /**
     * Custom Creative Screen
     */
    @SubscribeEvent
    public static void GuiOpenEventListener(final GuiOpenEvent event){
        Screen gui = event.getGui();
        if(gui instanceof CreativeScreen){
            event.setGui(new OdysseyCreativeScreen(((CreativeScreen) gui).inventory.player));
        }
    }

}
