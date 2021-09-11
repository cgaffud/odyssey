package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.MineralLeviathanModel;
import com.bedmen.odyssey.client.renderer.entity.model.MineralLeviathanPartModel;
import com.bedmen.odyssey.entity.boss.MineralLeviathanEntity;
import com.bedmen.odyssey.entity.boss.MineralLeviathanPartEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MineralLeviathanPartRenderer extends MobRenderer<MineralLeviathanPartEntity, MineralLeviathanPartModel> {
    private static final ResourceLocation MINERAL_LEVIATHAN_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/body.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(MINERAL_LEVIATHAN_LOCATION);
    public MineralLeviathanPartRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new MineralLeviathanPartModel(), 0.7F);
        this.shadowRadius = 0.5F;
    }

    public ResourceLocation getTextureLocation(MineralLeviathanPartEntity p_110775_1_) {
        return MINERAL_LEVIATHAN_LOCATION;
    }
}