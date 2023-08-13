package com.bedmen.odyssey.client.renderer.entity.layer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.OdysseyCreeperModel;
import com.bedmen.odyssey.entity.monster.OdysseyCreeper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.DyeableHorseArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CreeperDripstoneLayer<T extends OdysseyCreeper> extends RenderLayer<T, OdysseyCreeperModel<T>> {

    private static final ResourceLocation DRIPSTONE_LAYER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/creeper/dripstone_layer.png");
    private final OdysseyCreeperModel<T> model;
    public CreeperDripstoneLayer(RenderLayerParent<T, OdysseyCreeperModel<T>> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.model = new OdysseyCreeperModel<>(entityModelSet.bakeLayer(OdysseyCreeperModel.LAYER_LOCATION));
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T creeper, float limbSwing, float limbSwingAmount, float f3, float ageInTicks, float netHeadYaw, float headPitch) {
        if (creeper.hasDripstoneSpikes()) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(creeper, limbSwing, limbSwingAmount, f3);
            this.model.setupAnim(creeper, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation()));
            this.model.renderToBuffer(poseStack, vertexconsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    protected ResourceLocation getTextureLocation(){
        return DRIPSTONE_LAYER_LOCATION;
    }
}