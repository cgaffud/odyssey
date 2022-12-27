package com.bedmen.odyssey.client.model;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.coven.CovenRootEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class CovenRootModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Odyssey.MOD_ID, "coven_root"), "main");
	private final ModelPart branch1;
	private final ModelPart branch2;
	private final ModelPart root;

	public CovenRootModel(ModelPart root) {
		this.root = root;
		this.branch1 = root.getChild("branch1");
		this.branch2 = root.getChild("branch2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition branch1 = partdefinition.addOrReplaceChild("branch1", CubeListBuilder.create().texOffs(26, 29).addBox(5.0F, -12.0F, 4.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 23).addBox(4.0F, -12.0F, -1.0F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 9).addBox(1.0F, -8.0F, -5.0F, 3.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(14, 15).addBox(-3.0F, -4.0F, -8.0F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition branch2 = partdefinition.addOrReplaceChild("branch2", CubeListBuilder.create().texOffs(0, 29).addBox(-7.0F, 2.0F, 4.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(18, 3).addBox(-6.0F, -3.0F, 0.0F, 3.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-4.0F, -6.0F, -3.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -7.0F, -8.0F, 5.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = limbSwing * 2.0F;

		f = 1.0F - f * f * f;
		this.branch1.x = 0.75F;
		this.branch1.y = 3F;
		this.branch2.x = -0.75F;
		this.branch2.y = -3F;
		this.root.zRot = f * 0.35F * (float)Math.PI;
		float f1 = (limbSwing + Mth.sin(limbSwing * 2.7F)) * 0.6F * 2.5F;

		this.root.y = 5.0F - f1;
		System.out.println(branch1.y);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}