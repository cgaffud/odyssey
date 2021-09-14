package com.bedmen.odyssey.client.renderer.entity.layers;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.MineralLeviathanSegmentModel;
import com.bedmen.odyssey.entity.boss.MineralLeviathanSegmentEntity;
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
public class ShellLayer extends LayerRenderer<MineralLeviathanSegmentEntity, MineralLeviathanSegmentModel> {
    private static final ResourceLocation[] RUBY_SHELL_LOCATION = new ResourceLocation[11];
    private static final ResourceLocation[] COPPER_SHELL_LOCATION = new ResourceLocation[11];
    private static final ResourceLocation[] SILVER_SHELL_LOCATION = new ResourceLocation[11];
    private final EntityModel<MineralLeviathanSegmentEntity> model = new MineralLeviathanSegmentModel();

    static{
        RUBY_SHELL_LOCATION[0] = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/ruby_shell.png");
        for(int i = 0; i < 10; i++){
            RUBY_SHELL_LOCATION[i+1] = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/ruby_shell_"+i+".png");
        }
        COPPER_SHELL_LOCATION[0] = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/copper_shell.png");
        for(int i = 0; i < 10; i++){
            COPPER_SHELL_LOCATION[i+1] = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/copper_shell_"+i+".png");
        }
        SILVER_SHELL_LOCATION[0] = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/silver_shell.png");
        for(int i = 0; i < 10; i++){
            SILVER_SHELL_LOCATION[i+1] = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/mineral_leviathan/silver_shell_"+i+".png");
        }
    }

    public ShellLayer(IEntityRenderer<MineralLeviathanSegmentEntity, MineralLeviathanSegmentModel> p_i50923_1_) {
        super(p_i50923_1_);
    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer p_225628_2_, int p_225628_3_, MineralLeviathanSegmentEntity entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (entity.getShellHealth() > 0.0f) {
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

    public ResourceLocation getTextureLocation(MineralLeviathanSegmentEntity entity) {
        switch(entity.getShellType()){
            default:
                return getTextureLocation(RUBY_SHELL_LOCATION, entity);
            case 1:
                return getTextureLocation(COPPER_SHELL_LOCATION, entity);
            case 2:
                return getTextureLocation(SILVER_SHELL_LOCATION, entity);
        }
    }

    public ResourceLocation getTextureLocation(ResourceLocation[] locations, MineralLeviathanSegmentEntity entity) {
        float maxHealth = entity.getShellHealthFromType(entity.getShellType());
        float currentHealth = entity.getShellHealth();
        int i = MathHelper.ceil(11.0f - (currentHealth / maxHealth * 10.0f)) - 1;
        i = MathHelper.clamp(i, 0, 10);
        return locations[i];
    }
}