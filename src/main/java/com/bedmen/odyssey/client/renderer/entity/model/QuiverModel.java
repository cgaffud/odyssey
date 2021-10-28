package com.bedmen.odyssey.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QuiverModel<T extends LivingEntity> extends AgeableModel<T> {
    private final ModelRenderer bb_main;
    private final ModelRenderer quiver_r1;

    public QuiverModel() {
        texWidth = 16;
        texHeight = 16;

        bb_main = new ModelRenderer(this);
        bb_main.setPos(0.0F, 24.0F, 0.0F);


        quiver_r1 = new ModelRenderer(this);
        quiver_r1.setPos(0.0F, 0.0F, 0.0F);
        bb_main.addChild(quiver_r1);
        setRotationAngle(quiver_r1, 0.5236F, 0.0F, 0.0F);
        quiver_r1.texOffs(0, 0).addBox(-1.5F, -4.0F, -1.5F, 3.0F, 8.0F, 3.0F, 0.0F, false);
    }

    protected Iterable<ModelRenderer> headParts() {
        return ImmutableList.of();
    }

    protected Iterable<ModelRenderer> bodyParts() {
        return ImmutableList.of(this.quiver_r1);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}