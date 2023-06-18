package com.bedmen.odyssey.client.model;// Made with Blockbench 4.7.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.WraithAmalgam;
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

public class WraithAmalgamModel<T extends WraithAmalgam> extends AgeableListModel<T> implements ArmedModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Odyssey.MOD_ID, "wraith_amalgam"), "main");
	private final ModelPart body;
	private final ModelPart r_right_arm;
	private final ModelPart r_left_arm;
	private final ModelPart torso;
	private final ModelPart l_left_arm;
	private final ModelPart l_right_arm;
	private final ModelPart rhead;
	private final ModelPart lhead;

	private final float ALMOST_HALF_PI = Mth.HALF_PI * 3/4;

	public WraithAmalgamModel(ModelPart root) {
		this.body = root.getChild("body");
		this.r_right_arm = root.getChild("r_right_arm");
		this.r_left_arm = root.getChild("r_left_arm");
		this.torso = root.getChild("torso");
		this.l_left_arm = root.getChild("l_left_arm");
		this.l_right_arm = root.getChild("l_right_arm");
		this.rhead = root.getChild("rhead");
		this.lhead = root.getChild("lhead");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -15.0F, 0.0F));

		PartDefinition lbody_r1 = body.addOrReplaceChild("lbody", CubeListBuilder.create().texOffs(0, 16).addBox(3.0F, -40.0F, -14.0F, 8.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 48.0F, 0.0F, -0.4363F, 0.0F, -0.3054F));

		PartDefinition rbody_r1 = body.addOrReplaceChild("rbody", CubeListBuilder.create().texOffs(24, 12).addBox(-7.0F, -37.0F, 4.0F, 7.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 48.0F, 0.0F, 0.2182F, 0.0F, 0.2182F));

		PartDefinition r_right_arm = partdefinition.addOrReplaceChild("r_right_arm", CubeListBuilder.create().texOffs(0, 35).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -5.0F, -2.0F));

		PartDefinition r_left_arm = partdefinition.addOrReplaceChild("r_left_arm", CubeListBuilder.create().texOffs(0, 35).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -3.0F, -2.0F));

		PartDefinition torso = partdefinition.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(24, 27).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition l_left_arm = partdefinition.addOrReplaceChild("l_left_arm", CubeListBuilder.create().texOffs(14, 40).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -10.0F, 6.0F));

		PartDefinition l_right_arm = partdefinition.addOrReplaceChild("l_right_arm", CubeListBuilder.create().texOffs(14, 40).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -8.0F, 5.0F));

		PartDefinition rhead = partdefinition.addOrReplaceChild("rhead", CubeListBuilder.create(), PartPose.offset(5.3929F, -9.0229F, -2.0F));

		PartDefinition headlayer_r1 = rhead.addOrReplaceChild("headlayer_r1", CubeListBuilder.create().texOffs(28, 0).addBox(-13.0F, -36.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(5.6071F, 36.0229F, 1.0F, 0.0F, 0.0F, 0.1309F));

		PartDefinition lhead = partdefinition.addOrReplaceChild("lhead", CubeListBuilder.create(), PartPose.offset(-6.3223F, -14.843F, 5.5F));

		PartDefinition mouth_r1 = lhead.addOrReplaceChild("mouth_r1", CubeListBuilder.create().texOffs(40, 44).addBox(-6.0F, -40.0F, 4.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(-0.5F))
		.texOffs(0, 0).addBox(-6.0F, -43.0F, 2.0F, 6.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(6.3223F, 41.843F, -6.5F, 0.0F, 0.0F, -0.0873F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// Am not moving the prepareMobModel down here since I want access to partialTicks
		if (entity.getAttackAnimationTick() <= 0) {
			this.l_right_arm.xRot = (float) (Math.atan(limbSwing * 0.6662F) * 0.1 * limbSwingAmount);
			this.l_left_arm.xRot = (float) (Math.atan(limbSwing * 0.6662F) * 0.1 * limbSwingAmount);
		}
		this.r_right_arm.xRot = (float)(Math.atan(limbSwing * 0.6662F) * 0.1 * limbSwingAmount);
		this.r_left_arm.xRot = (float)(Math.atan(limbSwing * 0.6662F) * 0.1 * limbSwingAmount);

		this.l_right_arm.zRot = 0.0F;
		this.l_left_arm.zRot = 0.0F;
		this.r_right_arm.zRot = 0.0F;
		this.r_left_arm.zRot = 0.0F;
		this.torso.xRot = (float)(Math.atan(limbSwing * 0.6662) * 0.15 * limbSwingAmount);
		this.torso.yRot = 0.0F;
		this.torso.zRot = 0.0F;

		ItemStack itemstack = entity.getMainHandItem();
		if (entity.isScreaming()) {

			this.l_left_arm.xRot = Mth.HALF_PI/2;
			this.l_right_arm.xRot = Mth.HALF_PI/2;

			if ((GeneralUtil.isHashTick(entity, entity.getLevel(), 8) && (entity.getRandom().nextFloat() < 0.10))) {
				this.lhead.setRotation(entity.getRandom().nextFloat() * Mth.HALF_PI - Mth.HALF_PI/2 + ALMOST_HALF_PI/4, this.lhead.yRot, entity.getRandom().nextFloat() * Mth.HALF_PI - Mth.HALF_PI/2);
			} else if (GeneralUtil.isHashTick(entity, entity.getLevel(), 4) && (entity.getRandom().nextFloat() < 0.8)) {
				this.lhead.setRotation(entity.getRandom().nextFloat() * ALMOST_HALF_PI - ALMOST_HALF_PI/2 + ALMOST_HALF_PI/4,
						this.lhead.yRot,
						entity.getRandom().nextFloat() * ALMOST_HALF_PI - ALMOST_HALF_PI/2);
			} else {
				this.lhead.xRot = 0;
				this.lhead.zRot = 0;
			}
		} else {
			this.lhead.xRot = 0;
			this.lhead.zRot = 0;
			this.l_left_arm.xRot = 0;
			this.l_right_arm.xRot = 0;
		}

		if (entity.isAggressive() && (itemstack.isEmpty() || !(itemstack.getItem() instanceof CrossbowItem))) {
			float f = Mth.sin(this.attackTime * (float)Math.PI);
			float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float)Math.PI);
			this.r_right_arm.zRot = 0.0F;
			this.r_left_arm.zRot = 0.0F;
			this.r_right_arm.yRot = -(0.1F - f * 0.6F);
			this.r_left_arm.yRot = 0.1F - f * 0.6F;
			this.r_right_arm.xRot = (-(float)Math.PI / 2F);
			this.r_left_arm.xRot = (-(float)Math.PI / 2F);
			this.r_right_arm.xRot -= f * 1.2F - f1 * 0.4F;
			this.r_left_arm.xRot -= f * 1.2F - f1 * 0.4F;
//            AnimationUtils.bobArms(this.right_arm, this.left_arm, ageInTicks);
		}
	}

	public void prepareMobModel(T wraithAmalgam, float p_102958_, float p_102959_, float partialTicks) {
		int attackAnimationTick = wraithAmalgam.getAttackAnimationTick();
		if (attackAnimationTick > 0) {
			this.l_right_arm.xRot = -2.0F + 1.5F * Mth.triangleWave((float)attackAnimationTick - partialTicks, 10.0F);
			this.l_left_arm.xRot = -2.0F + 1.5F * Mth.triangleWave((float)attackAnimationTick - partialTicks, 10.0F);

		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		r_right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		r_left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		l_left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		l_right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rhead.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		lhead.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.lhead, this.rhead);
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
		return arm == HumanoidArm.LEFT ? this.r_left_arm : this.r_right_arm;
	}

}