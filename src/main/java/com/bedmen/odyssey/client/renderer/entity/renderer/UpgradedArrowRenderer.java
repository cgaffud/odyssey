package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.projectile.UpgradedArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UpgradedArrowRenderer extends ArrowRenderer<UpgradedArrowEntity> {
    public static final ResourceLocation[] UPGRADED_ARROW_LOCATION = new ResourceLocation[]{new ResourceLocation("textures/entity/projectiles/arrow.png"), new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/amethyst_arrow.png"), new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/quartz_arrow.png"), new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/razor_arrow.png")};

    public UpgradedArrowRenderer(EntityRendererManager p_i46549_1_) {
        super(p_i46549_1_);
    }

    public ResourceLocation getTextureLocation(UpgradedArrowEntity upgradedArrowEntity) {
        return UPGRADED_ARROW_LOCATION[upgradedArrowEntity.getArrowType().ordinal()];
    }
}