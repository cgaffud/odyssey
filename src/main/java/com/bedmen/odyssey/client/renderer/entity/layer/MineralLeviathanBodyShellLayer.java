package com.bedmen.odyssey.client.renderer.entity.layer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.MineralLeviathanBodyModel;
import com.bedmen.odyssey.entity.boss.MineralLeviathanBody;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MineralLeviathanBodyShellLayer extends RenderLayer<MineralLeviathanBody, MineralLeviathanBodyModel> {
    public static final ResourceLocation[][] SHELL_LOCATION = new ResourceLocation[9][11];
    private final EntityModel<MineralLeviathanBody> model;

    static{
        String[] names = new String[]{"ruby","coal","copper","iron","lapis","gold","silver","emerald","redstone"};
        for(int i = 0; i < SHELL_LOCATION.length; i++){
            SHELL_LOCATION[i][0] = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/"+names[i]+"_shell.png");
            for(int j = 1; j < SHELL_LOCATION[0].length; j++){
                SHELL_LOCATION[i][j] = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/"+names[i]+"_shell_"+(j-1)+".png");
            }
        }
    }

    public MineralLeviathanBodyShellLayer(RenderLayerParent<MineralLeviathanBody, MineralLeviathanBodyModel> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.model = new MineralLeviathanBodyModel(entityModelSet.bakeLayer(MineralLeviathanBodyModel.LAYER_LOCATION));
    }

    public void render(PoseStack matrixStack, MultiBufferSource p_225628_2_, int p_225628_3_, MineralLeviathanBody entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (entity.hasShell()) {
            matrixStack.pushPose();
            matrixStack.scale(1.1f,1.1f,1.1f);
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(entity, p_225628_5_, p_225628_6_, p_225628_7_);
            this.model.setupAnim(entity, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
            VertexConsumer ivertexbuilder = p_225628_2_.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
            this.model.renderToBuffer(matrixStack, ivertexbuilder, p_225628_3_, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }
    }

    public ResourceLocation getTextureLocation(MineralLeviathanBody entity) {
        return getTextureLocation(SHELL_LOCATION[entity.getShellType().ordinal()], entity);
    }

    public ResourceLocation getTextureLocation(ResourceLocation[] locations, MineralLeviathanBody entity) {
        float maxHealth = entity.getShellType().getShellMaxHealth();
        float currentHealth = entity.getShellHealth();
        int i = Mth.ceil(11.0f - (currentHealth / maxHealth * 10.0f)) - 1;
        i = Mth.clamp(i, 0, 10);
        return locations[i];
    }
}