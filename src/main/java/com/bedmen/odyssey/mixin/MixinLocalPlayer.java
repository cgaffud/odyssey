package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.combat.CombatUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends Player {

    private boolean alternateHands = false;

    public MixinLocalPlayer(Level p_36114_, BlockPos p_36115_, float p_36116_, GameProfile p_36117_) {
        super(p_36114_, p_36115_, p_36116_, p_36117_);
    }

    @ModifyVariable(method = "swing", at = @At(value = "HEAD"), argsOnly = true)
    private InteractionHand onSwing(InteractionHand interactionHand) {
        if(CombatUtil.isDualWielding(this)){
            this.alternateHands ^= true;
            return this.alternateHands ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        }
        return interactionHand;
    }
}
