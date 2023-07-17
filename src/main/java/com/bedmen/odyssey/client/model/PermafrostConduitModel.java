package com.bedmen.odyssey.client.model;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.permafrost.PermafrostConduit;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class PermafrostConduitModel<T extends Entity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Odyssey.MOD_ID, "permafrost_conduit_model"), "main");

    private final ModelPart root;
    private static final int CUBES_PER_SPIKE = 3;
    private static final int TOTAL_SPIKES = 6;
    private final ModelPart[][] spikes = new ModelPart[CUBES_PER_SPIKE][TOTAL_SPIKES];

    private double theta = 0.0d;
    private double[] phi = new double[3];
    private static final String SPIKE_IDENTIFIER = "SpikeIdentifier";


    public PermafrostConduitModel(ModelPart root) {
        this.root = root;
        for(int i = 0; i < this.spikes.length; ++i) {
            for (int j = 0; j < this.spikes[0].length; ++j) {
                this.spikes[i][j] = root.getChild(SPIKE_IDENTIFIER + "." + i + "." + j);
            }
        }

        for(int i = 0; i < phi.length; i++){
            this.phi[i] = Math.PI * 0.5d;
        }

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        for(int i = 0; i < CUBES_PER_SPIKE; ++i) {
            for (int j = 0; j < TOTAL_SPIKES; ++j) {
                partdefinition.addOrReplaceChild(SPIKE_IDENTIFIER + "." + i + "." + j, CubeListBuilder.create().texOffs(0, 0).addBox(-6.0f+2.0f*i, -14.0f+2.0f*i, -8.0f, 12.0f-4.0f*i, 12.0f-4.0f*i, 16.0f), PartPose.offset(0.0F, 0.0F, 0.0F));
            }
        }
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        PermafrostConduit permafrostEntity = (PermafrostConduit)entity;
        this.theta = Math.PI / 20.0D * (double) (ageInTicks % 20.0f);
        for(int j = 0; j < this.spikes[0].length; ++j) {
            double thetaA = this.theta + (double) j * Math.PI * 2.0D / (double) this.spikes[0].length;
            float thetaB = (float) (thetaA * (-1.0D) - Math.PI / 2.0D);
            float[] phiB = new float[3];
            
            phiB[0] = (float) (this.phi[0] * (-1.0D) - Math.PI / 2.0D);
            float r1 = 48.0f;
            float f1x = (float) (Math.cos(thetaA) * Math.sin(this.phi[0]) * r1);
            float f1y = (float) (Math.cos(this.phi[0]) * r1);
            float f1z = (float) (Math.sin(thetaA) * Math.sin(this.phi[0]) * r1);
            this.spikes[0][j].x = f1x;
            this.spikes[0][j].y = f1y;
            this.spikes[0][j].z = f1z;
            this.spikes[0][j].yRot = thetaB;
            this.spikes[0][j].xRot = phiB[0];

            phiB[1] = (float) (this.phi[1] * (-1.0D) - Math.PI / 2.0D);
            float r2 = 64.0f;
            float f2x = (float) (Math.cos(thetaA) * Math.sin(this.phi[1]) * r2);
            float f2y = (float) (Math.cos(this.phi[1]) * r2);
            float f2z = (float) (Math.sin(thetaA) * Math.sin(this.phi[1]) * r2);
            this.spikes[1][j].x = f2x;
            this.spikes[1][j].y = f2y;
            this.spikes[1][j].z = f2z;
            this.spikes[1][j].yRot = thetaB;
            this.spikes[1][j].xRot = phiB[1];
            
            phiB[2] = (float) (this.phi[2] * (-1.0D) - Math.PI / 2.0D);
            float r3 = 80.0f;
            float f3x = (float) (Math.cos(thetaA) * Math.sin(this.phi[2]) * r3);
            float f3y = (float) (Math.cos(this.phi[2]) * r3);
            float f3z = (float) (Math.sin(thetaA) * Math.sin(this.phi[2]) * r3);
            this.spikes[2][j].x = f3x;
            this.spikes[2][j].y = f3y;
            this.spikes[2][j].z = f3z;
            this.spikes[2][j].yRot = thetaB;
            this.spikes[2][j].xRot = phiB[2];
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}