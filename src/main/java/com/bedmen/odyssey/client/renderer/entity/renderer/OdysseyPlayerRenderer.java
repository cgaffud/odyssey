package com.bedmen.odyssey.client.renderer.entity.renderer;

import com.bedmen.odyssey.client.renderer.entity.layers.AmuletLayer;
import com.bedmen.odyssey.client.renderer.entity.layers.OdysseyElytraLayer;
import com.bedmen.odyssey.client.renderer.entity.layers.QuiverLayer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OdysseyPlayerRenderer extends PlayerRenderer {
    public OdysseyPlayerRenderer(EntityRendererManager p_i46102_1_) {
        this(p_i46102_1_, false);
    }

    public OdysseyPlayerRenderer(EntityRendererManager entityRendererManager, boolean flag) {
        super(entityRendererManager, flag);
        this.layers.removeIf((layer) -> layer instanceof ElytraLayer);
        this.addLayer(new QuiverLayer<>(this));
        this.addLayer(new AmuletLayer<>(this));
        this.addLayer(new OdysseyElytraLayer<>(this));
    }
}
