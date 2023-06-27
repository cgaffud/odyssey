package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.layer.ZombieOuterLayer;
import com.bedmen.odyssey.entity.monster.EncasedZombie;
import com.bedmen.odyssey.entity.monster.OvergrownZombie;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class OvergrownZombieRenderer<T extends OvergrownZombie> extends AbstractZombieRenderer<T, ZombieModel<T>> {
    private static final ResourceLocation OVERGROWN_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/zombie/overgrown.png");


    public OvergrownZombieRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR);
    }

    public OvergrownZombieRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation0, ModelLayerLocation modelLayerLocation1, ModelLayerLocation modelLayerLocation2) {
        super(context, new ZombieModel<>(context.bakeLayer(modelLayerLocation0)), new ZombieModel<>(context.bakeLayer(modelLayerLocation1)), new ZombieModel<>(context.bakeLayer(modelLayerLocation2)));
        this.addLayer(new ZombieOuterLayer<>(this, context.getModelSet()));
    }

    public ResourceLocation getTextureLocation(OvergrownZombie overgrownZombie) {
        return OVERGROWN_LOCATION;
    }
}