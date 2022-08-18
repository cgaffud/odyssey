package com.bedmen.odyssey.client.renderer.entity.layer;

import com.bedmen.odyssey.client.model.MineralLeviathanHead2Model;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanHead2;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MineralLeviathanHead2ShellLayer extends RenderLayer<MineralLeviathanHead2, MineralLeviathanHead2Model> {
    private final EntityModel<MineralLeviathanHead2> model;

    public MineralLeviathanHead2ShellLayer(RenderLayerParent<MineralLeviathanHead2, MineralLeviathanHead2Model> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.model = new MineralLeviathanHead2Model(entityModelSet.bakeLayer(MineralLeviathanHead2Model.LAYER_LOCATION));
    }

    public void render(PoseStack matrixStack, MultiBufferSource p_225628_2_, int p_225628_3_, MineralLeviathanHead2 entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (entity.hasShell()) {
            matrixStack.pushPose();
            matrixStack.scale(1.1f,1.1f,1.1f);
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(entity, p_225628_5_, p_225628_6_, p_225628_7_);
            this.model.setupAnim(entity, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
            VertexConsumer ivertexbuilder = p_225628_2_.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
            this.model.renderToBuffer(matrixStack, ivertexbuilder, p_225628_3_, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }
    }

    public ResourceLocation getTextureLocation(MineralLeviathanHead2 entity) {
        return getTextureLocation(MineralLeviathanBodyShellLayer.SHELL_LOCATION[entity.getShellType().ordinal()], entity);
    }

    public ResourceLocation getTextureLocation(ResourceLocation[] locations, MineralLeviathanHead2 entity) {
        float maxHealth = entity.getShellType().getShellMaxHealth();
        float currentHealth = entity.getShellHealth();
        int i = Mth.ceil(11.0f - (currentHealth / maxHealth * 10.0f)) - 1;
        i = Mth.clamp(i, 0, 10);
        return locations[i];
    }
}