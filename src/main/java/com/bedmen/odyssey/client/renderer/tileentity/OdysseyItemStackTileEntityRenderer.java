package com.bedmen.odyssey.client.renderer.tileentity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.OdysseyTridentModel;
import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.items.OdysseyTridentItem;
import com.bedmen.odyssey.util.ItemRegistry;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import net.minecraft.block.*;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.entity.model.TridentModel;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.tileentity.BannerTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.BedTileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.ConduitTileEntity;
import net.minecraft.tileentity.EnderChestTileEntity;
import net.minecraft.tileentity.ShulkerBoxTileEntity;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TrappedChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;

@OnlyIn(Dist.CLIENT)
public class OdysseyItemStackTileEntityRenderer extends ItemStackTileEntityRenderer implements Callable<ItemStackTileEntityRenderer> {
    public static final OdysseyItemStackTileEntityRenderer odysseyInstance = new OdysseyItemStackTileEntityRenderer();
    private final OdysseyTridentModel odysseyTridentModel = new OdysseyTridentModel();
    private final ShieldModel shieldModel = new ShieldModel();

    @Override
    public void renderByItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        Item item = itemStack.getItem();
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

    @Override
    public ItemStackTileEntityRenderer call() throws Exception {
        return this;
    }
}