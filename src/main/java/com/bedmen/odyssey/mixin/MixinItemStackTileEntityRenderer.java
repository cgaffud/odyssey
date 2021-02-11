package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.client.renderer.entity.model.NewTridentModel;
import com.bedmen.odyssey.items.NewTridentItem;
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
    private ChestTileEntity chestBasic;
    @Shadow
    private EnderChestTileEntity enderChest;
    @Shadow
    private ChestTileEntity chestTrap;
    @Shadow
    private static ShulkerBoxTileEntity SHULKER_BOX;
    @Shadow
    private static ShulkerBoxTileEntity[] SHULKER_BOXES;
    @Shadow
    private ShieldModel modelShield;

    private final NewTridentModel newtrident = new NewTridentModel();

    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
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
                        gameprofile = SkullTileEntity.updateGameProfile(gameprofile1);
                        compoundnbt.remove("SkullOwner");
                        compoundnbt.put("SkullOwner", NBTUtil.writeGameProfile(new CompoundNBT(), gameprofile));
                    }
                }

                SkullTileEntityRenderer.render((Direction)null, 180.0F, ((AbstractSkullBlock)block).getSkullType(), gameprofile, 0.0F, matrixStack, buffer, combinedLight);
            } else {
                TileEntity tileentity;
                if (block instanceof AbstractBannerBlock) {
                    this.banner.loadFromItemStack(stack, ((AbstractBannerBlock)block).getColor());
                    tileentity = this.banner;
                } else if (block instanceof BedBlock) {
                    this.bed.setColor(((BedBlock)block).getColor());
                    tileentity = this.bed;
                } else if (block == Blocks.CONDUIT) {
                    tileentity = this.conduit;
                } else if (block == Blocks.CHEST) {
                    tileentity = this.chestBasic;
                } else if (block == Blocks.ENDER_CHEST) {
                    tileentity = this.enderChest;
                } else if (block == Blocks.TRAPPED_CHEST) {
                    tileentity = this.chestTrap;
                } else {
                    if (!(block instanceof ShulkerBoxBlock)) {
                        return;
                    }

                    DyeColor dyecolor = ShulkerBoxBlock.getColorFromItem(item);
                    if (dyecolor == null) {
                        tileentity = SHULKER_BOX;
                    } else {
                        tileentity = SHULKER_BOXES[dyecolor.getId()];
                    }
                }

                TileEntityRendererDispatcher.instance.renderItem(tileentity, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        } else {
            if (item == Items.SHIELD) {
                boolean flag = stack.getChildTag("BlockEntityTag") != null;
                matrixStack.push();
                matrixStack.scale(1.0F, -1.0F, -1.0F);
                RenderMaterial rendermaterial = flag ? ModelBakery.LOCATION_SHIELD_BASE : ModelBakery.LOCATION_SHIELD_NO_PATTERN;
                IVertexBuilder ivertexbuilder = rendermaterial.getSprite().wrapBuffer(ItemRenderer.getEntityGlintVertexBuilder(buffer, this.modelShield.getRenderType(rendermaterial.getAtlasLocation()), true, stack.hasEffect()));
                this.modelShield.func_228294_b_().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                if (flag) {
                    List<Pair<BannerPattern, DyeColor>> list = BannerTileEntity.getPatternColorData(ShieldItem.getColor(stack), BannerTileEntity.getPatternData(stack));
                    BannerTileEntityRenderer.func_241717_a_(matrixStack, buffer, combinedLight, combinedOverlay, this.modelShield.func_228293_a_(), rendermaterial, false, list, stack.hasEffect());
                } else {
                    this.modelShield.func_228293_a_().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                }

                matrixStack.pop();
            } else if (item instanceof NewTridentItem) {
                matrixStack.push();
                matrixStack.scale(1.0F, -1.0F, -1.0F);
                IVertexBuilder ivertexbuilder1 = ItemRenderer.getEntityGlintVertexBuilder(buffer, this.newtrident.getRenderType(NewTridentModel.getTridentTexture(item)), false, stack.hasEffect());
                this.newtrident.render(matrixStack, ivertexbuilder1, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStack.pop();
            }

        }
    }

}
