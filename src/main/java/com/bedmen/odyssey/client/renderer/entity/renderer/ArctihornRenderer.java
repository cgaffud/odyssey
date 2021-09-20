package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.ArctihornModel;
import com.bedmen.odyssey.entity.monster.ArctihornEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ArctihornRenderer extends MobRenderer<ArctihornEntity, ArctihornModel<ArctihornEntity>> {
    private static final ResourceLocation ARCTIHORN_TEXTURES = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/arctihorn.png");

    public ArctihornRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ArctihornModel<>(), 0.7F);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(ArctihornEntity entity) {
        return ARCTIHORN_TEXTURES;
    }

    protected void setupRotations(ArctihornEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        float f = MathHelper.lerp(partialTicks, entityLiving.prevSquidPitch, entityLiving.squidPitch);
        float f1 = MathHelper.lerp(partialTicks, entityLiving.prevSquidYaw, entityLiving.squidYaw);
        matrixStackIn.translate(0.0D, 0.5D, 0.0D);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(f));
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f1));
        matrixStackIn.translate(0.0D, (double)-1.2F, 0.0D);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float getBob(ArctihornEntity livingBase, float partialTicks) {
        return MathHelper.lerp(partialTicks, livingBase.lastTentacleAngle, livingBase.tentacleAngle);
    }
}