package com.bedmen.odyssey.network.packet;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class SoundPacket{

    public int type;
    public int x;
    public int y;
    public int z;

    public SoundPacket(){
    }

    public SoundPacket(int type, int x, int y, int z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static void encode(SoundPacket soundPacket, PacketBuffer buf){
        buf.writeInt(soundPacket.type);
        buf.writeInt(soundPacket.x);
        buf.writeInt(soundPacket.y);
        buf.writeInt(soundPacket.z);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static SoundPacket decode(PacketBuffer buf){
        int type = buf.readInt();
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        return new SoundPacket(type, x, y, z);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(SoundPacket soundPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LogicalSide sideReceived = context.getDirection().getReceptionSide();
            Optional<ClientWorld> clientWorldOptional = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (clientWorldOptional.isPresent()) {
                ClientWorld clientWorld = clientWorldOptional.get();
                clientWorld.playLocalSound(soundPacket.x, soundPacket.y, soundPacket.z, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
            }
        });
        context.setPacketHandled(true);
    }
}
