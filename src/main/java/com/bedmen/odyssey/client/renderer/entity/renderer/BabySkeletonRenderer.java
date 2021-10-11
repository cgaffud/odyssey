package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.client.renderer.entity.model.BabySkeletonModel;
import com.bedmen.odyssey.entity.monster.BabySkeletonEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BabySkeletonRenderer extends BipedRenderer<BabySkeletonEntity, BabySkeletonModel<BabySkeletonEntity>> {
    private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    public BabySkeletonRenderer(EntityRendererManager p_i46143_1_) {
        super(p_i46143_1_, new BabySkeletonModel<>(), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new BabySkeletonModel<>(0.5F, true), new BabySkeletonModel(1.0F, true)));
    }

    public ResourceLocation getTextureLocation(BabySkeletonEntity p_110775_1_) {
        return SKELETON_LOCATION;
    }
}