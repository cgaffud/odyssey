package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.projectile.AmethystArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AmethystArrowRenderer extends ArrowRenderer<AmethystArrowEntity> {
    public static final ResourceLocation AMETHYST_ARROW_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/amethyst_arrow.png");

    public AmethystArrowRenderer(EntityRendererManager p_i46549_1_) {
        super(p_i46549_1_);
    }

    public ResourceLocation getTextureLocation(AmethystArrowEntity p_110775_1_) {
        return AMETHYST_ARROW_LOCATION;
    }
}