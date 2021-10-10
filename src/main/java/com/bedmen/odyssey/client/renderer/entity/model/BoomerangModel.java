package com.bedmen.odyssey.client.renderer.entity.model;

import com.bedmen.odyssey.client.renderer.entity.renderer.BoomerangRenderer;
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
    private final ModelRenderer modelRenderer;

    public BoomerangModel() {

        super(RenderType::entitySolid);

        this.texWidth = 32;
        this.texHeight = 32;

        modelRenderer = new ModelRenderer(this);
        modelRenderer.setPos(0.0F, 0.0F, 0.0F);
        modelRenderer.setTexSize(0, 0).addBox(4.0F, -1.0F, -6.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        modelRenderer.setTexSize(0, 0).addBox(4.0F, -1.0F, -4.0F, 2.0F, 1.0F, 10.0F, 0.0F, false);
        modelRenderer.setTexSize(0, 11).addBox(-6.0F, -1.0F, -6.0F, 10.0F, 1.0F, 2.0F, 0.0F, false);
    }

    public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public static ResourceLocation getBoomerangTexture(Item item){
        return BoomerangRenderer.BOOMERANG_LOCATION[BoomerangItem.BoomerangType.getBoomerangType(item).ordinal()];
    }
}
