package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.TrooperModel;
import com.bedmen.odyssey.entity.TrooperEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TrooperRenderer extends MobRenderer<TrooperEntity, TrooperModel<TrooperEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/trooper.png");

    public TrooperRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new TrooperModel<>(), 0.5F);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(TrooperEntity entity) {
        return TEXTURE;
    }
}