package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.OdysseyTridentItem;
import com.bedmen.odyssey.items.QuiverItem;
import com.bedmen.odyssey.items.equipment.BoomerangItem;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.StainedGlassPaneBlock;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer{

    @Shadow
    private ItemModelMesher itemModelShaper;
    @Shadow
    public static IVertexBuilder getCompassFoilBufferDirect(IRenderTypeBuffer buffer, RenderType renderType, MatrixStack.Entry matrixEntry) {return null;}
    @Shadow
    public static IVertexBuilder getCompassFoilBuffer(IRenderTypeBuffer buffer, RenderType renderType, MatrixStack.Entry matrixEntry) {return null;}
    @Shadow
    public static IVertexBuilder getFoilBufferDirect(IRenderTypeBuffer buffer, RenderType renderType, boolean noEntity, boolean withGlint) {return null;}
    @Shadow
    public static IVertexBuilder getFoilBuffer(IRenderTypeBuffer bufferIn, RenderType renderTypeIn, boolean isItemIn, boolean glintIn) {return null;}
    @Shadow
    public void renderModelLists(IBakedModel modelIn, ItemStack stack, int combinedLightIn, int combinedOverlayIn, MatrixStack matrixStackIn, IVertexBuilder bufferIn) {}

    public void renderStatic(@Nullable LivingEntity livingEntityIn, ItemStack itemStackIn, ItemCameraTransforms.TransformType transformTypeIn, boolean leftHand, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, @Nullable World worldIn, int combinedLightIn, int combinedOverlayIn) {
        if (!itemStackIn.isEmpty() && !(itemStackIn.getItem() instanceof QuiverItem && leftHand)) {
            IBakedModel ibakedmodel = this.getModel(itemStackIn, worldIn, livingEntityIn);
            this.render(itemStackIn, transformTypeIn, leftHand, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, ibakedmodel);
        }
    }

    public void render(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformTypeIn, boolean leftHand, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, IBakedModel modelIn) {
        if (!itemStackIn.isEmpty()) {
            matrixStackIn.pushPose();
            boolean flag = transformTypeIn == ItemCameraTransforms.TransformType.GUI || transformTypeIn == ItemCameraTransforms.TransformType.GROUND || transformTypeIn == ItemCameraTransforms.TransformType.FIXED;
            if (itemStackIn.getItem() == ItemRegistry.TRIDENT.get() && flag) {
                modelIn = this.itemModelShaper.getModelManager().getModel(new ModelResourceLocation("minecraft:trident#inventory"));
            } else if(itemStackIn.getItem() == ItemRegistry.LEVIATHAN_TRIDENT.get() && flag) {
                modelIn = this.itemModelShaper.getModelManager().getModel(new ModelResourceLocation("oddc:leviathan_trident#inventory"));
            } else if(itemStackIn.getItem() == ItemRegistry.COPPER_BOOMERANG.get() && flag) {
                modelIn = this.itemModelShaper.getModelManager().getModel(new ModelResourceLocation("oddc:copper_boomerang#inventory"));
            }

            modelIn = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStackIn, modelIn, transformTypeIn, leftHand);
            matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
            if (!modelIn.isCustomRenderer() && (!(itemStackIn.getItem() instanceof OdysseyTridentItem) || flag)) {
                boolean flag1;
                if (transformTypeIn != ItemCameraTransforms.TransformType.GUI && !transformTypeIn.firstPerson() && itemStackIn.getItem() instanceof BlockItem) {
                    Block block = ((BlockItem)itemStackIn.getItem()).getBlock();
                    flag1 = !(block instanceof BreakableBlock) && !(block instanceof StainedGlassPaneBlock);
                } else {
                    flag1 = true;
                }
                if (modelIn.isLayered()) { net.minecraftforge.client.ForgeHooksClient.drawItemLayered(getIR(this), modelIn, itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, flag1); }
                else {
                    RenderType rendertype = RenderTypeLookup.getRenderType(itemStackIn, flag1);
                    IVertexBuilder ivertexbuilder;
                    if (itemStackIn.getItem() == Items.COMPASS && itemStackIn.hasFoil()) {
                        matrixStackIn.pushPose();
                        MatrixStack.Entry matrixstack$entry = matrixStackIn.last();
                        if (transformTypeIn == ItemCameraTransforms.TransformType.GUI) {
                            matrixstack$entry.pose().multiply(0.5F);
                        } else if (transformTypeIn.firstPerson()) {
                            matrixstack$entry.pose().multiply(0.75F);
                        }

                        if (flag1) {
                            ivertexbuilder = getCompassFoilBufferDirect(bufferIn, rendertype, matrixstack$entry);
                        } else {
                            ivertexbuilder = getCompassFoilBuffer(bufferIn, rendertype, matrixstack$entry);
                        }

                        matrixStackIn.popPose();
                    } else if (flag1) {
                        ivertexbuilder = getFoilBufferDirect(bufferIn, rendertype, true, itemStackIn.hasFoil());
                    } else {
                        ivertexbuilder = getFoilBuffer(bufferIn, rendertype, true, itemStackIn.hasFoil());
                    }

                    this.renderModelLists(modelIn, itemStackIn, combinedLightIn, combinedOverlayIn, matrixStackIn, ivertexbuilder);
                }
            } else {
                itemStackIn.getItem().getItemStackTileEntityRenderer().renderByItem(itemStackIn, transformTypeIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
            }

            matrixStackIn.popPose();
        }
    }

    public IBakedModel getModel(ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entitylivingbaseIn) {
        Item item = stack.getItem();
        IBakedModel ibakedmodel;
        if (item instanceof OdysseyTridentItem || item instanceof BoomerangItem || item instanceof TridentItem) {
            ibakedmodel = this.itemModelShaper.getModelManager().getModel(new ModelResourceLocation("minecraft:trident_in_hand#inventory"));
        } else {
            ibakedmodel = this.itemModelShaper.getItemModel(stack);
        }

        ClientWorld clientworld = worldIn instanceof ClientWorld ? (ClientWorld)worldIn : null;
        IBakedModel ibakedmodel1 = ibakedmodel.getOverrides().resolve(ibakedmodel, stack, clientworld, entitylivingbaseIn);
        return ibakedmodel1 == null ? this.itemModelShaper.getModelManager().getMissingModel() : ibakedmodel1;
    }

    public ItemRenderer getIR(Object o){
        return (ItemRenderer)o;
    }

}
