package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.OdysseyPlayerRenderer;
import com.bedmen.odyssey.client.renderer.entity.layer.QuiverLayer;
import com.bedmen.odyssey.items.QuiverItem;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
    public static void RenderHandEventListener(final RenderHandEvent event){
        Item item = event.getItemStack().getItem();
        InteractionHand hand = event.getHand();
        if(item instanceof QuiverItem && hand == InteractionHand.OFF_HAND){
            event.setCanceled(true);
        }
    }

//    @SubscribeEvent
//    public static <T extends LivingEntity, M extends EntityModel<T> & ArmedModel> void RenderLivingEvent$PreListener(final RenderLivingEvent.Pre<T,M> event){
//        LivingEntityRenderer<T,M> renderer = event.getRenderer();
//        for(int i = 0; i < renderer.layers.size(); i++){
//            if(renderer.layers.get(i) instanceof ItemInHandLayer){
//                renderer.layers.set(i, new OdysseyHeldItemLayer<>(renderer));
//            }
//        }
//    }

    /**
     * Adjusts PlayerRenderer Layers
     */
    @SubscribeEvent
    public static void RenderPlayerEvent$PreListener(final RenderPlayerEvent.Pre event){
        PlayerRenderer playerRenderer = event.getRenderer();
        Player player = event.getPlayer();
        if(playerRenderer instanceof OdysseyPlayerRenderer odysseyPlayerRenderer && player instanceof AbstractClientPlayer abstractClientPlayer){
            odysseyPlayerRenderer.setModelProperties(abstractClientPlayer);
        }
    }
}
