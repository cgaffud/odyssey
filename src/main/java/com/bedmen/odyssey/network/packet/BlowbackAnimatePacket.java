package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.aspect.AspectUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BlowbackAnimatePacket {

    public int id;

    public BlowbackAnimatePacket(){
    }

    public BlowbackAnimatePacket(int id){
        this.id = id;
    }

    public BlowbackAnimatePacket(Entity entity){
        this.id = entity.getId();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static void encode(BlowbackAnimatePacket BlowbackAnimatePacket, FriendlyByteBuf buf){
        buf.writeVarInt(BlowbackAnimatePacket.id);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static BlowbackAnimatePacket decode(FriendlyByteBuf buf){
        return new BlowbackAnimatePacket(buf.readVarInt());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(BlowbackAnimatePacket BlowbackAnimatePacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if(minecraft.level != null){
                Entity entity = minecraft.level.getEntity(BlowbackAnimatePacket.id);
                if(entity != null){
                    AspectUtil.doAirAspectParticles(entity, 2);
                }
            }
        });
        context.setPacketHandled(true);
    }
}