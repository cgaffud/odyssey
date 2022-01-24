package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.equipment.DualWieldItem;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends Player {

    private boolean alternateHands = false;

    public MixinLocalPlayer(Level p_36114_, BlockPos p_36115_, float p_36116_, GameProfile p_36117_) {
        super(p_36114_, p_36115_, p_36116_, p_36117_);
    }

    @ModifyVariable(method = "swing", at = @At(value = "HEAD"), argsOnly = true)
    private InteractionHand onSwing(InteractionHand interactionHand) {
        if(DualWieldItem.isDualWielding(this)){
            this.alternateHands ^= true;
            return this.alternateHands ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        }
        return interactionHand;
    }
}
