package com.bedmen.odyssey.client.renderer.entity.layer;

import com.bedmen.odyssey.client.model.AbandonedIronGolemModel;
import com.bedmen.odyssey.entity.boss.AbandonedIronGolem;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.IronGolem;

import java.util.Map;

public class AbandonedIronGolemCrackinessLayer extends RenderLayer<AbandonedIronGolem, AbandonedIronGolemModel<AbandonedIronGolem>> {
    private static final Map<IronGolem.Crackiness, ResourceLocation> resourceLocations = ImmutableMap.of(IronGolem.Crackiness.LOW, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_low.png"), IronGolem.Crackiness.MEDIUM, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_medium.png"), IronGolem.Crackiness.HIGH, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_high.png"));

    public AbandonedIronGolemCrackinessLayer(RenderLayerParent<AbandonedIronGolem, AbandonedIronGolemModel<AbandonedIronGolem>> layerParent) {
        super(layerParent);
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int p_117150_, AbandonedIronGolem abandonedIronGolem, float p_117152_, float p_117153_, float p_117154_, float p_117155_, float p_117156_, float p_117157_) {
        if (!abandonedIronGolem.isInvisible()) {
            IronGolem.Crackiness irongolem$crackiness = abandonedIronGolem.getCrackiness();
            if (irongolem$crackiness != IronGolem.Crackiness.NONE) {
                ResourceLocation resourcelocation = resourceLocations.get(irongolem$crackiness);
                renderColoredCutoutModel(this.getParentModel(), resourcelocation, poseStack, multiBufferSource, p_117150_, abandonedIronGolem, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}