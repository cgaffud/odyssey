package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.entity.OdysseySignBlockEntity;
import com.bedmen.odyssey.client.gui.screens.OdysseyCreativeModeInventoryScreen;
import com.bedmen.odyssey.client.gui.screens.OdysseyInventoryScreen;
import com.bedmen.odyssey.client.gui.screens.OdysseySignEditScreen;
import com.bedmen.odyssey.effect.FireType;
import com.bedmen.odyssey.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
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
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft != null){
            if(screen instanceof SignEditScreen signEditScreen
                    && signEditScreen.sign instanceof OdysseySignBlockEntity odysseySignBlockEntity){
                event.setScreen(new OdysseySignEditScreen(odysseySignBlockEntity, Minecraft.getInstance().isTextFilteringEnabled()));
            }
            if(screen instanceof InventoryScreen && !(screen instanceof OdysseyInventoryScreen)){
                event.setScreen(new OdysseyInventoryScreen(minecraft.player));
            }
            if(screen instanceof CreativeModeInventoryScreen && !(screen instanceof OdysseyCreativeModeInventoryScreen)){
                event.setScreen(new OdysseyCreativeModeInventoryScreen(minecraft.player));
            }
        }
    }

    @SubscribeEvent
    public static void onRenderBlockOverlayEvent(final RenderBlockOverlayEvent event) {
        Player player = event.getPlayer();
        if ((event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE)) {
            // Check if the player has a strong fire type
            FireType fireType = RenderUtil.getStrongestFireType(player);
            if(fireType.isNotNone()){
                // Cancel the event
                event.setCanceled(true);
                // Send a new one with the right info
                RenderUtil.renderFireTypeBlockOverlay(event.getPoseStack(), fireType);
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
