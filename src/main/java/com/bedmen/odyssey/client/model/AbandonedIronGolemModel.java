package com.bedmen.odyssey.client.model;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.AbandonedIronGolem;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AbandonedIronGolemModel<T extends AbandonedIronGolem> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Odyssey.MOD_ID, "abandoned_iron_golem"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private static final float[] targetX = new float[]{toRadians(0), toRadians(-90), toRadians(-90)};
    private static final float[] targetY = new float[]{toRadians(0), toRadians(30), toRadians(-20)};
    public static final int[] KEY_CLAP_TICKS = AbandonedIronGolem.KEY_CLAP_TICKS;

    public AbandonedIronGolemModel(ModelPart modelPart) {
        this.root = modelPart;
        this.head = modelPart.getChild("head");
        this.rightArm = modelPart.getChild("right_arm");
        this.leftArm = modelPart.getChild("left_arm");
        this.rightLeg = modelPart.getChild("right_leg");
        this.leftLeg = modelPart.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F).texOffs(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.0F, -7.0F, -2.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F).texOffs(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -7.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F), PartPose.offset(0.0F, -7.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(60, 58).addBox(9.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F), PartPose.offset(0.0F, -7.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F), PartPose.offset(-4.0F, 11.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(60, 0).mirror().addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F), PartPose.offset(5.0F, 11.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public ModelPart root() {
        return this.root;
    }

    public void setupAnim(T p_102962_, float p_102963_, float p_102964_, float p_102965_, float p_102966_, float p_102967_) {
        this.head.yRot = p_102966_ * ((float)Math.PI / 180F);
        this.head.xRot = p_102967_ * ((float)Math.PI / 180F);
        this.rightLeg.xRot = -1.5F * Mth.triangleWave(p_102963_, 13.0F) * p_102964_;
        this.leftLeg.xRot = 1.5F * Mth.triangleWave(p_102963_, 13.0F) * p_102964_;
        this.rightLeg.yRot = 0.0F;
        this.leftLeg.yRot = 0.0F;
    }

    public void prepareMobModel(T abandonedIronGolem, float p_102958_, float p_102959_, float partialTicks) {
        int attackAnimationTick = abandonedIronGolem.getAttackAnimationTick();
        int clapCooldown = abandonedIronGolem.getClapCooldown();
        if (attackAnimationTick > 0) {
            this.rightArm.xRot = -2.0F + 1.5F * Mth.triangleWave((float)attackAnimationTick - partialTicks, 10.0F);
            this.leftArm.xRot = -2.0F + 1.5F * Mth.triangleWave((float)attackAnimationTick - partialTicks, 10.0F);
        } else if (clapCooldown > 0){
            float f = (float)clapCooldown - partialTicks;
            if(clapCooldown > KEY_CLAP_TICKS[1]){
                f = 1f-Mth.inverseLerp(f, KEY_CLAP_TICKS[1], KEY_CLAP_TICKS[0]);
                this.rightArm.xRot = Mth.lerp(f, targetX[0], targetX[1]);
                this.leftArm.xRot = Mth.lerp(f, targetX[0], targetX[1]);
                this.rightArm.yRot = Mth.lerp(f, targetY[0], targetY[1]);
                this.leftArm.yRot = -Mth.lerp(f, targetY[0], targetY[1]);
            } else if(clapCooldown > KEY_CLAP_TICKS[2]){
                f = 1f-Mth.inverseLerp(f, KEY_CLAP_TICKS[2], KEY_CLAP_TICKS[1]);
                this.rightArm.xRot = Mth.lerp(f, targetX[1], targetX[2]);
                this.leftArm.xRot = Mth.lerp(f, targetX[1], targetX[2]);
                this.rightArm.yRot = Mth.lerp(f, targetY[1], targetY[2]);
                this.leftArm.yRot = -Mth.lerp(f, targetY[1], targetY[2]);
            } else if(clapCooldown > KEY_CLAP_TICKS[3]){
                this.rightArm.xRot = targetX[2];
                this.leftArm.xRot = targetX[2];
                this.rightArm.yRot = targetY[2];
                this.leftArm.yRot = -targetY[2];
            } else {
                f = 1f-Mth.inverseLerp(f, 0f, KEY_CLAP_TICKS[3]);
                this.rightArm.xRot = Mth.lerp(f, targetX[2], targetX[0]);
                this.leftArm.xRot = Mth.lerp(f, targetX[2], targetX[0]);
                this.rightArm.yRot = Mth.lerp(f, targetY[2], targetY[0]);
                this.leftArm.yRot = -Mth.lerp(f, targetY[2], targetY[0]);
            }
        } else {
            this.rightArm.xRot = (-0.2F + 1.5F * Mth.triangleWave(p_102958_, 13.0F)) * p_102959_;
            this.leftArm.xRot = (-0.2F - 1.5F * Mth.triangleWave(p_102958_, 13.0F)) * p_102959_;
        }
    }

    private static float toRadians(float degrees){
        return (float)(degrees * Math.PI / 180f);
    }
}