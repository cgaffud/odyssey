package com.bedmen.odyssey.client.renderer.entity.model;

import com.bedmen.odyssey.entity.boss.MineralLeviathanSegmentEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

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

    @Override
    public Iterable<ModelRenderer> parts() {
        return this.modelRendererImmutableList;
    }

    public void setupAnim(MineralLeviathanSegmentEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.innerSegment.yRot = MathHelper.rotLerp(pAgeInTicks % 1.0f, pEntity.yRotO, pEntity.yRot) * (float)Math.PI / -90f; //Game automatically rotates models the wrong way based on yrot, so we rotate them back by double
        this.innerSegment.xRot = MathHelper.rotLerp(pAgeInTicks % 1.0f, pEntity.xRotO, pEntity.xRot) * (float)Math.PI / -180f;
        this.innerSegment.y = 8.0f;
    }
}