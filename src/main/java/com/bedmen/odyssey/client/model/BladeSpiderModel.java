package com.bedmen.odyssey.client.model;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.BladeSpider;
import com.ibm.icu.text.MessagePattern;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.checkerframework.checker.units.qual.C;



public class BladeSpiderModel<T extends BladeSpider> extends HierarchicalModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Odyssey.MOD_ID, "blade_spider"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightMiddleHindLeg;
    private final ModelPart leftMiddleHindLeg;
    private final ModelPart rightMiddleFrontLeg;
    private final ModelPart leftMiddleFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart leftMandible;
    private final ModelPart rightMandible;

    public BladeSpiderModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.rightHindLeg = root.getChild("leg1");
        this.leftHindLeg = root.getChild("leg2");
        this.rightMiddleHindLeg = root.getChild("leg3");
        this.leftMiddleHindLeg = root.getChild("leg4");
        this.rightMiddleFrontLeg = root.getChild("leg5");
        this.leftMiddleFrontLeg = root.getChild("leg6");
        this.rightFrontLeg = root.getChild("leg7");
        this.leftFrontLeg = root.getChild("leg8");
        this.leftMandible = head.getChild("lmandible");
        this.rightMandible = head.getChild("rmandible");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 19).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, -3.0F));
        PartDefinition lmandible = head.addOrReplaceChild("lmandible", CubeListBuilder.create().texOffs(38, 41).addBox(-5.0F, 2.0F, -15.0F, 3.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition rmandible = head.addOrReplaceChild("rmandible", CubeListBuilder.create().texOffs(24, 39).addBox(2.0F, 2.0F, -15.0F, 3.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

//                .texOffs(38, 41).addBox(-5.0F, 2.0F, -15.0F, 3.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
//                .texOffs(24, 39).addBox(2.0F, 2.0F, -15.0F, 3.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, -3.0F));
        PartDefinition neck = partdefinition.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 39).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -4.0F, -6.0F, 10.0F, 8.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 9.0F));

        CubeListBuilder cubelistbuilder =  CubeListBuilder.create().texOffs(0, 59).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F));
        CubeListBuilder cubelistbuilder1 = CubeListBuilder.create().texOffs(0, 59).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F));
        PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", cubelistbuilder, PartPose.offset(-4.0F, 15.0F, 4.0F));
        PartDefinition leg2 = partdefinition.addOrReplaceChild("leg2", cubelistbuilder1, PartPose.offset(4.0F, 15.0F, 4.0F));
        PartDefinition leg3 = partdefinition.addOrReplaceChild("leg3", cubelistbuilder, PartPose.offset(-4.0F, 15.0F, 1.0F));
        PartDefinition leg4 = partdefinition.addOrReplaceChild("leg4", cubelistbuilder1, PartPose.offset(4.0F, 15.0F, 1.0F));
        PartDefinition leg5 = partdefinition.addOrReplaceChild("leg5", cubelistbuilder, PartPose.offset(-4.0F, 15.0F, -2.0F));
        PartDefinition leg6 = partdefinition.addOrReplaceChild("leg6", cubelistbuilder1, PartPose.offset(4.0F, 15.0F, -2.0F));
        PartDefinition leg7 = partdefinition.addOrReplaceChild("leg7", cubelistbuilder, PartPose.offset(-4.0F, 15.0F, -5.0F));
        PartDefinition leg8 = partdefinition.addOrReplaceChild("leg8", cubelistbuilder1, PartPose.offset(4.0F, 15.0F, -5.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public ModelPart root() {
        return this.root;
    }

    @Override
    //T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch
    public void setupAnim(T entity, float p_103867_, float p_103868_, float p_103869_, float p_103870_, float p_103871_) {
        // Spider anim code
        this.head.yRot = p_103870_ * ((float)Math.PI / 180F);
        this.head.xRot = p_103871_ * ((float)Math.PI / 180F);
        float f = ((float)Math.PI / 4F);
        this.rightHindLeg.zRot = (-(float)Math.PI / 4F);
        this.leftHindLeg.zRot = ((float)Math.PI / 4F);
        this.rightMiddleHindLeg.zRot = -0.58119464F;
        this.leftMiddleHindLeg.zRot = 0.58119464F;
        this.rightMiddleFrontLeg.zRot = -0.58119464F;
        this.leftMiddleFrontLeg.zRot = 0.58119464F;
        this.rightFrontLeg.zRot = (-(float)Math.PI / 4F);
        this.leftFrontLeg.zRot = ((float)Math.PI / 4F);
        float f1 = -0.0F;
        float f2 = ((float)Math.PI / 8F);
        this.rightHindLeg.yRot = ((float)Math.PI / 4F);
        this.leftHindLeg.yRot = (-(float)Math.PI / 4F);
        this.rightMiddleHindLeg.yRot = ((float)Math.PI / 8F);
        this.leftMiddleHindLeg.yRot = (-(float)Math.PI / 8F);
        this.rightMiddleFrontLeg.yRot = (-(float)Math.PI / 8F);
        this.leftMiddleFrontLeg.yRot = ((float)Math.PI / 8F);
        this.rightFrontLeg.yRot = (-(float)Math.PI / 4F);
        this.leftFrontLeg.yRot = ((float)Math.PI / 4F);
        float f3 = -(Mth.cos(p_103867_ * 0.6662F * 2.0F + 0.0F) * 0.4F) * p_103868_;
        float f4 = -(Mth.cos(p_103867_ * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * p_103868_;
        float f5 = -(Mth.cos(p_103867_ * 0.6662F * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * p_103868_;
        float f6 = -(Mth.cos(p_103867_ * 0.6662F * 2.0F + ((float)Math.PI * 1.5F)) * 0.4F) * p_103868_;
        float f7 = Math.abs(Mth.sin(p_103867_ * 0.6662F + 0.0F) * 0.4F) * p_103868_;
        float f8 = Math.abs(Mth.sin(p_103867_ * 0.6662F + (float)Math.PI) * 0.4F) * p_103868_;
        float f9 = Math.abs(Mth.sin(p_103867_ * 0.6662F + ((float)Math.PI / 2F)) * 0.4F) * p_103868_;
        float f10 = Math.abs(Mth.sin(p_103867_ * 0.6662F + ((float)Math.PI * 1.5F)) * 0.4F) * p_103868_;
        this.rightHindLeg.yRot += f3;
        this.leftHindLeg.yRot += -f3;
        this.rightMiddleHindLeg.yRot += f4;
        this.leftMiddleHindLeg.yRot += -f4;
        this.rightMiddleFrontLeg.yRot += f5;
        this.leftMiddleFrontLeg.yRot += -f5;
        this.rightFrontLeg.yRot += f6;
        this.leftFrontLeg.yRot += -f6;
        this.rightHindLeg.zRot += f7;
        this.leftHindLeg.zRot += -f7;
        this.rightMiddleHindLeg.zRot += f8;
        this.leftMiddleHindLeg.zRot += -f8;
        this.rightMiddleFrontLeg.zRot += f9;
        this.leftMiddleFrontLeg.zRot += -f9;
        this.rightFrontLeg.zRot += f10;
        this.leftFrontLeg.zRot += -f10;

        // attack time is in double incr between 0 and 1 and not ticks
        if (this.attackTime < 0.25f) {
            this.leftMandible.yRot = (float) Math.PI / 2F * this.attackTime;
            this.rightMandible.yRot = -(float) Math.PI / 2F * this.attackTime;
        } else if (this.attackTime < 0.75f) {
            float adjt = this.attackTime - 0.25f * 2;
            this.leftMandible.yRot = Mth.lerp((float)Math.PI/ 8F, -(float)Math.PI/16F, adjt);
            this.rightMandible.yRot = Mth.lerp(-(float)Math.PI/ 8F, (float)Math.PI/16F, adjt);
        } else {
            float adjt = (this.attackTime-0.75f) * 2;
            this.leftMandible.yRot = Mth.lerp(-(float)Math.PI/16F, 0, adjt);
            this.rightMandible.yRot = Mth.lerp((float)Math.PI/16F, 0,adjt);
        }
    }
}
