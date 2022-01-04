package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.block.entity.TreasureChestBlockEntity;
import com.bedmen.odyssey.items.BEWLRBlockItem;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OdysseyBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {
    private static OdysseyBlockEntityWithoutLevelRenderer instance = null;
    private final EntityModelSet entityModelSet;
    private final ChestBlockEntity sterlingSilverChest = new TreasureChestBlockEntity(TreasureChestMaterial.STERLING_SILVER, BlockPos.ZERO, Blocks.CHEST.defaultBlockState());

    public OdysseyBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.entityModelSet = entityModelSet;
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
            super.renderByItem(itemStack, transformType, poseStack, multiBufferSource, p_108834_, p_108835_);
        }
    }
}