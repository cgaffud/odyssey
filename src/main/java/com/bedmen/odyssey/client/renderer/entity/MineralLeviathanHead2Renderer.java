package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.MineralLeviathanHead2Model;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.Segment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class MineralLeviathanHead2Renderer extends LivingEntityRenderer<Segment, MineralLeviathanHead2Model> {
    private static final ResourceLocation STONE_SEGMENT_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/stone_segment.png");

    public MineralLeviathanHead2Renderer(EntityRendererProvider.Context context) {
        super(context, new MineralLeviathanHead2Model(context.bakeLayer(MineralLeviathanHead2Model.LAYER_LOCATION)), 0.1F);
    }

    public ResourceLocation getTextureLocation(Segment entity) {
        return STONE_SEGMENT_LOCATION;
    }
}