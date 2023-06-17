package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.client.model.OdysseyAbstractSkeletonModel;
import com.bedmen.odyssey.entity.monster.OdysseyStray;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public abstract class OdysseyAbstractStrayRenderer<T extends OdysseyStray, M extends OdysseyAbstractSkeletonModel<T>> extends OdysseyAbstractSkeletonRenderer<T, M>{
    private static final ResourceLocation STRAY_SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/stray.png");

    protected OdysseyAbstractStrayRenderer(EntityRendererProvider.Context context, M p_173911_, M p_173912_, M p_173913_) {
        super(context, p_173911_, p_173912_, p_173913_);
    }

    public ResourceLocation getTextureLocation(T odysseyStray) {
        return STRAY_SKELETON_LOCATION;
    }
}
