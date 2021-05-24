package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.WerewolfModel;
import com.bedmen.odyssey.entity.monster.WerewolfEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WerewolfRenderer extends MobRenderer<WerewolfEntity, WerewolfModel<WerewolfEntity>> {
    private static final ResourceLocation ANGRY_WOLF_TEXTURES = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/werewolf.png");

    public WerewolfRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new WerewolfModel<>(), 0.5F);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(WerewolfEntity livingBase, float partialTicks) {
        return livingBase.getTailRotation();
    }

    public void render(WerewolfEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.isWolfWet()) {
            float f = entityIn.getShadingWhileWet(partialTicks);
            this.entityModel.setTint(f, f, f);
        }

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (entityIn.isWolfWet()) {
            this.entityModel.setTint(1.0F, 1.0F, 1.0F);
        }

    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(WerewolfEntity entity) {
        return ANGRY_WOLF_TEXTURES;
    }
}