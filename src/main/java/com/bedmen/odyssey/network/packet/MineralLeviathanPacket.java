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
    public int[] partIDS;

    public MineralLeviathanPacket(){
    }

    public MineralLeviathanPacket(int entityID, int[] partIDS){
        this.entityID = entityID;
        this.partIDS = partIDS;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static void encode(MineralLeviathanPacket mineralLeviathanPacket, PacketBuffer buf){
        buf.writeInt(mineralLeviathanPacket.entityID);
        buf.writeVarIntArray(mineralLeviathanPacket.partIDS);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static MineralLeviathanPacket decode(PacketBuffer buf){
        return new MineralLeviathanPacket(buf.readInt(), buf.readVarIntArray());
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
                if(entity instanceof MineralLeviathanEntity){
                    MineralLeviathanEntity mineralLeviathanEntity = (MineralLeviathanEntity)entity;
                    for(int i = 0; i < MineralLeviathanEntity.NUM_SEGMENTS-1; i++){
                        mineralLeviathanEntity.parts[i] = (MineralLeviathanPartEntity) clientWorld.getEntity(mineralLeviathanPacket.partIDS[i]);
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
