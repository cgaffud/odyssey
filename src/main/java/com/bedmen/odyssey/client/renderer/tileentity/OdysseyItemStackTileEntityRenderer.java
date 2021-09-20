package com.bedmen.odyssey.client.renderer.tileentity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.OdysseyTridentModel;
import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.items.OdysseyTridentItem;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.tileentity.SterlingSilverChestTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.tileentity.BannerTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.*;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OdysseyItemStackTileEntityRenderer extends ItemStackTileEntityRenderer implements Callable<ItemStackTileEntityRenderer> {
    public static final OdysseyItemStackTileEntityRenderer odysseyInstance = new OdysseyItemStackTileEntityRenderer();
    private final OdysseyTridentModel odysseyTridentModel = new OdysseyTridentModel();
    private final ShieldModel shieldModel = new ShieldModel();
    private final Supplier<SterlingSilverChestTileEntity> sterlingSilverChest = SterlingSilverChestTileEntity::new;

    @Override
    public void renderByItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        Item item = itemStack.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem)item).getBlock();
            TileEntity tileentity;
            if (block == BlockRegistry.STERLING_SILVER_CHEST.get()) {
                tileentity = this.sterlingSilverChest.get();
                TileEntityRendererDispatcher.instance.renderItem(tileentity, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        } else {
            if (item instanceof OdysseyShieldItem) {
                boolean flag = itemStack.getTagElement("BlockEntityTag") != null;
                matrixStack.pushPose();
                matrixStack.scale(1.0F, -1.0F, -1.0F);
                RenderMaterial rendermaterial;
                if(item == ItemRegistry.LEVIATHAN_SHIELD.get()){
                    rendermaterial = flag ?  Odyssey.LEVIATHAN_SHIELD_BASE : Odyssey.LEVIATHAN_SHIELD_BASE_NOPATTERN;
                }

                else {
                    rendermaterial = flag ? ModelBakery.SHIELD_BASE : ModelBakery.NO_PATTERN_SHIELD;
                }
                IVertexBuilder ivertexbuilder = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, this.shieldModel.renderType(rendermaterial.atlasLocation()), true, itemStack.hasFoil()));
                this.shieldModel.handle().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                if (flag) {
                    List<Pair<BannerPattern, DyeColor>> list = BannerTileEntity.createPatterns(ShieldItem.getColor(itemStack), BannerTileEntity.getItemPatterns(itemStack));
                    BannerTileEntityRenderer.renderPatterns(matrixStack, buffer, combinedLight, combinedOverlay, this.shieldModel.plate(), rendermaterial, false, list, itemStack.hasFoil());
                } else {
                    this.shieldModel.plate().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                }

                matrixStack.popPose();
            } else if (item instanceof OdysseyTridentItem) {
                matrixStack.pushPose();
                matrixStack.scale(1.0F, -1.0F, -1.0F);
                IVertexBuilder ivertexbuilder1 = ItemRenderer.getFoilBufferDirect(buffer, this.odysseyTridentModel.renderType(OdysseyTridentModel.getTridentTexture(item)), false, itemStack.hasFoil());
                this.odysseyTridentModel.renderToBuffer(matrixStack, ivertexbuilder1, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStack.popPose();
            } else {
                super.renderByItem(itemStack, transformType, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }

    @Override
    public ItemStackTileEntityRenderer call() throws Exception {
        return this;
    }
}