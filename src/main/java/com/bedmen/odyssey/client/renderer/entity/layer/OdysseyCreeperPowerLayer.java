package com.bedmen.odyssey.client.renderer.entity.layer;

import com.bedmen.odyssey.client.model.OdysseyCreeperModel;
import com.bedmen.odyssey.entity.monster.OdysseyCreeper;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class OdysseyCreeperPowerLayer<T extends OdysseyCreeper, M extends OdysseyCreeperModel<T>> extends EnergySwirlLayer<T, M> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final M model;

    public OdysseyCreeperPowerLayer(RenderLayerParent<T, M> renderLayerParent, EntityModelSet entityModelSet, Supplier<M> supplier) {
        super(renderLayerParent);
        this.model = supplier.get();
    }

    protected float xOffset(float p_116683_) {
        return p_116683_ * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected M model() {
        return this.model;
    }
}