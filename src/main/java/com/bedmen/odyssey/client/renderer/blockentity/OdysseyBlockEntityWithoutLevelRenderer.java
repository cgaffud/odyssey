package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.block.entity.TreasureChestBlockEntity;
import com.bedmen.odyssey.client.model.BoomerangModel;
import com.bedmen.odyssey.items.BEWLRBlockItem;
import com.bedmen.odyssey.items.equipment.BoomerangItem;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;

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