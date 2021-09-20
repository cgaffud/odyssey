package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.LupineModel;
import com.bedmen.odyssey.entity.monster.LupineEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LupineRenderer extends MobRenderer<LupineEntity, LupineModel<LupineEntity>> {
    private static final ResourceLocation ANGRY_WOLF_TEXTURES = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/lupine.png");

    public LupineRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new LupineModel<>(), 0.5F);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float getBob(LupineEntity livingBase, float partialTicks) {
        return livingBase.getTailRotation();
    }

    public void render(LupineEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.isWolfWet()) {
            float f = entityIn.getShadingWhileWet(partialTicks);
            this.model.setColor(f, f, f);
        }

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (entityIn.isWolfWet()) {
            this.model.setColor(1.0F, 1.0F, 1.0F);
        }

    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(LupineEntity entity) {
        return ANGRY_WOLF_TEXTURES;
    }
}