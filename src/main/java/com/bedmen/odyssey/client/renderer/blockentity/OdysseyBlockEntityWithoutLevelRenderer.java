package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.block.TreasureChestBlock;
import com.bedmen.odyssey.block.entity.TreasureChestBlockEntity;
import com.bedmen.odyssey.client.model.SpearModel;
import com.bedmen.odyssey.items.BEWLRBlockItem;
import com.bedmen.odyssey.items.aspect_items.AspectShieldItem;
import com.bedmen.odyssey.items.aspect_items.SpearItem;
import com.bedmen.odyssey.lock.TreasureChestType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OdysseyBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {

    private static OdysseyBlockEntityWithoutLevelRenderer instance = null;
    private final EntityModelSet entityModelSet;
    private final Map<TreasureChestType, TreasureChestBlockEntity> treasureChestMap = new HashMap<>();
    private SpearModel spearModel;
    private TridentModel tridentModel;

    public OdysseyBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.entityModelSet = entityModelSet;
        this.shieldModel = new ShieldModel(this.entityModelSet.bakeLayer(ModelLayers.SHIELD));
        for(TreasureChestType treasureChestType : TreasureChestType.values()){
            this.treasureChestMap.put(treasureChestType, new TreasureChestBlockEntity(BlockPos.ZERO, treasureChestType.getBlockState()));
        }
        this.spearModel = new SpearModel(this.entityModelSet.bakeLayer(SpearModel.LAYER_LOCATION));
        this.tridentModel = new TridentModel(this.entityModelSet.bakeLayer(ModelLayers.TRIDENT));
    }

    public static OdysseyBlockEntityWithoutLevelRenderer getInstance(){
        if(instance != null){
            return instance;
        }
        Minecraft minecraft = Minecraft.getInstance();
        instance = new OdysseyBlockEntityWithoutLevelRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        return instance;
    }

    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.shieldModel = new ShieldModel(this.entityModelSet.bakeLayer(ModelLayers.SHIELD));
        this.treasureChestMap.clear();
        for(TreasureChestType treasureChestType : TreasureChestType.values()){
            this.treasureChestMap.put(treasureChestType, new TreasureChestBlockEntity(BlockPos.ZERO, treasureChestType.getBlockState()));
        }
        this.spearModel = new SpearModel(this.entityModelSet.bakeLayer(SpearModel.LAYER_LOCATION));
        this.tridentModel = new TridentModel(this.entityModelSet.bakeLayer(ModelLayers.TRIDENT));
        super.onResourceManagerReload(resourceManager);
    }

    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        Item item = itemStack.getItem();
        if(item instanceof BEWLRBlockItem bewlrBlockItem){
            Block block = bewlrBlockItem.getBlock();
            BlockState blockstate = block.defaultBlockState();
            BlockEntity blockEntity = null;
            if (blockstate.getBlock() instanceof TreasureChestBlock treasureChestBlock) {
                blockEntity = this.treasureChestMap.get(treasureChestBlock.treasureChestType);
            }
            if(blockEntity != null){
                this.blockEntityRenderDispatcher.renderItem(blockEntity, poseStack, multiBufferSource, packedLight, packedOverlay);
            }
        } else {
            if (item instanceof AspectShieldItem aspectShieldItem) {
                boolean pattern = BlockItem.getBlockEntityData(itemStack) != null;
                poseStack.pushPose();
                poseStack.scale(1.0F, -1.0F, -1.0F);
                Material material = aspectShieldItem.shieldType.getRenderMaterial(pattern);
                VertexConsumer vertexconsumer = material.sprite().wrap(ItemRenderer.getFoilBufferDirect(multiBufferSource, this.shieldModel.renderType(material.atlasLocation()), true, itemStack.hasFoil()));
                this.shieldModel.handle().render(poseStack, vertexconsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                if (pattern) {
                    List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(itemStack), BannerBlockEntity.getItemPatterns(itemStack));
                    BannerRenderer.renderPatterns(poseStack, multiBufferSource, packedLight, packedOverlay, this.shieldModel.plate(), material, false, list, itemStack.hasFoil());
                } else {
                    this.shieldModel.plate().render(poseStack, vertexconsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                }

                poseStack.popPose();
            } else if (item instanceof SpearItem spearItem){
                boolean useItemModelNotEntityModel = transformType == ItemTransforms.TransformType.GUI || transformType == ItemTransforms.TransformType.GROUND || transformType == ItemTransforms.TransformType.FIXED;
                if(useItemModelNotEntityModel){
                    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                    BakedModel bakedModel = itemRenderer.itemModelShaper.getModelManager().getModel(spearItem.getSpearType().itemModelResourceLocation);
                    poseStack.popPose();
                    itemRenderer.render(itemStack, transformType, false, poseStack, multiBufferSource, packedLight, packedOverlay, bakedModel);
                    poseStack.pushPose();
                } else {
                    poseStack.pushPose();
                    poseStack.scale(1.0F, -1.0F, -1.0F);
                    Model model = spearItem.getSpearType().isTrident ? this.tridentModel : this.spearModel;
                    VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect(multiBufferSource, model.renderType(spearItem.getSpearType().entityTexture), false, itemStack.hasFoil());
                    model.renderToBuffer(poseStack, vertexconsumer1, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                    poseStack.popPose();
                }
            }
            super.renderByItem(itemStack, transformType, poseStack, multiBufferSource, packedLight, packedOverlay);
        }
    }
}