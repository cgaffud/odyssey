package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.entity.boss.coven.CovenMaster;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CovenMasterRenderer extends EntityRenderer<CovenMaster> {

    public CovenMasterRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(CovenMaster master) {
        return null;
    }
}
