package com.bedmen.odyssey.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class JumpingPacket {

    public boolean jumping;

    public JumpingPacket(){
    }

    public JumpingPacket(boolean jumping){
        this.jumping = jumping;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static void encode(JumpingPacket jumpingPacket, FriendlyByteBuf buf){
        buf.writeBoolean(jumpingPacket.jumping);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static JumpingPacket decode(FriendlyByteBuf buf){
        return new JumpingPacket(buf.readBoolean());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(JumpingPacket jumpingPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayerEntity = context.getSender();
            serverPlayerEntity.setJumping(jumpingPacket.jumping);
        });
        context.setPacketHandled(true);
    }
}
