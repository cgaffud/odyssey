package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.OdysseyCreeperModel;
import com.bedmen.odyssey.entity.monster.OdysseyCreeper;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class OdysseyCreeperRenderer extends AbstractCreeperRenderer<OdysseyCreeper, OdysseyCreeperModel<OdysseyCreeper>> {

    protected static final ResourceLocation DRIPSTONE_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/creeper/dripstone_spike_creeper.png");

    public OdysseyCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, () -> new OdysseyCreeperModel<>(context.bakeLayer(OdysseyCreeperModel.LAYER_LOCATION)));
    }

    public ResourceLocation getTextureLocation(OdysseyCreeper odysseyCreeper) {
        if(odysseyCreeper.hasDripstoneSpikes()){
            return DRIPSTONE_CREEPER_LOCATION;
        }
        return CREEPER_LOCATION;
    }
}