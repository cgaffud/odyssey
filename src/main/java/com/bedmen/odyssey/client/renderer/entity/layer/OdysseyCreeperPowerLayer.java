package com.bedmen.odyssey.client.renderer.entity.layer;

import com.bedmen.odyssey.client.model.OdysseyCreeperModel;
import com.bedmen.odyssey.entity.monster.OdysseyCreeper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OdysseyCreeperPowerLayer extends EnergySwirlLayer<OdysseyCreeper, OdysseyCreeperModel<OdysseyCreeper>> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final OdysseyCreeperModel<OdysseyCreeper> model;

    public OdysseyCreeperPowerLayer(RenderLayerParent<OdysseyCreeper, OdysseyCreeperModel<OdysseyCreeper>> p_174471_, EntityModelSet p_174472_) {
        super(p_174471_);
        this.model = new OdysseyCreeperModel<>(p_174472_.bakeLayer(ModelLayers.CREEPER_ARMOR));
    }

    protected float xOffset(float p_116683_) {
        return p_116683_ * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected EntityModel<OdysseyCreeper> model() {
        return this.model;
    }
}