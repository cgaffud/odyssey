package com.bedmen.odyssey.mixin;

import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SEntityVelocityPacket.class)
public abstract class MixinSEntityVelocityPacket {
    @Shadow
    private int xa;
    @Shadow
    private int ya;
    @Shadow
    private int za;

    @Inject(method = "<init>*", at = @At(value = "TAIL"))
    public void onInit(int id, Vector3d vector3d, CallbackInfo ci) {
        this.xa = (int)(vector3d.x * 8000.0D);
        this.ya = (int)(vector3d.y * 8000.0D);
        this.za = (int)(vector3d.z * 8000.0D);
    }
}
