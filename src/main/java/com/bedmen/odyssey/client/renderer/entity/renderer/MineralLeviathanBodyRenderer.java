package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.layers.MineralLeviathanBodyShellLayer;
import com.bedmen.odyssey.client.renderer.entity.model.MineralLeviathanBodyModel;
import com.bedmen.odyssey.entity.boss.MineralLeviathanBodyEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MineralLeviathanBodyRenderer extends MobRenderer<MineralLeviathanBodyEntity, MineralLeviathanBodyModel> {
    private static final ResourceLocation STONE_SEGMENT_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/stone_segment.png");
    public MineralLeviathanBodyRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new MineralLeviathanBodyModel(), 1.0F);
        this.addLayer(new MineralLeviathanBodyShellLayer(this));
    }

    public ResourceLocation getTextureLocation(MineralLeviathanBodyEntity entity) {
        return STONE_SEGMENT_LOCATION;
    }
}