package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.BabyLeviathanModel;
import com.bedmen.odyssey.entity.monster.BabyLeviathanEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BabyLeviathanRenderer extends MobRenderer<BabyLeviathanEntity, BabyLeviathanModel> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/baby_leviathan.png");
    public BabyLeviathanRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new BabyLeviathanModel(), 0.1F);
    }

    public ResourceLocation getTextureLocation(BabyLeviathanEntity entity) {
        return TEXTURE_LOCATION;
    }
}