package com.bedmen.odyssey.client.model;// Made with Blockbench 4.7.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.permafrost.Wraithling;
import com.bedmen.odyssey.util.GeneralUtil;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

public class WraithlingModel<T extends Wraithling> extends AgeableListModel<T> implements ArmedModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Odyssey.MOD_ID, "wraithling"), "main");
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart torso;

	private final float ALMOST_HALF_PI = Mth.HALF_PI * 3 / 4;

	public WraithlingModel(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.left_arm = root.getChild("left_arm");
		this.right_arm = root.getChild("right_arm");
		this.torso = root.getChild("torso");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 4).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(45, 7).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(26, 16).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(48, 16).mirror().addBox(-2.0F, -2.0F, -2.0F, 2.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(48, 16).addBox(0.0F, -2.0F, -2.0F, 2.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition torso = partdefinition.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-3.0F, 0.0F, 0.0F, 6.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 8.0F, -2.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Wraithling entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.right_arm.xRot = (float) (Math.atan(limbSwing * 0.6662F) * 0.1 * limbSwingAmount);
		this.left_arm.xRot = (float) (Math.atan(limbSwing * 0.6662F) * 0.1 * limbSwingAmount);
		this.right_arm.zRot = 0.0F;
		this.left_arm.zRot = 0.0F;
		this.right_arm.y = 2.0F;
		this.left_arm.y = 2.0F;
		this.torso.xRot = (float) (Math.atan(limbSwing * 0.6662) * 0.15 * limbSwingAmount);
		this.torso.yRot = 0.0F;
		this.torso.zRot = 0.0F;

		ItemStack itemstack = entity.getMainHandItem();
		if (entity.isAggressive()) {
			if ((GeneralUtil.isHashTick(entity, entity.getLevel(), 32) && (entity.getRandom().nextFloat() < 0.10))) {
				this.head.setRotation(entity.getRandom().nextFloat() * Mth.HALF_PI - Mth.HALF_PI / 2 + ALMOST_HALF_PI / 4, this.head.yRot, entity.getRandom().nextFloat() * Mth.HALF_PI - Mth.HALF_PI / 2);
			} else if (GeneralUtil.isHashTick(entity, entity.getLevel(), 16) && (entity.getRandom().nextFloat() < 0.8)) {
				this.head.setRotation(entity.getRandom().nextFloat() * ALMOST_HALF_PI - ALMOST_HALF_PI / 2 + ALMOST_HALF_PI / 4,
						this.head.yRot,
						entity.getRandom().nextFloat() * ALMOST_HALF_PI - ALMOST_HALF_PI / 2);
			} else {
				this.head.xRot = ALMOST_HALF_PI / 4;
				this.head.zRot = 0;
			}
		} else {
			this.head.xRot = ALMOST_HALF_PI / 2;
			this.head.zRot = 0;
		}

		if (entity.isAggressive() && (itemstack.isEmpty() || !(itemstack.getItem() instanceof CrossbowItem))) {
			float f = Mth.sin(this.attackTime * (float) Math.PI);
			float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float) Math.PI);
			this.right_arm.zRot = 0.0F;
			this.left_arm.zRot = 0.0F;
			this.right_arm.yRot = -(0.1F - f * 0.6F);
			this.left_arm.yRot = 0.1F - f * 0.6F;
			this.right_arm.xRot = (-(float) Math.PI / 2F);
			this.left_arm.xRot = (-(float) Math.PI / 2F);
			this.right_arm.xRot -= f * 1.2F - f1 * 0.4F;
			this.left_arm.xRot -= f * 1.2F - f1 * 0.4F;
//            AnimationUtils.bobArms(this.right_arm, this.left_arm, ageInTicks);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body);
	}

	@Override
	public void translateToHand(HumanoidArm p_102854_, PoseStack p_102855_) {
		this.getArm(p_102854_).translateAndRotate(p_102855_);
	}

	private ModelPart getArm(HumanoidArm arm) {
		return arm == HumanoidArm.LEFT ? this.left_arm : this.right_arm;
	}
}