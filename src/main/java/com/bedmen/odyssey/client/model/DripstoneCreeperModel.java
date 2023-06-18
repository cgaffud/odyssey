package com.bedmen.odyssey.client.model;// Made with Blockbench 4.7.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.DripstoneCreeper;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class DripstoneCreeperModel<T extends DripstoneCreeper> extends OdysseyCreeperModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Odyssey.MOD_ID, "dripstone_creeper"), "main");

	public DripstoneCreeperModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition drip4 = head.addOrReplaceChild("drip4", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.9253F, -8.7371F, -3.8984F, 0.765F, 1.2514F, 2.0666F));

		PartDefinition drip2_2_r1 = drip4.addOrReplaceChild("drip2_2_r1", CubeListBuilder.create().texOffs(39, -9).addBox(2.0F, -4.0F, -7.5F, 0.0F, 11.0F, 9.0F, new CubeDeformation(-2.0F)), PartPose.offsetAndRotation(-3.1969F, 4.2103F, -0.3058F, -1.1905F, -0.4164F, -0.1186F));

		PartDefinition drip1_3_r1 = drip4.addOrReplaceChild("drip1_3_r1", CubeListBuilder.create().texOffs(39, -9).addBox(-2.0F, -4.5F, -6.5F, 0.0F, 11.0F, 9.0F, new CubeDeformation(-2.0F)), PartPose.offsetAndRotation(-1.1391F, 1.3117F, -1.0891F, -2.0153F, -0.3463F, 1.6125F));

		PartDefinition drip5 = head.addOrReplaceChild("drip5", CubeListBuilder.create(), PartPose.offsetAndRotation(0.3854F, -3.2377F, 4.8373F, -2.8599F, 0.2375F, -3.0382F));

		PartDefinition drip3_3_r1 = drip5.addOrReplaceChild("drip3_3_r1", CubeListBuilder.create().texOffs(39, -9).addBox(2.0F, -4.0F, -7.5F, 0.0F, 11.0F, 9.0F, new CubeDeformation(-2.0F)), PartPose.offsetAndRotation(-3.0815F, 4.8963F, 0.3734F, -1.1905F, -0.4164F, -0.1186F));

		PartDefinition drip1_4_r1 = drip5.addOrReplaceChild("drip1_4_r1", CubeListBuilder.create().texOffs(39, -9).addBox(-2.0F, -4.5F, -6.5F, 0.0F, 11.0F, 9.0F, new CubeDeformation(-2.0F)), PartPose.offsetAndRotation(-1.0237F, 1.9977F, -0.4099F, -2.0153F, -0.3463F, 1.6125F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition drip1 = body.addOrReplaceChild("drip1", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.7065F, 7.7629F, -1.0478F, 2.2893F, 0.0418F, 2.7031F));

		PartDefinition drip1_1_r1 = drip1.addOrReplaceChild("drip1_1_r1", CubeListBuilder.create().texOffs(39, -9).addBox(2.0F, -5.0F, -7.5F, 0.0F, 11.0F, 9.0F, new CubeDeformation(-2.0F)), PartPose.offsetAndRotation(-3.0475F, 3.9341F, 0.6436F, -1.1905F, -0.4164F, -0.1186F));

		PartDefinition drip1_2_r1 = drip1.addOrReplaceChild("drip1_2_r1", CubeListBuilder.create().texOffs(39, -9).addBox(-2.0F, -5.5F, -6.5F, 0.0F, 11.0F, 9.0F, new CubeDeformation(-2.0F)), PartPose.offsetAndRotation(-0.9897F, 1.0355F, -0.1397F, -2.0153F, -0.3463F, 1.6125F));

		PartDefinition drip2 = body.addOrReplaceChild("drip2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0475F, 2.5659F, -3.1436F, -0.9728F, -0.1717F, 0.1193F));

		PartDefinition drip1_1_r2 = drip2.addOrReplaceChild("drip1_1_r2", CubeListBuilder.create().texOffs(39, -9).addBox(2.0F, -4.0F, -7.5F, 0.0F, 11.0F, 9.0F, new CubeDeformation(-2.0F)), PartPose.offsetAndRotation(-3.0475F, 3.9341F, 0.6436F, -1.1905F, -0.4164F, -0.1186F));

		PartDefinition drip1_2_r2 = drip2.addOrReplaceChild("drip1_2_r2", CubeListBuilder.create().texOffs(39, -9).addBox(-2.0F, -4.5F, -6.5F, 0.0F, 11.0F, 9.0F, new CubeDeformation(-2.0F)), PartPose.offsetAndRotation(-0.9897F, 1.0355F, -0.1397F, -2.0153F, -0.3463F, 1.6125F));

		PartDefinition drip3 = body.addOrReplaceChild("drip3", CubeListBuilder.create(), PartPose.offsetAndRotation(3.7712F, 7.5526F, 2.1253F, -2.6505F, -0.9608F, 2.7638F));

		PartDefinition drip2_2_r2 = drip3.addOrReplaceChild("drip2_2_r2", CubeListBuilder.create().texOffs(39, -9).addBox(2.0F, -4.0F, -7.5F, 0.0F, 11.0F, 9.0F, new CubeDeformation(-2.0F)), PartPose.offsetAndRotation(-3.0815F, 4.8963F, 0.3734F, -1.1905F, -0.4164F, -0.1186F));

		PartDefinition drip1_3_r2 = drip3.addOrReplaceChild("drip1_3_r2", CubeListBuilder.create().texOffs(39, -9).addBox(-2.0F, -4.5F, -6.5F, 0.0F, 11.0F, 9.0F, new CubeDeformation(-2.0F)), PartPose.offsetAndRotation(-1.0237F, 1.9977F, -0.4099F, -2.0153F, -0.3463F, 1.6125F));

		PartDefinition leg1 = partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 18.0F, 4.0F));

		PartDefinition leg2 = partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 18.0F, 4.0F));

		PartDefinition leg3 = partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 18.0F, -4.0F));

		PartDefinition leg4 = partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 18.0F, -4.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}
}