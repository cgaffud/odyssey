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
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends Player {

    @Shadow
    @Final
    public ClientPacketListener connection;
    private boolean alternateHands = false;

    public MixinLocalPlayer(Level p_36114_, BlockPos p_36115_, float p_36116_, GameProfile p_36117_) {
        super(p_36114_, p_36115_, p_36116_, p_36117_);
    }

    public void swing(InteractionHand interactionHand) {
        if(DualWieldItem.isDualWielding(this)){
            interactionHand = this.alternateHands ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            this.alternateHands ^= true;
        }
        super.swing(interactionHand);
        this.connection.send(new ServerboundSwingPacket(interactionHand));
    }
}
