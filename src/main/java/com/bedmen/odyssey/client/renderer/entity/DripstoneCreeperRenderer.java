package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.CamoCreeperModel;
import com.bedmen.odyssey.client.model.DripstoneCreeperModel;
import com.bedmen.odyssey.client.model.OdysseyCreeperModel;
import com.bedmen.odyssey.entity.monster.DripstoneCreeper;
import com.bedmen.odyssey.entity.monster.OdysseyCreeper;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class DripstoneCreeperRenderer extends AbstractCreeperRenderer<DripstoneCreeper, DripstoneCreeperModel<DripstoneCreeper>>{
    protected static final ResourceLocation DRIPSTONE_CREEPER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/creeper/dripstone_creeper.png");

    public DripstoneCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, () -> new DripstoneCreeperModel<>(context.bakeLayer(DripstoneCreeperModel.LAYER_LOCATION)));
    }

    public ResourceLocation getTextureLocation(OdysseyCreeper odysseyCreeper) {
        return DRIPSTONE_CREEPER_LOCATION;
    }

}
