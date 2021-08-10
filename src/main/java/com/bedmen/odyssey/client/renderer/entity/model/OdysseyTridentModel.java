package com.bedmen.odyssey.client.renderer.entity.model;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.util.ItemRegistry;
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
public class OdysseyTridentModel extends Model {
    public static final ResourceLocation TRIDENT_TEXTURE = new ResourceLocation("textures/entity/trident.png");
    public static final ResourceLocation SERPENT_TRIDENT_TEXTURE = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/serpent_trident.png");
    private final ModelRenderer modelRenderer = new ModelRenderer(32, 32, 0, 6);

    public OdysseyTridentModel() {
        super(RenderType::entitySolid);
        this.modelRenderer.addBox(-0.5F, 2.0F, -0.5F, 1.0F, 25.0F, 1.0F, 0.0F);
        ModelRenderer modelrenderer = new ModelRenderer(32, 32, 4, 0);
        modelrenderer.addBox(-1.5F, 0.0F, -0.5F, 3.0F, 2.0F, 1.0F);
        this.modelRenderer.addChild(modelrenderer);
        ModelRenderer modelrenderer1 = new ModelRenderer(32, 32, 4, 3);
        modelrenderer1.addBox(-2.5F, -3.0F, -0.5F, 1.0F, 4.0F, 1.0F);
        this.modelRenderer.addChild(modelrenderer1);
        ModelRenderer modelrenderer2 = new ModelRenderer(32, 32, 0, 0);
        modelrenderer2.addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F);
        this.modelRenderer.addChild(modelrenderer2);
        ModelRenderer modelrenderer3 = new ModelRenderer(32, 32, 4, 3);
        modelrenderer3.mirror = true;
        modelrenderer3.addBox(1.5F, -3.0F, -0.5F, 1.0F, 4.0F, 1.0F);
        this.modelRenderer.addChild(modelrenderer3);
    }

    public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public static ResourceLocation getTridentTexture(Item item){
        if(item == ItemRegistry.SERPENT_TRIDENT.get()) return SERPENT_TRIDENT_TEXTURE;
        else return TRIDENT_TEXTURE;
    }
}
