package com.bedmen.odyssey.client.renderer.entity.model;

import com.bedmen.odyssey.entity.boss.MineralLeviathanHeadEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class MineralLeviathanHeadModel extends SegmentedModel<MineralLeviathanHeadEntity> {
    private final ImmutableList<ModelRenderer> modelRendererImmutableList;
    private final ModelRenderer base;
    private final ModelRenderer s_mouth;
    private final ModelRenderer e_mouth;
    private final ModelRenderer n_mouth;
    private final ModelRenderer w_mouth;

    public MineralLeviathanHeadModel() {
        texWidth = 128;
        texHeight = 64;

        base = new ModelRenderer(this);
        base.setPos(0.0F, 8.0F, 0.0F);
        base.texOffs(0, 0).addBox(-16.0F, 6.0F, 14.0F, 30.0F, 4.0F, 2.0F, 0.0F, false);
        base.texOffs(0, 0).addBox(-14.0F, 6.0F, -16.0F, 30.0F, 4.0F, 2.0F, 0.0F, false);
        base.texOffs(0, 0).addBox(14.0F, 6.0F, -14.0F, 2.0F, 4.0F, 30.0F, 0.0F, false);
        base.texOffs(0, 0).addBox(-16.0F, 6.0F, -16.0F, 2.0F, 4.0F, 30.0F, 0.0F, false);
        base.texOffs(0, 0).addBox(-16.0F, 10.0F, -16.0F, 32.0F, 2.0F, 32.0F, 0.0F, false);

        s_mouth = new ModelRenderer(this);
        s_mouth.setPos(0.0F, 6.0F, 15.0F);
        base.addChild(s_mouth);
        s_mouth.texOffs(0, 0).addBox(-16.0F, -2.0F, -1.0F, 32.0F, 2.0F, 2.0F, 0.0F, false);
        s_mouth.texOffs(0, 0).addBox(-15.0F, -4.0F, -1.0F, 30.0F, 2.0F, 2.0F, 0.0F, false);
        s_mouth.texOffs(0, 0).addBox(-14.0F, -6.0F, -1.0F, 28.0F, 2.0F, 2.0F, 0.0F, false);
        s_mouth.texOffs(0, 0).addBox(-13.0F, -8.0F, -1.0F, 26.0F, 2.0F, 2.0F, 0.0F, false);
        s_mouth.texOffs(0, 0).addBox(-12.0F, -10.0F, -1.0F, 24.0F, 2.0F, 2.0F, 0.0F, false);
        s_mouth.texOffs(0, 0).addBox(-11.0F, -12.0F, -1.0F, 22.0F, 2.0F, 2.0F, 0.0F, false);
        s_mouth.texOffs(0, 0).addBox(-10.0F, -14.0F, -1.0F, 20.0F, 2.0F, 2.0F, 0.0F, false);
        s_mouth.texOffs(0, 0).addBox(-9.0F, -16.0F, -1.0F, 18.0F, 2.0F, 2.0F, 0.0F, false);
        s_mouth.texOffs(0, 0).addBox(-8.0F, -18.0F, -2.0F, 16.0F, 2.0F, 2.0F, 0.0F, false);
        s_mouth.texOffs(0, 0).addBox(-6.0F, -20.0F, -3.0F, 12.0F, 2.0F, 2.0F, 0.0F, false);
        s_mouth.texOffs(0, 0).addBox(-4.0F, -22.0F, -4.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);
        s_mouth.texOffs(0, 0).addBox(-2.0F, -24.0F, -5.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);
        s_mouth.texOffs(0, 0).addBox(-1.0F, -26.0F, -6.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        e_mouth = new ModelRenderer(this);
        e_mouth.setPos(-15.0F, 6.0F, 0.0F);
        base.addChild(e_mouth);
        e_mouth.texOffs(0, 0).addBox(-1.0F, -2.0F, -16.0F, 2.0F, 2.0F, 32.0F, 0.0F, false);
        e_mouth.texOffs(0, 0).addBox(-1.0F, -4.0F, -15.0F, 2.0F, 2.0F, 30.0F, 0.0F, false);
        e_mouth.texOffs(0, 0).addBox(-1.0F, -6.0F, -14.0F, 2.0F, 2.0F, 28.0F, 0.0F, false);
        e_mouth.texOffs(0, 0).addBox(-1.0F, -8.0F, -13.0F, 2.0F, 2.0F, 26.0F, 0.0F, false);
        e_mouth.texOffs(0, 0).addBox(-1.0F, -10.0F, -12.0F, 2.0F, 2.0F, 24.0F, 0.0F, false);
        e_mouth.texOffs(0, 0).addBox(-1.0F, -12.0F, -11.0F, 2.0F, 2.0F, 22.0F, 0.0F, false);
        e_mouth.texOffs(0, 0).addBox(-1.0F, -14.0F, -10.0F, 2.0F, 2.0F, 20.0F, 0.0F, false);
        e_mouth.texOffs(0, 0).addBox(-1.0F, -16.0F, -9.0F, 2.0F, 2.0F, 18.0F, 0.0F, false);
        e_mouth.texOffs(0, 0).addBox(0.0F, -18.0F, -8.0F, 2.0F, 2.0F, 16.0F, 0.0F, false);
        e_mouth.texOffs(0, 0).addBox(1.0F, -20.0F, -6.0F, 2.0F, 2.0F, 12.0F, 0.0F, false);
        e_mouth.texOffs(0, 0).addBox(2.0F, -22.0F, -4.0F, 2.0F, 2.0F, 8.0F, 0.0F, false);
        e_mouth.texOffs(0, 0).addBox(3.0F, -24.0F, -2.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        e_mouth.texOffs(0, 0).addBox(4.0F, -26.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        n_mouth = new ModelRenderer(this);
        n_mouth.setPos(0.0F, 6.0F, -15.0F);
        base.addChild(n_mouth);
        n_mouth.texOffs(0, 0).addBox(-16.0F, -2.0F, -1.0F, 32.0F, 2.0F, 2.0F, 0.0F, false);
        n_mouth.texOffs(0, 0).addBox(-15.0F, -4.0F, -1.0F, 30.0F, 2.0F, 2.0F, 0.0F, false);
        n_mouth.texOffs(0, 0).addBox(-14.0F, -6.0F, -1.0F, 28.0F, 2.0F, 2.0F, 0.0F, false);
        n_mouth.texOffs(0, 0).addBox(-13.0F, -8.0F, -1.0F, 26.0F, 2.0F, 2.0F, 0.0F, false);
        n_mouth.texOffs(0, 0).addBox(-12.0F, -10.0F, -1.0F, 24.0F, 2.0F, 2.0F, 0.0F, false);
        n_mouth.texOffs(0, 0).addBox(-11.0F, -12.0F, -1.0F, 22.0F, 2.0F, 2.0F, 0.0F, false);
        n_mouth.texOffs(0, 0).addBox(-10.0F, -14.0F, -1.0F, 20.0F, 2.0F, 2.0F, 0.0F, false);
        n_mouth.texOffs(0, 0).addBox(-9.0F, -16.0F, -1.0F, 18.0F, 2.0F, 2.0F, 0.0F, false);
        n_mouth.texOffs(0, 0).addBox(-8.0F, -18.0F, 0.0F, 16.0F, 2.0F, 2.0F, 0.0F, false);
        n_mouth.texOffs(0, 0).addBox(-6.0F, -20.0F, 1.0F, 12.0F, 2.0F, 2.0F, 0.0F, false);
        n_mouth.texOffs(0, 0).addBox(-4.0F, -22.0F, 2.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);
        n_mouth.texOffs(0, 0).addBox(-2.0F, -24.0F, 3.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);
        n_mouth.texOffs(0, 0).addBox(-1.0F, -26.0F, 4.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        w_mouth = new ModelRenderer(this);
        w_mouth.setPos(15.0F, 6.0F, 0.0F);
        base.addChild(w_mouth);
        w_mouth.texOffs(0, 0).addBox(-1.0F, -2.0F, -16.0F, 2.0F, 2.0F, 32.0F, 0.0F, false);
        w_mouth.texOffs(0, 0).addBox(-1.0F, -4.0F, -15.0F, 2.0F, 2.0F, 30.0F, 0.0F, false);
        w_mouth.texOffs(0, 0).addBox(-1.0F, -6.0F, -14.0F, 2.0F, 2.0F, 28.0F, 0.0F, false);
        w_mouth.texOffs(0, 0).addBox(-1.0F, -8.0F, -13.0F, 2.0F, 2.0F, 26.0F, 0.0F, false);
        w_mouth.texOffs(0, 0).addBox(-1.0F, -10.0F, -12.0F, 2.0F, 2.0F, 24.0F, 0.0F, false);
        w_mouth.texOffs(0, 0).addBox(-1.0F, -12.0F, -11.0F, 2.0F, 2.0F, 22.0F, 0.0F, false);
        w_mouth.texOffs(0, 0).addBox(-1.0F, -14.0F, -10.0F, 2.0F, 2.0F, 20.0F, 0.0F, false);
        w_mouth.texOffs(0, 0).addBox(-1.0F, -16.0F, -9.0F, 2.0F, 2.0F, 18.0F, 0.0F, false);
        w_mouth.texOffs(0, 0).addBox(-2.0F, -18.0F, -8.0F, 2.0F, 2.0F, 16.0F, 0.0F, false);
        w_mouth.texOffs(0, 0).addBox(-3.0F, -20.0F, -6.0F, 2.0F, 2.0F, 12.0F, 0.0F, false);
        w_mouth.texOffs(0, 0).addBox(-4.0F, -22.0F, -4.0F, 2.0F, 2.0F, 8.0F, 0.0F, false);
        w_mouth.texOffs(0, 0).addBox(-5.0F, -24.0F, -2.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        w_mouth.texOffs(0, 0).addBox(-6.0F, -26.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.add(this.base);
        this.modelRendererImmutableList = builder.build();
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return this.modelRendererImmutableList;
    }

    @Override
    public void setupAnim(MineralLeviathanHeadEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        this.base.yRot = MathHelper.rotLerp(ageInTicks % 1.0f, entity.yRotO, entity.yRot) * (float)Math.PI / -90f; //Game automatically rotates models the wrong way based on yrot, so we rotate them back by double
        this.base.xRot = (MathHelper.rotLerp(ageInTicks % 1.0f, entity.xRotO, entity.xRot) - 90f) * (float)Math.PI / -180f;
        float mouthAngle = MathHelper.rotLerp(ageInTicks % 1.0f, entity.getMouthAngleO(), entity.getMouthAngle()) * (float)Math.PI / 180f;
        this.n_mouth.xRot = mouthAngle;
        this.s_mouth.xRot = -mouthAngle;
        this.e_mouth.zRot = -mouthAngle;
        this.w_mouth.zRot = mouthAngle;
    }
}