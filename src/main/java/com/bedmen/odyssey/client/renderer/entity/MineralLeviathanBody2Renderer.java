package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.MineralLeviathanBody2Model;
import com.bedmen.odyssey.client.model.MineralLeviathanBodyModel;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.Segment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class MineralLeviathanBody2Renderer extends LivingEntityRenderer<Segment, MineralLeviathanBody2Model> {
    private static final ResourceLocation STONE_SEGMENT_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/stone_segment.png");

    public MineralLeviathanBody2Renderer(EntityRendererProvider.Context context) {
        super(context, new MineralLeviathanBody2Model(context.bakeLayer(MineralLeviathanBodyModel.LAYER_LOCATION)), 0.1F);
    }

    public ResourceLocation getTextureLocation(Segment entity) {
        return STONE_SEGMENT_LOCATION;
    }
}