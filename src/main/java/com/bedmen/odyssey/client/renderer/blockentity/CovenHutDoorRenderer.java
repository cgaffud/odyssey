package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.block.entity.CovenHutDoorBlockEntity;
import com.bedmen.odyssey.block.wood.CovenHutDoorBlock;
import com.bedmen.odyssey.potions.FireType;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CovenHutDoorRenderer implements BlockEntityRenderer<CovenHutDoorBlockEntity> {

    public CovenHutDoorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CovenHutDoorBlockEntity covenHutDoorBlockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        Level level = covenHutDoorBlockEntity.getLevel();
        if(level != null){
            BlockPos blockPos = covenHutDoorBlockEntity.getBlockPos();
            BlockState blockState = level.getBlockState(blockPos);
            if(blockState.is(BlockRegistry.COVEN_DOOR.get())){
                poseStack.pushPose();
                Direction direction = blockState.getValue(DoorBlock.FACING);
                VertexConsumer vertexconsumer = multiBufferSource.getBuffer(Sheets.translucentCullBlockSheet());
                TextureAtlasSprite sprite = FireType.HEX.material0.sprite();
                float u0 = sprite.getU0();
                float v0 = sprite.getV0();
                float u1 = sprite.getU1();
                float v1 = sprite.getV1();
                PoseStack.Pose posestack$pose = poseStack.last();
                switch (direction){
                    case WEST -> {
                        fireVertex(posestack$pose, vertexconsumer, 0.5f, 0, 0, u1, v1);
                        fireVertex(posestack$pose, vertexconsumer, 0.5f, 0, 1, u0, v1);
                        fireVertex(posestack$pose, vertexconsumer, 0.5f, 2, 1, u0, v0);
                        fireVertex(posestack$pose, vertexconsumer, 0.5f, 2, 0, u1, v0);
                    }
                    case EAST -> {
                        fireVertex(posestack$pose, vertexconsumer, 0.5f, 0, 1, u1, v1);
                        fireVertex(posestack$pose, vertexconsumer, 0.5f, 0, 0, u0, v1);
                        fireVertex(posestack$pose, vertexconsumer, 0.5f, 2, 0, u0, v0);
                        fireVertex(posestack$pose, vertexconsumer, 0.5f, 2, 1, u1, v0);
                    }
                    case NORTH -> {
                        fireVertex(posestack$pose, vertexconsumer, 1, 0, 0.5f, u1, v1);
                        fireVertex(posestack$pose, vertexconsumer, 0, 0, 0.5f, u0, v1);
                        fireVertex(posestack$pose, vertexconsumer, 0, 2, 0.5f, u0, v0);
                        fireVertex(posestack$pose, vertexconsumer, 1, 2, 0.5f, u1, v0);
                    }
                    case SOUTH -> {
                        fireVertex(posestack$pose, vertexconsumer, 0, 0, 0.5f, u1, v1);
                        fireVertex(posestack$pose, vertexconsumer, 1, 0, 0.5f, u0, v1);
                        fireVertex(posestack$pose, vertexconsumer, 1, 2, 0.5f, u0, v0);
                        fireVertex(posestack$pose, vertexconsumer, 0, 2, 0.5f, u1, v0);
                    }

                }
                poseStack.popPose();
            }
        }
    }

    private static void fireVertex(PoseStack.Pose pose, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v) {
        vertexConsumer.vertex(pose.pose(), x, y, z).color(255, 255, 255, 128).uv(u, v).overlayCoords(0, 10).uv2(240).normal(pose.normal(), 0.0F, 1.0F, 0.0F).endVertex();
    }

}
