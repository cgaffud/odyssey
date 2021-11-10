package com.bedmen.odyssey.client.renderer.entity.model;

import com.bedmen.odyssey.entity.boss.MineralLeviathanSegmentEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class MineralLeviathanSegmentModel extends SegmentedModel<MineralLeviathanSegmentEntity> {
    private final ImmutableList<ModelRenderer> modelRendererImmutableList;
    private final ModelRenderer innerSegment;

    public MineralLeviathanSegmentModel() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.innerSegment = new ModelRenderer(this, 0, 0);
        this.innerSegment.addBox(-16.0f,-16.0f,-16.0f,32.0f,32.0f,32.0f);

        ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.add(this.innerSegment);
        this.modelRendererImmutableList = builder.build();
    }

    public void prepareMobModel(MineralLeviathanSegmentEntity entity, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
        this.innerSegment.yRot = entity.getYRot() * (float)Math.PI / -180f;
        this.innerSegment.xRot = entity.getXRot() * (float)Math.PI / -180f;
        this.innerSegment.y = 8.0f;
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return this.modelRendererImmutableList;
    }

    public void setupAnim(MineralLeviathanSegmentEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
    }
}