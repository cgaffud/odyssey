package com.bedmen.odyssey.client.renderer.entity.layers;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.MineralLeviathanBodyModel;
import com.bedmen.odyssey.entity.boss.MineralLeviathanBodyEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MineralLeviathanBodyShellLayer extends LayerRenderer<MineralLeviathanBodyEntity, MineralLeviathanBodyModel> {
    public static final ResourceLocation[][] SHELL_LOCATION = new ResourceLocation[9][11];
    private final EntityModel<MineralLeviathanBodyEntity> model = new MineralLeviathanBodyModel();

    static{
        String[] names = new String[]{"ruby","coal","copper","iron","lapis","gold","silver","emerald","redstone"};
        for(int i = 0; i < SHELL_LOCATION.length; i++){
            SHELL_LOCATION[i][0] = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/"+names[i]+"_shell.png");
            for(int j = 1; j < SHELL_LOCATION[0].length; j++){
                SHELL_LOCATION[i][j] = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/"+names[i]+"_shell_"+(j-1)+".png");
            }
        }
    }

    public MineralLeviathanBodyShellLayer(IEntityRenderer<MineralLeviathanBodyEntity, MineralLeviathanBodyModel> p_i50923_1_) {
        super(p_i50923_1_);
    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer p_225628_2_, int p_225628_3_, MineralLeviathanBodyEntity entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (entity.hasShell()) {
            matrixStack.pushPose();
            matrixStack.scale(1.1f,1.1f,1.1f);
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(entity, p_225628_5_, p_225628_6_, p_225628_7_);
            this.model.setupAnim(entity, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
            IVertexBuilder ivertexbuilder = p_225628_2_.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
            this.model.renderToBuffer(matrixStack, ivertexbuilder, p_225628_3_, LivingRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }
    }

    public ResourceLocation getTextureLocation(MineralLeviathanBodyEntity entity) {
        return getTextureLocation(SHELL_LOCATION[entity.getShellType().ordinal()], entity);
    }

    public ResourceLocation getTextureLocation(ResourceLocation[] locations, MineralLeviathanBodyEntity entity) {
        float maxHealth = entity.getShellType().getShellMaxHealth();
        float currentHealth = entity.getShellHealth();
        int i = MathHelper.ceil(11.0f - (currentHealth / maxHealth * 10.0f)) - 1;
        i = MathHelper.clamp(i, 0, 10);
        return locations[i];
    }
}