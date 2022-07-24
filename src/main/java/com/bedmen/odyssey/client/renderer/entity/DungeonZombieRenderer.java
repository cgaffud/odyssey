package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.DungeonZombie;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DungeonZombieRenderer<T extends DungeonZombie> extends AbstractZombieRenderer<T, ZombieModel<T>> {
    private static final ResourceLocation MOON_TOWER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/zombie/moon_tower.png");

    public DungeonZombieRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR);
    }

    public DungeonZombieRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation0, ModelLayerLocation modelLayerLocation1, ModelLayerLocation modelLayerLocation2) {
        super(context, new ZombieModel<>(context.bakeLayer(modelLayerLocation0)), new ZombieModel<>(context.bakeLayer(modelLayerLocation1)), new ZombieModel<>(context.bakeLayer(modelLayerLocation2)));
    }

    public ResourceLocation getTextureLocation(DungeonZombie zombie) {
        return MOON_TOWER_LOCATION;
    }
}
