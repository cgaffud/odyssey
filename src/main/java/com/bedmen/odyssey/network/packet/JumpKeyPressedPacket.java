package com.bedmen.odyssey.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class JumpKeyPressedPacket {

    public JumpKeyPressedPacket(){
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static void encode(JumpKeyPressedPacket jumpKeyPressedPacket, FriendlyByteBuf buf){
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static JumpKeyPressedPacket decode(FriendlyByteBuf buf){
        return new JumpKeyPressedPacket();
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(JumpKeyPressedPacket jumpKeyPressedPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            if(serverPlayer != null){
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 2, 0, false, false, true));
            }
        });
        context.setPacketHandled(true);
    }
}
