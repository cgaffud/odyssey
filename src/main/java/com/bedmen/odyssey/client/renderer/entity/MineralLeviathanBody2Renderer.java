package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.MineralLeviathanBody2Model;
import com.bedmen.odyssey.client.renderer.entity.layer.MineralLeviathanBody2ShellLayer;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanBody2;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MineralLeviathanBody2Renderer extends MobRenderer<MineralLeviathanBody2, MineralLeviathanBody2Model> {
    private static final ResourceLocation STONE_SEGMENT_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/stone_segment.png");

    public MineralLeviathanBody2Renderer(EntityRendererProvider.Context context) {
        super(context, new MineralLeviathanBody2Model(context.bakeLayer(MineralLeviathanBody2Model.LAYER_LOCATION)), 0.1F);
        this.addLayer(new MineralLeviathanBody2ShellLayer(this, context.getModelSet()));
    }

    public ResourceLocation getTextureLocation(MineralLeviathanBody2 entity) {
        return STONE_SEGMENT_LOCATION;
    }
}