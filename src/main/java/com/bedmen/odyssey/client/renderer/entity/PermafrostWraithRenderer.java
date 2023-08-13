package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.PermafrostWraithModel;
import com.bedmen.odyssey.entity.boss.permafrost.PermafrostWraith;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;

public class PermafrostWraithRenderer<T extends PermafrostWraith> extends MobRenderer<T, PermafrostWraithModel<T>> {
    private static final ResourceLocation WRAITH_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/permafrost/wraith.png");

    public PermafrostWraithRenderer(EntityRendererProvider.Context context) {
        super(context, new PermafrostWraithModel<>(context.bakeLayer(PermafrostWraithModel.LAYER_LOCATION)), 0.8F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    protected void scale(T entity, PoseStack poseStack, float partialTicks) {
        float f = entity.getScale();
        poseStack.scale(f,f,f);
    }

    @Override
    public void render(T permafrostWraith, float p_115456_, float partialTicks, PoseStack poseStack, MultiBufferSource p_115459_, int p_115460_) {
        super.render(permafrostWraith, p_115456_, partialTicks, poseStack, p_115459_, p_115460_);
        if (permafrostWraith.doDeathAnim()) {
            poseStack.translate(-0.5, 0, -0.5);
            long t = permafrostWraith.getLevel().getGameTime();
            int beaconStart = Mth.floor(permafrostWraith.getEyeHeight());
            BeaconRenderer.renderBeaconBeam(poseStack, p_115459_, BeaconRenderer.BEAM_LOCATION, partialTicks, 1.0f, t, beaconStart, permafrostWraith.level.getMaxBuildHeight(), DyeColor.WHITE.getTextureDiffuseColors(), 0.2F, 0.25F);
        }
    }

    protected float getFlipDegrees(T pLivingEntity) {
        return 180.0F;
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(T pEntity) {
        return WRAITH_LOCATION;
    }
}