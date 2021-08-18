package com.bedmen.odyssey.network.packet;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;

public class JumpingPacket{

    public boolean jumping;

    public JumpingPacket(){
    }

    public JumpingPacket(boolean jumping){
        this.jumping = jumping;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static void encode(JumpingPacket jumpingPacket, PacketBuffer buf){
        buf.writeBoolean(jumpingPacket.jumping);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static JumpingPacket decode(PacketBuffer buf){
        return new JumpingPacket(buf.readBoolean());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(JumpingPacket jumpingPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity serverPlayerEntity = context.getSender();
            serverPlayerEntity.setJumping(jumpingPacket.jumping);
        });
        context.setPacketHandled(true);
    }
}
