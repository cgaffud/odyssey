package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.MineralLeviathanModel;
import com.bedmen.odyssey.entity.boss.MineralLeviathanEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MineralLeviathanPartRenderer extends MobRenderer<MineralLeviathanEntity, MineralLeviathanModel> {
    private static final ResourceLocation MINERAL_LEVIATHAN_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/head.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(MINERAL_LEVIATHAN_LOCATION);
    public MineralLeviathanPartRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new MineralLeviathanModel(), 0.7F);
        this.shadowRadius = 0.5F;
    }

    public ResourceLocation getTextureLocation(MineralLeviathanEntity p_110775_1_) {
        return MINERAL_LEVIATHAN_LOCATION;
    }
}