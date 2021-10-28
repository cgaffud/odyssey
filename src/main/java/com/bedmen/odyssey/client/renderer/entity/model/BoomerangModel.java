package com.bedmen.odyssey.client.renderer.entity.model;

import com.bedmen.odyssey.items.equipment.BoomerangItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BoomerangModel extends Model {
    private final ModelRenderer bone;
    private final ModelRenderer arm2_r1;

    public BoomerangModel() {

        super(RenderType::entitySolid);

        this.texWidth = 32;
        this.texHeight = 32;

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 0.0F, 0.0F);
        bone.texOffs(0, 0).addBox(4.0F, -0.5F, -6.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        bone.texOffs(0, 0).addBox(4.0F, -0.5F, -4.0F, 2.0F, 1.0F, 10.0F, 0.0F, false);

        arm2_r1 = new ModelRenderer(this);
        arm2_r1.setPos(0.0F, 0.0F, 0.0F);
        bone.addChild(arm2_r1);
        setRotationAngle(arm2_r1, 0.0F, -1.5708F, 0.0F);
        arm2_r1.texOffs(0, 0).addBox(-6.0F, -0.5F, -4.0F, 2.0F, 1.0F, 10.0F, 0.0F, true);
    }

    public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.bone.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public static ResourceLocation getBoomerangTexture(Item item){
        return ((BoomerangItem)item).getBoomerangType().getResourceLocation();
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
