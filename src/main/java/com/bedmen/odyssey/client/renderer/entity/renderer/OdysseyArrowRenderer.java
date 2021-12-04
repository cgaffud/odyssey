package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.entity.projectile.OdysseyArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OdysseyArrowRenderer extends ArrowRenderer<OdysseyArrow> {

    public OdysseyArrowRenderer(EntityRendererProvider.Context p_174422_) {
        super(p_174422_);
    }

    public ResourceLocation getTextureLocation(OdysseyArrow odysseyArrowEntity) {
        return odysseyArrowEntity.getArrowType().getResourceLocation();
    }
}