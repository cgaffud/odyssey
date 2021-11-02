package com.bedmen.odyssey.client.renderer;

import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.util.FogUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderEvents {

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

}
