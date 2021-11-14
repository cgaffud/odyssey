package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.layers.MineralLeviathanHeadShellLayer;
import com.bedmen.odyssey.client.renderer.entity.model.MineralLeviathanHeadModel;
import com.bedmen.odyssey.entity.boss.MineralLeviathanHeadEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MineralLeviathanHeadRenderer extends MobRenderer<MineralLeviathanHeadEntity, MineralLeviathanHeadModel> {
    private static final ResourceLocation STONE_SEGMENT_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/stone_segment.png");
    public MineralLeviathanHeadRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new MineralLeviathanHeadModel(), 1.0F);
        this.addLayer(new MineralLeviathanHeadShellLayer(this));
    }

    public ResourceLocation getTextureLocation(MineralLeviathanHeadEntity entity) {
        return STONE_SEGMENT_LOCATION;
    }
}