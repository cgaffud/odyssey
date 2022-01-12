package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.entity.TreasureChestBlockEntity;
import com.bedmen.odyssey.items.BEWLRBlockItem;
import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class OdysseyBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {
    public static final ResourceLocation WOODEN_SHIELD_BASE_NOPATTERN_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/shields/wooden_shield_base_nopattern");
    public static final ResourceLocation WOODEN_SHIELD_BASE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/shields/wooden_shield_base");
    public static final Material WOODEN_SHIELD_BASE_NOPATTERN_MATERIAL = new Material(Sheets.SHIELD_SHEET, WOODEN_SHIELD_BASE_NOPATTERN_LOCATION);
    public static final Material WOODEN_SHIELD_BASE_MATERIAL = new Material(Sheets.SHIELD_SHEET, WOODEN_SHIELD_BASE_LOCATION);
    public static final ResourceLocation COPPER_SHIELD_BASE_NOPATTERN_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/shields/copper_shield_base_nopattern");
    public static final ResourceLocation COPPER_SHIELD_BASE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/shields/copper_shield_base");
    public static final Material COPPER_SHIELD_BASE_NOPATTERN_MATERIAL = new Material(Sheets.SHIELD_SHEET, COPPER_SHIELD_BASE_NOPATTERN_LOCATION);
    public static final Material COPPER_SHIELD_BASE_MATERIAL = new Material(Sheets.SHIELD_SHEET, COPPER_SHIELD_BASE_LOCATION);
    public static final ResourceLocation REINFORCED_SHIELD_BASE_NOPATTERN_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/shields/reinforced_shield_base_nopattern");
    public static final ResourceLocation REINFORCED_SHIELD_BASE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/shields/reinforced_shield_base");
    public static final Material REINFORCED_SHIELD_BASE_NOPATTERN_MATERIAL = new Material(Sheets.SHIELD_SHEET, REINFORCED_SHIELD_BASE_NOPATTERN_LOCATION);
    public static final Material REINFORCED_SHIELD_BASE_MATERIAL = new Material(Sheets.SHIELD_SHEET, REINFORCED_SHIELD_BASE_LOCATION);
    private static OdysseyBlockEntityWithoutLevelRenderer instance = null;
    private final EntityModelSet entityModelSet;
    private final ChestBlockEntity sterlingSilverChest = new TreasureChestBlockEntity(TreasureChestMaterial.STERLING_SILVER, BlockPos.ZERO, Blocks.CHEST.defaultBlockState());

    public OdysseyBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.entityModelSet = entityModelSet;
        this.shieldModel = new ShieldModel(this.entityModelSet.bakeLayer(ModelLayers.SHIELD));
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
        super.onResourceManagerReload(resourceManager);
    }

    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_108834_, int p_108835_) {
        Item item = itemStack.getItem();
        if(item instanceof BEWLRBlockItem bewlrBlockItem){
            Block block = bewlrBlockItem.getBlock();
            BlockState blockstate = block.defaultBlockState();
            BlockEntity blockEntity = null;
            if (blockstate.is(BlockRegistry.STERLING_SILVER_CHEST.get())) {
                blockEntity = this.sterlingSilverChest;
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