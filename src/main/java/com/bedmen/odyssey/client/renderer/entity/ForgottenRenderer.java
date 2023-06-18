package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.ForgottenModel;
import com.bedmen.odyssey.entity.monster.Forgotten;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ForgottenRenderer<T extends Forgotten> extends AbstractZombieRenderer<T, ZombieModel<T>> {
    private static final ResourceLocation FORGOTTEN_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/zombie/forgotten.png");

    public ForgottenRenderer(EntityRendererProvider.Context context) {
        this(context, ForgottenModel.LAYER_LOCATION, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR);
    }

    public ForgottenRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation0, ModelLayerLocation modelLayerLocation1, ModelLayerLocation modelLayerLocation2) {
        super(context, new ForgottenModel<>(context.bakeLayer(modelLayerLocation0)), new ForgottenModel<>(context.bakeLayer(modelLayerLocation1)), new ForgottenModel<>(context.bakeLayer(modelLayerLocation2)));
    }

    public ResourceLocation getTextureLocation(Forgotten forgotten) {
        return FORGOTTEN_LOCATION;
    }
}
