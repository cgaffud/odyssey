package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenEffectRenderer.class)
public class MixinScreenEffectRenderer {

    @Redirect(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isOnFire()Z"))
    private static boolean renderScreenEffect$isOnFire(LocalPlayer player){
        return (player.isOnFire()) || (player.hasEffect(EffectRegistry.HEXFLAME.get()));
    }
}
