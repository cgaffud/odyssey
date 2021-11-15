package com.bedmen.odyssey.client.renderer.entity.layers;

import com.bedmen.odyssey.client.renderer.entity.model.OdysseyCreeperModel;
import com.bedmen.odyssey.entity.monster.OdysseyCreeperEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OdysseyCreeperChargeLayer<T extends OdysseyCreeperEntity> extends EnergyLayer<T, OdysseyCreeperModel<T>> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final OdysseyCreeperModel<T> model = new OdysseyCreeperModel<>(2.0F);

    public OdysseyCreeperChargeLayer(IEntityRenderer<T, OdysseyCreeperModel<T>> p_i50947_1_) {
        super(p_i50947_1_);
    }

    protected float xOffset(float p_225634_1_) {
        return p_225634_1_ * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected EntityModel<T> model() {
        return this.model;
    }
}