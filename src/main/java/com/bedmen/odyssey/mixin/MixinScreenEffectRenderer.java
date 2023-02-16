package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.util.RenderUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenEffectRenderer.class)
public class MixinScreenEffectRenderer {

    @Redirect(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isOnFire()Z"))
    private static boolean renderScreenEffect$isOnFire(LocalPlayer localPlayer){
        return localPlayer.isOnFire() || RenderUtil.getStrongestFire(localPlayer).isPresent();
    }
}
