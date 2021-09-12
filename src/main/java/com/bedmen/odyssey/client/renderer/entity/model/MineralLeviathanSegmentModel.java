package com.bedmen.odyssey.client.renderer.entity.model;

import com.bedmen.odyssey.entity.boss.MineralLeviathanSegmentEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class MineralLeviathanSegmentModel extends SegmentedModel<MineralLeviathanSegmentEntity> {
    private final ImmutableList<ModelRenderer> modelRendererImmutableList;
    private final ModelRenderer segment;
    private MineralLeviathanSegmentEntity entity;

    public MineralLeviathanSegmentModel() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.segment = new ModelRenderer(this, 0, 0);
        this.segment.addBox(-16.0f,-16.0f,-16.0f,32.0f,32.0f,32.0f);

        ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.add(this.segment);
        this.modelRendererImmutableList = builder.build();
    }

    public void prepareMobModel(MineralLeviathanSegmentEntity entity, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
        this.entity = entity;
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return this.modelRendererImmutableList;
    }

    public void setupAnim(MineralLeviathanSegmentEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
    }

    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        matrixStack.pushPose();
        this.segment.yRot = this.entity.getYRot() * (float)Math.PI / -180f;
        this.segment.xRot = this.entity.getXRot() * (float)Math.PI / -180f;
        this.segment.y = 8.0f;
        matrixStack.popPose();
        super.renderToBuffer(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }
}