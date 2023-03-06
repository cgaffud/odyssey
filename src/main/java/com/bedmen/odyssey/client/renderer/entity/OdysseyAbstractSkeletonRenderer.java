package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.client.model.OdysseyAbstractSkeletonModel;
import com.bedmen.odyssey.entity.monster.OdysseyAbstractSkeleton;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class OdysseyAbstractSkeletonRenderer<T extends OdysseyAbstractSkeleton, M extends OdysseyAbstractSkeletonModel<T>> extends HumanoidMobRenderer<T, M> {
    private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    protected OdysseyAbstractSkeletonRenderer(EntityRendererProvider.Context context, M p_173911_, M p_173912_, M p_173913_) {
        super(context, p_173911_, 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, p_173912_, p_173913_));
    }

    public ResourceLocation getTextureLocation(OdysseyAbstractSkeleton odysseyAbstractSkeleton) {
        return SKELETON_LOCATION;
    }

    protected boolean isShaking(OdysseyAbstractSkeleton odysseyAbstractSkeleton) {
        return odysseyAbstractSkeleton.isShaking();
    }
}
