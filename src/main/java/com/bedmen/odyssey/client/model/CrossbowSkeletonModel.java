package com.bedmen.odyssey.client.model;

import com.bedmen.odyssey.entity.monster.CrossbowSkeleton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.checkerframework.checker.units.qual.C;

@OnlyIn(Dist.CLIENT)
public class CrossbowSkeletonModel<T extends CrossbowSkeleton> extends HumanoidModel<T> {
    public CrossbowSkeletonModel(ModelPart modelPart) {
        super(modelPart);
    }

    public void prepareMobModel(T mob, float p_103794_, float p_103795_, float p_103796_) {
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        if (mob.getMainArm() == HumanoidArm.RIGHT) {
            this.rightArmPose = mob.getArmPose();
        } else {
            this.leftArmPose = mob.getArmPose();
        }
        super.prepareMobModel(mob, p_103794_, p_103795_, p_103796_);
    }

    public void setupAnim(T mob, float p_103799_, float p_103800_, float p_103801_, float p_103802_, float p_103803_) {
        super.setupAnim(mob, p_103799_, p_103800_, p_103801_, p_103802_, p_103803_);
        ItemStack itemstack = mob.getMainHandItem();
        if (mob.isAggressive() && (itemstack.isEmpty() || !(itemstack.getItem() instanceof CrossbowItem))) {
            float f = Mth.sin(this.attackTime * (float)Math.PI);
            float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float)Math.PI);
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightArm.yRot = -(0.1F - f * 0.6F);
            this.leftArm.yRot = 0.1F - f * 0.6F;
            this.rightArm.xRot = (-(float)Math.PI / 2F);
            this.leftArm.xRot = (-(float)Math.PI / 2F);
            this.rightArm.xRot -= f * 1.2F - f1 * 0.4F;
            this.leftArm.xRot -= f * 1.2F - f1 * 0.4F;
            AnimationUtils.bobArms(this.rightArm, this.leftArm, p_103801_);
        }

    }

    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        float f = humanoidArm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        ModelPart modelpart = this.getArm(humanoidArm);
        modelpart.x += f;
        modelpart.translateAndRotate(poseStack);
        modelpart.x -= f;
    }
}