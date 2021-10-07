package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.layers.ShellLayer;
import com.bedmen.odyssey.client.renderer.entity.model.MineralLeviathanSegmentModel;
import com.bedmen.odyssey.entity.boss.MineralLeviathanSegmentEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MineralLeviathanSegmentRenderer extends MobRenderer<MineralLeviathanSegmentEntity, MineralLeviathanSegmentModel> {
    private static final ResourceLocation RUBY_SEGMENT_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/ruby_segment.png");
    private static final ResourceLocation COPPER_SEGMENT_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/copper_segment.png");
    private static final ResourceLocation SILVER_SEGMENT_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/silver_segment.png");
    private static final ResourceLocation SHELL_BREAKING_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/breaking.png");
    private static final RenderType BREAKING = RenderType.entityCutout(SHELL_BREAKING_LOCATION);
    public MineralLeviathanSegmentRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new MineralLeviathanSegmentModel(), 1.0F);
        this.addLayer(new ShellLayer(this));
    }

    public ResourceLocation getTextureLocation(MineralLeviathanSegmentEntity entity) {
        switch(entity.getShellType()){
            default:
                return RUBY_SEGMENT_LOCATION;
            case COPPER:
                return COPPER_SEGMENT_LOCATION;
            case SILVER:
                return SILVER_SEGMENT_LOCATION;
        }
    }
}