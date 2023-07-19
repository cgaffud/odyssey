package com.bedmen.odyssey.client.model;// Made with Blockbench 4.7.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.permafrost.PermafrostBigIcicleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class PermafrostBigIcicleModel<T extends PermafrostBigIcicleEntity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Odyssey.MOD_ID, "permafrost_big_icicle"), "main");

	private final ModelPart spikes;

	public PermafrostBigIcicleModel(ModelPart root) {
		this.spikes = root.getChild("spikes");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition spikes = partdefinition.addOrReplaceChild("spikes", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -9.0F, -5.5F, 11.0F, 9.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(-6, -6).addBox(-16.0F, -7.5F, -4.0F, 12.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(-2, -2).addBox(-28.0F, -6.0F, -2.0F, 12.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

		double theta = Mth.TWO_PI / 20.0D * (double) (-ageInTicks % 20.0f);
		double thetaA = theta - (double) entity.getIcicleId() * Math.PI * 2.0D / (double) PermafrostBigIcicleEntity.TOTAL_NUM;
//		System.out.printf("Theta: %g, ThetaA: %g\n", theta, thetaA);
		float thetaB = (float) (thetaA - Mth.HALF_PI);
		this.spikes.yRot = thetaB;
		this.spikes.xRot = -Mth.PI;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		spikes.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}