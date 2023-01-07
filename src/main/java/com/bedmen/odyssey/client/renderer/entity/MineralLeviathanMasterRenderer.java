package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanMaster;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MineralLeviathanMasterRenderer extends EntityRenderer<MineralLeviathanMaster> {
    public MineralLeviathanMasterRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(MineralLeviathanMaster master) {
        return null;
    }
}
