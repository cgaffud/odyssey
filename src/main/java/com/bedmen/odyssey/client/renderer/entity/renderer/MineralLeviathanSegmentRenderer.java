package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.MineralLeviathanSegmentModel;
import com.bedmen.odyssey.entity.boss.MineralLeviathanEntity;
import com.bedmen.odyssey.entity.boss.MineralLeviathanSegmentEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MineralLeviathanSegmentRenderer extends MobRenderer<MineralLeviathanSegmentEntity, MineralLeviathanSegmentModel> {
    private static final ResourceLocation MINERAL_LEVIATHAN_HEAD_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/head.png");
    private static final ResourceLocation MINERAL_LEVIATHAN_BODY_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/body.png");
    public MineralLeviathanSegmentRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new MineralLeviathanSegmentModel(), 0.7F);
        this.shadowRadius = 0.5F;
    }

    public ResourceLocation getTextureLocation(MineralLeviathanSegmentEntity entity) {
        return entity instanceof MineralLeviathanEntity ? MINERAL_LEVIATHAN_HEAD_LOCATION : MINERAL_LEVIATHAN_BODY_LOCATION;
    }
}