package client.entity.render;

import entities.RubyGolemEntity;
import client.entity.model.RubyGolemCracksLayer;
import client.entity.model.RubyGolemModel;
import com.bedmen.odyssey.Odyssey;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RubyGolemRenderer extends MobRenderer<RubyGolemEntity, RubyGolemModel<RubyGolemEntity>> {
   private static final ResourceLocation TEXTURES = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/ruby_golem.png");

   public RubyGolemRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new RubyGolemModel<>(), 0.7F);
      this.addLayer(new RubyGolemCracksLayer(this));
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getEntityTexture(RubyGolemEntity entity) {
      return TEXTURES;
   }

   protected void applyRotations(RubyGolemEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
      super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
      if (!((double)entityLiving.limbSwingAmount < 0.01D)) {
         float f = 13.0F;
         float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
         float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
         matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(6.5F * f2));
      }
   }
}
