package com.bedmen.odyssey.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class SneakingPacket {

    public boolean sneaking;

    public SneakingPacket(){
    }

    public SneakingPacket(boolean sneaking){
        this.sneaking = sneaking;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static void encode(SneakingPacket sneakingPacket, FriendlyByteBuf buf){
        buf.writeBoolean(sneakingPacket.sneaking);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static SneakingPacket decode(FriendlyByteBuf buf){
        return new SneakingPacket(buf.readBoolean());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(SneakingPacket sneakingPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayerEntity = context.getSender();
            if(serverPlayerEntity != null)
                serverPlayerEntity.setShiftKeyDown(sneakingPacket.sneaking);
        });
        context.setPacketHandled(true);
    }
}
