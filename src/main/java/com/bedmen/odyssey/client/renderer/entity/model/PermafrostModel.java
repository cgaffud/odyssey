package com.bedmen.odyssey.client.renderer.entity.model;

import com.bedmen.odyssey.entity.boss.PermafrostEntity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Arrays;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PermafrostModel<T extends Entity> extends SegmentedModel<T> {
    private final ImmutableList<ModelRenderer> parts;
    private final ModelRenderer[][] spikes = new ModelRenderer[3][6];
    private double theta = 0.0d;
    private double[] phi = new double[3];

    public PermafrostModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        
        for(int i = 0; i < phi.length; i++){
            this.phi[i] = Math.PI * 0.5d;
        }

        for(int i = 0; i < this.spikes.length; ++i) {
            for (int j = 0; j < this.spikes[0].length; ++j) {
                this.spikes[i][j] = new ModelRenderer(this, 0, 0);
                this.spikes[i][j].addBox(-6.0f+2.0f*i, -14.0f+2.0f*i, -8.0f, 12.0f-4.0f*i, 12.0f-4.0f*i, 16.0f);
            }
        }

        Builder<ModelRenderer> builder = ImmutableList.builder();
        for(int i = 0; i < this.spikes.length; ++i) {
            builder.addAll(Arrays.asList(this.spikes[i]));
        }
        this.parts = builder.build();
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        PermafrostEntity permafrostEntity = (PermafrostEntity)entity;
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

    public void prepareMobModel(T entity, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
        /*
        PermafrostEntity permafrostEntity = (PermafrostEntity)entity;
        int i = permafrostEntity.getAttackTimer(1);
        if (i >= 1 && permafrostEntity.getTarget() != null) {
            if (i >= 24) {
                this.phi[1] = Math.PI * 0.1d * (i - 23);
            } else {
                this.phi[1] = (Math.PI / 45.0d) * (23 - i);
            }
        }*/
    }

    public Iterable<ModelRenderer> parts() {
        return this.parts;
    }
}