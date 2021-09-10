package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.MineralLeviathanEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class MineralLeviathanRenderer extends MobRenderer<MineralLeviathanEntity, MineralLeviathanRenderer.MineralLeviathanModel> {
    private static final ResourceLocation MINERAL_LEVIATHAN_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/body.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(MINERAL_LEVIATHAN_LOCATION);
    public MineralLeviathanRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new MineralLeviathanModel(), 0.7F);
        this.shadowRadius = 0.5F;
    }

    public void render(MineralLeviathanEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        p_225623_4_.pushPose();
        float f = (float)p_225623_1_.getLatencyPos(7, p_225623_3_)[0];
        float f1 = (float)(p_225623_1_.getLatencyPos(5, p_225623_3_)[1] - p_225623_1_.getLatencyPos(10, p_225623_3_)[1]);
        p_225623_4_.mulPose(Vector3f.YP.rotationDegrees(-f));
        p_225623_4_.mulPose(Vector3f.XP.rotationDegrees(f1 * 10.0F));
        p_225623_4_.translate(0.0D, 0.0D, 1.0D);
        p_225623_4_.scale(-1.0F, -1.0F, 1.0F);
        p_225623_4_.translate(0.0D, (double)-1.501F, 0.0D);
        boolean flag = p_225623_1_.hurtTime > 0;
        this.model.prepareMobModel(p_225623_1_, 0.0F, 0.0F, p_225623_3_);
        IVertexBuilder ivertexbuilder3 = p_225623_5_.getBuffer(RENDER_TYPE);
        this.model.renderToBuffer(p_225623_4_, ivertexbuilder3, p_225623_6_, OverlayTexture.pack(0.0F, flag), 1.0F, 1.0F, 1.0F, 1.0F);

        p_225623_4_.popPose();

        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    public ResourceLocation getTextureLocation(MineralLeviathanEntity p_110775_1_) {
        return MINERAL_LEVIATHAN_LOCATION;
    }

    @OnlyIn(Dist.CLIENT)
    public static class MineralLeviathanModel extends EntityModel<MineralLeviathanEntity> {
        private final ImmutableList<ModelRenderer> parts;
        private final ModelRenderer head;
        private final ModelRenderer[] body = new ModelRenderer[9];
        @Nullable
        private MineralLeviathanEntity entity;
        private float a;

        public MineralLeviathanModel() {
            this.texWidth = 128;
            this.texHeight = 128;
            this.head = new ModelRenderer(this, 0, 0);
            this.head.addBox(-16.0f,-8.0f,-16.0f,32.0f,32.0f,32.0f);
            for(int i = 0; i < 9; i++){
                this.body[i] = new ModelRenderer(this, 0, 0);
                this.body[i].addBox(-16.0f,-8.0f,-16.0f,32.0f,32.0f,32.0f);
            }

            ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
            builder.add(this.head);
            builder.addAll(Arrays.asList(this.body));
            this.parts = builder.build();
        }

        public void prepareMobModel(MineralLeviathanEntity entity, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
            this.entity = entity;
            this.a = p_212843_4_;
        }

        public void setupAnim(MineralLeviathanEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
        }

        public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
            /*p_225598_1_.pushPose();
            float f = MathHelper.lerp(this.a, this.entity.oFlapTime, this.entity.flapTime);
            this.jaw.xRot = (float)(Math.sin((double)(f * ((float)Math.PI * 2F))) + 1.0D) * 0.2F;
            float f1 = (float)(Math.sin((double)(f * ((float)Math.PI * 2F) - 1.0F)) + 1.0D);
            f1 = (f1 * f1 + f1 * 2.0F) * 0.05F;
            p_225598_1_.translate(0.0D, (double)(f1 - 2.0F), -3.0D);
            p_225598_1_.mulPose(Vector3f.XP.rotationDegrees(f1 * 2.0F));
            float f2 = 0.0F;
            float f3 = 20.0F;
            float f4 = -12.0F;
            float f5 = 1.5F;
            double[] adouble = this.entity.getLatencyPos(6, this.a);
            float f6 = MathHelper.rotWrap(this.entity.getLatencyPos(5, this.a)[0] - this.entity.getLatencyPos(10, this.a)[0]);
            float f7 = MathHelper.rotWrap(this.entity.getLatencyPos(5, this.a)[0] + (double)(f6 / 2.0F));
            float f8 = f * ((float)Math.PI * 2F);

            for(int i = 0; i < 5; ++i) {
                double[] adouble1 = this.entity.getLatencyPos(5 - i, this.a);
                float f9 = (float)Math.cos((double)((float)i * 0.45F + f8)) * 0.15F;
                this.neck.yRot = MathHelper.rotWrap(adouble1[0] - adouble[0]) * ((float)Math.PI / 180F) * 1.5F;
                this.neck.xRot = f9 + this.entity.getHeadPartYOffset(i, adouble, adouble1) * ((float)Math.PI / 180F) * 1.5F * 5.0F;
                this.neck.zRot = -MathHelper.rotWrap(adouble1[0] - (double)f7) * ((float)Math.PI / 180F) * 1.5F;
                this.neck.y = f3;
                this.neck.z = f4;
                this.neck.x = f2;
                f3 = (float)((double)f3 + Math.sin((double)this.neck.xRot) * 10.0D);
                f4 = (float)((double)f4 - Math.cos((double)this.neck.yRot) * Math.cos((double)this.neck.xRot) * 10.0D);
                f2 = (float)((double)f2 - Math.sin((double)this.neck.yRot) * Math.cos((double)this.neck.xRot) * 10.0D);
                this.neck.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_);
            }

            this.head.y = f3;
            this.head.z = f4;
            this.head.x = f2;
            double[] adouble2 = this.entity.getLatencyPos(0, this.a);
            this.head.yRot = MathHelper.rotWrap(adouble2[0] - adouble[0]) * ((float)Math.PI / 180F);
            this.head.xRot = MathHelper.rotWrap((double)this.entity.getHeadPartYOffset(6, adouble, adouble2)) * ((float)Math.PI / 180F) * 1.5F * 5.0F;
            this.head.zRot = -MathHelper.rotWrap(adouble2[0] - (double)f7) * ((float)Math.PI / 180F);
            this.head.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_);
            p_225598_1_.pushPose();
            p_225598_1_.translate(0.0D, 1.0D, 0.0D);
            p_225598_1_.mulPose(Vector3f.ZP.rotationDegrees(-f6 * 1.5F));
            p_225598_1_.translate(0.0D, -1.0D, 0.0D);
            this.body.zRot = 0.0F;
            this.body.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_);
            float f10 = f * ((float)Math.PI * 2F);
            this.leftWing.xRot = 0.125F - (float)Math.cos((double)f10) * 0.2F;
            this.leftWing.yRot = -0.25F;
            this.leftWing.zRot = -((float)(Math.sin((double)f10) + 0.125D)) * 0.8F;
            this.leftWingTip.zRot = (float)(Math.sin((double)(f10 + 2.0F)) + 0.5D) * 0.75F;
            this.rightWing.xRot = this.leftWing.xRot;
            this.rightWing.yRot = -this.leftWing.yRot;
            this.rightWing.zRot = -this.leftWing.zRot;
            this.rightWingTip.zRot = -this.leftWingTip.zRot;
            this.renderSide(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, f1, this.leftWing, this.leftFrontLeg, this.leftFrontLegTip, this.leftFrontFoot, this.leftRearLeg, this.leftRearLegTip, this.leftRearFoot);
            this.renderSide(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, f1, this.rightWing, this.rightFrontLeg, this.rightFrontLegTip, this.rightFrontFoot, this.rightRearLeg, this.rightRearLegTip, this.rightRearFoot);
            p_225598_1_.popPose();
            float f11 = -((float)Math.sin((double)(f * ((float)Math.PI * 2F)))) * 0.0F;
            f8 = f * ((float)Math.PI * 2F);
            f3 = 10.0F;
            f4 = 60.0F;
            f2 = 0.0F;
            adouble = this.entity.getLatencyPos(11, this.a);

            for(int j = 0; j < 12; ++j) {
                adouble2 = this.entity.getLatencyPos(12 + j, this.a);
                f11 = (float)((double)f11 + Math.sin((double)((float)j * 0.45F + f8)) * (double)0.05F);
                this.neck.yRot = (MathHelper.rotWrap(adouble2[0] - adouble[0]) * 1.5F + 180.0F) * ((float)Math.PI / 180F);
                this.neck.xRot = f11 + (float)(adouble2[1] - adouble[1]) * ((float)Math.PI / 180F) * 1.5F * 5.0F;
                this.neck.zRot = MathHelper.rotWrap(adouble2[0] - (double)f7) * ((float)Math.PI / 180F) * 1.5F;
                this.neck.y = f3;
                this.neck.z = f4;
                this.neck.x = f2;
                f3 = (float)((double)f3 + Math.sin((double)this.neck.xRot) * 10.0D);
                f4 = (float)((double)f4 - Math.cos((double)this.neck.yRot) * Math.cos((double)this.neck.xRot) * 10.0D);
                f2 = (float)((double)f2 - Math.sin((double)this.neck.yRot) * Math.cos((double)this.neck.xRot) * 10.0D);
                this.neck.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_);
            }

            p_225598_1_.popPose();*/
        }
    }
}