package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.NewTridentModel;
import com.bedmen.odyssey.entity.projectile.AbstractTridentEntity;
import com.bedmen.odyssey.util.ItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NewTridentRenderer extends EntityRenderer<AbstractTridentEntity> {
   public static final ResourceLocation TRIDENT = new ResourceLocation("textures/entity/trident.png");
   public static final ResourceLocation SERPENT_TRIDENT = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/serpent_trident.png");
   private final NewTridentModel tridentModel = new NewTridentModel();

   public NewTridentRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn);
   }

   public void render(AbstractTridentEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
      matrixStackIn.pushPose();
      matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.yRotO, entityIn.yRot) - 90.0F));
      matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.xRotO, entityIn.xRot) + 90.0F));
      IVertexBuilder ivertexbuilder = net.minecraft.client.renderer.ItemRenderer.getFoilBufferDirect(bufferIn, this.tridentModel.renderType(getTextureLocation(entityIn)), false, entityIn.isFoil());
      this.tridentModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      matrixStackIn.popPose();
      super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getTextureLocation(AbstractTridentEntity entity) {
      Item item = entity.getItem();
      if(item == ItemRegistry.SERPENT_TRIDENT.get()) return SERPENT_TRIDENT;
      return TRIDENT;
   }
}
