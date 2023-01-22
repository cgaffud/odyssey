package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.block.TreasureChestBlock;
import com.bedmen.odyssey.block.entity.TreasureChestBlockEntity;
import com.bedmen.odyssey.client.model.SpearModel;
import com.bedmen.odyssey.items.BEWLRBlockItem;
import com.bedmen.odyssey.items.aspect_items.AspectShieldItem;
import com.bedmen.odyssey.items.aspect_items.SpearItem;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
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
    private final Map<TreasureChestMaterial, TreasureChestBlockEntity> treasureChestMap = new HashMap<>();
    private SpearModel spearModel;

    public OdysseyBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.entityModelSet = entityModelSet;
        this.shieldModel = new ShieldModel(this.entityModelSet.bakeLayer(ModelLayers.SHIELD));
        for(TreasureChestMaterial treasureChestMaterial : TreasureChestMaterial.values()){
            this.treasureChestMap.put(treasureChestMaterial, new TreasureChestBlockEntity(BlockPos.ZERO, treasureChestMaterial.getBlockState()));
        }
        this.spearModel = new SpearModel(this.entityModelSet.bakeLayer(SpearModel.LAYER_LOCATION));
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
        for(TreasureChestMaterial treasureChestMaterial : TreasureChestMaterial.values()){
            this.treasureChestMap.put(treasureChestMaterial, new TreasureChestBlockEntity(BlockPos.ZERO, treasureChestMaterial.getBlockState()));
        }
        this.spearModel = new SpearModel(this.entityModelSet.bakeLayer(SpearModel.LAYER_LOCATION));
        super.onResourceManagerReload(resourceManager);
    }

    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        Item item = itemStack.getItem();
        if(item instanceof BEWLRBlockItem bewlrBlockItem){
            Block block = bewlrBlockItem.getBlock();
            BlockState blockstate = block.defaultBlockState();
            BlockEntity blockEntity = null;
            if (blockstate.getBlock() instanceof TreasureChestBlock treasureChestBlock) {
                blockEntity = this.treasureChestMap.get(treasureChestBlock.treasureChestMaterial);
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
                    List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(itemStack), BannerBlockEntity.getItemPatterns(itemStack));
                    BannerRenderer.renderPatterns(poseStack, multiBufferSource, packedLight, packedOverlay, this.shieldModel.plate(), material, false, list, itemStack.hasFoil());
                } else {
                    this.shieldModel.plate().render(poseStack, vertexconsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                }

                poseStack.popPose();
            } else if (item instanceof SpearItem spearItem){
                boolean useItemModelNotEntityModel = transformType == ItemTransforms.TransformType.GUI || transformType == ItemTransforms.TransformType.GROUND || transformType == ItemTransforms.TransformType.FIXED;
                if(useItemModelNotEntityModel){
                    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                    BakedModel bakedModel = itemRenderer.getModel(itemStack, Minecraft.getInstance().level, Minecraft.getInstance().player, 0);
                    if (bakedModel.isLayered()) { net.minecraftforge.client.ForgeHooksClient.drawItemLayered(itemRenderer, bakedModel, itemStack, poseStack, multiBufferSource, packedLight, packedOverlay, true); }
                    else {
                        RenderType rendertype = ItemBlockRenderTypes.getRenderType(itemStack, true);
                        VertexConsumer vertexconsumer;
                        vertexconsumer = ItemRenderer.getFoilBufferDirect(multiBufferSource, rendertype, true, itemStack.hasFoil());
                        itemRenderer.renderModelLists(bakedModel, itemStack, packedLight, packedOverlay, poseStack, vertexconsumer);
                    }
                } else {
                    poseStack.pushPose();
                    poseStack.scale(1.0F, -1.0F, -1.0F);
                    VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect(multiBufferSource, this.spearModel.renderType(spearItem.getSpearType().entityTexture), false, itemStack.hasFoil());
                    this.spearModel.renderToBuffer(poseStack, vertexconsumer1, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                    poseStack.popPose();
                }
            }
            super.renderByItem(itemStack, transformType, poseStack, multiBufferSource, packedLight, packedOverlay);
        }
    }
}