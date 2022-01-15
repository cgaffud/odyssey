package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.block.TreasureChestBlock;
import com.bedmen.odyssey.block.entity.TreasureChestBlockEntity;
import com.bedmen.odyssey.items.BEWLRBlockItem;
import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class OdysseyBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {
    private static OdysseyBlockEntityWithoutLevelRenderer instance = null;
    private final EntityModelSet entityModelSet;
    private final Map<TreasureChestMaterial, TreasureChestBlockEntity> treasureChestMap = new HashMap<>();

    public OdysseyBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.entityModelSet = entityModelSet;
        this.shieldModel = new ShieldModel(this.entityModelSet.bakeLayer(ModelLayers.SHIELD));
        for(TreasureChestMaterial treasureChestMaterial : TreasureChestMaterial.values()){
            this.treasureChestMap.put(treasureChestMaterial, new TreasureChestBlockEntity(BlockPos.ZERO, treasureChestMaterial.getBlockState()));
        }
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
        this.treasureChestMap.clear();
        for(TreasureChestMaterial treasureChestMaterial : TreasureChestMaterial.values()){
            this.treasureChestMap.put(treasureChestMaterial, new TreasureChestBlockEntity(BlockPos.ZERO, treasureChestMaterial.getBlockState()));
        }
        super.onResourceManagerReload(resourceManager);
    }

    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_108834_, int p_108835_) {
        Item item = itemStack.getItem();
        if(item instanceof BEWLRBlockItem bewlrBlockItem){
            Block block = bewlrBlockItem.getBlock();
            BlockState blockstate = block.defaultBlockState();
            BlockEntity blockEntity = null;
            if (blockstate.getBlock() instanceof TreasureChestBlock treasureChestBlock) {
                blockEntity = this.treasureChestMap.get(treasureChestBlock.treasureChestMaterial);
            }
            if(blockEntity != null){
                this.blockEntityRenderDispatcher.renderItem(blockEntity, poseStack, multiBufferSource, p_108834_, p_108835_);
            }
        } else {
            if (itemStack.getItem() instanceof OdysseyShieldItem odysseyShieldItem) {
                boolean flag = BlockItem.getBlockEntityData(itemStack) != null;
                poseStack.pushPose();
                poseStack.scale(1.0F, -1.0F, -1.0F);
                Material material = odysseyShieldItem.getRenderMaterial(flag);
                VertexConsumer vertexconsumer = material.sprite().wrap(ItemRenderer.getFoilBufferDirect(multiBufferSource, this.shieldModel.renderType(material.atlasLocation()), true, itemStack.hasFoil()));
                this.shieldModel.handle().render(poseStack, vertexconsumer, p_108834_, p_108835_, 1.0F, 1.0F, 1.0F, 1.0F);
                if (flag) {
                    List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(itemStack), BannerBlockEntity.getItemPatterns(itemStack));
                    BannerRenderer.renderPatterns(poseStack, multiBufferSource, p_108834_, p_108835_, this.shieldModel.plate(), material, false, list, itemStack.hasFoil());
                } else {
                    this.shieldModel.plate().render(poseStack, vertexconsumer, p_108834_, p_108835_, 1.0F, 1.0F, 1.0F, 1.0F);
                }

                poseStack.popPose();
            }
            super.renderByItem(itemStack, transformType, poseStack, multiBufferSource, p_108834_, p_108835_);
        }
    }
}