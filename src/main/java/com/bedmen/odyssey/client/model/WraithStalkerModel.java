package com.bedmen.odyssey.client.model;// Made with Blockbench 4.7.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.WraithStalker;
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

public class WraithStalkerModel<T extends WraithStalker> extends AgeableListModel<T> implements ArmedModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Odyssey.MOD_ID, "wraith_stalker"), "main");
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart torso;
	private final float ALMOST_HALF_PI = Mth.HALF_PI * 3/4;

	public WraithStalkerModel(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.torso = root.getChild("torso");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 20.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -9.0F, -4.25F, 8.0F, 10.0F, 8.0F, new CubeDeformation(-0.5F))
				.texOffs(36, 34).addBox(-4.0F, -5.0F, -3.25F, 8.0F, 6.0F, 7.0F, new CubeDeformation(-0.5F)), PartPose.offset(0.0F, -13.0F, 0.25F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(36, 0).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 30.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -13.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(24, 18).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 30.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -13.0F, 0.0F));

		PartDefinition torso = partdefinition.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 42).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	private ModelPart getArm(HumanoidArm arm) {
		return arm == HumanoidArm.LEFT ? this.left_arm : this.right_arm;
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, this.left_arm, this.right_arm, this.torso);
	}

	@Override
	public void translateToHand(HumanoidArm p_102854_, PoseStack p_102855_) {
		this.getArm(p_102854_).translateAndRotate(p_102855_);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//this.setupAttackAnimation(entity, ageInTicks);
		this.right_arm.xRot = (float)(Math.atan(limbSwing * 0.6662F) * 0.1 * limbSwingAmount);
		this.left_arm.xRot = (float)(Math.atan(limbSwing * 0.6662F) * 0.1 * limbSwingAmount);
		this.right_arm.zRot = 0.0F;
		this.left_arm.zRot = 0.0F;
		this.torso.xRot = (float)(Math.atan(limbSwing * 0.6662) * 0.15 * limbSwingAmount);
		this.torso.yRot = 0.0F;
		this.torso.zRot = 0.0F;

		ItemStack itemstack = entity.getMainHandItem();

		if (!entity.isAggressive()) {
			if ((GeneralUtil.isHashTick(entity, entity.getLevel(), 8) && (entity.getRandom().nextFloat() < 0.10))) {
				this.head.setRotation(entity.getRandom().nextFloat() * Mth.HALF_PI - Mth.HALF_PI/2 + ALMOST_HALF_PI/4, this.head.yRot, entity.getRandom().nextFloat() * Mth.HALF_PI - Mth.HALF_PI/2);
			} else if (GeneralUtil.isHashTick(entity, entity.getLevel(), 4) && (entity.getRandom().nextFloat() < 0.8)) {
				this.head.setRotation(entity.getRandom().nextFloat() * ALMOST_HALF_PI - ALMOST_HALF_PI/2 + ALMOST_HALF_PI/4,
						this.head.yRot,
						entity.getRandom().nextFloat() * ALMOST_HALF_PI - ALMOST_HALF_PI/2);
			} else {
				this.head.xRot = 0;
				this.head.zRot = 0;
			}
		} else {
			this.head.xRot = 0;
			this.head.zRot = 0;
		}

		if (entity.isAggressive() && (itemstack.isEmpty() || !(itemstack.getItem() instanceof CrossbowItem))) {
			float f = Mth.sin(this.attackTime * (float)Math.PI);
			float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float)Math.PI);
			this.right_arm.zRot = 0.0F;
			this.left_arm.zRot = 0.0F;
			this.right_arm.yRot = -(0.1F - f * 0.6F);
			this.left_arm.yRot = 0.1F - f * 0.6F;
			this.right_arm.xRot -= f * 1.2F - f1 * 0.4F;
			this.left_arm.xRot -= f * 1.2F - f1 * 0.4F;
		}

	}
}