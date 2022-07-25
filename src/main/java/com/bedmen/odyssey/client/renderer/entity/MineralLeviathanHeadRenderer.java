package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.MineralLeviathanHeadModel;
import com.bedmen.odyssey.client.renderer.entity.layer.MineralLeviathanHeadShellLayer;
import com.bedmen.odyssey.entity.boss.MineralLeviathanHead;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MineralLeviathanHeadRenderer extends MobRenderer<MineralLeviathanHead, MineralLeviathanHeadModel> {
    private static final ResourceLocation STONE_SEGMENT_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/stone_segment.png");

    public MineralLeviathanHeadRenderer(EntityRendererProvider.Context context) {
        super(context, new MineralLeviathanHeadModel(context.bakeLayer(MineralLeviathanHeadModel.LAYER_LOCATION)), 0.1F);
        this.addLayer(new MineralLeviathanHeadShellLayer(this, context.getModelSet()));
    }

    public ResourceLocation getTextureLocation(MineralLeviathanHead entity) {
        return STONE_SEGMENT_LOCATION;
    }
}