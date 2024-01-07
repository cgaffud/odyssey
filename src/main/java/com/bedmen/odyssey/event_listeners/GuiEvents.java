package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.entity.OdysseySignBlockEntity;
import com.bedmen.odyssey.client.gui.screens.OdysseyCreativeModeInventoryScreen;
import com.bedmen.odyssey.client.gui.screens.OdysseyInventoryScreen;
import com.bedmen.odyssey.client.gui.screens.OdysseySignEditScreen;
import com.bedmen.odyssey.effect.FireType;
import com.bedmen.odyssey.trades.MenuWithMerchantData;
import com.bedmen.odyssey.trades.OdysseyMerchantInfo;
import com.bedmen.odyssey.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = {Dist.CLIENT}, modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GuiEvents {

    @SubscribeEvent
    public static void onScreenOpenEvent(final ScreenEvent.Opening event){
        Screen screen = event.getScreen();
        Minecraft minecraft = Minecraft.getInstance();
        if (screen instanceof SignEditScreen signEditScreen
                && signEditScreen.sign instanceof OdysseySignBlockEntity odysseySignBlockEntity) {
            event.setNewScreen(new OdysseySignEditScreen(odysseySignBlockEntity, Minecraft.getInstance().isTextFilteringEnabled()));
        }
        if(screen instanceof InventoryScreen && !(screen instanceof OdysseyInventoryScreen)){
            event.setNewScreen(new OdysseyInventoryScreen(minecraft.player));
        }
        if(screen instanceof CreativeModeInventoryScreen && !(screen instanceof OdysseyCreativeModeInventoryScreen)){
            event.setNewScreen(new OdysseyCreativeModeInventoryScreen(minecraft.player));
        }
    }

    @SubscribeEvent
    public static void onRenderBlockOverlayEvent(final RenderBlockScreenEffectEvent event) {
        Player player = event.getPlayer();
        if ((event.getOverlayType() == RenderBlockScreenEffectEvent.OverlayType.FIRE)) {
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

    @SubscribeEvent
    public static void onScreenInitPostEvent(final ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof MerchantScreen screen) {
            int i = (screen.width - 176) / 2;
            int j = (screen.height - 166) / 2;
            Button questionButton = new Button(i + 67, j + 39, 11, 11, CommonComponents.EMPTY, (button) -> {
                ItemStack itemstack1 = screen.getMenu().slots.get(0).getItem();
//                System.out.println(itemstack1.getItem().getName(itemstack1));
                ItemStack itemstack2 = screen.getMenu().slots.get(1).getItem();
//                System.out.println(itemstack2.getItem().getName(itemstack2));
                Player localplayer = screen.getMinecraft().player;
                if ((itemstack1 == null || itemstack1.isEmpty())
                        && (itemstack2 == null || itemstack2.isEmpty())) {
                    OdysseyMerchantInfo.respondToEmptyRequest(localplayer, "Villager");
                }

                if (screen.getMenu() instanceof MenuWithMerchantData menu) {
                    if (itemstack1 != null && !itemstack1.isEmpty()) {
                        OdysseyMerchantInfo.respondToVillagerRequest(localplayer, itemstack1.getItem(), menu.villagerProfession());
                    }
                }

                screen.onClose();
            });
//            questionButton.visible = false;
            event.addListener(questionButton);
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
