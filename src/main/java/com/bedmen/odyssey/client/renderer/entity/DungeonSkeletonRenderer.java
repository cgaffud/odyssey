package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class DungeonSkeletonRenderer extends SkeletonRenderer {
    private static final ResourceLocation MOON_TOWER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/skeleton/moon_tower.png");

    public DungeonSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
    }

    public ResourceLocation getTextureLocation(AbstractSkeleton skeleton) {
        return MOON_TOWER_LOCATION;
    }
}
