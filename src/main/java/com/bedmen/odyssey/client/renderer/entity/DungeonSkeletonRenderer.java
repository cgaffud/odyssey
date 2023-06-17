package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.OdysseySkeleton;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DungeonSkeletonRenderer extends OdysseySkeletonRenderer {
    private static final ResourceLocation MOON_TOWER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/skeleton/moon_tower.png");

    public DungeonSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
    }

    public ResourceLocation getTextureLocation(OdysseySkeleton odysseySkeleton) {
        return MOON_TOWER_LOCATION;
    }
}
