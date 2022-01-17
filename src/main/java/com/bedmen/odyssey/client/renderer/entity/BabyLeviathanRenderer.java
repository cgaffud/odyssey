package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.BabyLeviathanModel;
import com.bedmen.odyssey.entity.monster.BabyLeviathan;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BabyLeviathanRenderer extends MobRenderer<BabyLeviathan, BabyLeviathanModel> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/baby_leviathan.png");
    public BabyLeviathanRenderer(EntityRendererProvider.Context context) {
        super(context, new BabyLeviathanModel(context.bakeLayer(BabyLeviathanModel.LAYER_LOCATION)), 0.1F);
    }

    public ResourceLocation getTextureLocation(BabyLeviathan entity) {
        return TEXTURE_LOCATION;
    }
}