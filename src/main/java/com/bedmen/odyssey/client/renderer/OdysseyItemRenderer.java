package com.bedmen.odyssey.client.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.items.aspect_items.SpearItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;

import javax.annotation.Nullable;

public class OdysseyItemRenderer extends ItemRenderer {
    public OdysseyItemRenderer(TextureManager textureManager, ModelManager modelManager, ItemColors itemColors, BlockEntityWithoutLevelRenderer blockEntityWithoutLevelRenderer) {
        super(textureManager, modelManager, itemColors, blockEntityWithoutLevelRenderer);
    }

    public void render(ItemStack itemStack, ItemTransforms.TransformType transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, BakedModel bakedModel) {
        if (!itemStack.isEmpty()) {
            boolean flag = transformType == ItemTransforms.TransformType.GUI || transformType == ItemTransforms.TransformType.GROUND || transformType == ItemTransforms.TransformType.FIXED;
            if (flag) {
                if (itemStack.getItem() instanceof SpearItem) {
                    bakedModel = this.itemModelShaper.getModelManager().getModel(new ModelResourceLocation(Odyssey.MOD_ID+":spear#inventory"));
                }
            }
        }
        super.render(itemStack, transformType, leftHand, poseStack, multiBufferSource, packedLight, packedOverlay, bakedModel);
    }

//    public BakedModel getModel(ItemStack itemStack, @Nullable Level level, @Nullable LivingEntity livingEntity, int i) {
//        BakedModel bakedmodel;
//        if (itemStack.is(Items.TRIDENT)) {
//            bakedmodel = this.itemModelShaper.getModelManager().getModel(new ModelResourceLocation("minecraft:trident_in_hand#inventory"));
//        } else if (itemStack.is(Items.SPYGLASS)) {
//            bakedmodel = this.itemModelShaper.getModelManager().getModel(new ModelResourceLocation("minecraft:spyglass_in_hand#inventory"));
//        } else {
//            bakedmodel = this.itemModelShaper.getItemModel(itemStack);
//        }
//
//        ClientLevel clientlevel = level instanceof ClientLevel ? (ClientLevel)level : null;
//        BakedModel bakedmodel1 = bakedmodel.getOverrides().resolve(bakedmodel, itemStack, clientlevel, livingEntity, i);
//        return bakedmodel1 == null ? this.itemModelShaper.getModelManager().getMissingModel() : bakedmodel1;
//    }
}
