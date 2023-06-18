package com.bedmen.odyssey.client.model;

import com.bedmen.odyssey.entity.monster.ZombieBrute;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;

public class ZombieBruteModel<T extends ZombieBrute> extends ZombieModel<T> {
    public static final float BODY_SCALE = 1.5f;
    public static final float HEAD_SCALE = 1.2f;
    public static final float HEAD_RATIO = HEAD_SCALE / BODY_SCALE;

    public ZombieBruteModel(ModelPart modelPart) {
        super(modelPart);
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int p_102036_, int p_102037_, float p_102038_, float p_102039_, float p_102040_, float p_102041_) {
        this.bodyParts().forEach((modelPart) -> modelPart.render(poseStack, vertexConsumer, p_102036_, p_102037_, p_102038_, p_102039_, p_102040_, p_102041_));
        poseStack.pushPose();
        poseStack.scale(HEAD_RATIO, HEAD_RATIO, HEAD_RATIO);
        this.headParts().forEach((modelPart) -> modelPart.render(poseStack, vertexConsumer, p_102036_, p_102037_, p_102038_, p_102039_, p_102040_, p_102041_));
        poseStack.popPose();
    }
}
