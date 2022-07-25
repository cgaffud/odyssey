package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.TreasureChestBlock;
import com.bedmen.odyssey.block.entity.TreasureChestBlockEntity;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
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

public class TreasureChestRenderer<T extends TreasureChestBlockEntity> implements BlockEntityRenderer<T> {
    public static final Material COPPER_MATERIAL = new Material(Sheets.CHEST_SHEET, new ResourceLocation(Odyssey.MOD_ID, "entity/treasure_chests/copper"));
    public static final Material COPPER_MATERIAL_LOCKED = new Material(Sheets.CHEST_SHEET, new ResourceLocation(Odyssey.MOD_ID, "entity/treasure_chests/copper_locked"));
    public static final Material STERLING_SILVER_MATERIAL = new Material(Sheets.CHEST_SHEET, new ResourceLocation(Odyssey.MOD_ID, "entity/treasure_chests/sterling_silver"));
    public static final Material STERLING_SILVER_MATERIAL_LOCKED = new Material(Sheets.CHEST_SHEET, new ResourceLocation(Odyssey.MOD_ID, "entity/treasure_chests/sterling_silver_locked"));
    private static final String BOTTOM = "bottom";
    private static final String LID = "lid";
    private static final String LOCK = "lock";
    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;

    public TreasureChestRenderer(TreasureChestMaterial treasureChestMaterial, BlockEntityRendererProvider.Context context) {
        ModelPart modelpart = context.bakeLayer(ModelLayers.CHEST);
        this.bottom = modelpart.getChild(BOTTOM);
        this.lid = modelpart.getChild(LID);
        this.lock = modelpart.getChild(LOCK);
    }

    public void render(T blockEntity, float p_112364_, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_112367_, int p_112368_) {
        Level level = blockEntity.getLevel();
        boolean flag = level != null;

        BlockState blockstate = flag ? blockEntity.getBlockState() : blockEntity.getBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        TreasureChestMaterial treasureChestMaterial = ((TreasureChestBlock)blockstate.getBlock()).treasureChestMaterial;
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
            VertexConsumer vertexconsumer = getRenderMaterial(treasureChestMaterial, blockstate.getValue(TreasureChestBlock.LOCKED)).buffer(multiBufferSource, RenderType::entityCutout);
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

    public static Material getRenderMaterial(TreasureChestMaterial treasureChestMaterial, boolean locked){
        return switch(treasureChestMaterial){
            case COPPER -> locked ? COPPER_MATERIAL_LOCKED : COPPER_MATERIAL;
            case STERLING_SILVER -> locked ? STERLING_SILVER_MATERIAL_LOCKED : STERLING_SILVER_MATERIAL;
        };
    }
}
