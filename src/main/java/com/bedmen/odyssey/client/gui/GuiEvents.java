package com.bedmen.odyssey.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GuiEvents {

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
