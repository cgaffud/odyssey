package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.items.equipment.ProjectileLaunchItem;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.ShootSwungProjectilePacket;
import com.bedmen.odyssey.network.packet.SwungWithVolatilePacket;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = {Dist.CLIENT}, modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InputEvents {

    @SubscribeEvent
    public static void onClickInputEvent(final InputEvent.ClickInputEvent event){
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        InteractionHand hand = event.getHand();
        if(localPlayer != null){
            ItemStack itemStack = localPlayer.getItemInHand(hand);
            if (EnchantmentUtil.hasVolatile(itemStack)){
                OdysseyNetwork.CHANNEL.sendToServer(new SwungWithVolatilePacket());
            }
            if(event.isAttack()){
                if(itemStack.getItem() instanceof ProjectileLaunchItem && localPlayer.getAttackStrengthScale(0.5F) > 0.9f){
                    localPlayer.resetAttackStrengthTicker();
                    OdysseyNetwork.CHANNEL.sendToServer(new ShootSwungProjectilePacket(hand));
                }
            }
        }
    }
}
