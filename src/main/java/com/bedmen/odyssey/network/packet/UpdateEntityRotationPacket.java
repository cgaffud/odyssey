package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.entity.IRotationallyIncompetent;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class UpdateEntityRotationPacket {

    public float yRot;
    public float xRot;
    public int id;

    public UpdateEntityRotationPacket(){
    }

    public UpdateEntityRotationPacket(float yRot, float xRot, int id){
        this.yRot = yRot;
        this.xRot = xRot;
        this.id = id;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static void encode(UpdateEntityRotationPacket updateEntityRotationPacket, PacketBuffer buf){
        buf.writeFloat(updateEntityRotationPacket.yRot);
        buf.writeFloat(updateEntityRotationPacket.xRot);
        buf.writeInt(updateEntityRotationPacket.id);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static UpdateEntityRotationPacket decode(PacketBuffer buf){
        return new UpdateEntityRotationPacket(buf.readFloat(), buf.readFloat(), buf.readInt());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(UpdateEntityRotationPacket updateEntityRotationPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LogicalSide sideReceived = context.getDirection().getReceptionSide();
            Optional<ClientWorld> clientWorldOptional = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (clientWorldOptional.isPresent()) {
                ClientWorld clientWorld = clientWorldOptional.get();
                Entity entity = clientWorld.getEntity(updateEntityRotationPacket.id);
                if(entity instanceof IRotationallyIncompetent){
                    ((IRotationallyIncompetent) entity).setYRot(updateEntityRotationPacket.yRot);
                    ((IRotationallyIncompetent) entity).setXRot(updateEntityRotationPacket.xRot);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
