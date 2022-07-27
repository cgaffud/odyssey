package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.MineralLeviathanBodyModel;
import com.bedmen.odyssey.client.renderer.entity.layer.MineralLeviathanBodyShellLayer;
import com.bedmen.odyssey.entity.boss.MineralLeviathanBody;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MineralLeviathanBodyRenderer extends MobRenderer<MineralLeviathanBody, MineralLeviathanBodyModel> {
    private static final ResourceLocation STONE_SEGMENT_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/stone_segment.png");

    public MineralLeviathanBodyRenderer(EntityRendererProvider.Context context) {
        super(context, new MineralLeviathanBodyModel(context.bakeLayer(MineralLeviathanBodyModel.LAYER_LOCATION)), 0.1F);
        this.addLayer(new MineralLeviathanBodyShellLayer(this, context.getModelSet()));
    }

    public ResourceLocation getTextureLocation(MineralLeviathanBody entity) {
        return STONE_SEGMENT_LOCATION;
    }
}