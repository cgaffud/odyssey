package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.client.model.OdysseyCreeperModel;
import com.bedmen.odyssey.client.renderer.entity.layer.CreeperDripstoneLayer;
import com.bedmen.odyssey.entity.monster.OdysseyCreeper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import java.util.function.Supplier;

public class OdysseyCreeperRenderer<T extends OdysseyCreeper> extends AbstractCreeperRenderer<T, OdysseyCreeperModel<T>> {

    public OdysseyCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, () -> new OdysseyCreeperModel<>(context.bakeLayer(OdysseyCreeperModel.LAYER_LOCATION)));
        this.addLayer(new CreeperDripstoneLayer<>(this, context.getModelSet()));
    }

    public OdysseyCreeperRenderer(EntityRendererProvider.Context context, Supplier<OdysseyCreeperModel<T>> modelSupplier) {
        super(context, modelSupplier);
        this.addLayer(new CreeperDripstoneLayer<>(this, context.getModelSet()));
    }
}