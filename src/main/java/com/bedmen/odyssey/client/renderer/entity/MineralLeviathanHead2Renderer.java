package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.MineralLeviathanHead2Model;
import com.bedmen.odyssey.client.renderer.entity.layer.MineralLeviathanHead2ShellLayer;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanHead2;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MineralLeviathanHead2Renderer extends MobRenderer<MineralLeviathanHead2, MineralLeviathanHead2Model> {
    private static final ResourceLocation STONE_SEGMENT_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/stone_segment.png");

    public MineralLeviathanHead2Renderer(EntityRendererProvider.Context context) {
        super(context, new MineralLeviathanHead2Model(context.bakeLayer(MineralLeviathanHead2Model.LAYER_LOCATION)), 0.1F);
        this.addLayer(new MineralLeviathanHead2ShellLayer(this, context.getModelSet()));
    }

    public ResourceLocation getTextureLocation(MineralLeviathanHead2 entity) {
        return STONE_SEGMENT_LOCATION;
    }
}