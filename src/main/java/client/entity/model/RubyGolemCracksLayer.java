package client.entity.model;

import entities.RubyGolemEntity;
import client.entity.model.RubyGolemModel;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.Map;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RubyGolemCracksLayer extends LayerRenderer<RubyGolemEntity, RubyGolemModel<RubyGolemEntity>> {
   private static final Map<RubyGolemEntity.Cracks, ResourceLocation> field_229134_a_ = ImmutableMap.of(RubyGolemEntity.Cracks.LOW, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_low.png"), RubyGolemEntity.Cracks.MEDIUM, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_medium.png"), RubyGolemEntity.Cracks.HIGH, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_high.png"));

   public RubyGolemCracksLayer(IEntityRenderer<RubyGolemEntity, RubyGolemModel<RubyGolemEntity>> p_i226040_1_) {
      super(p_i226040_1_);
   }

   public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, RubyGolemEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (!entitylivingbaseIn.isInvisible()) {
         RubyGolemEntity.Cracks RubyGolementity$cracks = entitylivingbaseIn.func_226512_l_();
         if (RubyGolementity$cracks != RubyGolemEntity.Cracks.NONE) {
            ResourceLocation resourcelocation = field_229134_a_.get(RubyGolementity$cracks);
            renderCutoutModel(this.getEntityModel(), resourcelocation, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, 1.0F, 1.0F, 1.0F);
         }
      }
   }
}
