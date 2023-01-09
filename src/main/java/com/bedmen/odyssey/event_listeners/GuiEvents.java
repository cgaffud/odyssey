package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.entity.OdysseySignBlockEntity;
import com.bedmen.odyssey.client.gui.screens.OdysseySignEditScreen;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
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
        //todo trinkets / amulets
//        if(gui instanceof CreativeModeInventoryScreen){
//            event.setGui(new OdysseyCreativeScreen(((CreativeModeInventoryScreen) gui).inventory.player));
//        }
    }

    @SubscribeEvent
    public static void onRenderBlockOverlayEvent(final RenderBlockOverlayEvent event) {
        Player player = event.getPlayer();
        if ((event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE)) {
            // Check if the event has the incorrect overlay
            if (player.hasEffect(EffectRegistry.HEXFLAME.get())) {
                // Grab other info from event and kill it
                PoseStack mat = event.getPoseStack();
                event.setCanceled(true);
                // Send a new one with the right info
                RenderUtil.renderModdedFire(Minecraft.getInstance(), mat, RenderUtil.HEX_FIRE);
            }
        }
    }

//    @SubscribeEvent
//    public static void onRenderLevelLastEvent(final RenderLevelLastEvent event) {
//        Minecraft minecraft =  Minecraft.getInstance();
//        PoseStack poseStack = event.getPoseStack();
//        // In survival/adventure & in first person view so overlays can occur
//        if ((minecraft.cameraEntity instanceof Player player) && (!player.noPhysics)
//            && (!player.isSpectator()) && (!player.isSleeping()) && (minecraft.options.getCameraType().isFirstPerson())) {
//
//            if (player.hasEffect(EffectRegistry.HEXFLAME.get())) {
//                ForgeEventFactory.renderBlockOverlay(player, poseStack, RenderBlockOverlayEvent.OverlayType.FIRE, Blocks.SOUL_FIRE.defaultBlockState(), player.blockPosition());
//            }
//        }
//    }
}
