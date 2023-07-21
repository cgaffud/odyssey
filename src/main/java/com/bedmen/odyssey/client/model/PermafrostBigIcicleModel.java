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
		this.spikes.yRot = entity.getYRot();
		this.spikes.xRot = entity.getXRot();
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		spikes.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}