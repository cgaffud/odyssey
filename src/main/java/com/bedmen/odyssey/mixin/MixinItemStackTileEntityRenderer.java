package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.NewTridentModel;
import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.items.OdysseyTridentItem;
import com.bedmen.odyssey.util.ItemRegistry;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.*;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.BannerTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.SkullTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.*;
import net.minecraft.util.Direction;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.UUID;

@Mixin(ItemStackTileEntityRenderer.class)
public abstract class MixinItemStackTileEntityRenderer {

    @Shadow
    private BannerTileEntity banner;
    @Shadow
    private BedTileEntity bed;
    @Shadow
    private ConduitTileEntity conduit;
    @Shadow
    private ChestTileEntity chest;
    @Shadow
    private EnderChestTileEntity enderChest;
    @Shadow
    private ChestTileEntity trappedChest;
    @Shadow
    private static ShulkerBoxTileEntity DEFAULT_SHULKER_BOX;
    @Shadow
    private static ShulkerBoxTileEntity[] SHULKER_BOXES;
    @Shadow
    private ShieldModel shieldModel;

    private final NewTridentModel newtrident = new NewTridentModel();

    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem)item).getBlock();
            if (block instanceof AbstractSkullBlock) {
                GameProfile gameprofile = null;
                if (stack.hasTag()) {
                    CompoundNBT compoundnbt = stack.getTag();
                    if (compoundnbt.contains("SkullOwner", 10)) {
                        gameprofile = NBTUtil.readGameProfile(compoundnbt.getCompound("SkullOwner"));
                    } else if (compoundnbt.contains("SkullOwner", 8) && !StringUtils.isBlank(compoundnbt.getString("SkullOwner"))) {
                        GameProfile gameprofile1 = new GameProfile((UUID)null, compoundnbt.getString("SkullOwner"));
                        gameprofile = SkullTileEntity.updateGameprofile(gameprofile1);
                        compoundnbt.remove("SkullOwner");
                        compoundnbt.put("SkullOwner", NBTUtil.writeGameProfile(new CompoundNBT(), gameprofile));
                    }
                }

                SkullTileEntityRenderer.renderSkull((Direction)null, 180.0F, ((AbstractSkullBlock)block).getType(), gameprofile, 0.0F, matrixStack, buffer, combinedLight);
            } else {
                TileEntity tileentity;
                if (block instanceof AbstractBannerBlock) {
                    this.banner.fromItem(stack, ((AbstractBannerBlock)block).getColor());
                    tileentity = this.banner;
                } else if (block instanceof BedBlock) {
                    this.bed.setColor(((BedBlock)block).getColor());
                    tileentity = this.bed;
                } else if (block == Blocks.CONDUIT) {
                    tileentity = this.conduit;
                } else if (block == Blocks.CHEST) {
                    tileentity = this.chest;
                } else if (block == Blocks.ENDER_CHEST) {
                    tileentity = this.enderChest;
                } else if (block == Blocks.TRAPPED_CHEST) {
                    tileentity = this.trappedChest;
                } else {
                    if (!(block instanceof ShulkerBoxBlock)) {
                        return;
                    }

                    DyeColor dyecolor = ShulkerBoxBlock.getColorFromItem(item);
                    if (dyecolor == null) {
                        tileentity = DEFAULT_SHULKER_BOX;
                    } else {
                        tileentity = SHULKER_BOXES[dyecolor.getId()];
                    }
                }

                TileEntityRendererDispatcher.instance.renderItem(tileentity, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        } else {
            if (item instanceof OdysseyShieldItem) {
                boolean flag = stack.getTagElement("BlockEntityTag") != null;
                matrixStack.pushPose();
                matrixStack.scale(1.0F, -1.0F, -1.0F);
                RenderMaterial rendermaterial;
                if(item == ItemRegistry.SERPENT_SHIELD.get()){
                    rendermaterial = flag ?  Odyssey.SERPENT_SHIELD_BASE : Odyssey.SERPENT_SHIELD_BASE_NOPATTERN;
                }

                else {
                    rendermaterial = flag ? ModelBakery.SHIELD_BASE : ModelBakery.NO_PATTERN_SHIELD;
                }
                IVertexBuilder ivertexbuilder = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, this.shieldModel.renderType(rendermaterial.atlasLocation()), true, stack.hasFoil()));
                this.shieldModel.handle().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                if (flag) {
                    List<Pair<BannerPattern, DyeColor>> list = BannerTileEntity.createPatterns(ShieldItem.getColor(stack), BannerTileEntity.getItemPatterns(stack));
                    BannerTileEntityRenderer.renderPatterns(matrixStack, buffer, combinedLight, combinedOverlay, this.shieldModel.plate(), rendermaterial, false, list, stack.hasFoil());
                } else {
                    this.shieldModel.plate().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                }

                matrixStack.popPose();
            } else if (item instanceof OdysseyTridentItem) {
                matrixStack.pushPose();
                matrixStack.scale(1.0F, -1.0F, -1.0F);
                IVertexBuilder ivertexbuilder1 = ItemRenderer.getFoilBufferDirect(buffer, this.newtrident.renderType(NewTridentModel.getTridentTexture(item)), false, stack.hasFoil());
                this.newtrident.renderToBuffer(matrixStack, ivertexbuilder1, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStack.popPose();
            }

        }
    }

}
