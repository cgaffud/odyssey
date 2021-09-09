package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.PermafrostModel;
import com.bedmen.odyssey.entity.boss.PermafrostEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PermafrostRenderer extends MobRenderer<PermafrostEntity, PermafrostModel<PermafrostEntity>> {
    public static final ResourceLocation ACTIVE_SHELL_RESOURCE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/permafrost/cage");
    public static final ResourceLocation WIND_RESOURCE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/permafrost/wind");
    public static final ResourceLocation VERTICAL_WIND_RESOURCE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/permafrost/wind_vertical");
    public static final ResourceLocation OPEN_EYE_RESOURCE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/permafrost/open_eye");
    public static final RenderMaterial ACTIVE_SHELL_TEXTURE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, ACTIVE_SHELL_RESOURCE_LOCATION);
    public static final RenderMaterial WIND_TEXTURE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, WIND_RESOURCE_LOCATION);
    public static final RenderMaterial VERTICAL_WIND_TEXTURE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, VERTICAL_WIND_RESOURCE_LOCATION);
    public static final RenderMaterial OPEN_EYE_TEXTURE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, OPEN_EYE_RESOURCE_LOCATION);
    private static final ResourceLocation PERMAFROST_TEXTURE = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/permafrost/permafrost.png");
    private final ModelRenderer eye = new ModelRenderer(16, 16, 0, 0);
    private final ModelRenderer wind;
    private final ModelRenderer shell;
    private final ModelRenderer cage;

    public PermafrostRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new PermafrostModel<>(), 1.0F);
        this.eye.addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, 0.01F);
        this.wind = new ModelRenderer(64, 32, 0, 0);
        this.wind.addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F);
        this.shell = new ModelRenderer(32, 16, 0, 0);
        this.shell.addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F);
        this.cage = new ModelRenderer(32, 16, 0, 0);
        this.cage.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(PermafrostEntity p_110775_1_) {
        return PERMAFROST_TEXTURE;
    }

    public void render(PermafrostEntity entity, float p_225616_2_, float secondfloat, MatrixStack p_225616_3_, IRenderTypeBuffer p_225616_4_, int p_225616_5_) {
        super.render(entity, p_225616_2_, secondfloat, p_225616_3_, p_225616_4_, p_225616_5_);
        p_225616_5_ = 15728640;
        float f = (float)entity.tickCount + p_225616_2_;
        float f1 = entity.getActiveRotation(p_225616_2_) * (180F / (float)Math.PI);
        double halfheight = entity.getBbHeight() * 0.5d;
        p_225616_3_.pushPose();
        p_225616_3_.translate( 0.0D, halfheight, 0.0D);
        float f6 = 3.0f;
        p_225616_3_.scale(f6, f6, f6);
        Vector3f vector3f = new Vector3f(0.5F, 1.0F, 0.5F);
        vector3f.normalize();
        p_225616_3_.mulPose(new Quaternion(vector3f, f1, true));
        this.cage.render(p_225616_3_, ACTIVE_SHELL_TEXTURE.buffer(p_225616_4_, RenderType::entityCutoutNoCull), p_225616_5_, p_225616_5_);
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

        IVertexBuilder ivertexbuilder = (i == 1 ? VERTICAL_WIND_TEXTURE : WIND_TEXTURE).buffer(p_225616_4_, RenderType::entityCutoutNoCull);
        this.wind.render(p_225616_3_, ivertexbuilder, p_225616_5_, p_225616_5_);
        p_225616_3_.popPose();
        p_225616_3_.pushPose();
        p_225616_3_.translate(0.0D, halfheight, 0.0D);
        float f5 = 3.0f * 0.875f;
        p_225616_3_.scale(f5, f5, f5);
        p_225616_3_.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        p_225616_3_.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        this.wind.render(p_225616_3_, ivertexbuilder, p_225616_5_, p_225616_5_);
        p_225616_3_.popPose();
        ActiveRenderInfo activerenderinfo = this.entityRenderDispatcher.camera;
        p_225616_3_.pushPose();
        p_225616_3_.translate(0.0D, halfheight, 0.0D);
        float f3 = -activerenderinfo.getYRot();
        p_225616_3_.mulPose(Vector3f.YP.rotationDegrees(f3));
        p_225616_3_.mulPose(Vector3f.XP.rotationDegrees(activerenderinfo.getXRot()));
        p_225616_3_.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        float f4 = 2.0f;
        p_225616_3_.scale(f4, f4, f4);
        this.eye.render(p_225616_3_, (OPEN_EYE_TEXTURE).buffer(p_225616_4_, RenderType::entityCutoutNoCull), p_225616_5_, p_225616_5_);
        p_225616_3_.popPose();
    }
}