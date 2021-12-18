package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.JumpKeyPressedPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InputEvents {

    @SubscribeEvent
    public static void KeyboardKeyPressedEventListener(final InputEvent.KeyInputEvent event){
        if(event.getKey() == Minecraft.getInstance().options.keyJump.getKey().getValue() && event.getAction() == GLFW.GLFW_PRESS){
            try{
                OdysseyNetwork.CHANNEL.sendToServer(new JumpKeyPressedPacket());
            } catch(NullPointerException e){
            }
        }
    }
}
