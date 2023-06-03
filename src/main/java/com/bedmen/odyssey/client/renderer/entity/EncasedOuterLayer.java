package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.Encased;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Drowned;

public class EncasedOuterLayer<T extends Encased> extends RenderLayer<T, ZombieModel<T>> {
    private static final ResourceLocation ENCASED_OUTER_LAYER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/zombie/encased_outer_layer.png");
    private final ZombieModel<T> model;

    public EncasedOuterLayer(RenderLayerParent<T, ZombieModel<T>> p_174490_, EntityModelSet p_174491_) {
        super(p_174490_);
        this.model = new ZombieModel<>(p_174491_.bakeLayer(ModelLayers.DROWNED_OUTER_LAYER));
    }

    public void render(PoseStack p_116924_, MultiBufferSource p_116925_, int p_116926_, T mob, float p_116928_, float p_116929_, float p_116930_, float p_116931_, float p_116932_, float p_116933_) {
        if (mob.getArmorHealth() > 0)
            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, ENCASED_OUTER_LAYER_LOCATION, p_116924_, p_116925_, p_116926_, mob, p_116928_, p_116929_, p_116931_, p_116932_, p_116933_, p_116930_, 1.0F, 1.0F, 1.0F);
    }
}