package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.client.renderer.entity.renderer.OdysseyPlayerRenderer;
import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.IReloadableResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EntityRendererManager.class)
public class MixinEntityRendererManager {
    @Shadow
    private PlayerRenderer defaultPlayerRenderer;
    @Shadow
    private Map<String, PlayerRenderer> playerRenderers;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void onInit(TextureManager p_i226034_1_, net.minecraft.client.renderer.ItemRenderer p_i226034_2_, IReloadableResourceManager p_i226034_3_, FontRenderer p_i226034_4_, GameSettings p_i226034_5_, CallbackInfo ci) {
        this.defaultPlayerRenderer = new OdysseyPlayerRenderer(getEntityRendererManager());
        this.playerRenderers.put("default", this.defaultPlayerRenderer);
        this.playerRenderers.put("slim", new OdysseyPlayerRenderer(getEntityRendererManager(), true));
    }

    private EntityRendererManager getEntityRendererManager(){
        return (EntityRendererManager) (Object)this;
    }
}
