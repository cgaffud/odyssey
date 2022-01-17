package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.entity.OdysseySignBlockEntity;
import com.bedmen.odyssey.block.wood.OdysseySignBlock;
import com.bedmen.odyssey.block.wood.OdysseyStandingSignBlock;
import com.bedmen.odyssey.block.wood.OdysseyWallSignBlock;
import com.bedmen.odyssey.block.wood.OdysseyWoodType;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OdysseySignRenderer implements BlockEntityRenderer<OdysseySignBlockEntity> {
    public static final int MAX_LINE_WIDTH = 90;
    private static final int LINE_HEIGHT = 10;
    private static final String STICK = "stick";
    private static final int BLACK_TEXT_OUTLINE_COLOR = -988212;
    private static final int OUTLINE_RENDER_DISTANCE = Mth.square(16);
    public final Map<WoodType, OdysseySignModel> signModels;
    private final Font font;

    public OdysseySignRenderer(BlockEntityRendererProvider.Context p_173636_) {
        this.signModels = OdysseyWoodType.WOOD_TYPES.stream().collect(ImmutableMap.toImmutableMap((woodType) -> {
            return woodType;
        }, (woodType) -> {
            return new OdysseySignModel(p_173636_.bakeLayer(OdysseySignModel.LAYER_LOCATION.get(woodType)));
        }));
        this.font = p_173636_.getFont();
    }

    public void render(OdysseySignBlockEntity odysseySignBlockEntity, float p_112498_, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_112501_, int p_112502_) {
        BlockState blockstate = odysseySignBlockEntity.getBlockState();
        poseStack.pushPose();
        float f = 0.6666667F;
        WoodType woodtype = getWoodType(blockstate.getBlock());
        OdysseySignRenderer.OdysseySignModel signrenderer$signmodel = this.signModels.get(woodtype);
        if (blockstate.getBlock() instanceof OdysseyStandingSignBlock) {
            poseStack.translate(0.5D, 0.5D, 0.5D);
            float f1 = -((float)(blockstate.getValue(StandingSignBlock.ROTATION) * 360) / 16.0F);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(f1));
            signrenderer$signmodel.stick.visible = true;
        } else {
            poseStack.translate(0.5D, 0.5D, 0.5D);
            float f4 = -blockstate.getValue(OdysseyWallSignBlock.FACING).toYRot();
            poseStack.mulPose(Vector3f.YP.rotationDegrees(f4));
            poseStack.translate(0.0D, -0.3125D, -0.4375D);
            signrenderer$signmodel.stick.visible = false;
        }

        poseStack.pushPose();
        poseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
        Material material = Sheets.getSignMaterial(woodtype);
        VertexConsumer vertexconsumer = material.buffer(multiBufferSource, signrenderer$signmodel::renderType);
        signrenderer$signmodel.root.render(poseStack, vertexconsumer, p_112501_, p_112502_);
        poseStack.popPose();
        float f2 = 0.010416667F;
        poseStack.translate(0.0D, (double)0.33333334F, (double)0.046666667F);
        poseStack.scale(0.010416667F, -0.010416667F, 0.010416667F);
        int i = getDarkColor(odysseySignBlockEntity);
        int j = 20;
        FormattedCharSequence[] aformattedcharsequence = odysseySignBlockEntity.getRenderMessages(Minecraft.getInstance().isTextFilteringEnabled(), (p_173653_) -> {
            List<FormattedCharSequence> list = this.font.split(p_173653_, 90);
            return list.isEmpty() ? FormattedCharSequence.EMPTY : list.get(0);
        });
        int k;
        boolean flag;
        int l;
        if (odysseySignBlockEntity.hasGlowingText()) {
            k = odysseySignBlockEntity.getColor().getTextColor();
            flag = isOutlineVisible(odysseySignBlockEntity, k);
            l = 15728880;
        } else {
            k = i;
            flag = false;
            l = p_112501_;
        }

        for(int i1 = 0; i1 < 4; ++i1) {
            FormattedCharSequence formattedcharsequence = aformattedcharsequence[i1];
            float f3 = (float)(-this.font.width(formattedcharsequence) / 2);
            if (flag) {
                this.font.drawInBatch8xOutline(formattedcharsequence, f3, (float)(i1 * 10 - 20), k, i, poseStack.last().pose(), multiBufferSource, l);
            } else {
                this.font.drawInBatch(formattedcharsequence, f3, (float)(i1 * 10 - 20), k, false, poseStack.last().pose(), multiBufferSource, false, 0, l);
            }
        }

        poseStack.popPose();
    }

    private static boolean isOutlineVisible(OdysseySignBlockEntity p_173642_, int p_173643_) {
        if (p_173643_ == DyeColor.BLACK.getTextColor()) {
            return true;
        } else {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer localplayer = minecraft.player;
            if (localplayer != null && minecraft.options.getCameraType().isFirstPerson() && localplayer.isScoping()) {
                return true;
            } else {
                Entity entity = minecraft.getCameraEntity();
                return entity != null && entity.distanceToSqr(Vec3.atCenterOf(p_173642_.getBlockPos())) < (double)OUTLINE_RENDER_DISTANCE;
            }
        }
    }

    private static int getDarkColor(OdysseySignBlockEntity p_173640_) {
        int i = p_173640_.getColor().getTextColor();
        double d0 = 0.4D;
        int j = (int)((double)NativeImage.getR(i) * 0.4D);
        int k = (int)((double)NativeImage.getG(i) * 0.4D);
        int l = (int)((double)NativeImage.getB(i) * 0.4D);
        return i == DyeColor.BLACK.getTextColor() && p_173640_.hasGlowingText() ? -988212 : NativeImage.combine(0, l, k, j);
    }

    public static WoodType getWoodType(Block p_173638_) {
        WoodType woodtype;
        if (p_173638_ instanceof OdysseySignBlock) {
            woodtype = ((OdysseySignBlock)p_173638_).type();
        } else {
            woodtype = OdysseyWoodType.PALM;
        }

        return woodtype;
    }

    public static OdysseySignRenderer.OdysseySignModel createSignModel(EntityModelSet p_173647_, WoodType woodType) {
        return new OdysseySignRenderer.OdysseySignModel(p_173647_.bakeLayer(OdysseySignModel.LAYER_LOCATION.get(woodType)));
    }

    public static LayerDefinition createSignLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("sign", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -14.0F, -1.0F, 24.0F, 12.0F, 2.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 14.0F, 2.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    public static ModelLayerLocation createSignModelName(WoodType p_171292_) {
        ResourceLocation location = new ResourceLocation(p_171292_.name());
        return new ModelLayerLocation(new ResourceLocation(Odyssey.MOD_ID, "sign/" + location.getPath()), "main");
    }

    @OnlyIn(Dist.CLIENT)
    public static final class OdysseySignModel extends Model {
        public static final Map<WoodType, ModelLayerLocation> LAYER_LOCATION = WoodType.values().collect(ImmutableMap.toImmutableMap((woodType) -> {
            return woodType;
        }, OdysseySignRenderer::createSignModelName));
        public final ModelPart root;
        public final ModelPart stick;

        public OdysseySignModel(ModelPart p_173657_) {
            super(RenderType::entityCutoutNoCull);
            this.root = p_173657_;
            this.stick = p_173657_.getChild("stick");
        }

        public void renderToBuffer(PoseStack p_112510_, VertexConsumer p_112511_, int p_112512_, int p_112513_, float p_112514_, float p_112515_, float p_112516_, float p_112517_) {
            this.root.render(p_112510_, p_112511_, p_112512_, p_112513_, p_112514_, p_112515_, p_112516_, p_112517_);
        }
    }
}