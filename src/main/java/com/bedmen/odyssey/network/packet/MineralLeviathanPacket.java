package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.entity.IRotationallyIncompetent;
import com.bedmen.odyssey.entity.boss.IMineralLeviathanSegment;
import com.bedmen.odyssey.entity.boss.MineralLeviathanEntity;
import com.bedmen.odyssey.entity.boss.MineralLeviathanPartEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class MineralLeviathanPacket {

    public int entityID;
    public int headID;
    public int prevSegmentID;
    public int nextSegmentID;

    public MineralLeviathanPacket(){
    }

    public MineralLeviathanPacket(int entityID, int headID, int prevSegmentID, int nextSegmentID){
        this.entityID = entityID;
        this.headID = headID;
        this.prevSegmentID = prevSegmentID;
        this.nextSegmentID = nextSegmentID;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static void encode(MineralLeviathanPacket mineralLeviathanPacket, PacketBuffer buf){
        buf.writeInt(mineralLeviathanPacket.entityID);
        buf.writeInt(mineralLeviathanPacket.headID);
        buf.writeInt(mineralLeviathanPacket.prevSegmentID);
        buf.writeInt(mineralLeviathanPacket.nextSegmentID);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static MineralLeviathanPacket decode(PacketBuffer buf){
        return new MineralLeviathanPacket(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(MineralLeviathanPacket mineralLeviathanPacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LogicalSide sideReceived = context.getDirection().getReceptionSide();
            Optional<ClientWorld> clientWorldOptional = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (clientWorldOptional.isPresent()) {
                ClientWorld clientWorld = clientWorldOptional.get();
                Entity entity = clientWorld.getEntity(mineralLeviathanPacket.entityID);
                if(entity instanceof MineralLeviathanPartEntity){
                    MineralLeviathanEntity mineralLeviathanEntity = (MineralLeviathanEntity) clientWorld.getEntity(mineralLeviathanPacket.headID);
                    System.out.println((mineralLeviathanEntity == null)+" beans2");
                    ((MineralLeviathanPartEntity) entity).head = (MineralLeviathanEntity) clientWorld.getEntity(mineralLeviathanPacket.headID);
                    ((MineralLeviathanPartEntity) entity).prevSegment = (IMineralLeviathanSegment) clientWorld.getEntity(mineralLeviathanPacket.prevSegmentID);
                    if(mineralLeviathanPacket.nextSegmentID == -1){
                        ((MineralLeviathanPartEntity) entity).nextSegment = null;
                    } else {
                        ((MineralLeviathanPartEntity) entity).nextSegment = (MineralLeviathanPartEntity) clientWorld.getEntity(mineralLeviathanPacket.nextSegmentID);
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
