package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.BoomerangModel;
import com.bedmen.odyssey.entity.projectile.BoomerangEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BoomerangRenderer extends EntityRenderer<BoomerangEntity> {
    public static final ResourceLocation[] BOOMERANG_LOCATION = new ResourceLocation[]{new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/bone_boomerang.png"), new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/copper_boomerang.png")};
    private final BoomerangModel boomerangModel = new BoomerangModel();

    public BoomerangRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(BoomerangEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(((float)entityIn.tickCount + partialTicks) * 72));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(0));
        IVertexBuilder ivertexbuilder = net.minecraft.client.renderer.ItemRenderer.getFoilBufferDirect(bufferIn, this.boomerangModel.renderType(getTextureLocation(entityIn)), false, entityIn.isFoil());
        this.boomerangModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(BoomerangEntity boomerangEntity) {
        return BOOMERANG_LOCATION[boomerangEntity.getBoomerangType().ordinal()];
    }
}
