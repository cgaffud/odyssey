package com.bedmen.odyssey.client.renderer.entity.layers;

import com.bedmen.odyssey.client.renderer.entity.model.AbstractCreeperModel;
import com.bedmen.odyssey.entity.monster.AbstractCreeperEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AbstractCreeperChargeLayer extends EnergyLayer<AbstractCreeperEntity, AbstractCreeperModel<AbstractCreeperEntity>> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final AbstractCreeperModel<AbstractCreeperEntity> model = new AbstractCreeperModel<>(2.0F);

    public AbstractCreeperChargeLayer(IEntityRenderer<AbstractCreeperEntity, AbstractCreeperModel<AbstractCreeperEntity>> p_i50947_1_) {
        super(p_i50947_1_);
    }

    protected float xOffset(float p_225634_1_) {
        return p_225634_1_ * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected EntityModel<AbstractCreeperEntity> model() {
        return this.model;
    }
}