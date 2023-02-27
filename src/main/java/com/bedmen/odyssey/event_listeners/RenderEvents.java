package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.OdysseyPlayerRenderer;
import com.bedmen.odyssey.entity.player.OdysseyPlayer;
import com.bedmen.odyssey.items.WarpTotemItem;
import com.bedmen.odyssey.items.aspect_items.AspectBowItem;
import com.bedmen.odyssey.items.aspect_items.QuiverItem;
import com.bedmen.odyssey.effect.FireType;
import com.bedmen.odyssey.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpyglassItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.FOVModifierEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = {Dist.CLIENT}, modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderEvents {

//    /**
//     * Custom Fog rendering
//     */
//    @SubscribeEvent
//    public static void EntityViewRenderEvent$RenderFogEventListener(final EntityViewRenderEvent.FogDensity event){
//        Entity entity = event.getInfo().getEntity();
//        if(entity instanceof LivingEntity){
//            LivingEntity livingEntity = (LivingEntity)entity;
//            if(livingEntity.hasEffect(EffectRegistry.LAVA_VISION.get()) && event.getInfo().getFluidInCamera().is(FluidTags.LAVA)){
//                event.setDensity(0.05f);
//                event.setCanceled(true);
//            } else {
//                int i = FogUtil.inFog(livingEntity);
//                if(i > 0){
//                    float f = 0.1f;
//                    i = 9-i;
//                    event.setDensity(0.2f / (f*i*i + (1-f)*i));
//                    event.setCanceled(true);
//                }
//            }
//        }
//    }

    /**
     * Prevents Quiver from being rendered in hand
     */
    @SubscribeEvent
    public static void onRenderHandEvent(final RenderHandEvent event){
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();
        InteractionHand hand = event.getHand();
        boolean isMainHand = hand == InteractionHand.MAIN_HAND;
        if(item instanceof QuiverItem && !isMainHand){
            event.setCanceled(true);
        }
    }

    /**
     * Adjusts PlayerRenderer Layers
     */
    @SubscribeEvent
    public static void onRenderPlayerEvent$Pre(final RenderPlayerEvent.Pre event){
        PlayerRenderer playerRenderer = event.getRenderer();
        Player player = event.getPlayer();
        if(playerRenderer instanceof OdysseyPlayerRenderer odysseyPlayerRenderer && player instanceof AbstractClientPlayer abstractClientPlayer){
            odysseyPlayerRenderer.setModelProperties(abstractClientPlayer);
        }
    }

    /**
     * Adjusts FOV for sniper bow
     */
    @SubscribeEvent
    public static void onFOVModifierEvent(final FOVModifierEvent event) {
        Player player = event.getEntity();
        if(Minecraft.getInstance().options.getCameraType().isFirstPerson() && player instanceof OdysseyPlayer odysseyPlayer && odysseyPlayer.isSniperScoping()){
            event.setNewfov(SpyglassItem.ZOOM_FOV_MODIFIER);
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
                event.setNewfov(event.getFov() * (1.0F - f1 * maxFOVDecrease));
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
