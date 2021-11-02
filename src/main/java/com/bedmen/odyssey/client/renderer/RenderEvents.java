package com.bedmen.odyssey.client.renderer;

import com.bedmen.odyssey.client.renderer.entity.layers.OdysseyHeldItemLayer;
import com.bedmen.odyssey.items.QuiverItem;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.util.FogUtil;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Hand;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderEvents {

    /**
     * Custom Fog rendering
     */
    @SubscribeEvent
    public static void EntityViewRenderEvent$RenderFogEventListener(final EntityViewRenderEvent.FogDensity event){
        Entity entity = event.getInfo().getEntity();
        if(entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity)entity;
            if(livingEntity.hasEffect(EffectRegistry.LAVA_VISION.get()) && event.getInfo().getFluidInCamera().is(FluidTags.LAVA)){
                event.setDensity(0.05f);
                event.setCanceled(true);
            } else {
                int i = FogUtil.inFog(livingEntity);
                if(i > 0){
                    float f = 0.1f;
                    i = 9-i;
                    event.setDensity(0.2f / (f*i*i + (1-f)*i));
                    event.setCanceled(true);
                }
            }
        }
    }

    /**
     * Prevents Quiver from being rendered in hand
     */
    @SubscribeEvent
    public static void RenderHandEventListener(final RenderHandEvent event){
        Item item = event.getItemStack().getItem();
        Hand hand = event.getHand();
        if(item instanceof QuiverItem && hand == Hand.OFF_HAND){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static <T extends LivingEntity, M extends EntityModel<T> & IHasArm> void RenderLivingEvent$PreListener(final RenderLivingEvent.Pre<T,M> event){
        LivingRenderer<T,M> renderer = event.getRenderer();
        for(int i = 0; i < renderer.layers.size(); i++){
            if(renderer.layers.get(i) instanceof HeldItemLayer){
                renderer.layers.set(i, new OdysseyHeldItemLayer<>(renderer));
            }
        }

    }

}
