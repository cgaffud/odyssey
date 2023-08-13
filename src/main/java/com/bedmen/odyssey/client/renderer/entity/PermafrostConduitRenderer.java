package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.permafrost.PermafrostConduit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PermafrostConduitRenderer extends MobRenderer<PermafrostConduit, EntityModel<PermafrostConduit>> {
    public static final ResourceLocation SHELL_RESOURCE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/permafrost/base");
    public static final ResourceLocation ACTIVE_SHELL_RESOURCE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/permafrost/cage");
    public static final ResourceLocation WIND_RESOURCE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/permafrost/wind");
    public static final ResourceLocation VERTICAL_WIND_RESOURCE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/permafrost/wind_vertical");
    public static final ResourceLocation OPEN_EYE_RESOURCE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/permafrost/open_eye");
    public static final Material SHELL_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, SHELL_RESOURCE_LOCATION);
    public static final Material ACTIVE_SHELL_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, ACTIVE_SHELL_RESOURCE_LOCATION);
    public static final Material WIND_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, WIND_RESOURCE_LOCATION);
    public static final Material VERTICAL_WIND_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, VERTICAL_WIND_RESOURCE_LOCATION);
    public static final Material OPEN_EYE_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, OPEN_EYE_RESOURCE_LOCATION);
    private static final ResourceLocation PERMAFROST_TEXTURE = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/permafrost/permafrost.png");
    private final ModelPart shell;
    private final ModelPart eye;
    private final ModelPart wind;
    private final ModelPart cage;

    public PermafrostConduitRenderer(EntityRendererProvider.Context context) {
        super(context, new EntityModel<>() {
            @Override
            public void setupAnim(PermafrostConduit p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) { }

            @Override
            public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) { }
        }, 1.0F);
        this.eye = context.bakeLayer(ModelLayers.CONDUIT_EYE);
        this.wind = context.bakeLayer(ModelLayers.CONDUIT_WIND);
        this.cage = context.bakeLayer(ModelLayers.CONDUIT_CAGE);
        this.shell = context.bakeLayer(ModelLayers.CONDUIT_SHELL);
    }

    @Override
    public ResourceLocation getTextureLocation(PermafrostConduit p_110775_1_) {
        return PERMAFROST_TEXTURE;
    }

    public void render(PermafrostConduit entity, float p_225616_2_, float secondfloat, PoseStack p_225616_3_, MultiBufferSource p_225616_4_, int packedLightIn) {
        super.render(entity, p_225616_2_, secondfloat, p_225616_3_, p_225616_4_, packedLightIn);
        int packedOverlayMagicNumber = 655360;
        if (entity.getMaster().isPresent() && (entity.getMaster().get().getTotalPhase() != 0)) {
            float f5 = entity.getActiveRotation(0.0F);
            VertexConsumer vertexconsumer1 = SHELL_TEXTURE.buffer(p_225616_4_, RenderType::entitySolid);
            double halfheight = entity.getBbHeight() * 0.5d;
            p_225616_3_.pushPose();
            float f7 = 2.0f;
            p_225616_3_.translate(0.0D, halfheight, 0.0D);
            p_225616_3_.scale(f7, f7, f7);
            p_225616_3_.mulPose(Vector3f.YP.rotationDegrees(f5));
            this.shell.render(p_225616_3_, vertexconsumer1,packedLightIn, packedOverlayMagicNumber);
            p_225616_3_.popPose();
        } else {
            float f = (float) entity.tickCount + p_225616_2_;
            float f1 = entity.getActiveRotation(p_225616_2_) * (180F / (float) Math.PI);
            double halfheight = entity.getBbHeight() * 0.5d;
            p_225616_3_.pushPose();
            p_225616_3_.translate(0.0D, halfheight, 0.0D);
            float f6 = 3.0f;
            p_225616_3_.scale(f6, f6, f6);
            Vector3f vector3f = new Vector3f(0.5F, 1.0F, 0.5F);
            vector3f.normalize();
            p_225616_3_.mulPose(vector3f.rotationDegrees(f1));
            this.cage.render(p_225616_3_, ACTIVE_SHELL_TEXTURE.buffer(p_225616_4_, RenderType::entityCutoutNoCull), packedLightIn, packedOverlayMagicNumber);
            p_225616_3_.popPose();
            int i = entity.tickCount / 66 % 3;
            p_225616_3_.pushPose();
            p_225616_3_.translate(0.0D, halfheight, 0.0D);
            p_225616_3_.scale(f6, f6, f6);
            if (i == 1) {
                p_225616_3_.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            } else if (i == 2) {
                p_225616_3_.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
            }

            VertexConsumer ivertexbuilder = (i == 1 ? VERTICAL_WIND_TEXTURE : WIND_TEXTURE).buffer(p_225616_4_, RenderType::entityCutoutNoCull);
            this.wind.render(p_225616_3_, ivertexbuilder, packedLightIn, packedOverlayMagicNumber);
            p_225616_3_.popPose();
            p_225616_3_.pushPose();
            p_225616_3_.translate(0.0D, halfheight, 0.0D);
            float f5 = 3.0f * 0.875f;
            p_225616_3_.scale(f5, f5, f5);
            p_225616_3_.mulPose(Vector3f.XP.rotationDegrees(180.0F));
            p_225616_3_.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            this.wind.render(p_225616_3_, ivertexbuilder, packedLightIn, packedOverlayMagicNumber);
            p_225616_3_.popPose();
            Camera camera = this.entityRenderDispatcher.camera;
            p_225616_3_.pushPose();
            p_225616_3_.translate(0.0D, halfheight, 0.0D);
            float f3 = -camera.getYRot();
            p_225616_3_.mulPose(Vector3f.YP.rotationDegrees(f3));
            p_225616_3_.mulPose(Vector3f.XP.rotationDegrees(camera.getXRot()));
            p_225616_3_.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            float f4 = 2.0f;
            p_225616_3_.scale(f4, f4, f4);
            this.eye.render(p_225616_3_, (OPEN_EYE_TEXTURE).buffer(p_225616_4_, RenderType::entityCutoutNoCull), packedLightIn, packedOverlayMagicNumber);
            p_225616_3_.popPose();
        }
    }
}