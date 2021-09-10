package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.MineralLeviathanEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class MineralLeviathanRenderer extends MobRenderer<MineralLeviathanEntity, MineralLeviathanRenderer.MineralLeviathanModel> {
    private static final ResourceLocation MINERAL_LEVIATHAN_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/body.png");
    public MineralLeviathanRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new MineralLeviathanModel(), 0.7F);
        this.shadowRadius = 0.5F;
    }

    public void render(MineralLeviathanEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    public ResourceLocation getTextureLocation(MineralLeviathanEntity p_110775_1_) {
        return MINERAL_LEVIATHAN_LOCATION;
    }

    @OnlyIn(Dist.CLIENT)
    public static class MineralLeviathanModel extends SegmentedModel<MineralLeviathanEntity> {
        private final ImmutableList<ModelRenderer> parts;
        private final ModelRenderer head;
        private final ModelRenderer[] body = new ModelRenderer[9];

        public MineralLeviathanModel() {
            this.texWidth = 128;
            this.texHeight = 128;
            this.head = new ModelRenderer(this, 0, 0);
            this.head.addBox(-16.0f,-8.0f,-16.0f,32.0f,32.0f,32.0f);
            for(int i = 0; i < 9; i++){
                this.body[i] = new ModelRenderer(this, 0, 0);
                this.body[i].addBox(-16.0f,-8.0f,-16.0f,32.0f,32.0f,32.0f);
            }

            ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
            builder.add(this.head);
            builder.addAll(Arrays.asList(this.body));
            this.parts = builder.build();
        }

        public void setupAnim(MineralLeviathanEntity mineralLeviathanEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            double[][] bodyInfo = mineralLeviathanEntity.getBodyInfo();
            for(int i = 0; i < 9; i++){
                this.body[i].x = getRelativePosition(bodyInfo, i, 0);
                this.body[i].y = getRelativePosition(bodyInfo, i, 1);
                this.body[i].z = getRelativePosition(bodyInfo, i, 2);
            }
        }

        public float getRelativePosition(double[][] bodyInfo, int i, int d){
            if(i == 0){
                return 0.0f;
            }
            float f0 = (float)(bodyInfo[0][d] - bodyInfo[i][d]);
            f0 *= 16.0f;
            if(d == 0){
                f0 *= -1.0f;
            }
            return f0;
        }

        public Iterable<ModelRenderer> parts() {
            return this.parts;
        }
    }
}