package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.JumpKeyPressedPacket;
import com.bedmen.odyssey.network.packet.ShootSonicBoomPacket;
import com.bedmen.odyssey.network.packet.SwungWithVolatilePacket;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
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

    @SubscribeEvent
    public static void ClickInputEventListener(final InputEvent.ClickInputEvent event){
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        InteractionHand hand = event.getHand();
        if(localPlayer != null){
            ItemStack itemStack = localPlayer.getItemInHand(hand);
            if (EnchantmentUtil.hasVolatile(itemStack)){
                //TODO add SwungWithVolatile to also send when attacking mobs
                OdysseyNetwork.CHANNEL.sendToServer(new SwungWithVolatilePacket());
            }
            if(event.isAttack()){
                if(itemStack.getItem() == ItemRegistry.RUSTY_SONIC_FORK.get() && localPlayer.getAttackStrengthScale(0.5F) > 0.9f){
                    localPlayer.resetAttackStrengthTicker();
                    OdysseyNetwork.CHANNEL.sendToServer(new ShootSonicBoomPacket(hand));
                }
            }
        }
    }
}
