package com.bedmen.odyssey.mixin;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow
    private ClientWorld level;

    @Inject(method = "levelEvent", at = @At(value = "HEAD") )
    private void onlevelEvent(PlayerEntity p_180439_1_, int p_180439_2_, BlockPos p_180439_3_, int p_180439_4_, CallbackInfo ci) {
        if(p_180439_2_ == 10000){
            this.level.playLocalSound(p_180439_3_, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
    }
}
