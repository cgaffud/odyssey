package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.entity.OdysseySignBlockEntity;
import com.bedmen.odyssey.client.gui.screens.OdysseyInventoryScreen;
import com.bedmen.odyssey.client.gui.screens.OdysseySignEditScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = {Dist.CLIENT}, modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GuiEvents {

    @SubscribeEvent
    public static void onScreenOpenEvent(final ScreenOpenEvent event){
        Screen screen = event.getScreen();
        if(screen instanceof SignEditScreen signEditScreen && signEditScreen.sign instanceof OdysseySignBlockEntity odysseySignBlockEntity){
            event.setScreen(new OdysseySignEditScreen(odysseySignBlockEntity, Minecraft.getInstance().isTextFilteringEnabled()));
        }
        if(screen instanceof InventoryScreen && !(screen instanceof OdysseyInventoryScreen)){
            Minecraft minecraft = Minecraft.getInstance();
            if(minecraft != null){
                event.setScreen(new OdysseyInventoryScreen(minecraft.player));
            }
        }
    }

}
