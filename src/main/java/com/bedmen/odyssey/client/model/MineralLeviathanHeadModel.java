package com.bedmen.odyssey.client.model;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanHead;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MineralLeviathanHeadModel extends EntityModel<MineralLeviathanHead> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Odyssey.MOD_ID, "mineral_leviathan_head"), "main");
	private final ModelPart base;
	private final ModelPart s_mouth;
	private final ModelPart e_mouth;
	private final ModelPart n_mouth;
	private final ModelPart w_mouth;

	public MineralLeviathanHeadModel(ModelPart root) {
		this.base = root.getChild("base");
		this.s_mouth = this.base.getChild("s_mouth");
		this.e_mouth = this.base.getChild("e_mouth");
		this.n_mouth = this.base.getChild("n_mouth");
		this.w_mouth = this.base.getChild("w_mouth");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, 10.0F, 14.0F, 30.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-14.0F, 10.0F, -16.0F, 30.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(14.0F, 10.0F, -14.0F, 2.0F, 4.0F, 30.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-16.0F, 10.0F, -16.0F, 2.0F, 4.0F, 30.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-16.0F, 14.0F, -16.0F, 32.0F, 2.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition s_mouth = base.addOrReplaceChild("s_mouth", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -4.0F, -1.0F, 32.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 4).addBox(-15.0F, -6.0F, -1.0F, 30.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 8).addBox(-14.0F, -8.0F, -1.0F, 28.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 12).addBox(-13.0F, -10.0F, -1.0F, 26.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 16).addBox(-12.0F, -12.0F, -1.0F, 24.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 20).addBox(-11.0F, -14.0F, -1.0F, 22.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 24).addBox(-10.0F, -16.0F, -1.0F, 20.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 28).addBox(-9.0F, -18.0F, -1.0F, 18.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 32).addBox(-8.0F, -20.0F, -2.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 36).addBox(-6.0F, -22.0F, -3.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-4.0F, -24.0F, -4.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 44).addBox(-2.0F, -26.0F, -5.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 48).addBox(-1.0F, -28.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 15.0F));

		PartDefinition e_mouth = base.addOrReplaceChild("e_mouth", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -16.0F, 2.0F, 2.0F, 32.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -6.0F, -15.0F, 2.0F, 2.0F, 30.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -8.0F, -14.0F, 2.0F, 2.0F, 28.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -10.0F, -13.0F, 2.0F, 2.0F, 26.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -12.0F, -12.0F, 2.0F, 2.0F, 24.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -14.0F, -11.0F, 2.0F, 2.0F, 22.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -16.0F, -10.0F, 2.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -18.0F, -9.0F, 2.0F, 2.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(0.0F, -20.0F, -8.0F, 2.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(1.0F, -22.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(2.0F, -24.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.0F, -26.0F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(4.0F, -28.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-15.0F, 12.0F, 0.0F));

		PartDefinition n_mouth = base.addOrReplaceChild("n_mouth", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -4.0F, -1.0F, 32.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 4).addBox(-15.0F, -6.0F, -1.0F, 30.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 8).addBox(-14.0F, -8.0F, -1.0F, 28.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 12).addBox(-13.0F, -10.0F, -1.0F, 26.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 16).addBox(-12.0F, -12.0F, -1.0F, 24.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 20).addBox(-11.0F, -14.0F, -1.0F, 22.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 24).addBox(-10.0F, -16.0F, -1.0F, 20.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 28).addBox(-9.0F, -18.0F, -1.0F, 18.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 32).addBox(-8.0F, -20.0F, 0.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 36).addBox(-6.0F, -22.0F, 1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-4.0F, -24.0F, 2.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 44).addBox(-2.0F, -26.0F, 3.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 48).addBox(-1.0F, -28.0F, 4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, -15.0F));

		PartDefinition w_mouth = base.addOrReplaceChild("w_mouth", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -16.0F, 2.0F, 2.0F, 32.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -6.0F, -15.0F, 2.0F, 2.0F, 30.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -8.0F, -14.0F, 2.0F, 2.0F, 28.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -10.0F, -13.0F, 2.0F, 2.0F, 26.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -12.0F, -12.0F, 2.0F, 2.0F, 24.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -14.0F, -11.0F, 2.0F, 2.0F, 22.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -16.0F, -10.0F, 2.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -18.0F, -9.0F, 2.0F, 2.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-2.0F, -20.0F, -8.0F, 2.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-3.0F, -22.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-4.0F, -24.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-5.0F, -26.0F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-6.0F, -28.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(15.0F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(MineralLeviathanHead entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.base.yRot = Mth.rotLerp(ageInTicks % 1.0f, entity.yRotO, entity.getYRot()) * (float)Math.PI / -90f; //Game automatically rotates models the wrong way based on yrot, so we rotate them back by double
        this.base.xRot = (Mth.rotLerp(ageInTicks % 1.0f, entity.xRotO, entity.getXRot()) - 90f) * (float)Math.PI / -180f;
        float mouthAngle = Mth.rotLerp(ageInTicks % 1.0f, entity.mouthAngleO, entity.mouthAngle) * (float)Math.PI / 180f;
        this.n_mouth.xRot = mouthAngle;
        this.s_mouth.xRot = -mouthAngle;
        this.e_mouth.zRot = -mouthAngle;
        this.w_mouth.zRot = mouthAngle;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		base.render(poseStack, buffer, packedLight, packedOverlay);
	}
}