package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import net.minecraftforge.fml.common.Mod;

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
//
//    /**
//     * Prevents Quiver from being rendered in hand
//     */
//    @SubscribeEvent
//    public static void RenderHandEventListener(final RenderHandEvent event){
//        Item item = event.getItemStack().getItem();
//        InteractionHand hand = event.getHand();
//        if(item instanceof QuiverItem && hand == InteractionHand.OFF_HAND){
//            event.setCanceled(true);
//        }
//    }
//
//    @SubscribeEvent
//    public static <T extends LivingEntity, M extends EntityModel<T> & ArmedModel> void RenderLivingEvent$PreListener(final RenderLivingEvent.Pre<T,M> event){
//        LivingEntityRenderer<T,M> renderer = event.getRenderer();
//        for(int i = 0; i < renderer.layers.size(); i++){
//            if(renderer.layers.get(i) instanceof ItemInHandLayer){
//                renderer.layers.set(i, new OdysseyHeldItemLayer<>(renderer));
//            }
//        }
//    }
//
//    /**
//     * Adjusts PlayerRenderer Layers
//     */
//    @SubscribeEvent
//    public static void RRenderPlayerEvent$PreListener(final RenderPlayerEvent.Pre event){
//        PlayerRenderer playerRenderer = event.getRenderer();
//        List<RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>> layers = playerRenderer.layers;
//        boolean[] flags = new boolean[3];
//        layers.removeIf((layer) -> layer instanceof ElytraLayer);
//        for(int i = 0; i < layers.size(); i++){
//            if(layers.get(i) instanceof QuiverLayer){
//                flags[0] = true;
//            } else if(layers.get(i) instanceof AmuletLayer){
//                flags[1] = true;
//            } else if(layers.get(i) instanceof OdysseyElytraLayer){
//                flags[2] = true;
//            }
//        }
//        if(!flags[0]){
//            layers.add(new QuiverLayer<>(playerRenderer));
//        }
//        if(!flags[1]){
//            layers.add(new AmuletLayer<>(playerRenderer));
//        }
//        if(!flags[2]){
//            layers.add(new OdysseyElytraLayer<>(playerRenderer));
//        }
//    }
}
