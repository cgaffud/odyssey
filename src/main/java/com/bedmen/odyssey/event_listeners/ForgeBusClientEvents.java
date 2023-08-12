package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.OdysseyPlayerRenderer;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.effect.FireType;
import com.bedmen.odyssey.entity.player.OdysseyPlayer;
import com.bedmen.odyssey.items.WarpTotemItem;
import com.bedmen.odyssey.items.aspect_items.AspectBowItem;
import com.bedmen.odyssey.items.aspect_items.QuiverItem;
import com.bedmen.odyssey.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpyglassItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.checkerframework.checker.units.qual.min;

@Mod.EventBusSubscriber(value = {Dist.CLIENT}, modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeBusClientEvents {

    @SubscribeEvent
    public static void EntityViewRenderEvent$RenderFogEventListener(final ViewportEvent.RenderFog event){
        if(event.getMode() == FogRenderer.FogMode.FOG_TERRAIN){
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer localPlayer = minecraft.player;
            if(localPlayer instanceof OdysseyPlayer odysseyPlayer){
                float blizzardFogScale = odysseyPlayer.getBlizzardFogScale((float) event.getPartialTick());
                if(blizzardFogScale < 1f){
                    float nearDistance = Mth.lerp(blizzardFogScale, 0f, event.getNearPlaneDistance());
                    float farDistance = Mth.lerp(blizzardFogScale, 16f, event.getFarPlaneDistance());
                    event.setNearPlaneDistance(nearDistance);
                    event.setFarPlaneDistance(farDistance);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void EntityViewRenderEvent$FogColors(final ViewportEvent.ComputeFogColor event){
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if(localPlayer instanceof OdysseyPlayer odysseyPlayer){
            float blizzardFogScale = odysseyPlayer.getBlizzardFogScale((float) event.getPartialTick());
            if(blizzardFogScale < 1f){
                event.setRed(event.getRed() * Mth.lerp(blizzardFogScale, 0.6f, 1.0f));
                event.setGreen(event.getGreen() * Mth.lerp(blizzardFogScale, 0.6f, 1.0f));
                event.setBlue(event.getBlue() * Mth.lerp(blizzardFogScale, 0.7f, 1.0f));
            }
        }
    }

    /**
     * Prevents Quiver from being rendered in hand
     */
    @SubscribeEvent
    public static void onRenderHandEvent(final RenderHandEvent event){
        Minecraft minecraft = Minecraft.getInstance();
        ItemInHandRenderer.HandRenderSelection iteminhandrenderer$handrenderselection = RenderUtil.evaluateWhichHandsToRender(minecraft.player);
        if(event.getHand() == InteractionHand.MAIN_HAND && !iteminhandrenderer$handrenderselection.renderMainHand){
            event.setCanceled(true);
            return;
        }
        if(event.getHand() == InteractionHand.OFF_HAND && !iteminhandrenderer$handrenderselection.renderOffHand){
            event.setCanceled(true);
            return;
        }
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();
        InteractionHand interactionHand = event.getHand();
        boolean isMainHand = interactionHand == InteractionHand.MAIN_HAND;
        if(item instanceof QuiverItem && !isMainHand){
            event.setCanceled(true);
        } else {
            if(minecraft.player != null && WeaponUtil.isDualWielding(minecraft.player)){
                ItemInHandRenderer itemInHandRenderer = minecraft.gameRenderer.itemInHandRenderer;
                itemInHandRenderer.renderArmWithItem(minecraft.player, event.getPartialTick(), event.getInterpolatedPitch(), interactionHand, event.getSwingProgress(), event.getItemStack(), 0.0f, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
                event.setCanceled(true);
            }
        }
    }

    /**
     * Adjusts PlayerRenderer Layers
     */
    @SubscribeEvent
    public static void onRenderPlayerEvent$Pre(final RenderPlayerEvent.Pre event){
        PlayerRenderer playerRenderer = event.getRenderer();
        Player player = event.getEntity();
        if(playerRenderer instanceof OdysseyPlayerRenderer odysseyPlayerRenderer && player instanceof AbstractClientPlayer abstractClientPlayer){
            odysseyPlayerRenderer.setModelProperties(abstractClientPlayer);
        }
    }

    /**
     * Adjusts FOV for sniper bow
     */
    @SubscribeEvent
    public static void onFOVModifierEvent(final ComputeFovModifierEvent event) {
        Player player = event.getPlayer();
        if(Minecraft.getInstance().options.getCameraType().isFirstPerson() && player instanceof OdysseyPlayer odysseyPlayer && odysseyPlayer.isSniperScoping()){
            event.setNewFovModifier(SpyglassItem.ZOOM_FOV_MODIFIER);
        }
        else if (player.isUsingItem()) {
            ItemStack itemStack = player.getUseItem();
            Item item = itemStack.getItem();
            float maxFOVUseTime = -1.0f;
            float maxFOVDecrease = 0.15f;
            if(item instanceof AspectBowItem && !itemStack.is(Items.BOW)){
                maxFOVUseTime = 20.0f;
            } else if(item instanceof WarpTotemItem){
                maxFOVUseTime = item.getUseDuration(itemStack);
                maxFOVDecrease = 0.3f;
            }
            if(maxFOVUseTime > 0.0f){
                int i = player.getTicksUsingItem();
                float f1 = (float)i / maxFOVUseTime;
                if (f1 > 1.0F) {
                    f1 = 1.0F;
                } else {
                    f1 *= f1;
                }
                event.setNewFovModifier(event.getFovModifier() * (1.0F - f1 * maxFOVDecrease));
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLivingEvent(final RenderLivingEvent event){
        LivingEntity livingEntity = event.getEntity();
        FireType fireType = RenderUtil.getStrongestFireType(livingEntity);
        RenderUtil.renderFireTypeExternalView(livingEntity, fireType, event.getPoseStack(), event.getMultiBufferSource());
    }
}
