package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.combat.WeaponUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends Player {

    private boolean alternateHands = false;

    public MixinLocalPlayer(Level level, BlockPos blockPos, float f, GameProfile gameProfile, @Nullable ProfilePublicKey profilePublicKey) {
        super(level, blockPos, f, gameProfile, profilePublicKey);
    }

    @ModifyVariable(method = "swing", at = @At(value = "HEAD"), argsOnly = true)
    private InteractionHand onSwing(InteractionHand interactionHand) {
        if(WeaponUtil.isDualWielding(this)){
            this.alternateHands ^= true;
            return this.alternateHands ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        }
        return interactionHand;
    }
}
