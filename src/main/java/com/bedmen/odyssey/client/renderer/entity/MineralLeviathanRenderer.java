package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.entity.boss.MineralLeviathanEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MineralLeviathanRenderer extends EntityRenderer<MineralLeviathanEntity> {
    public static final ResourceLocation CRYSTAL_BEAM_LOCATION = new ResourceLocation("textures/entity/end_crystal/end_crystal_beam.png");
    private static final ResourceLocation DRAGON_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon.png");
    private static final ResourceLocation DRAGON_EYES_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(DRAGON_LOCATION);
    private static final RenderType DECAL = RenderType.entityDecal(DRAGON_LOCATION);
    private static final RenderType EYES = RenderType.eyes(DRAGON_EYES_LOCATION);
    private static final RenderType BEAM = RenderType.entitySmoothCutout(CRYSTAL_BEAM_LOCATION);
    private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0D) / 2.0D);
    private final MineralLeviathanRenderer.MineralLeviathanModel model = new MineralLeviathanRenderer.MineralLeviathanModel();

    public MineralLeviathanRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager);
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

        IVertexBuilder ivertexbuilder4 = p_225623_5_.getBuffer(EYES);
        this.model.renderToBuffer(p_225623_4_, ivertexbuilder4, p_225623_6_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        p_225623_4_.popPose();

        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    private static void vertex01(IVertexBuilder p_229061_0_, Matrix4f p_229061_1_, int p_229061_2_) {
        p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
        p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
    }

    private static void vertex2(IVertexBuilder p_229060_0_, Matrix4f p_229060_1_, float p_229060_2_, float p_229060_3_) {
        p_229060_0_.vertex(p_229060_1_, -HALF_SQRT_3 * p_229060_3_, p_229060_2_, -0.5F * p_229060_3_).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex3(IVertexBuilder p_229062_0_, Matrix4f p_229062_1_, float p_229062_2_, float p_229062_3_) {
        p_229062_0_.vertex(p_229062_1_, HALF_SQRT_3 * p_229062_3_, p_229062_2_, -0.5F * p_229062_3_).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex4(IVertexBuilder p_229063_0_, Matrix4f p_229063_1_, float p_229063_2_, float p_229063_3_) {
        p_229063_0_.vertex(p_229063_1_, 0.0F, p_229063_2_, 1.0F * p_229063_3_).color(255, 0, 255, 0).endVertex();
    }

    public ResourceLocation getTextureLocation(MineralLeviathanEntity p_110775_1_) {
        return DRAGON_LOCATION;
    }

    @OnlyIn(Dist.CLIENT)
    public static class MineralLeviathanModel extends EntityModel<MineralLeviathanEntity> {
        private final ModelRenderer head;
        private final ModelRenderer neck;
        private final ModelRenderer jaw;
        private final ModelRenderer body;
        private ModelRenderer leftWing;
        private ModelRenderer leftWingTip;
        private ModelRenderer leftFrontLeg;
        private ModelRenderer leftFrontLegTip;
        private ModelRenderer leftFrontFoot;
        private ModelRenderer leftRearLeg;
        private ModelRenderer leftRearLegTip;
        private ModelRenderer leftRearFoot;
        private ModelRenderer rightWing;
        private ModelRenderer rightWingTip;
        private ModelRenderer rightFrontLeg;
        private ModelRenderer rightFrontLegTip;
        private ModelRenderer rightFrontFoot;
        private ModelRenderer rightRearLeg;
        private ModelRenderer rightRearLegTip;
        private ModelRenderer rightRearFoot;
        @Nullable
        private MineralLeviathanEntity entity;
        private float a;

        public MineralLeviathanModel() {
            this.texWidth = 256;
            this.texHeight = 256;
            float f = -16.0F;
            this.head = new ModelRenderer(this);
            this.head.addBox("upperlip", -6.0F, -1.0F, -24.0F, 12, 5, 16, 0.0F, 176, 44);
            this.head.addBox("upperhead", -8.0F, -8.0F, -10.0F, 16, 16, 16, 0.0F, 112, 30);
            this.head.mirror = true;
            this.head.addBox("scale", -5.0F, -12.0F, -4.0F, 2, 4, 6, 0.0F, 0, 0);
            this.head.addBox("nostril", -5.0F, -3.0F, -22.0F, 2, 2, 4, 0.0F, 112, 0);
            this.head.mirror = false;
            this.head.addBox("scale", 3.0F, -12.0F, -4.0F, 2, 4, 6, 0.0F, 0, 0);
            this.head.addBox("nostril", 3.0F, -3.0F, -22.0F, 2, 2, 4, 0.0F, 112, 0);
            this.jaw = new ModelRenderer(this);
            this.jaw.setPos(0.0F, 4.0F, -8.0F);
            this.jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16, 0.0F, 176, 65);
            this.head.addChild(this.jaw);
            this.neck = new ModelRenderer(this);
            this.neck.addBox("box", -5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F, 192, 104);
            this.neck.addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6, 0.0F, 48, 0);
            this.body = new ModelRenderer(this);
            this.body.setPos(0.0F, 4.0F, 8.0F);
            this.body.addBox("body", -12.0F, 0.0F, -16.0F, 24, 24, 64, 0.0F, 0, 0);
            this.body.addBox("scale", -1.0F, -6.0F, -10.0F, 2, 6, 12, 0.0F, 220, 53);
            this.body.addBox("scale", -1.0F, -6.0F, 10.0F, 2, 6, 12, 0.0F, 220, 53);
            this.body.addBox("scale", -1.0F, -6.0F, 30.0F, 2, 6, 12, 0.0F, 220, 53);
            this.leftWing = new ModelRenderer(this);
            this.leftWing.mirror = true;
            this.leftWing.setPos(12.0F, 5.0F, 2.0F);
            this.leftWing.addBox("bone", 0.0F, -4.0F, -4.0F, 56, 8, 8, 0.0F, 112, 88);
            this.leftWing.addBox("skin", 0.0F, 0.0F, 2.0F, 56, 0, 56, 0.0F, -56, 88);
            this.leftWingTip = new ModelRenderer(this);
            this.leftWingTip.mirror = true;
            this.leftWingTip.setPos(56.0F, 0.0F, 0.0F);
            this.leftWingTip.addBox("bone", 0.0F, -2.0F, -2.0F, 56, 4, 4, 0.0F, 112, 136);
            this.leftWingTip.addBox("skin", 0.0F, 0.0F, 2.0F, 56, 0, 56, 0.0F, -56, 144);
            this.leftWing.addChild(this.leftWingTip);
            this.leftFrontLeg = new ModelRenderer(this);
            this.leftFrontLeg.setPos(12.0F, 20.0F, 2.0F);
            this.leftFrontLeg.addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8, 0.0F, 112, 104);
            this.leftFrontLegTip = new ModelRenderer(this);
            this.leftFrontLegTip.setPos(0.0F, 20.0F, -1.0F);
            this.leftFrontLegTip.addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6, 0.0F, 226, 138);
            this.leftFrontLeg.addChild(this.leftFrontLegTip);
            this.leftFrontFoot = new ModelRenderer(this);
            this.leftFrontFoot.setPos(0.0F, 23.0F, 0.0F);
            this.leftFrontFoot.addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16, 0.0F, 144, 104);
            this.leftFrontLegTip.addChild(this.leftFrontFoot);
            this.leftRearLeg = new ModelRenderer(this);
            this.leftRearLeg.setPos(16.0F, 16.0F, 42.0F);
            this.leftRearLeg.addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16, 0.0F, 0, 0);
            this.leftRearLegTip = new ModelRenderer(this);
            this.leftRearLegTip.setPos(0.0F, 32.0F, -4.0F);
            this.leftRearLegTip.addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12, 0.0F, 196, 0);
            this.leftRearLeg.addChild(this.leftRearLegTip);
            this.leftRearFoot = new ModelRenderer(this);
            this.leftRearFoot.setPos(0.0F, 31.0F, 4.0F);
            this.leftRearFoot.addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24, 0.0F, 112, 0);
            this.leftRearLegTip.addChild(this.leftRearFoot);
            this.rightWing = new ModelRenderer(this);
            this.rightWing.setPos(-12.0F, 5.0F, 2.0F);
            this.rightWing.addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8, 0.0F, 112, 88);
            this.rightWing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56, 0.0F, -56, 88);
            this.rightWingTip = new ModelRenderer(this);
            this.rightWingTip.setPos(-56.0F, 0.0F, 0.0F);
            this.rightWingTip.addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4, 0.0F, 112, 136);
            this.rightWingTip.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56, 0.0F, -56, 144);
            this.rightWing.addChild(this.rightWingTip);
            this.rightFrontLeg = new ModelRenderer(this);
            this.rightFrontLeg.setPos(-12.0F, 20.0F, 2.0F);
            this.rightFrontLeg.addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8, 0.0F, 112, 104);
            this.rightFrontLegTip = new ModelRenderer(this);
            this.rightFrontLegTip.setPos(0.0F, 20.0F, -1.0F);
            this.rightFrontLegTip.addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6, 0.0F, 226, 138);
            this.rightFrontLeg.addChild(this.rightFrontLegTip);
            this.rightFrontFoot = new ModelRenderer(this);
            this.rightFrontFoot.setPos(0.0F, 23.0F, 0.0F);
            this.rightFrontFoot.addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16, 0.0F, 144, 104);
            this.rightFrontLegTip.addChild(this.rightFrontFoot);
            this.rightRearLeg = new ModelRenderer(this);
            this.rightRearLeg.setPos(-16.0F, 16.0F, 42.0F);
            this.rightRearLeg.addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16, 0.0F, 0, 0);
            this.rightRearLegTip = new ModelRenderer(this);
            this.rightRearLegTip.setPos(0.0F, 32.0F, -4.0F);
            this.rightRearLegTip.addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12, 0.0F, 196, 0);
            this.rightRearLeg.addChild(this.rightRearLegTip);
            this.rightRearFoot = new ModelRenderer(this);
            this.rightRearFoot.setPos(0.0F, 31.0F, 4.0F);
            this.rightRearFoot.addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24, 0.0F, 112, 0);
            this.rightRearLegTip.addChild(this.rightRearFoot);
        }

        public void prepareMobModel(MineralLeviathanEntity p_212843_1_, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
            this.entity = p_212843_1_;
            this.a = p_212843_4_;
        }

        public void setupAnim(MineralLeviathanEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
        }

        public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
            p_225598_1_.pushPose();
            float f = 0.0f;
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
                this.neck.xRot = f9 + 0.0f * ((float)Math.PI / 180F) * 1.5F * 5.0F;
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
            this.head.xRot = MathHelper.rotWrap((double)0.0f * ((float)Math.PI / 180F) * 1.5F * 5.0F);
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

            p_225598_1_.popPose();
        }

        private void renderSide(MatrixStack p_229081_1_, IVertexBuilder p_229081_2_, int p_229081_3_, int p_229081_4_, float p_229081_5_, ModelRenderer p_229081_6_, ModelRenderer p_229081_7_, ModelRenderer p_229081_8_, ModelRenderer p_229081_9_, ModelRenderer p_229081_10_, ModelRenderer p_229081_11_, ModelRenderer p_229081_12_) {
            p_229081_10_.xRot = 1.0F + p_229081_5_ * 0.1F;
            p_229081_11_.xRot = 0.5F + p_229081_5_ * 0.1F;
            p_229081_12_.xRot = 0.75F + p_229081_5_ * 0.1F;
            p_229081_7_.xRot = 1.3F + p_229081_5_ * 0.1F;
            p_229081_8_.xRot = -0.5F - p_229081_5_ * 0.1F;
            p_229081_9_.xRot = 0.75F + p_229081_5_ * 0.1F;
            p_229081_6_.render(p_229081_1_, p_229081_2_, p_229081_3_, p_229081_4_);
            p_229081_7_.render(p_229081_1_, p_229081_2_, p_229081_3_, p_229081_4_);
            p_229081_10_.render(p_229081_1_, p_229081_2_, p_229081_3_, p_229081_4_);
        }
    }
}