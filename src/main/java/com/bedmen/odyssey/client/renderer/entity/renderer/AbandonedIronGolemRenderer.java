package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.client.renderer.entity.layers.AbandonedIronGolemCracksLayer;
import com.bedmen.odyssey.client.renderer.entity.model.AbandonedIronGolemModel;
import com.bedmen.odyssey.entity.boss.AbandonedIronGolemEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AbandonedIronGolemRenderer extends MobRenderer<AbandonedIronGolemEntity, AbandonedIronGolemModel<AbandonedIronGolemEntity>> {
    private static final ResourceLocation GOLEM_LOCATION = new ResourceLocation("textures/entity/iron_golem/iron_golem.png");

    public AbandonedIronGolemRenderer(EntityRendererManager p_i46133_1_) {
        super(p_i46133_1_, new AbandonedIronGolemModel<>(), 0.7F);
        this.addLayer(new AbandonedIronGolemCracksLayer(this));
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(AbandonedIronGolemEntity pEntity) {
        return GOLEM_LOCATION;
    }

    protected void setupRotations(AbandonedIronGolemEntity pEntityLiving, MatrixStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        super.setupRotations(pEntityLiving, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
        if (!((double)pEntityLiving.animationSpeed < 0.01D)) {
            float f = 13.0F;
            float f1 = pEntityLiving.animationPosition - pEntityLiving.animationSpeed * (1.0F - pPartialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(6.5F * f2));
        }
    }
}