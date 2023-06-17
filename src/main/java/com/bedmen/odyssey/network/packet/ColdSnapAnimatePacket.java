package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.aspect.AspectUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ColdSnapAnimatePacket {

    public int id;

    public ColdSnapAnimatePacket(){
    }

    public ColdSnapAnimatePacket(int id){
        this.id = id;
    }

    public ColdSnapAnimatePacket(Entity entity){
        this.id = entity.getId();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static void encode(ColdSnapAnimatePacket ColdSnapAnimatePacket, FriendlyByteBuf buf){
        buf.writeVarInt(ColdSnapAnimatePacket.id);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static ColdSnapAnimatePacket decode(FriendlyByteBuf buf){
        return new ColdSnapAnimatePacket(buf.readVarInt());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(ColdSnapAnimatePacket ColdSnapAnimatePacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if(minecraft.level != null){
                Entity entity = minecraft.level.getEntity(ColdSnapAnimatePacket.id);
                if(entity != null){
                    AspectUtil.doFrostAspectParticles(entity, 5);
                }
            }
        });
        context.setPacketHandled(true);
    }
}