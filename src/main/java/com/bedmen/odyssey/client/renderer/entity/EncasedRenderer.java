package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.ForgottenModel;
import com.bedmen.odyssey.entity.monster.Encased;
import com.bedmen.odyssey.entity.monster.Forgotten;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;

public class EncasedRenderer<T extends Encased> extends AbstractZombieRenderer<T, ZombieModel<T>> {
    private static final ResourceLocation ENCASED_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/zombie/encased.png");
    private static final ResourceLocation ENCASED_BROKEN_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/zombie/encased_broken.png");


    public EncasedRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR);
    }

    public EncasedRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation0, ModelLayerLocation modelLayerLocation1, ModelLayerLocation modelLayerLocation2) {
        super(context, new ZombieModel<>(context.bakeLayer(modelLayerLocation0)), new ZombieModel<>(context.bakeLayer(modelLayerLocation1)), new ZombieModel<>(context.bakeLayer(modelLayerLocation2)));
        this.addLayer(new EncasedOuterLayer<>(this, context.getModelSet()));
    }

    public ResourceLocation getTextureLocation(Encased encased) {
        return (encased.getArmorHealth() > 0) ? this.ENCASED_LOCATION : ENCASED_BROKEN_LOCATION;
    }
}