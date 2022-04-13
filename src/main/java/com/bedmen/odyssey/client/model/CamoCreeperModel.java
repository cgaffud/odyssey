package com.bedmen.odyssey.client.model;

import com.bedmen.odyssey.client.renderer.entity.CamoCreeperRenderer;
import com.bedmen.odyssey.entity.monster.CamoCreeper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;

public class CamoCreeperModel<T extends CamoCreeper> extends OdysseyCreeperModel<T> {
    private float red;
    private float green;
    private float blue;

    public CamoCreeperModel(ModelPart modelPart) {
        super(modelPart);
    }

    public void setupAnim(T camoCreeper, float p_102464_, float p_102465_, float p_102466_, float p_102467_, float p_102468_) {
        super.setupAnim(camoCreeper, p_102464_, p_102465_, p_102466_, p_102467_, p_102468_);

        //Coloring
        ResourceLocation resourceLocation = CamoCreeperRenderer.getTexture(camoCreeper);
        if(resourceLocation == CamoCreeperRenderer.GRAY_CREEPER_LOCATION){
            int color = camoCreeper.level.getBiome(camoCreeper.eyeBlockPosition()).getFoliageColor();
            this.red = (float)(color >> 16 & 255) / 255.0F;
            this.green = (float)(color >> 8 & 255) / 255.0F;
            this.blue = (float)(color & 255) / 255.0F;
        } else {
            this.red = 1.0f;
            this.green = 1.0f;
            this.blue = 1.0f;
        }
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int p_102036_, int p_102037_, float r, float g, float b, float a) {
        super.renderToBuffer(poseStack, vertexConsumer, p_102036_, p_102037_, this.red * r, this.green * g, this.blue * b, a);
    }
}