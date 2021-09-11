package com.bedmen.odyssey.client.renderer.entity.model;

import com.bedmen.odyssey.entity.boss.MineralLeviathanEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class MineralLeviathanModel extends SegmentedModel<MineralLeviathanEntity> {
    private final ImmutableList<ModelRenderer> parts;
    private final ModelRenderer head;
    private MineralLeviathanEntity entity;

    public MineralLeviathanModel() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-16.0f,-16.0f,-16.0f,32.0f,32.0f,32.0f);

        ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.add(this.head);
        this.parts = builder.build();
    }

    public void prepareMobModel(MineralLeviathanEntity entity, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
        this.entity = entity;
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return this.parts;
    }

    public void setupAnim(MineralLeviathanEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
    }

    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        matrixStack.pushPose();
        this.head.yRot = this.entity.getYRot() * (float)Math.PI / -180f;
        this.head.xRot = this.entity.getXRot() * (float)Math.PI / -180f;
        this.head.y = 8.0f;
        matrixStack.popPose();
        super.renderToBuffer(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }
}