package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.SculkCreeperModel;
import com.bedmen.odyssey.entity.monster.OdysseyCreeper;
import com.bedmen.odyssey.entity.monster.SculkCreeper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SculkCreeperRenderer extends AbstractCreeperRenderer<SculkCreeper, SculkCreeperModel<SculkCreeper>>{
    protected static final ResourceLocation SCULK_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/creeper/sculk_creeper.png");

    public SculkCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, () -> new SculkCreeperModel<>(context.bakeLayer(SculkCreeperModel.LAYER_LOCATION)));
    }

    public ResourceLocation getTextureLocation(OdysseyCreeper odysseyCreeper) {
        return SCULK_CREEPER_LOCATION;
    }

}
