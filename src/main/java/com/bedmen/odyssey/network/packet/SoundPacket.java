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
    public double x;
    public double y;
    public double z;

    public SoundPacket(){
    }

    public SoundPacket(int type, double x, double y, double z) {
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
        buf.writeDouble(soundPacket.x);
        buf.writeDouble(soundPacket.y);
        buf.writeDouble(soundPacket.z);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static SoundPacket decode(PacketBuffer buf){
        int type = buf.readInt();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
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
                switch(soundPacket.type){
                    case 0:
                        clientWorld.playLocalSound(soundPacket.x, soundPacket.y, soundPacket.z, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
                        break;
                    case 1:
                        clientWorld.playLocalSound(soundPacket.x, soundPacket.y, soundPacket.z, SoundEvents.ITEM_BREAK, SoundCategory.HOSTILE, 1.5f, 0.8f, false);
                        break;
                    case 2:
                        clientWorld.playLocalSound(soundPacket.x, soundPacket.y, soundPacket.z, SoundEvents.STONE_BREAK, SoundCategory.HOSTILE, 1.5f, 0.8f, false);
                        break;
                }
            }
        });
        context.setPacketHandled(true);
    }
}
