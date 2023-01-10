package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.entity.projectile.OdysseyArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class OdysseyArrowRenderer extends ArrowRenderer<OdysseyArrow> {

    public OdysseyArrowRenderer(EntityRendererProvider.Context p_174422_) {
        super(p_174422_);
    }

    public ResourceLocation getTextureLocation(OdysseyArrow odysseyArrow) {
        return odysseyArrow.getArrowType().resourceLocation;
    }
}