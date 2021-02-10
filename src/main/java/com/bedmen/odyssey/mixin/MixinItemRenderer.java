package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.QuiverItem;
import com.bedmen.odyssey.util.ItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer{

    @Shadow
    public IBakedModel getItemModelWithOverrides(ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entitylivingbaseIn) {return null;}
    @Shadow
    public void renderItem(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformTypeIn, boolean leftHand, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, IBakedModel modelIn) {}

    public void renderItem(@Nullable LivingEntity livingEntityIn, ItemStack itemStackIn, ItemCameraTransforms.TransformType transformTypeIn, boolean leftHand, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, @Nullable World worldIn, int combinedLightIn, int combinedOverlayIn) {
        if (!itemStackIn.isEmpty() && !(itemStackIn.getItem() instanceof QuiverItem && leftHand)) {
            IBakedModel ibakedmodel = this.getItemModelWithOverrides(itemStackIn, worldIn, livingEntityIn);
            this.renderItem(itemStackIn, transformTypeIn, leftHand, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, ibakedmodel);
        }
    }

}
