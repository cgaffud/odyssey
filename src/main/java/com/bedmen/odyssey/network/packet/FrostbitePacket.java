package com.bedmen.odyssey.network.packet;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.registry.ParticleTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FrostbitePacket {

    public int id;

    public FrostbitePacket(){
    }

    public FrostbitePacket(int id){
        this.id = id;
    }

    public FrostbitePacket(Entity entity){
        this.id = entity.getId();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public static void encode(FrostbitePacket FrostbitePacket, FriendlyByteBuf buf){
        buf.writeVarInt(FrostbitePacket.id);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public static FrostbitePacket decode(FriendlyByteBuf buf){
        return new FrostbitePacket(buf.readVarInt());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public static void handle(FrostbitePacket FrostbitePacket, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if(minecraft.level != null){
                Entity entity = minecraft.level.getEntity(FrostbitePacket.id);
                if(entity != null){
                    AspectUtil.doFrostAspectParticles(entity, 3);
                }
            }
        });
        context.setPacketHandled(true);
    }
}