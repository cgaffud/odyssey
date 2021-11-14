package com.bedmen.odyssey.client.renderer.entity.model;

import com.bedmen.odyssey.entity.monster.BabyLeviathanEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class BabyLeviathanModel extends SegmentedModel<BabyLeviathanEntity> {
    private final ImmutableList<ModelRenderer> modelRendererImmutableList;
    private final ModelRenderer center;
    private final ModelRenderer s_1;
    private final ModelRenderer s_2;
    private final ModelRenderer s_3;
    private final ModelRenderer s_4;
    private final ModelRenderer s_5;
    private final ModelRenderer s_6;
    private final ModelRenderer s_7;

    public BabyLeviathanModel() {
        texWidth = 32;
        texHeight = 32;

        center = new ModelRenderer(this);
        center.setPos(0.0F, 23.0F, 0.0F);


        s_1 = new ModelRenderer(this);
        s_1.setPos(0.0F, 0.0F, -5.0F);
        center.addChild(s_1);
        s_1.texOffs(4, 15).addBox(-2.0F, -1.0F, -3.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        s_1.texOffs(23, 15).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        s_1.texOffs(21, 3).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        s_1.texOffs(4, 4).addBox(1.0F, -1.0F, -3.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        s_1.texOffs(10, 25).addBox(-1.0F, 1.0F, -3.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        s_2 = new ModelRenderer(this);
        s_2.setPos(0.0F, 0.0F, -3.0F);
        center.addChild(s_2);
        s_2.texOffs(16, 11).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        s_3 = new ModelRenderer(this);
        s_3.setPos(0.0F, 0.0F, -1.0F);
        center.addChild(s_3);
        s_3.texOffs(2, 26).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        s_4 = new ModelRenderer(this);
        s_4.setPos(0.0F, 0.0F, 1.0F);
        center.addChild(s_4);
        s_4.texOffs(5, 13).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        s_5 = new ModelRenderer(this);
        s_5.setPos(0.0F, 0.0F, 3.0F);
        center.addChild(s_5);
        s_5.texOffs(14, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        s_6 = new ModelRenderer(this);
        s_6.setPos(0.0F, 0.0F, 5.0F);
        center.addChild(s_6);
        s_6.texOffs(19, 21).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        s_7 = new ModelRenderer(this);
        s_7.setPos(0.0F, 0.0F, 7.0F);
        center.addChild(s_7);
        s_7.texOffs(20, 24).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.add(this.center);
        this.modelRendererImmutableList = builder.build();
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return this.modelRendererImmutableList;
    }

    @Override
    public void setupAnim(BabyLeviathanEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        float f0 = (float) (Math.PI / 3.0d);
        this.s_1.x = MathHelper.sin(pLimbSwing);
        this.s_2.x = MathHelper.sin(pLimbSwing+f0);
        this.s_3.x = MathHelper.sin(pLimbSwing+f0*2);
        this.s_4.x = MathHelper.sin(pLimbSwing+f0*3);
        this.s_5.x = MathHelper.sin(pLimbSwing+f0*4);
        this.s_6.x = MathHelper.sin(pLimbSwing+f0*5);
        this.s_7.x = MathHelper.sin(pLimbSwing+f0*6);
    }
}
