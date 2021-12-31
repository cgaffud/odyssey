package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.TreasureChestBlock;
import com.bedmen.odyssey.block.entity.TreasureChestBlockEntity;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import java.util.Calendar;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TreasureChestRenderer<T extends TreasureChestBlockEntity> implements BlockEntityRenderer<T> {
    private static final String BOTTOM = "bottom";
    private static final String LID = "lid";
    private static final String LOCK = "lock";
    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;
    private final TreasureChestMaterial treasureChestMaterial;
    public static final ResourceLocation STERLING_SILVER_RESOURCE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/sterling_silver_chest/single");
    public static final ResourceLocation STERLING_SILVER_LOCKED_RESOURCE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "entity/sterling_silver_chest/single_locked");
    public static final Material STERLING_SILVER_RENDER_MATERIAL = new Material(Sheets.CHEST_SHEET, STERLING_SILVER_RESOURCE_LOCATION);
    public static final Material STERLING_SILVER_LOCKED_RENDER_MATERIAL = new Material(Sheets.CHEST_SHEET, STERLING_SILVER_LOCKED_RESOURCE_LOCATION);

    public TreasureChestRenderer(TreasureChestMaterial treasureChestMaterial, BlockEntityRendererProvider.Context context) {
        this.treasureChestMaterial = treasureChestMaterial;
        ModelPart modelpart = context.bakeLayer(ModelLayers.CHEST);
        this.bottom = modelpart.getChild(BOTTOM);
        this.lid = modelpart.getChild(LID);
        this.lock = modelpart.getChild(LOCK);
    }

    public void render(T blockEntity, float p_112364_, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_112367_, int p_112368_) {
        Level level = blockEntity.getLevel();
        boolean flag = level != null;
        BlockState blockstate = flag ? blockEntity.getBlockState() : this.treasureChestMaterial.getBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        Block block = blockstate.getBlock();
        if (block instanceof AbstractChestBlock) {
            AbstractChestBlock<?> abstractchestblock = (AbstractChestBlock)block;
            poseStack.pushPose();
            float f = blockstate.getValue(ChestBlock.FACING).toYRot();
            poseStack.translate(0.5D, 0.5D, 0.5D);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(-f));
            poseStack.translate(-0.5D, -0.5D, -0.5D);
            DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> neighborcombineresult;
            if (flag) {
                neighborcombineresult = abstractchestblock.combine(blockstate, level, blockEntity.getBlockPos(), true);
            } else {
                neighborcombineresult = DoubleBlockCombiner.Combiner::acceptNone;
            }

            float f1 = neighborcombineresult.<Float2FloatFunction>apply(ChestBlock.opennessCombiner(blockEntity)).get(p_112364_);
            f1 = 1.0F - f1;
            f1 = 1.0F - f1 * f1 * f1;
            int i = neighborcombineresult.<Int2IntFunction>apply(new BrightnessCombiner<>()).applyAsInt(p_112367_);
            Material material = this.getMaterial(blockstate.getValue(TreasureChestBlock.LOCKED));
            VertexConsumer vertexconsumer = material.buffer(multiBufferSource, RenderType::entityCutout);
            this.render(poseStack, vertexconsumer, this.lid, this.lock, this.bottom, f1, i, p_112368_);

            poseStack.popPose();
        }
    }

    private void render(PoseStack p_112370_, VertexConsumer p_112371_, ModelPart p_112372_, ModelPart p_112373_, ModelPart p_112374_, float p_112375_, int p_112376_, int p_112377_) {
        p_112372_.xRot = -(p_112375_ * ((float)Math.PI / 2F));
        p_112373_.xRot = p_112372_.xRot;
        p_112372_.render(p_112370_, p_112371_, p_112376_, p_112377_);
        p_112373_.render(p_112370_, p_112371_, p_112376_, p_112377_);
        p_112374_.render(p_112370_, p_112371_, p_112376_, p_112377_);
    }

    protected Material getMaterial(boolean locked) {
        switch(this.treasureChestMaterial){
            default:
            case STERLING_SILVER:
                return locked ? STERLING_SILVER_LOCKED_RENDER_MATERIAL : STERLING_SILVER_RENDER_MATERIAL;
        }
    }
}
