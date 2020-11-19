package client.entity.model;

import com.google.common.collect.ImmutableList;

import entities.RubyGolemEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RubyGolemModel<T extends RubyGolemEntity> extends SegmentedModel<T> {
   private final ModelRenderer rubyGolemHead;
   private final ModelRenderer rubyGolemBody;
   private final ModelRenderer rubyGolemRightArm;
   private final ModelRenderer rubyGolemLeftArm;
   private final ModelRenderer rubyGolemLeftLeg;
   private final ModelRenderer rubyGolemRightLeg;

   public RubyGolemModel() {
      int i = 128;
      int j = 128;
      this.rubyGolemHead = (new ModelRenderer(this)).setTextureSize(128, 128);
      this.rubyGolemHead.setRotationPoint(0.0F, -7.0F, -2.0F);
      this.rubyGolemHead.setTextureOffset(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, 0.0F);
      this.rubyGolemHead.setTextureOffset(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F, 0.0F);
      this.rubyGolemBody = (new ModelRenderer(this)).setTextureSize(128, 128);
      this.rubyGolemBody.setRotationPoint(0.0F, -7.0F, 0.0F);
      this.rubyGolemBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F, 0.0F);
      this.rubyGolemBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F, 0.5F);
      this.rubyGolemRightArm = (new ModelRenderer(this)).setTextureSize(128, 128);
      this.rubyGolemRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
      this.rubyGolemRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F, 0.0F);
      this.rubyGolemLeftArm = (new ModelRenderer(this)).setTextureSize(128, 128);
      this.rubyGolemLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
      this.rubyGolemLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F, 0.0F);
      this.rubyGolemLeftLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(128, 128);
      this.rubyGolemLeftLeg.setRotationPoint(-4.0F, 11.0F, 0.0F);
      this.rubyGolemLeftLeg.setTextureOffset(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, 0.0F);
      this.rubyGolemRightLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(128, 128);
      this.rubyGolemRightLeg.mirror = true;
      this.rubyGolemRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0F, 11.0F, 0.0F);
      this.rubyGolemRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, 0.0F);
   }

   public Iterable<ModelRenderer> getParts() {
      return ImmutableList.of(this.rubyGolemHead, this.rubyGolemBody, this.rubyGolemLeftLeg, this.rubyGolemRightLeg, this.rubyGolemRightArm, this.rubyGolemLeftArm);
   }

   /**
    * Sets this entity's model rotation angles
    */
   public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.rubyGolemHead.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
      this.rubyGolemHead.rotateAngleX = headPitch * ((float)Math.PI / 180F);
      this.rubyGolemLeftLeg.rotateAngleX = -1.5F * MathHelper.func_233021_e_(limbSwing, 13.0F) * limbSwingAmount;
      this.rubyGolemRightLeg.rotateAngleX = 1.5F * MathHelper.func_233021_e_(limbSwing, 13.0F) * limbSwingAmount;
      this.rubyGolemLeftLeg.rotateAngleY = 0.0F;
      this.rubyGolemRightLeg.rotateAngleY = 0.0F;
   }

   public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
      int i = entityIn.getAttackTimer();
      if (i > 0) {
         this.rubyGolemRightArm.rotateAngleX = -2.0F + 1.5F * MathHelper.func_233021_e_((float)i - partialTick, 10.0F);
         this.rubyGolemLeftArm.rotateAngleX = -2.0F + 1.5F * MathHelper.func_233021_e_((float)i - partialTick, 10.0F);
      } else {
            this.rubyGolemRightArm.rotateAngleX = (-0.2F + 1.5F * MathHelper.func_233021_e_(limbSwing, 13.0F)) * limbSwingAmount;
            this.rubyGolemLeftArm.rotateAngleX = (-0.2F - 1.5F * MathHelper.func_233021_e_(limbSwing, 13.0F)) * limbSwingAmount;
         }

   }
}
