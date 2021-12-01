package com.bedmen.odyssey.mixin;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundSetEntityMotionPacket.class)
public abstract class MixinClientboundSetEntityMotionPacket {
    @Shadow
    private int xa;
    @Shadow
    private int ya;
    @Shadow
    private int za;

    @Inject(method = "<init>*", at = @At(value = "TAIL"))
    public void onInit(int id, Vec3 vector3d, CallbackInfo ci) {
        this.xa = (int)(vector3d.x * 8000.0D);
        this.ya = (int)(vector3d.y * 8000.0D);
        this.za = (int)(vector3d.z * 8000.0D);
    }
}
