package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.client.renderer.entity.layers.AbstractCreeperChargeLayer;
import com.bedmen.odyssey.client.renderer.entity.model.AbstractCreeperModel;
import com.bedmen.odyssey.entity.monster.AbstractCreeperEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AbstractCreeperRenderer extends MobRenderer<AbstractCreeperEntity, AbstractCreeperModel<AbstractCreeperEntity>> {
    private static final ResourceLocation CREEPER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");

    public AbstractCreeperRenderer(EntityRendererManager p_i46186_1_) {
        super(p_i46186_1_, new AbstractCreeperModel<>(), 0.5F);
        this.addLayer(new AbstractCreeperChargeLayer(this));
    }

    protected void scale(CreeperEntity pLivingEntity, MatrixStack pMatrixStack, float pPartialTickTime) {
        float f = pLivingEntity.getSwelling(pPartialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        pMatrixStack.scale(f2, f3, f2);
    }

    protected float getWhiteOverlayProgress(AbstractCreeperEntity pLivingEntity, float pPartialTicks) {
        float f = pLivingEntity.getSwelling(pPartialTicks);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(AbstractCreeperEntity pEntity) {
        return CREEPER_LOCATION;
    }
}