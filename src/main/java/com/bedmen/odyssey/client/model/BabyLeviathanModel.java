package com.bedmen.odyssey.client.model;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.BabyLeviathan;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BabyLeviathanModel extends EntityModel<BabyLeviathan> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Odyssey.MOD_ID, "baby_leviathan"), "main");
	private final ModelPart s_1;
	private final ModelPart s_2;
	private final ModelPart s_3;
	private final ModelPart s_4;
	private final ModelPart s_5;
	private final ModelPart s_6;
	private final ModelPart s_7;

	public BabyLeviathanModel(ModelPart root) {
		this.s_1 = root.getChild("s_1");
		this.s_2 = root.getChild("s_2");
		this.s_3 = root.getChild("s_3");
		this.s_4 = root.getChild("s_4");
		this.s_5 = root.getChild("s_5");
		this.s_6 = root.getChild("s_6");
		this.s_7 = root.getChild("s_7");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition s_1 = partdefinition.addOrReplaceChild("s_1", CubeListBuilder.create().texOffs(4, 15).addBox(-2.0F, -1.0F, -3.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(23, 15).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(21, 3).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(4, 4).addBox(1.0F, -1.0F, -3.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(10, 25).addBox(-1.0F, 1.0F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, -5.0F));

		PartDefinition s_2 = partdefinition.addOrReplaceChild("s_2", CubeListBuilder.create().texOffs(16, 11).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, -3.0F));

		PartDefinition s_3 = partdefinition.addOrReplaceChild("s_3", CubeListBuilder.create().texOffs(2, 26).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, -1.0F));

		PartDefinition s_4 = partdefinition.addOrReplaceChild("s_4", CubeListBuilder.create().texOffs(5, 13).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, 1.0F));

		PartDefinition s_5 = partdefinition.addOrReplaceChild("s_5", CubeListBuilder.create().texOffs(14, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, 3.0F));

		PartDefinition s_6 = partdefinition.addOrReplaceChild("s_6", CubeListBuilder.create().texOffs(19, 21).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, 5.0F));

		PartDefinition s_7 = partdefinition.addOrReplaceChild("s_7", CubeListBuilder.create().texOffs(20, 24).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, 7.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
    public void setupAnim(BabyLeviathan pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        float f0 = (float) (Math.PI / 3.0d);
        this.s_1.x = Mth.sin(pLimbSwing);
        this.s_2.x = Mth.sin(pLimbSwing+f0);
        this.s_3.x = Mth.sin(pLimbSwing+f0*2);
        this.s_4.x = Mth.sin(pLimbSwing+f0*3);
        this.s_5.x = Mth.sin(pLimbSwing+f0*4);
        this.s_6.x = Mth.sin(pLimbSwing+f0*5);
        this.s_7.x = Mth.sin(pLimbSwing);
    }

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		s_1.render(poseStack, buffer, packedLight, packedOverlay);
		s_2.render(poseStack, buffer, packedLight, packedOverlay);
		s_3.render(poseStack, buffer, packedLight, packedOverlay);
		s_4.render(poseStack, buffer, packedLight, packedOverlay);
		s_5.render(poseStack, buffer, packedLight, packedOverlay);
		s_6.render(poseStack, buffer, packedLight, packedOverlay);
		s_7.render(poseStack, buffer, packedLight, packedOverlay);
	}
}