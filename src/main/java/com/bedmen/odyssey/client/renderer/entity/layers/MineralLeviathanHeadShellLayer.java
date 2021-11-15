package com.bedmen.odyssey.client.renderer.entity.layers;

import com.bedmen.odyssey.client.renderer.entity.model.MineralLeviathanHeadModel;
import com.bedmen.odyssey.entity.boss.MineralLeviathanHeadEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MineralLeviathanHeadShellLayer extends LayerRenderer<MineralLeviathanHeadEntity, MineralLeviathanHeadModel> {
    private final EntityModel<MineralLeviathanHeadEntity> model = new MineralLeviathanHeadModel();

    public MineralLeviathanHeadShellLayer(IEntityRenderer<MineralLeviathanHeadEntity, MineralLeviathanHeadModel> p_i50923_1_) {
        super(p_i50923_1_);
    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer p_225628_2_, int p_225628_3_, MineralLeviathanHeadEntity entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (entity.hasShell()) {
            matrixStack.pushPose();
            matrixStack.scale(1.1f,1.1f,1.1f);
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(entity, p_225628_5_, p_225628_6_, p_225628_7_);
            this.model.setupAnim(entity, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
            IVertexBuilder ivertexbuilder = p_225628_2_.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
            this.model.renderToBuffer(matrixStack, ivertexbuilder, p_225628_3_, LivingRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }
    }

    public ResourceLocation getTextureLocation(MineralLeviathanHeadEntity entity) {
        return getTextureLocation(MineralLeviathanBodyShellLayer.SHELL_LOCATION[entity.getShellType().ordinal()], entity);
    }

    public ResourceLocation getTextureLocation(ResourceLocation[] locations, MineralLeviathanHeadEntity entity) {
        float maxHealth = entity.getShellType().getShellMaxHealth();
        float currentHealth = entity.getShellHealth();
        int i = MathHelper.ceil(11.0f - (currentHealth / maxHealth * 10.0f)) - 1;
        i = MathHelper.clamp(i, 0, 10);
        return locations[i];
    }
}