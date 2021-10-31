package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.WeaverModel;
import com.bedmen.odyssey.entity.monster.WeaverEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WeaverRenderer<T extends WeaverEntity> extends MobRenderer<T, WeaverModel<T>> {
    private static final ResourceLocation WEAVER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/weaver.png");

    public WeaverRenderer(EntityRendererManager p_i46139_1_) {
        super(p_i46139_1_, new WeaverModel<>(), 0.8F);
    }

    protected float getFlipDegrees(T pLivingEntity) {
        return 180.0F;
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(T pEntity) {
        return WEAVER_LOCATION;
    }
}