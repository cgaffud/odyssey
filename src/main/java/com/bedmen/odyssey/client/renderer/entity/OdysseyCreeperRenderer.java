package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.client.model.OdysseyCreeperModel;
import com.bedmen.odyssey.entity.monster.OdysseyCreeper;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class OdysseyCreeperRenderer extends AbstractCreeperRenderer<OdysseyCreeper, OdysseyCreeperModel<OdysseyCreeper>> {
    protected static final ResourceLocation CREEPER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");

    public OdysseyCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, () -> new OdysseyCreeperModel<>(context.bakeLayer(ModelLayers.CREEPER)));
    }
}