package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.entity.boss.permafrost.PermafrostMaster;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PermafrostMasterRenderer extends EntityRenderer<PermafrostMaster> {

    public PermafrostMasterRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(PermafrostMaster master) {
        return null;
    }
}
