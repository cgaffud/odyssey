package com.bedmen.odyssey.client.model;

import com.bedmen.odyssey.entity.monster.ZombieBrute;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;

public class ZombieBruteModel<T extends ZombieBrute> extends ZombieModel<T> {
    private static final float HEAD_SCALE = 1.2f;

    public ZombieBruteModel(ModelPart modelPart) {
        super(modelPart);
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int p_102036_, int p_102037_, float p_102038_, float p_102039_, float p_102040_, float p_102041_) {
        this.bodyParts().forEach((p_102051_) -> {
            p_102051_.render(poseStack, vertexConsumer, p_102036_, p_102037_, p_102038_, p_102039_, p_102040_, p_102041_);
        });
        float f = HEAD_SCALE / ZombieBrute.SCALE;
        poseStack.scale(f, f, f);
        this.headParts().forEach((p_102061_) -> {
            p_102061_.render(poseStack, vertexConsumer, p_102036_, p_102037_, p_102038_, p_102039_, p_102040_, p_102041_);
        });
        f = 1f / f;
        poseStack.scale(f, f, f);
    }
}
