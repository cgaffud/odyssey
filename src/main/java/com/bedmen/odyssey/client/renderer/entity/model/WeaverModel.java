package com.bedmen.odyssey.client.renderer.entity.model;

import com.bedmen.odyssey.entity.monster.WeaverEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class WeaverModel<T extends WeaverEntity> extends EntityModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer leg1;
    private final ModelRenderer leg2;
    private final ModelRenderer leg3;
    private final ModelRenderer leg4;
    private final ModelRenderer leg5;
    private final ModelRenderer leg6;

    public WeaverModel() {
        texWidth = 64;
        texHeight = 32;

        body = new ModelRenderer(this);
        body.setPos(0.0F, 15.0F, -4.25F);
        body.texOffs(2, 0).addBox(-5.0F, -4.0F, 2.25F, 10.0F, 8.0F, 14.0F, 0.0F, false);
        body.texOffs(4, 0).addBox(-1.0F, -1.0F, 0.25F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        body.texOffs(36, 0).addBox(-4.0F, -3.0F, -5.75F, 8.0F, 6.0F, 6.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, 15.0F, -10.0F);
        head.texOffs(0, 4).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        head.texOffs(0, 0).addBox(-1.5F, 0.5F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        head.texOffs(0, 0).addBox(0.5F, 0.5F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        leg1 = new ModelRenderer(this);
        leg1.setPos(0.0F, 15.0F, 0.0F);
        leg1.texOffs(0, 22).addBox(-17.0F, -1.0F, -8.0F, 14.0F, 2.0F, 2.0F, 0.0F, false);

        leg2 = new ModelRenderer(this);
        leg2.setPos(0.0F, 15.0F, 0.0F);
        leg2.texOffs(0, 22).addBox(3.0F, -1.0F, -8.0F, 14.0F, 2.0F, 2.0F, 0.0F, true);

        leg3 = new ModelRenderer(this);
        leg3.setPos(0.0F, 15.0F, 0.0F);
        leg3.texOffs(0, 22).addBox(-17.0F, -1.0F, -2.0F, 14.0F, 2.0F, 2.0F, 0.0F, false);

        leg4 = new ModelRenderer(this);
        leg4.setPos(0.0F, 15.0F, 0.0F);
        leg4.texOffs(0, 22).addBox(3.0F, -1.0F, -2.0F, 14.0F, 2.0F, 2.0F, 0.0F, true);

        leg5 = new ModelRenderer(this);
        leg5.setPos(0.0F, 15.0F, 0.0F);
        leg5.texOffs(0, 22).addBox(-17.0F, -1.0F, 4.0F, 14.0F, 2.0F, 2.0F, 0.0F, false);

        leg6 = new ModelRenderer(this);
        leg6.setPos(0.0F, 15.0F, 0.0F);
        leg6.texOffs(0, 22).addBox(3.0F, -1.0F, 4.0F, 14.0F, 2.0F, 2.0F, 0.0F, true);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        float f = ((float)Math.PI / 4F);
        this.leg1.zRot = -f;
        this.leg2.zRot = f;
        this.leg3.zRot = -f;
        this.leg4.zRot = f;
        this.leg5.zRot = -f;
        this.leg6.zRot = f;
        float f3 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
        this.leg1.yRot = -f3;
        this.leg2.yRot = f3;
        this.leg3.yRot = f3;
        this.leg4.yRot = -f3;
        this.leg5.yRot = -f3;
        this.leg6.yRot = f3;
        float f4 = (MathHelper.cos(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
        this.leg1.xRot = f4;
        this.leg2.xRot = -f4;
        this.leg3.xRot = -f4;
        this.leg4.xRot = f4;
        this.leg5.xRot = f4;
        this.leg6.xRot = -f4;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        leg1.render(matrixStack, buffer, packedLight, packedOverlay);
        leg2.render(matrixStack, buffer, packedLight, packedOverlay);
        leg3.render(matrixStack, buffer, packedLight, packedOverlay);
        leg4.render(matrixStack, buffer, packedLight, packedOverlay);
        leg5.render(matrixStack, buffer, packedLight, packedOverlay);
        leg6.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}