package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.layers.MineralLeviathanShellLayer;
import com.bedmen.odyssey.client.renderer.entity.model.MineralLeviathanSegmentModel;
import com.bedmen.odyssey.entity.boss.MineralLeviathanSegmentEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MineralLeviathanSegmentRenderer extends MobRenderer<MineralLeviathanSegmentEntity, MineralLeviathanSegmentModel> {
    private static final ResourceLocation STONE_SEGMENT_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/stone_segment.png");
    public MineralLeviathanSegmentRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new MineralLeviathanSegmentModel(), 1.0F);
        this.addLayer(new MineralLeviathanShellLayer(this));
    }

    public ResourceLocation getTextureLocation(MineralLeviathanSegmentEntity entity) {
        return STONE_SEGMENT_LOCATION;
    }
}