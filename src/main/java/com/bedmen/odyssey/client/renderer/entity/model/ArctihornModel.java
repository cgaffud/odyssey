package com.bedmen.odyssey.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Arrays;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ArctihornModel<T extends Entity> extends SegmentedModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer horn1;
    private final ModelRenderer horn2;
    private final ModelRenderer[] legs = new ModelRenderer[8];
    private final ImmutableList<ModelRenderer> parts;

    public ArctihornModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F);
        this.body.y += 6.0F;
        this.horn1 = new ModelRenderer(this, 0, 24);
        this.horn1.addBox(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F);
        this.horn2 = new ModelRenderer(this, 0, 36);
        this.horn2.addBox(-1.0F, -16.0F, -1.0F, 2.0F, 8.0F, 2.0F);

        for(int j = 0; j < this.legs.length; ++j) {
            this.legs[j] = new ModelRenderer(this, 48, 0);
            double d0 = (double)j * Math.PI * 2.0D / (double)this.legs.length;
            float f = (float)Math.cos(d0) * 5.0F;
            float f1 = (float)Math.sin(d0) * 5.0F;
            this.legs[j].addBox(-1.0F, -2.0F, -1.0F, 2.0F, 18.0F, 2.0F);
            this.legs[j].x = f;
            this.legs[j].z = f1;
            this.legs[j].y = 13.0F;
            d0 = (double)j * Math.PI * -2.0D / (double)this.legs.length + (Math.PI / 2D);
            this.legs[j].yRot = (float)d0;
        }

        Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.add(this.body);
        builder.add(this.horn1);
        builder.add(this.horn2);
        builder.addAll(Arrays.asList(this.legs));
        this.parts = builder.build();
    }

    /**
     * Sets this entity's model rotation angles
     */
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        for(ModelRenderer modelrenderer : this.legs) {
            modelrenderer.xRot = ageInTicks;
        }

    }

    public Iterable<ModelRenderer> parts() {
        return this.parts;
    }
}