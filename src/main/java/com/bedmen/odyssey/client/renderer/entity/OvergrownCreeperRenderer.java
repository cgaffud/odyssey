package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.OdysseyCreeper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class OvergrownCreeperRenderer extends OdysseyCreeperRenderer {
    protected static final ResourceLocation CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/creeper/overgrown.png");

    public OvergrownCreeperRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(OdysseyCreeper odysseyCreeper) {
        return CREEPER_LOCATION;
    }
}
