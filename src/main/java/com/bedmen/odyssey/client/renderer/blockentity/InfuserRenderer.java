package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.block.entity.InfuserBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class InfuserRenderer extends AbstractInfusionPedestalRenderer<InfuserBlockEntity> {

    private static final ResourceLocation EXPERIENCE_ORB_LOCATION = new ResourceLocation("textures/entity/experience_orb.png");
    private static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(EXPERIENCE_ORB_LOCATION);

    public InfuserRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public void render(InfuserBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        super.render(blockEntity, partialTicks, poseStack, multiBufferSource, packedLight, packedOverlay);
        blockEntity.pathParticleList.forEach(pathParticle -> renderPathParticle(blockEntity, pathParticle, partialTicks, poseStack, multiBufferSource, packedLight));
    }

    protected float getItemScale(InfuserBlockEntity blockEntity) {
        return 1.0f;
    }

    private static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, int red, int blue, float u, float v, int packedLight) {
        vertexConsumer.vertex(matrix4f, x, y, 0.0F).color(red, 255, blue, 128).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private void renderPathParticle(InfuserBlockEntity infuserBlockEntity, InfuserBlockEntity.PathParticle pathParticle, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight){
        if(pathParticle.isVisible){

//            ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;
//            BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
//            particlerendertype.begin(bufferbuilder, particleEngine.textureManager);
//
//            EnchantmentTableParticle.render

            poseStack.pushPose();
            int xpOrbTypeIndex = 0;
            float f = (float)(xpOrbTypeIndex % 4 * 16) / 64.0F;
            float f1 = (float)(xpOrbTypeIndex % 4 * 16 + 16) / 64.0F;
            float f2 = (float)(xpOrbTypeIndex / 4 * 16) / 64.0F;
            float f3 = (float)(xpOrbTypeIndex / 4 * 16 + 16) / 64.0F;

            float f8 = (infuserBlockEntity.infuserCraftingTicks + partialTicks) / 2.0F;
            int j = (int)((Mth.sin(f8 + 0.0F) + 1.0F) * 0.5F * 255.0F);
            int l = (int)((Mth.sin(f8 + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
            Vec3 position = pathParticle.getPosition(partialTicks);
            poseStack.translate(position.x, position.y, position.z);
            poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            poseStack.scale(0.3F, 0.3F, 0.3F);
            VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RENDER_TYPE);
            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            vertex(vertexconsumer, matrix4f, matrix3f, -0.5F, -0.25F, j, l, f, f3, packedLight);
            vertex(vertexconsumer, matrix4f, matrix3f, 0.5F, -0.25F, j, l, f1, f3, packedLight);
            vertex(vertexconsumer, matrix4f, matrix3f, 0.5F, 0.75F, j, l, f1, f2, packedLight);
            vertex(vertexconsumer, matrix4f, matrix3f, -0.5F, 0.75F, j, l, f, f2, packedLight);
            poseStack.popPose();
        }
    }


}
