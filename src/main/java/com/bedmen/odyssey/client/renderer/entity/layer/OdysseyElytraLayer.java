package com.bedmen.odyssey.client.renderer.entity.layer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.IOdysseyLivingEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class OdysseyElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M> {
    private static final ResourceLocation GLIDER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/elytras/glider.png");
    public OdysseyElytraLayer(RenderLayerParent<T, M> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent, entityModelSet);
    }

    public boolean shouldRender(ItemStack stack, T entity) {
        return entity instanceof IOdysseyLivingEntity odysseyLivingEntity && odysseyLivingEntity.getGlidingLevel() > 0;
    }

    public ResourceLocation getElytraTexture(ItemStack itemStack, T entity) {
        return GLIDER_LOCATION;
    }
}
